package wattaina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class UriageSyukei {
	public static void main(String[] args) {
			if(args.length != 1){
				System.out.println("正しくディレクトリを指定してください。");
				System.exit(1);
			}
			LinkedHashMap<String, String> branchMap = new LinkedHashMap<String, String>();
			//商品定義ファイルを読み込む
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\branch.lst"));

				String str;
				int i =0;
				while((str = br.readLine()) != null){
					branchMap.put("code"+i, str.substring(0,3));
					branchMap.put("name"+i, str.substring(4));
					i=i+1;
				}
//				System.out.println(branchMap.size());
				br.close();
			}catch(IOException e){
				System.out.println("支店定義ファイルが存在しません");
			}
			//商品定義ファイルを読み込む
			LinkedHashMap<String, String> commodityMap = new LinkedHashMap<String, String>();
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\commodity.lst"));


				String str;
				int i=0;
				while((str = br.readLine()) != null){
					commodityMap.put("code"+i, str.substring(0,8));
					commodityMap.put("name"+i, str.substring(9));
					i=i+1;
				}
				br.close();
			}catch(IOException e){
				System.out.println("商品定義ファイルが存在しません");
			}

			ArrayList<String> rcdin = new ArrayList<String>();
			try{
				File dir = new File(args[0]);
			    File[] files = dir.listFiles();
			    ArrayList<String> rcdfile = new ArrayList<String>();
			    for (int i = 0; i < files.length; i++) {
			        File file = files[i];

					if(file.getName().matches("^\\d{8}.rcd$")) {
						rcdfile.add(file.getName());
					}

			    }

				for (int i = 0; i < rcdfile.size(); i++){
					 BufferedReader br = new BufferedReader(new FileReader(args[0] +"\\" +rcdfile.get(i)));
					 String str;
					 while((str = br.readLine()) != null){
					 	rcdin.add(str);
					 }
					 br.close();
				}
			}catch(IOException e){
				System.out.println("売上ファイルが存在しません");
			}
			try {
				File file = new File(args[0]+"\\branch.out");
				FileWriter fw = new FileWriter(file);
				int a=0;
				for(int i=0;i<(branchMap.size()/2);i++){
					if(branchMap.get("code"+i).equals(rcdin.get(a*3))){
						System.out.println(branchMap.get("code"+i)+","+branchMap.get("name"+i)+","+rcdin.get(a*3+2));
						a = a+1;
					}else if(!(branchMap.get("code"+i).equals(rcdin.get(a*3)))){
						System.out.println(branchMap.get("code"+i)+","+branchMap.get("name"+i)+"," +0);
					}
				}
				fw.close();
			} catch (IOException e) {
				System.out.println("支店別集計ファイルが存在しません");
			}
			try {
				File file = new File(args[0]+"\\branch.out");
				FileWriter fw = new FileWriter(file);
				int a=0;
				for(int i=0;i<(branchMap.size()/2);i++){
					if(branchMap.get("code"+i).equals(rcdin.get(a*3))){
						fw.write(branchMap.get("code"+i)+","+branchMap.get("name"+i)+","+rcdin.get(a*3+2)+"\n");
						a = a+1;
					}else if(!(branchMap.get("code"+i).equals(rcdin.get(a*3)))){
						fw.write(branchMap.get("code"+i)+","+branchMap.get("name"+i)+"," +0+"\n");
					}
				}
				fw.close();
			} catch (IOException e) {
				System.out.println("支店別集計ファイルが存在しません");
			}
			try {
				File file = new File(args[0]+"\\commodity.out");
				FileWriter fw = new FileWriter(file);
				int a=0;
				
				for(int i=0;i<(commodityMap.size()/2);i++){
					if(rcdin.get(a*3+1).equals(commodityMap.get("code"+i))){
						fw.write(commodityMap.get("code"+i)+","+commodityMap.get("name"+i)+","+rcdin.get(a*3+2)+"\n");
						a = a+1;
					}else if(!(rcdin.get(a*3+1).equals(commodityMap.get("code"+i)))){
						fw.write(commodityMap.get("code"+i)+","+commodityMap.get("name"+i)+"," +0+"\n");
					}
				}
				fw.close();
			} catch (IOException e) {
				System.out.println("商品別集計ファイルが存在しません");
			}

	}


}
