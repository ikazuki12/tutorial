package wattaina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UriageSyukei {
	public static void main(String[] args) {
//		try{
			if(args.length != 1){
				System.out.println("正しくディレクトリを指定してください。");
				System.exit(1);
			}
			HashMap<String, String> map = new HashMap<String, String>();
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\branch.lst"));


				String str;
				while((str = br.readLine()) != null){
					map.put(str.substring(0,3), str.substring(4));
				}
				br.close();
			}catch(IOException e){
				System.out.println("支店定義ファイルが存在しません");
			}
			//商品定義ファイルを
			try{
				BufferedReader br = new BufferedReader(new FileReader(args[0]+"\\commodilty.lst"));


				String str;
				while((str = br.readLine()) != null){
					map.put(str.substring(0,8), str.substring(9));
				}
				br.close();
			}catch(IOException e){
				System.out.println("支店定義ファイルが存在しません");
			}

			File dir = new File(args[0]);
		    File[] files = dir.listFiles();
		    for (int i = 0; i < files.length; i++) {
		        File file = files[i];

				if(file.getName().matches("^\\d{8}.rcd$")) {
					String[] rcdfile = file.getName().split("\\.", -1);
					
					System.out.println(rcdfile[0]);
				}

		    }

//			File file4 = new File("C:\\java\\branch.out");
//			FileWriter fw1 = new FileWriter(file4);
//			for(int i=0;i<6;i++){
//				int stn_total = 0;
//				if(!(stn_code[i].equals(urg[0]))){
//					fw1.write(stn_code[i]+","+stn_name[i]+","+stn_total+"\n");
//				}else if(stn_code[i].equals(urg[0])){
//					fw1.write(stn_code[i]+","+stn_name[i]+","+(stn_total+val[i])+"\n");
//				}else{
//					break;
//				}
//			}
//			fw1.close();
	}


}
