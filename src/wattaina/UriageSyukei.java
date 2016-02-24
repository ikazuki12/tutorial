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
	static String exists = "linefeedExists";
	static String match = "notMatch";
	static String illegal = "illegalFormat";
	static String over = "overflow";
	static String fraud = "codeFraud";

	static String loadFile(String[] args, String files, String regex,
			LinkedHashMap<String, String> definitionMap,
			LinkedHashMap<String, Long> salesMap) throws IOException, FileNotFoundException {
		BufferedReader br = null;
		try{
			FileReader file = new FileReader(args[0] + File.separator + files);
			br = new BufferedReader(file);
			String str;
			while((str = br.readLine()) != null){
				if(str.equals("")){
					return "linefeedExists";
				}
				String[] format = str.split(",");
				if(format.length != 2){
					return "formatMore";
				}
				if(!format[0].matches(regex)){
					return "notMatch";
				}
				definitionMap.put(format[0],format[1]);
				salesMap.put(format[0], (long) 0);
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
		for(int i=0; i < rcdfile.size(); i++){
			rcd = rcdfile.get(i).split("\\.");
			Arrays.sort(rcd);
			if(i + 1 != Integer.parseInt(rcd[0])){
				return false;
			}
		}
		return true;
	}

	static String aggregateData(String[] args, LinkedHashMap<String, Long> branchSalesMap,
			LinkedHashMap<String, Long> commoditySalesMap,
			LinkedHashMap<String, Integer>rcdMap,
			ArrayList<String> rcdfile) throws IOException{
		BufferedReader br = null;
		try{
		    for(int i = 0; i < rcdfile.size(); i++){
		    	ArrayList<String> rcdRead = new ArrayList<String>();
				br = new BufferedReader(new FileReader(args[0] + File.separator + rcdfile.get(i)));
				String str;
				while((str = br.readLine()) != null){
					rcdRead.add(str);
				}
				if(rcdRead.isEmpty() || (double) rcdRead.size() / 3 != 1){
					errorHandingDisplay(rcdfile.get(i), "illegalFormat");
					return "illegalFormat";
				}
				if(branchSalesMap.containsKey(rcdRead.get(0))){
					branchSalesMap.put(rcdRead.get(0), branchSalesMap.get(rcdRead.get(0)) + Long.parseLong(rcdRead.get(2)));
				}else{
					errorHandingDisplay(rcdfile.get(i) + "の支店", "codeFraud");
					return "codeFraud";
				}
				if(commoditySalesMap.containsKey(rcdRead.get(1))){
					commoditySalesMap.put(rcdRead.get(1), commoditySalesMap.get(rcdRead.get(1)) + Long.parseLong(rcdRead.get(2)));
				}else{
					errorHandingDisplay(rcdfile.get(i) + "の商品", "codeFraud");
					return "codeFraud";
				}
				for(String key : branchSalesMap.keySet()){
					if(branchSalesMap.get(key).toString().length() > 10){
						return "overflow";
					}
				}
				for(String key : commoditySalesMap.keySet()){
					if(commoditySalesMap.get(key).toString().length() > 10){
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
		LinkedHashMap<String, String> map, LinkedHashMap<String, Long> salesMap) throws IOException{
		FileWriter fw = null;
		try{
			File file = new File(args[0] + File.separator + files);
			fw = new FileWriter(file);
			Map<Long, String> out = new TreeMap<Long, String>();
			long a = 0;
			for(String str : map.keySet()){
				if(salesMap.get(str) != 0){
					out.put(salesMap.get(str), str + ',' + map.get(str));
				}else{
					a++;
					out.put(a,  str + ',' + map.get(str) + "," + salesMap.get(str));
				}
			}
			List<Entry<Long, String>> entries = new ArrayList<Entry<Long, String>>(out.entrySet());
			Collections.sort(entries, new Comparator<Entry<Long, String>>(){
				@Override
				public int compare(Entry<Long, String> o1, Entry<Long, String> o2){
					return o2.getKey().compareTo(o1.getKey());
				}
			});
			for(Entry<Long, String> entry : entries){
				if(!entry.getKey().equals(a)){
					fw.write(out.get(entry.getKey()) + "," + entry.getKey() + System.getProperty("line.separator"));
				}else{
					fw.write(out.get(entry.getKey()) + System.getProperty("line.separator"));
					a--;
				}
			}
			fw.close();
		}
		catch(IOException e){
			throw e;
		}finally{
			if(fw != null){
				fw.close();
			}
		}
	}

	static void errorHandingDisplay(String filename, String errorCode){
		if(errorCode.equals("noFile")){
			System.err.println(filename + "ファイルが存在しません");
		}
		if(errorCode.equals("formatMore") || errorCode.equals("illegalFormat")
				|| errorCode.equals("linefeedExists")){
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
		LinkedHashMap<String, Long> branchSalesMap = new LinkedHashMap<String, Long>();
		LinkedHashMap<String, Long> commoditySalesMap = new LinkedHashMap<String, Long>();
		LinkedHashMap<String, Integer> rcdMap = new LinkedHashMap<String, Integer>();

		String errorCharacter = null;
		boolean hasError = false;
		try{
			errorCharacter = loadFile(args, "branch.lst", "^\\d{3}$", branchMap, branchSalesMap);
			if(more.equals(errorCharacter)){
				errorHandingDisplay("支店定義", errorCharacter);
				return;
			}else if(exists.equals(errorCharacter)){
				errorHandingDisplay("支店定義", errorCharacter);
				return;
			}else if(match.equals(errorCharacter)){
				errorHandingDisplay("支店定義", errorCharacter);
				return;
			}
		}catch(FileNotFoundException e){
			errorHandingDisplay("支店定義", "noFile");
			return;
		}catch(IOException e){
			return;
		}
		try{
			errorCharacter = loadFile(args, "commodity.lst", "^\\w{8}$", commodityMap, commoditySalesMap);
			if(more.equals(errorCharacter)){
				errorHandingDisplay("商品定義", errorCharacter);
				return;
			}else if(exists.equals(errorCharacter)){
				errorHandingDisplay("商品定義", errorCharacter);
				return;
			}else if(match.equals(errorCharacter)){
				errorHandingDisplay("商品定義", errorCharacter);
				return;
			}
		}catch(FileNotFoundException e){
			errorHandingDisplay("商品定義", "noFile");
			return;
		}

		hasError = numberCheck(args, rcdfile);
		if(!hasError){
			errorHandingDisplay("売上", "notSequence");
			return;
		}

		try{
			errorCharacter = aggregateData(args, branchSalesMap, commoditySalesMap, rcdMap, rcdfile);
			if(illegal.equals(errorCharacter)){
				return;
			}else if(over.equals(errorCharacter)){
				errorHandingDisplay("合計", "overflow");
				return;
			}else if(fraud.equals(errorCharacter)){
				return;
			}
		}catch(IOException e){
			errorHandingDisplay("売上", "noFile");
			return;
		}

		try{
			fileOutput(args, "branch.out",  branchMap, branchSalesMap);
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