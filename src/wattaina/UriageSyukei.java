package wattaina;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class UriageSyukei {

	static String loadFile(String[] args, String files, String regex, LinkedHashMap<String, String> definitionMap, LinkedHashMap<String, Integer> salesMap){
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(args[0] + File.separator + files));
			String str;
			while((str = br.readLine()) != null){
				String[] format = str.split(",");
				if(format.length != 2){
					return "format";
				}
				if(!format[0].matches(regex)){
					return "format";
				}
				definitionMap.put(format[0],format[1]);
				salesMap.put(format[0], 0);

			}
		}catch(FileNotFoundException e){
			return "not";
		}catch(IOException e){
			return "not";
		}finally{
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	static void numberCheck(String[] args, ArrayList<String> rcdfile){
		File dir = new File(args[0]);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if(file.getName().matches("^\\d{8}.rcd$")) {
				rcdfile.add(file.getName());
			}
		}
		String[] rcd = new String[rcdfile.size()];
		int[] rcd2 = new int[rcdfile.size()];
		for(int i=0; i < rcdfile.size(); i++){
			rcd = rcdfile.get(i).split("\\.");
			Arrays.sort(rcd);
			rcd2[i] = Integer.parseInt(rcd[0]);
			if(i + 1 != rcd2[i]){
				System.err.println("売上ファイル名が連番になっていません");
				return;
			}
		}
	}

	static boolean aggregateData(String[] args, LinkedHashMap<String, Integer> beanchSalesMap, LinkedHashMap<String, Integer> commoditySalesMap,
			LinkedHashMap<String, Integer>rcdMap, ArrayList<String> rcdfile){
		BufferedReader br = null;
		try{
		    for(int i = 0; i < rcdfile.size(); i++){
		    	ArrayList<String> rcdRead = new ArrayList<String>();
				br = new BufferedReader(new FileReader(args[0] + File.separator + rcdfile.get(i)));
				String str;
				int price = 0;
				while((str = br.readLine()) != null){
					rcdRead.add(str);
				}

				if(rcdRead.get(2).length() < 10){
					price = Integer.parseInt(rcdRead.get(2));
				}else{
					System.err.println("売上金額が10桁を超えました");
				}
				if(beanchSalesMap.containsKey(rcdRead.get(0))){
					beanchSalesMap.put(rcdRead.get(0), beanchSalesMap.get(rcdRead.get(0)) + price);
//					System.out.println(beanchSalesMap);
				}else{
					codeFraud(rcdfile.get(i), "支店");
					return true;
				}
				if(commoditySalesMap.containsKey(rcdRead.get(1))){
					commoditySalesMap.put(rcdRead.get(1), commoditySalesMap.get(rcdRead.get(1)) + price);
				}else{
					codeFraud(rcdfile.get(i), "商品");
					return true;
				}
				for(String key : rcdMap.keySet()){
					if(rcdMap.get(key).toString().length() > 10){
						System.err.println("合計金額が10桁を超えています");
						return true;
					}
				}
				br.close();
			}
		}catch(IOException e){
			fileDoseNotExist("売上");
			return true;
		}finally{
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	static boolean fileOutput(String[] args, String files,
			LinkedHashMap<String, String> map, LinkedHashMap<String, Integer> salesMap){
			FileWriter fw = null;
			try{
				File file = new File(args[0] + File.separator + files);
				fw = new FileWriter(file);
				Map<Integer, String> out = new TreeMap<Integer, String>();
				int a = 0;

				for(String str : map.keySet()){
					System.out.println(salesMap.get(str));
					if(salesMap.get(str) != 0){
						out.put(salesMap.get(str) , str + ',' + map.get(str));
					}else{
						a++;
						out.put(a , str + ',' + map.get(str));
					}
				}
				List<Entry<Integer, String>> entries = new ArrayList<Entry<Integer, String>>(out.entrySet());
				Collections.sort(entries, new Comparator<Entry<Integer, String>>() {
				    public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2) {
				        return o2.getKey().compareTo(o1.getKey());
				    }
				});
				for(Entry<Integer,String> entry : entries){
					if(!(entry.getKey().equals(a))){
						fw.write(out.get(entry.getKey()) + "," + entry.getKey() + System.getProperty("line.separator"));
					}else{
						fw.write(out.get(entry.getKey()) + "," + 0 + System.getProperty("line.separator"));
						a--;
					}
				}
				fw.close();
			}catch(FileNotFoundException e) {
				return true;
			}
			catch (IOException e) {
				return true;
			}finally{
				if (fw != null){
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return false;
	}

	public static void fileDoseNotExist(String filename){
		System.err.println(filename + "ファイルが存在しません");
	}
	public static void formatFraud(String format){
		System.err.println(format + "のフォーマットが不正です");
	}
	public static void codeFraud(String code, String filename){
		System.err.println(code + "の" + filename +"コードが不正です");
	}

	public static void main(String[] args) {
		if(args.length != 1){
			System.err.println("正しくディレクトリを指定してください。");
			return;
		}
		ArrayList<String> rcdfile = new ArrayList<String>();
		String regex = "^\\d{3}|^\\w{8}";
		LinkedHashMap<String, String> branchMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> commodityMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Integer> beanchSalesMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> commoditySalesMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> rcdMap = new LinkedHashMap<String, Integer>();


		String type = loadFile(args, "branch.lst", regex, branchMap, beanchSalesMap);
		if(type.equals("not")){
			fileDoseNotExist("支店定義");
			return;
		}else if(type.equals("format")){
			formatFraud("支店定義");
			return;
		}

		loadFile(args, "commodity.lst", regex, commodityMap, commoditySalesMap);
		if(loadFile(args, "commodity.lst", regex, commodityMap, commoditySalesMap).equals("not")){
			fileDoseNotExist("商品定義");
			return;
		}else if(loadFile(args, "commodity.lst", regex, commodityMap, commoditySalesMap).equals("format")){
			formatFraud("商品定義");
			return;
		}

		numberCheck(args, rcdfile);

		aggregateData(args, beanchSalesMap, commoditySalesMap, rcdMap, rcdfile);

//		System.out.println(beanchSalesMap);

		fileOutput(args, "branch.out",  branchMap, beanchSalesMap);

		fileOutput(args, "commodity.out", commodityMap, commoditySalesMap);
	}
}