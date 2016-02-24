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

	static String more = "formatMore";
	static String match = "notMatch";
	static String illegal = "illegalFormat";
	static String over = "overflow";
	static String fraud = "codeFraud";

	static String loadFile(String[] args, String files, String regex,
			LinkedHashMap<String, String> definitionMap,
			LinkedHashMap<String, Integer> salesMap) throws IOException, FileNotFoundException {
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(args[0] + File.separator + files));
			String str;

			while((str = br.readLine()) != null){
				String[] format = str.split(",");
				if(format.length != 2){
					return "formatMore";
				}
				if(!format[0].matches(regex)){
					return "notMatch";
				}
				definitionMap.put(format[0],format[1]);
				salesMap.put(format[0], 0);
			}
		}catch(FileNotFoundException e){
			throw e;
		}catch(IOException e){
			throw e;
		}finally{
			if (br != null){
				br.close();
			}
		}
		return "";
	}

	static boolean numberCheck(String[] args, ArrayList<String> rcdfile){
		File dir = new File(args[0]);
		File[] files = dir.listFiles();
		for(int i = 0; i < files.length; i++){
			if(files[i].getName().matches("^\\d{8}.rcd$") && files[i].isFile()){
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
				return false;
			}
		}
		return true;
	}

	static String aggregateData(String[] args, LinkedHashMap<String, Integer> beanchSalesMap, LinkedHashMap<String, Integer> commoditySalesMap,
			LinkedHashMap<String, Integer>rcdMap, ArrayList<String> rcdfile) throws IOException{
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

				if(rcdRead.isEmpty()){
					errorHandingDisplay(rcdfile.get(i), "illegalFormat");
					return "illegalFormat";
				}
				if(rcdRead.size() % 3 != 0){
					errorHandingDisplay(rcdfile.get(i), "illegalFormat");
					return "illegalFormat";
				}
				if(rcdRead.get(2).length() < 10){
					price = Integer.parseInt(rcdRead.get(2));
				}else{
					return "overflow";
				}
				if(beanchSalesMap.containsKey(rcdRead.get(0))){
					beanchSalesMap.put(rcdRead.get(0), beanchSalesMap.get(rcdRead.get(0)) + price);
				}else{
					errorHandingDisplay(rcdfile.get(i) + "の支店", "codeFraud");
					return "codeFraud";
				}
				if(commoditySalesMap.containsKey(rcdRead.get(1))){
					commoditySalesMap.put(rcdRead.get(1), commoditySalesMap.get(rcdRead.get(1)) + price);
				}else{
					errorHandingDisplay(rcdfile.get(i) + "の商品", "codeFraud");
					return "codeFraud";
				}
				for(String key : rcdMap.keySet()){
					if(rcdMap.get(key).toString().length() > 10){
						return "overflow";
					}
				}
				br.close();
			}
		}catch(IOException e){
			throw e;
		}finally{
			if(br != null){
				br.close();
			}
		}
		return "";
	}

	static void fileOutput(String[] args, String files,
		LinkedHashMap<String, String> map, LinkedHashMap<String, Integer> salesMap) throws IOException{
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
		}
		catch(IOException e){
			throw e;
		}finally{
			if(fw != null){
				try{
					fw.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}

	static void errorHandingDisplay(String filename, String errorCode){
		if(errorCode.equals("noFile")){
			System.err.println(filename + "ファイルが存在しません");
		}
		if(errorCode.equals("illegalFormat")){
			System.err.println(filename + "のフォーマットが不正です");
		}
		if(errorCode.equals("overflow")){
			System.err.println(filename + "金額が10桁を超えました");
		}
		if(errorCode.equals("codeFraud")){
			System.err.println(filename + "コードが不正です");
		}
		if(errorCode.equals("notSequence")){
			System.err.println(filename + "ファイル名が連番になっていません");
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
		boolean hasError = false;
		try{
			str = loadFile(args, "branch.lst", "^\\d{3}$", branchMap, beanchSalesMap);
			if(more.equals(str)){
				errorHandingDisplay("支店定義", str);
				return;
			}else if(match.equals(str)){
				errorHandingDisplay("支店定義", str);
				return;
			}
		}catch(FileNotFoundException e){
			errorHandingDisplay("支店定義", "noFile");
			return;
		}catch(IOException e){
			return;
		}
		try{
			str = loadFile(args, "commodity.lst", "^\\w{8}$", commodityMap, commoditySalesMap);
			if(more.equals(str)){
				errorHandingDisplay("商品定義", str);
				return;
			}else if(match.equals(str)){
				errorHandingDisplay("商品定義", str);
				return;
			}
		}catch(FileNotFoundException e){
			errorHandingDisplay("商品定義", "noFile");
			return;
		}

		hasError = numberCheck(args, rcdfile);
		if(hasError == false){
			errorHandingDisplay("売上", "notSequence");
			return;
		}

		try{
			str = aggregateData(args, beanchSalesMap, commoditySalesMap, rcdMap, rcdfile);
			if(illegal.equals(str)){
				return;
			}else if(over.equals(str)){
				errorHandingDisplay("合計", "overflow");
				return;
			}else if(fraud.equals(str)){
				return;
			}
		}catch(IOException e){
			errorHandingDisplay("売上", "noFile");
			return;
		}

		try{
			fileOutput(args, "branch.out",  branchMap, beanchSalesMap);
		}catch(IOException e){
			return;
		}

		try{
			fileOutput(args, "commodity.out",  commodityMap, commoditySalesMap);
		}catch(IOException e){
			return;
		}
	}
}