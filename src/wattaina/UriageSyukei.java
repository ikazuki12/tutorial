package wattaina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class UriageSyukei {
	public static void main(String[] args) {
			if(args.length != 1){
				System.out.println("正しくディレクトリを指定してください。");
				System.exit(1);
			}
			LinkedHashMap<String, String> branchMap = new LinkedHashMap<String, String>();
			ArrayList<String> branchCode = new ArrayList<String>();
			//商品定義ファイルを読み込む
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\branch.lst"));

				String str;
				while((str = br.readLine()) != null){
					String[] branch = str.split(",");
					if(!(branch[0].matches("^\\d{3}"))){
						System.out.println("支店定義ファイルのフォーマットが不正です");
						return;
					}
					if(!(branch[1].matches("^\\D{2,3}支店"))){
						System.out.println("支店定義ファイルのフォーマットが不正です");
						return;
					}

					branchMap.put(branch[0], branch[1]);
					branchCode.add(branch[0]);

				}
				br.close();
			}catch(IOException e){
				System.out.println("支店定義ファイルが存在しません");
			}
			//商品定義ファイルのフォーマットエラー処理//

			//商品定義ファイルを読み込む
			LinkedHashMap<String, String> commodityMap = new LinkedHashMap<String, String>();
			ArrayList<String> commodityCode = new ArrayList<String>();
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\commodity.lst"));

				String str;
				while((str = br.readLine()) != null){
					String[] commodity = str.split(",");
					if(!(commodity[0].matches("^\\D{3}\\d{5}"))){
						System.out.println("商品定義ファイルのフォーマットが不正です");
						return;
					}
					if((commodity[1].matches(",\\n"))){
						System.out.println("商品定義ファイルのフォーマットが不正です");
						return;
					}
					commodityMap.put(str.substring(0,8), str.substring(9));
					commodityCode.add(str.substring(0,8));
				}
				br.close();
			}catch(IOException e){
				System.out.println("商品定義ファイルが存在しません");
			}

			ArrayList<String> rcdin = new ArrayList<String>();
			ArrayList<String> rcdfile = new ArrayList<String>();
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
					BufferedReader br = new BufferedReader(new FileReader(args[0] +"\\" +rcdfile.get(i)));
					String str;
					int a = 0;
					int b = 0;
					while((str = br.readLine()) != null){
						rcdin.add(str);
						a++;
						if(a % 3 == 0){
							if(!(b == (i / 3))){
								System.out.println(rcdfile.get(i) + "フォーマットが不正です");
								return;
							}
							b++;
						}
					}
					br.close();
				}
			}catch(IOException e){
				System.out.println("売上ファイルが存在しません");
			}
			//売上ファイルファイルフォーマット//
			for(int i=0; i < (rcdin.size()/3); i++){
				if(!(rcdin.get(i*3).matches("\\d{3}"))){
					System.out.println(rcdfile.get(i)+"の支店コードが不正です");
					return;
				}
				if(!(rcdin.get(i*3+1).matches("\\D{3}\\d{5}"))){
					System.out.println(rcdfile.get(i)+"の商品コードが不正です");
					return;
				}
			}
			//売上集計//
			int[] val = new int[rcdin.size()/3];
			for(int i=0; i < (branchMap.size()/2); i++){
				for(int j = 0; j <(branchMap.size()/2);j++){
					if( ! (branchMap.get(rcdin.get(i*3)) == null)){
						val[i] = Integer.parseInt(rcdin.get(i*3+2));
						if(branchMap.get(rcdin.get(i*3))==rcdin.get(j*3)){
							val[i] = val[i]+val[j];
						}
					}else{
						val[i] = 0;
					}
				}
				if(val[i] >= 100000000){
					System.out.println("合計結果が10桁を超えました");
					return;
				}
			}

			try {
				File file = new File(args[0]+"\\branch.out");
				FileWriter fw = new FileWriter(file);
				String[] branchOut = new String[branchMap.size()];
				int a=0;
				for(int i = 0; i < (branchMap.size()); i++){
					for(int j = 0; j < (rcdin.size()/3); j++){
						if((branchCode.get(i).equals(rcdin.get(j*3)))){
							if((rcdin.get(j*3).equals(branchCode.get(i)))){
								branchOut[i] = (rcdin.get(a*3)+","+branchMap.get(rcdin.get(a*3))+","+val[a]+"\n");
								a++;
								break;
							}
						}else if(!(branchCode.get(i).equals(rcdin.get(j*3)))){
							if(!(branchCode.get(i).equals(rcdin.get(j*3)))){
								branchOut[i] = (branchCode.get(i)+","+branchMap.get(branchCode.get(i))+","+0+"\n");
							}
						}
					}
					fw.write(branchOut[i]);
				}

				fw.close();
			} catch (IOException e) {
				System.out.println("支店別集計ファイルが存在しません");
			}
			try {
				File file = new File(args[0]+"\\commodity.out");
				FileWriter fw = new FileWriter(file);
				String[] commodityOut = new String[commodityMap.size()];

				int a=0;
				for(int i = 0; i < (commodityMap.size()); i++){
					for(int j = 0; j < (rcdin.size()/3); j++){
						if((commodityCode.get(i).equals(rcdin.get(j*3+1)))){
							if((rcdin.get(j*3+1).equals(commodityCode.get(i)))){
								commodityOut[i] = (rcdin.get(a*3+1)+","+commodityMap.get(rcdin.get(a*3+1))+","+val[a]+"\n");
								a++;
								break;
							}
						}else if(!(commodityCode.get(i).equals(rcdin.get(j*3+1)))){
							if(!(commodityCode.get(i).equals(rcdin.get(j*3+1)))){
								commodityOut[i] = (commodityCode.get(i)+","+commodityMap.get(commodityCode.get(i))+","+0+"\n");
							}
						}
					}
					fw.write(commodityOut[i]);
				}

				fw.close();
			} catch (IOException e) {
				System.out.println("商品別集計ファイルが存在しません");
			}

	}

}
