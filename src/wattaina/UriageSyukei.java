package wattaina;
import java.io.BufferedReader;
import java.io.File;
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

	static boolean inputfile(String[] args, ArrayList<String> listfile, String regex, LinkedHashMap<String, String> Map, int filenum){
		for(int i = 0; i < listfile.size(); i++){
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0] + File.separator + listfile.get(i)));
				String str;
				while((str = br.readLine()) != null){
					if(!(str.matches(regex))){
						
					}
					String[] format = str.split(",");
					Map.put(format[0],format[1]);
				}
				br.close();
			}catch(IOException e){
				filenum = filenum + i;
				notfile(null, filenum);
			}
		}
		return false;
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
		    	System.exit(0);
		    }
	    }
	}

	static void tally(String[] args, LinkedHashMap<String, String> Map,
			LinkedHashMap<String, Integer> rcdMap, ArrayList<String> rcdfile, int filenum){
		try{
		    ArrayList<String> rcdread = new ArrayList<String>();
		    for(int i = 0; i < rcdfile.size(); i++){
				BufferedReader br = new BufferedReader(new FileReader(args[0] + File.separator + rcdfile.get(i)));
				String str;
				while((str = br.readLine()) != null){
					rcdread.add(str);
				}
				int[] price = new int[rcdfile.size()];
				price[i] = Integer.parseInt(rcdread.get(i * 3 + 2));
				if(price[i] <= 1000000000){
					price[i] = Integer.parseInt(rcdread.get(i * 3 + 2));
				}else{
					System.err.println("売上金額が10桁を超えました");
				}
				if(Map.get(rcdread.get(i * 3)) != null){
					if(rcdMap.get(rcdread.get(i * 3)) == null){
						rcdMap.put(rcdread.get(i * 3) , price[i]);
					}else{
						rcdMap.put(rcdread.get(i * 3) , rcdMap.get(rcdread.get(i * 3)) + price[i]);
					}
				}else{
					System.err.println(rcdfile.get(i) + "の支店コードが不正です");
					System.exit(0);
				}
				if(Map.get(rcdread.get(i * 3 + 1)) != null){
					if(rcdMap.get(rcdread.get(i * 3 + 1)) == null){
						rcdMap.put(rcdread.get(i * 3 + 1) , price[i]);
					}else{
						rcdMap.put(rcdread.get(i * 3 + 1) , rcdMap.get(rcdread.get(i * 3 + 1)) + price[i]);
					}
				}else{
					System.err.println(rcdfile.get(i) + "の商品コードが不正です");
					System.exit(0);
				}
				br.close();
			}
		}catch(IOException e){
			filenum = filenum + 1;
			notfile(null, filenum);
		}
		for(String key : rcdMap.keySet()){
			if(rcdMap.get(key) >= 1000000000){
				System.err.println("合計金額が10桁を超えています");
				System.exit(0);
			}
		}
	}

	static void outputfile(String[] args, ArrayList<String> outfile, String[] match,
			LinkedHashMap<String, String> Map, LinkedHashMap<String, Integer> rcdMap, int filenum){
		for(int i = 0; i < outfile.size(); i++){
			try{
				File file = new File(args[0] + File.separator + outfile.get(i));
				FileWriter fw = new FileWriter(file);
				Map<Integer, String> Out = new TreeMap<Integer, String>();
				int a = 0;

				for(String str : Map.keySet()){
					if(str.matches(match[i]) == true){
						if(rcdMap.get(str) != null){
							Out.put(rcdMap.get(str) , str + ',' + Map.get(str));
						}else if(rcdMap.get(str) == null){
							a++;
							Out.put(a , str + ',' + Map.get(str));
						}
					}
				}
				List<Entry<Integer, String>> entries = new ArrayList<Entry<Integer, String>>(Out.entrySet());
				Collections.sort(entries, new Comparator<Entry<Integer, String>>() {
				    public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2) {
				        return o2.getKey().compareTo(o1.getKey());
				    }
				});
				for(Entry<Integer,String> entry : entries){
					if(!(entry.getKey().equals(a))){
						fw.write(Out.get(entry.getKey()) + "," + entry.getKey() + System.getProperty("line.separator"));
					}else{
						fw.write(Out.get(entry.getKey()) + "," + 0 + System.getProperty("line.separator"));
						a--;
					}
				}
				fw.close();
			} catch (IOException e) {
				filenum = filenum + 2 + i;
				notfile(null, filenum);
			}
		}
	}
	public static void notfile(String[] filename, int filenum){
		main(filename);
		System.err.println(filename[filenum] + "ファイルが存在しません");
		System.exit(0);
	}
	public static void formaterror(){
		
	}

	public static void main(String[] args) {
		if(args.length != 1){
			System.err.println("正しくディレクトリを指定してください。");
			System.exit(1);
		}
		ArrayList<String> listfile = new ArrayList<String>();
		ArrayList<String> outfile = new ArrayList<String>();
		ArrayList<String> rcdfile = new ArrayList<String>();
		File dir = new File(args[0]);
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if(file.getName().matches("^\\D{1,}.lst$")) {
				listfile.add(file.getName());
			}else if(file.getName().matches("^\\D{1,}.out$")){
				outfile.add(file.getName());
			}
		}
		String[] filename = new String[5];
		filename[0] = "支店定義";
		filename[1] = "商品定義";
		filename[2] = "売上";
		filename[3] = "支店別集計";
		filename[4] = "商品別集計";
		int filenum = 0;
		String regex = "^\\d{3},[^,]\\D{1,}[^,]$|^\\w{8},[^,]\\D{1,}[^,]$";
		LinkedHashMap<String, String> Map = new LinkedHashMap<String, String>();
		LinkedHashMap<String, Integer> rcdMap = new LinkedHashMap<String, Integer>();
		String[] match = new String[listfile.size()];
		match[0] = "^\\d{3}";
		match[1] = "^\\D{3}\\d{5}";
		inputfile(args, listfile, regex, Map, filenum);
		numberCheck(args, rcdfile);
		tally(args, Map, rcdMap, rcdfile ,filenum);
		outputfile(args, outfile, match, Map, rcdMap ,filenum);
	}
}