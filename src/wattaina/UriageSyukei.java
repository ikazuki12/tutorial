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

public class UriageSyukei {

	static String no = "fileDoseNotExist";
	static String more = "formatMore";
	static String exists = "linefeedExists";
	static String match = "notMatch";
	static String illegal = "illegalFormat";
	static String over = "overflow";
	static String fraud = "codeFraud";
	static String different  = "notSequence";

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
					return exists;
				}
				String[] format = str.split(",");
				if(format.length != 2){
					return more;
				}
				if(!format[0].matches(regex)){
					return match;
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
				if(rcdRead.isEmpty() || rcdRead.size() != 3){
					errorHandingDisplay(rcdfile.get(i), illegal);
					return illegal;
				}
				if(branchSalesMap.containsKey(rcdRead.get(0))){
					branchSalesMap.put(rcdRead.get(0), branchSalesMap.get(rcdRead.get(0)) + Long.parseLong(rcdRead.get(2)));
				}else{
					errorHandingDisplay(rcdfile.get(i) + "の支店", fraud);
					return fraud;
				}
				if(commoditySalesMap.containsKey(rcdRead.get(1))){
					commoditySalesMap.put(rcdRead.get(1), commoditySalesMap.get(rcdRead.get(1)) + Long.parseLong(rcdRead.get(2)));
				}else{
					errorHandingDisplay(rcdfile.get(i) + "の商品", fraud);
					return fraud;
				}
				for(String key : branchSalesMap.keySet()){
					if(branchSalesMap.get(key).toString().length() > 10){
						return over;
					}
				}
				for(String key : commoditySalesMap.keySet()){
					if(commoditySalesMap.get(key).toString().length() > 10){
						errorHandingDisplay("合計", over);
						return over;
					}
				}
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
			List<Map.Entry<String,Long>> entries = new ArrayList<Map.Entry<String,Long>>(salesMap.entrySet());
				Collections.sort(entries, new Comparator<Map.Entry<String,Long>>() {
					@Override
					public int compare(Entry<String,Long> entry1, Entry<String,Long> entry2) {
							return ((Long)entry2.getValue()).compareTo((Long)entry1.getValue());
						}
					});
			for(Entry<String, Long> entry : entries){
				fw.write(entry.getKey() + "," + map.get(entry.getKey()) + "," +
						entry.getValue() + System.getProperty("line.separator"));
			}
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
		if(errorCode.equals(no)){
			System.err.println(filename + "ファイルが存在しません");
		}
		if(errorCode.equals(more) || errorCode.equals(illegal)
				|| errorCode.equals(exists) || errorCode.equals(match)){
			System.err.println(filename + "のフォーマットが不正です");
		}
		if(errorCode.equals(over)){
			System.err.println(filename + "金額が10桁を超えました");
		}
		if(errorCode.equals(fraud)){
			System.err.println(filename + "コードが不正です");
		}
		if(errorCode.equals(different)){
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

		String errorCharacter = "";
		boolean hasError = false;
		try{
			errorCharacter = loadFile(args, "branch.lst", "^\\d{3}", branchMap, branchSalesMap);
			if(!errorCharacter.equals("")){
				errorHandingDisplay("支店定義", errorCharacter);
				return;
			}
		}catch(FileNotFoundException e){
			errorHandingDisplay("支店定義", no);
			return;
		}catch(IOException e){
			return;
		}
		try{
			errorCharacter = loadFile(args, "commodity.lst", "^\\w{8}$", commodityMap, commoditySalesMap);
			if(!errorCharacter.equals("")){
				errorHandingDisplay("商品定義", errorCharacter);
				return;
			}
		}catch(FileNotFoundException e){
			errorHandingDisplay("商品定義", no);
			return;
		}

		hasError = numberCheck(args, rcdfile);
		if(!hasError){
			errorHandingDisplay("売上", different);
			return;
		}

		try{
			errorCharacter = aggregateData(args, branchSalesMap, commoditySalesMap, rcdMap, rcdfile);
			if(!errorCharacter.equals("")){
				return;
			}
		}catch(IOException e){
			errorHandingDisplay("売上", no);
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