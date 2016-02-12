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
				while((str = br.readLine()) != null){
					branchMap.put(str.substring(0,3), str.substring(4));
				}
				br.close();
			}catch(IOException e){
				System.out.println("支店定義ファイルが存在しません");
			}
			//商品定義ファイルを読み込む
			LinkedHashMap<String, String> commodityMap = new LinkedHashMap<String, String>();
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\commodity.lst"));


				String str;
				while((str = br.readLine()) != null){
					commodityMap.put(str.substring(0,8), str.substring(9));
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
//			try{
				for(int i=0;i<(branchMap.size()/2);i++){
					if(!(branchMap.get(rcdin.get(i*3))==null)){
						int rcdearnigs = Integer.parseInt(rcdin.get(i*3+2));
					}
				}
//			}
			System.out.println(rcdin.get(0*3+1));
			try {
				File file = new File(args[0]+"\\branch.out");
				FileWriter fw = new FileWriter(file);

				fw.close();
			} catch (IOException e) {
				System.out.println("支店別集計ファイルが存在しません");
			}
			try {
				File file = new File(args[0]+"\\commodity.out");
				FileWriter fw = new FileWriter(file);

				fw.close();
			} catch (IOException e) {
				System.out.println("商品別集計ファイルが存在しません");
			}

	}


}
