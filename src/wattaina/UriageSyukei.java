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

	static String loadFile(String[] args, String files, String regex, LinkedHashMap<String, String> map){
		try{
			BufferedReader br = new BufferedReader(new FileReader(args[0] + File.separator + files));
			String str;
			while((str = br.readLine()) != null){
				String[] format = str.split(",");
				if(format.length != 2){
						return "format";
				}
				if(!(format[0].matches(regex))){
					return "format";
				}	
				map.put(format[0],format[1]);
			}
			br.close();
		}catch(FileNotFoundException e){
			return "not";
		}catch(IOException e){
			return "not";
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

	static void aggregateData(String[] args, LinkedHashMap<String, String> map, String name,
			LinkedHashMap<String, Integer> rcdMap, ArrayList<String> rcdfile){
		try{
			ArrayList<String> rcdread = new ArrayList<String>();
		    for(int i = 0; i < rcdfile.size(); i++){
				BufferedReader br = new BufferedReader(new FileReader(args[0] + File.separator + rcdfile.get(i)));
				String str;
				while((str = br.readLine()) != null){
					rcdread.add(str);
				}
				if(rcdread.size() % 3 != 0){
					formaterror(rcdfile.get(i));
				}
				int[] price = new int[rcdfile.size()];
				if(rcdread.get(i * 3 + 2).length() <= 10){
					price[i] = Integer.parseInt(rcdread.get(i * 3 + 2));
				}else{
					System.err.println("売上金額が10桁を超えました");
				}
				if(name.equals("branch")){
					if(map.get(rcdread.get(i * 3)) != null){
						if(rcdMap.get(rcdread.get(i * 3)) == null){
							rcdMap.put(rcdread.get(i * 3) , price[i]);
						}else{
							rcdMap.put(rcdread.get(i * 3) , rcdMap.get(rcdread.get(i * 3)) + price[i]);
						}
					}else{
						codeerror(rcdfile.get(i), "支店");
						System.exit(0);
					}
				}
				if(name.equals("commodity")){
					if(map.get(rcdread.get(i * 3 + 1)) != null){
						if(rcdMap.get(rcdread.get(i * 3 + 1)) == null){
							rcdMap.put(rcdread.get(i * 3 + 1) , price[i]);
						}else{
							rcdMap.put(rcdread.get(i * 3 + 1) , rcdMap.get(rcdread.get(i * 3 + 1)) + price[i]);
						}
					}else{
						codeerror(rcdfile.get(i), "商品");
						System.exit(0);
					}
				}
				br.close();
			}
		}catch(IOException e){
			notfile("売上");
			System.exit(0);
		}
		for(String key : rcdMap.keySet()){
			if(rcdMap.get(key).toString().length() >= 10){
				System.err.println("合計金額が10桁を超えています");
				System.exit(0);
			}
		}
	}

	static boolean fileOutput(String[] args, String files,
			LinkedHashMap<String, String> map, LinkedHashMap<String, Integer> rcdMap){
			try{
				File file = new File(args[0] + File.separator + files);
				FileWriter fw = new FileWriter(file);
				Map<Integer, String> out = new TreeMap<Integer, String>();
				int a = 0;

				for(String str : map.keySet()){
					if(rcdMap.get(str) != null){
						out.put(rcdMap.get(str) , str + ',' + map.get(str));
					}else if(rcdMap.get(str) == null){
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
			}
			return false;
	}

	public static void notfile(String filename){
		System.err.println(filename + "ファイルが存在しません");
	}
	public static void formaterror(String format){
		System.err.println(format + "のフォーマットが不正です");
	}
	public static void codeerror(String code, String filename){
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
		LinkedHashMap<String, Integer> rcdMap = new LinkedHashMap<String, Integer>();

		if(loadFile(args, "branch.lst", regex, branchMap) == null){
			loadFile(args, "branch.lst", regex, branchMap);
		}else if(loadFile(args, "branch.lst", regex, commodityMap).equals("not")){
			notfile("支店定義");
			return;
		}else if(loadFile(args, "branch.lst", regex, commodityMap).equals("format")){
			formaterror("支店定義");
			return;
		}

		if(loadFile(args, "commodity.lst", regex, commodityMap) == null){
			loadFile(args, "commodity.lst", regex, commodityMap);
		}else if(loadFile(args, "commodity.lst", regex, commodityMap).equals("not")){
			notfile("商品定義");
			return;
		}else if(loadFile(args, "commodity.lst", regex, commodityMap).equals("format")){
			formaterror("商品定義");
			return;
		}

		numberCheck(args, rcdfile);

		aggregateData(args, branchMap, "branch", rcdMap, rcdfile);
		aggregateData(args, commodityMap, "commodity", rcdMap, rcdfile);

		if(fileOutput(args, "branch.out",  branchMap, rcdMap) == false){
			fileOutput(args, "branch.out",  branchMap, rcdMap);
		}else{
			notfile("支店別集計");
			return;
		}

		if(fileOutput(args, "commodity.out",  branchMap, rcdMap) == false){
			fileOutput(args, "commodity.out", commodityMap, rcdMap);
		}else{
			notfile("商品別集計");
			return;
		}
	}
}