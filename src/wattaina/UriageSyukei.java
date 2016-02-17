package wattaina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class UriageSyukei {
	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("正しくディレクトリを指定してください。");
			System.exit(1);
		}
		LinkedHashMap<String, String> branchMap = new LinkedHashMap<String, String>();
		ArrayList<String> branchCode = new ArrayList<String>();
//		商品定義ファイルを読み込む		//
		try{
			BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\branch.lst"));
			String str;
			while((str = br.readLine()) != null){
				if(!(str.matches("^\\d{3},\\D{1,}支店$"))){
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				}
				String[] branch = str.split(",");
				if(!(branch[0].matches("^\\d{3}"))){
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				}
				if(!(branch[1].matches("^\\D{1,}支店"))){
					System.out.println("支店定義ファイルのフォーマットが不正です");
					return;
				}
				branchMap.put(branch[0], branch[1]);
				branchCode.add(branch[0]);
			}
			br.close();
		}catch(IOException e){
			System.out.println("支店定義ファイルが存在しません");
			return;
		}
//		商品定義ファイルを読み込む		//
		LinkedHashMap<String, String> commodityMap = new LinkedHashMap<String, String>();
		ArrayList<String> commodityCode = new ArrayList<String>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\commodity.lst"));
			String str;
			while((str = br.readLine()) != null){
				if(!(str.matches("^\\D{3}\\d{5},\\D{1,}[^,]$"))){
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				if(!(str.matches("^\\D{3}\\d{5},\\D{1,}[^,]\\D"))){
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				String[] commodity = str.split(",");
				if(!(commodity[0].matches("^\\D{3}\\d{5}"))){
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				if(!(commodity[1].matches("^\\D{1,}"))){
					System.out.println("商品定義ファイルのフォーマットが不正です");
					return;
				}
				commodityMap.put(commodity[0], commodity[1]);
				commodityCode.add(commodity[0]);
			}
			br.close();
		}catch(IOException e){
			System.out.println("商品定義ファイルが存在しません");
			return;
		}

		ArrayList<String> rcdin = new ArrayList<String>();
		ArrayList<String> rcdfile = new ArrayList<String>();
		LinkedHashMap<String,Integer> rcdMap = new LinkedHashMap<String,Integer>();
		try{
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
			    	System.out.println("売上ファイル名が連番になっていません");
			    	return;
			    }
		    }
		    
		    for(int i = 0; i < rcdfile.size(); i++){
		    	if(rcdfile.get(i).length() != 12){
		    		System.out.println("支店定義ファイルが存在しません");
		    		return;
		    	}
				BufferedReader br = new BufferedReader(new FileReader(args[0] +"\\" +rcdfile.get(i)));
				String str;
				int a = 0;
				int b = 0;
				int[] price = new int[rcdfile.size()];
				long[] bigprice = new long[rcdfile.size()];
				while((str = br.readLine()) != null){
					if((branchMap.get(str) != null)){
						rcdin.add(str);
						while((str = br.readLine()) != null){
							if((commodityMap.get(str) != null)){
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
										System.out.println("合計金額が10桁を超えました");
										return;
									}
								}
							}else{
								System.out.println(rcdfile.get(i)+"の支店コードが不正です");
								return;
							}
						}
					}else{
						System.out.println(rcdfile.get(i)+"の支店コードが不正です");
						return;
					}
					if(a % 3 == 0){
						if(!(b == (a / 3))){
							System.out.println(rcdfile.get(i) + "フォーマットが不正です");
							return;
						}
						b++;
					}
					a++;
				}
				br.close();
			}
		}catch(IOException e){
			System.out.println("売上ファイルが存在しません");
			return;
		}
		try {
			File file = new File(args[0]+"\\branch.out");
			FileWriter fw = new FileWriter(file);
			ArrayList<Integer> branchList = new ArrayList<Integer>();
			HashMap<Integer,String> branch = new HashMap<Integer,String>();
			int a = 0;
			for(int i = 0; i < (branchMap.size()); i++){
				if(rcdMap.get(branchCode.get(i))!=null){
					branchList.add(rcdMap.get(branchCode.get(i)));
					branch.put(rcdMap.get(branchCode.get(i)),branchCode.get(i)+','+branchMap.get(branchCode.get(i)));
				}else{
					branchList.add(0);
					branch.put(a,branchCode.get(i)+','+branchMap.get(branchCode.get(i)));
					a++;
				}
			}
			Collections.sort(branchList);
		    Collections.reverse(branchList);
			a=0;
			for(int i = 0; i < (branchMap.size()); i++){
				if(!(branchList.get(i).equals(0))){
					fw.write(branch.get(branchList.get(i)) + "," + branchList.get(i) + "\n");
				}else{
					fw.write(branch.get(a) + "," + branchList.get(i) + "\n");
					a++;
				}
			}
			fw.close();
		} catch (IOException e) {
			System.out.println("支店別集計ファイルが存在しません");
		}

		try {
			File file = new File(args[0]+"\\commodity.out");
			FileWriter fw = new FileWriter(file);
			ArrayList<Integer> commodityList = new ArrayList<Integer>();
			HashMap<Integer,String> commodity = new HashMap<Integer,String>();
			int a = 0;
			for(int i = 0; i < (commodityMap.size()); i++){
				if(rcdMap.get(commodityCode.get(i))!=null){
					commodityList.add(rcdMap.get(commodityCode.get(i)));
					commodity.put(rcdMap.get(commodityCode.get(i)),commodityCode.get(i)+','+commodityMap.get(commodityCode.get(i)));
				}else{
					commodityList.add(0);
					commodity.put(a,commodityCode.get(i)+','+commodityMap.get(commodityCode.get(i)));
					a++;
				}
			}
			Collections.sort(commodityList);
		    Collections.reverse(commodityList);
			a=0;
			for(int i = 0; i < (commodityMap.size()); i++){
				if(!(commodityList.get(i).equals(0))){
					fw.write(commodity.get(commodityList.get(i)) + "," + commodityList.get(i) + "\n");
				}else{
					fw.write(commodity.get(a) + "," + commodityList.get(i) + "\n");
					a++;
				}
			}
			fw.close();
		} catch (IOException e) {
			System.out.println("商品別集計ファイルが存在しません");
			return;
		}
	}
}
