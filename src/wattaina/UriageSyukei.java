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

	static boolean inputfile(String[] args, String[] filename, String[] fileNameJp,
			String regex, LinkedHashMap<String,String> Map){
		for(int i = 0; i < filename.length; i++){
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]
															+ "\\" + filename[i] + ".lst"));
				String str;
				while((str = br.readLine()) != null){
					if(!(str.matches(regex))){
						System.err.println(fileNameJp[i] + "ファイルのフォーマットが不正です");
						System.exit(0);
					}
					String[] format = str.split(",");
					if(!(format[0].matches(regex))){
						System.err.println(fileNameJp[i] + "ファイルのフォーマットが不正です");
						System.exit(0);
					}
					if(!(format[1].matches(regex))){
						System.err.println(fileNameJp[i] + "ファイルのフォーマットが不正です");
						System.exit(0);
					}

					Map.put(format[0],format[1]);
				}
				br.close();
			}catch(IOException e){
				System.err.println(fileNameJp[i] + "定義ファイルが存在しません");
				System.exit(0);
			}
		}
		return false;
	}
	
	static void tally(String[] args, LinkedHashMap<String,String> Map,LinkedHashMap<String,Integer> rcdMap){
		try{
			ArrayList<String> rcdin = new ArrayList<String>();
			ArrayList<String> rcdfile = new ArrayList<String>();
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
			    if(i+1 != rcd2[i]){
			    	System.err.println("売上ファイル名が連番になっていません");
			    	System.exit(0);
			    }
		    }

		    for(int i = 0; i < rcdfile.size(); i++){
		    	if(rcdfile.get(i).length() != 12){
		    		System.err.println("売上ファイルが存在しません");
		    		return;
		    	}
				BufferedReader br = new BufferedReader(new FileReader(args[0] +"\\" +rcdfile.get(i)));
				String str;
				int a = 0;
				int b = 0;
				int[] price = new int[rcdfile.size()];
				long[] bigprice = new long[rcdfile.size()];
				while((str = br.readLine()) != null){
					if((Map.get(str) != null)){
						rcdin.add(str);
						while((str = br.readLine()) != null){
							if((Map.get(str) != null)){
								rcdin.add(str);
								while((str = br.readLine()) != null){
									if(!(str.length() >= 10)){
										price[i] = Integer.parseInt(str);
									}else{
										bigprice[i] = Long.parseLong(str);
									}
									if(rcdMap.get(rcdin.get(i*2)) == null){
										rcdMap.put(rcdin.get(i*2),price[i]);
									}else{
										rcdMap.put(rcdin.get(i*2),rcdMap.get(rcdin.get(i*2)) + price[i]);
									}
									if(rcdMap.get(rcdin.get(i*2+1)) == null){
										rcdMap.put(rcdin.get(i*2+1),price[i]);
									}else{
										rcdMap.put(rcdin.get(i*2+1),rcdMap.get(rcdin.get(i*2+1)) + price[i]);
									}
									if(rcdMap.get(rcdin.get(i*2)) >= 1000000000 || rcdMap.get(rcdin.get(i*2+1)) >= 1000000000 || bigprice[i] >= 1000000000){
										System.err.println("合計金額が10桁を超えました");
										System.exit(0);
									}
								}
							}else{
								System.err.println(rcdfile.get(i)+"の支店コードが不正です");
								System.exit(0);
							}
						}
					}else{
						System.err.println(rcdfile.get(i)+"のコードが不正です");
						System.exit(0);
					}
					if(a % 3 == 0){
						if(!(b == (a / 3))){
							System.err.println(rcdfile.get(i) + "フォーマットが不正です");
							System.exit(0);
						}
						b++;
					}
					a++;
				}
				br.close();
			}
		}catch(IOException e){
			System.err.println("売上ファイルが存在しません");
			System.exit(0);
		}
	}

	static void outputfile(String[] args, String[] filename,String[] fileNameJp,
			String[] match,LinkedHashMap<String,String> Map,LinkedHashMap<String,Integer> rcdMap){
		for(int i = 0; i < filename.length; i++){
			try {
				File file = new File(args[0] + "\\" + filename[i] + ".out");
				FileWriter fw = new FileWriter(file);
				Map<Integer,String> Out = new TreeMap<Integer,String>();
				int a = 0;

				for(String str: Map.keySet()){
					if(str.matches(match[i])==true){
						if(rcdMap.get(str)!=null){
							Out.put(rcdMap.get(str),str+','+Map.get(str));
						}else if(rcdMap.get(str)==null){
							a++;
							Out.put(a,str+','+Map.get(str));
						}
					}
				}
				List<Entry<Integer, String>> entries = new ArrayList<Entry<Integer, String>>(Out.entrySet());

				Collections.sort(entries, new Comparator<Entry<Integer, String>>() {
				    public int compare(Entry<Integer, String> o1, Entry<Integer, String> o2) {
				        return o2.getKey().compareTo(o1.getKey());    //降順
				    }
				});
				for(Entry<Integer,String> entry : entries){
					if(!(entry.getKey().equals(a))){
						fw.write(Out.get(entry.getKey()) + "," + entry.getKey() + "\n");
					}else{
						fw.write(Out.get(entry.getKey()) + ","+ 0 +"\n");
						a--;
					}
				}
				fw.close();
			} catch (IOException e) {
				System.err.println(fileNameJp[i] + "別集計ファイルが存在しません");
				System.exit(0);
			}
		}
	}

	public static void main(String[] args) {
		if(args.length != 1){
			System.err.println("正しくディレクトリを指定してください。");
			System.exit(1);
		}
		String[] fileNameJp = new String[5];
		fileNameJp[0] = "支店";
		fileNameJp[1] = "商品";
		fileNameJp[2] = "売上";
		String[] filename = new String[2];
		filename[0] = "branch";
		filename[1] = "commodity";
		String regex = "^\\d{3},\\D{1,}支店$|^\\d{3}|^\\D{1,}支店|"
				+ "^\\D{3}\\d{5},\\D{1,}[^,]$|^\\D{3}\\d{5},\\D{1,}[^,]\\D|^\\D{3}\\d{5}|^\\D{1,}";
		LinkedHashMap<String, String> Map = new LinkedHashMap<String, String>();

		inputfile(args, filename, fileNameJp, regex, Map);

		LinkedHashMap<String,Integer> rcdMap = new LinkedHashMap<String,Integer>();
		tally(args, Map, rcdMap);
		String[] match = new String[filename.length];
		match[0] = "^\\d{3}";
		match[1] = "^\\D{3}\\d{5}";
		outputfile(args, filename, fileNameJp, match, Map, rcdMap);

	}
}
