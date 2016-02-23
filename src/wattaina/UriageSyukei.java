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

	static String formatMore = "001";
	static String notMatch = "002";

	static String loadFile(String[] args, String files, String regex,
			LinkedHashMap<String, String> definitionMap,
			LinkedHashMap<String, Integer> salesMap) throws Exception, IOException, FileNotFoundException {
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(args[0] + File.separator + files));
			String str;

			while((str = br.readLine()) != null){
				String[] format = str.split(",");
				if(format.length != 2){
					return "001";
				}
				if(!format[0].matches(regex)){
					return "002";
				}
				definitionMap.put(format[0],format[1]);
				salesMap.put(format[0], 0);
			}
		}catch(FileNotFoundException e){
			throw new FileNotFoundException("noFile");
		}catch(IOException e){
			throw e;
		}finally{
			if (br != null){
				br.close();
			}
		}
		return null;
	}

	static boolean numberCheck(String[] args, ArrayList<String> rcdfile){
		File dir = new File(args[0]);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			if(files[i].getName().matches("^\\d{8}.rcd$")){
				rcdfile.add(files[i].getName());
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
				return false;
			}
		}
		return true;
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
				if(rcdRead.size() % 3 != 0){
					errorHandingDisplay(rcdfile.get(i), "illegalFormat");
					return false;
				}
				if(rcdRead.get(2).length() < 10){
					price = Integer.parseInt(rcdRead.get(2));
				}else{
					errorHandingDisplay("売上", "overflow");
					return false;
				}
				if(beanchSalesMap.containsKey(rcdRead.get(0))){
					beanchSalesMap.put(rcdRead.get(0), beanchSalesMap.get(rcdRead.get(0)) + price);
				}else{
					errorHandingDisplay(rcdfile.get(i) + "の支店", "codeFraud");
					return false;
				}
				if(commoditySalesMap.containsKey(rcdRead.get(1))){
					commoditySalesMap.put(rcdRead.get(1), commoditySalesMap.get(rcdRead.get(1)) + price);
				}else{
					errorHandingDisplay(rcdfile.get(i) + "の商品", "codeFraud");
					return false;
				}
				for(String key : rcdMap.keySet()){
					if(rcdMap.get(key).toString().length() > 10){
						errorHandingDisplay("合計", "overflow");
						return false;
					}
				}
				br.close();
			}
		}catch(IOException e){
			errorHandingDisplay("売上", "noFile");
			return false;
		}finally{
			if(br != null){
				try{
					br.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return true;
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
				if(salesMap.get(str) != 0){
					out.put(salesMap.get(str), str + ',' + map.get(str));
				}else{
					a++;
					out.put(a, str + ',' + map.get(str));
				}
			}
			List<Entry<Integer, String>> entries = new ArrayList<Entry<Integer, String>>(out.entrySet());
			Collections.sort(entries, new Comparator<Entry<Integer, String>>(){
				public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2){
					return o2.getKey().compareTo(o1.getKey());
				}
			});
			for(Entry<Integer,String> entry : entries){
				if(!entry.getKey().equals(a)){
					fw.write(out.get(entry.getKey()) + "," + entry.getKey() + System.getProperty("line.separator"));
				}else{
					fw.write(out.get(entry.getKey()) + "," + 0 + System.getProperty("line.separator"));
					a--;
				}
			}
			fw.close();
		}catch(FileNotFoundException e){
			return false;
		}
		catch(IOException e){
			return false;
		}finally{
			if(fw != null){
				try{
					fw.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	static void errorHandingDisplay(String filename, String errorContent){
		if(errorContent.equals("noFile")){
			System.err.println(filename + "ファイルが存在しません");
		}
		if(errorContent.equals("illegalFormat")){
			System.err.println(filename + "のフォーマットが不正です");
		}
		if(errorContent.equals("overflow")){
			System.err.println(filename + "金額が10桁を超えました");
		}
		if(errorContent.equals("codeFraud")){
			System.err.println(filename + "コードが不正です");
		}
		if(errorContent.equals("notSequence")){
			System.err.println("売上ファイル名が連番になっていません");
		}
	}

	public static void main(String[] args) throws Exception{
		if(args.length != 1){
			System.err.println("正しくディレクトリを指定してください。");
			return;
		}
		ArrayList<String> rcdfile = new ArrayList<String>();
		LinkedHashMap<String, String> branchMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> commodityMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Integer> beanchSalesMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> commoditySalesMap = new LinkedHashMap<String, Integer>();
		LinkedHashMap<String, Integer> rcdMap = new LinkedHashMap<String, Integer>();

		String str = null;
		try{
			str = loadFile(args, "branch.lst", "^\\d{3}$", branchMap, beanchSalesMap);
			if(formatMore.equals(str)){
				errorHandingDisplay("支店定義", "illegalFormat");
				return;
			}else if(notMatch.equals(str)){
				errorHandingDisplay("支店定義", "illegalFormat");
				return;
			}
		}catch(FileNotFoundException e){
			errorHandingDisplay("支店定義", "noFile");
			return;
		}
		try{
			str = loadFile(args, "commodity.lst", "^\\w{8}$", commodityMap, commoditySalesMap);
			if(formatMore.equals(str)){
				errorHandingDisplay("商品定義", "illegalFormat");
				return;
			}else if(notMatch.equals(str)){
				errorHandingDisplay("商品定義", "illegalFormat");
				return;
			}
		}catch(FileNotFoundException e){
			errorHandingDisplay("商品定義", "noFile");
			return;
		}

		if(!numberCheck(args, rcdfile)){
			return;
		}

		if(!aggregateData(args, beanchSalesMap, commoditySalesMap, rcdMap, rcdfile)){
			return;
		}

		if(!fileOutput(args, "branch.out",  branchMap, beanchSalesMap)){
			return;
		}

		if(!fileOutput(args, "commodity.out", commodityMap, commoditySalesMap)){
			return;
		}
	}
}