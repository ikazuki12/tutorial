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
			ArrayList<String> branchCode = new ArrayList<String>();
			//商品定義ファイルを読み込む
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\branch.lst"));

				String str;
				while((str = br.readLine()) != null){
					branchMap.put(str.substring(0,3), str.substring(4));
					branchCode.add(str.substring(0,3));
				}
				br.close();
			}catch(IOException e){
				System.out.println("支店定義ファイルが存在しません");
			}
			//商品定義ファイルを読み込む
			LinkedHashMap<String, String> commodityMap = new LinkedHashMap<String, String>();
			ArrayList<String> commodityCode = new ArrayList<String>();
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\commodity.lst"));


				String str;
				while((str = br.readLine()) != null){
					commodityMap.put(str.substring(0,8), str.substring(9));
					commodityCode.add(str.substring(0,8));
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
			int[] val = new int[rcdin.size()/3];
			try{
				for(int i=0;i<(branchMap.size()/2);i++){
					for(int j=0;j<(branchMap.size()/2);j++){
						if(!(branchMap.get(rcdin.get(i*3))==null)){
							val[i] = Integer.parseInt(rcdin.get(i*3+2));
							if(branchMap.get(rcdin.get(i*3))==rcdin.get(j*3)){
								val[i] = val[i]+val[j];
							}
						}else{
							val[i] = 0;
						}
					}
				}
			}finally{

			}

			try {
				File file = new File(args[0]+"\\branch.out");
				FileWriter fw = new FileWriter(file);
				int a =0;
				for(int i=0;i<(branchMap.size());i++){
					if(branchCode.get(i).equals(rcdin.get(a*3))){
						fw.write(rcdin.get(a*3)+","+branchMap.get(rcdin.get(a*3))+","+val[a]+"\n");
						a=a+1;
					}else if(!(branchCode.get(i).equals(rcdin.get(a*3)))){
						fw.write(branchCode.get(i)+","+branchMap.get(branchCode.get(i))+","+0+"\n");
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
				for(int i=0;i<(commodityMap.size());i++){
					if(commodityCode.get(i).equals(rcdin.get(a*3+1))){
						fw.write(rcdin.get(a*3+1)+","+commodityMap.get(rcdin.get(a*3+1))+","+val[a]+"\n");
						a=a+1;
					}else if(!(commodityCode.get(i).equals(rcdin.get(a*3+1)))){
						fw.write(commodityCode.get(i)+","+commodityMap.get(commodityCode.get(i))+","+0+"\n");
					}
				}
				fw.close();
			} catch (IOException e) {
				System.out.println("商品別集計ファイルが存在しません");
			}

	}


}
