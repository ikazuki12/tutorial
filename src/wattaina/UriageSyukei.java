package wattaina;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class UriageSyukei {
	public static void main(String[] args) {
//		try{
			if(args.length != 1){
				System.out.println("ファイル名を正しく指定してください。");
				System.exit(1);
			}
			try{
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("branchfile", "branch.lst");
				map.put("commodiltyfile", "commodilty.lst");
				for(int i=0;i<map.size();i++){
					BufferedReader br = new BufferedReader(new FileReader(args[i] + map.get(key)));

					int a =0;
					String[] strarray = new String[6];
					String[] stnCode = new String[6];
					String str;
					while((str = br.readLine()) != null){
						strarray[a] = str;
						stnCode[a] = strarray[a].substring(0,3);
						a=a+1;
					}
					br.close();
				}
			}catch(IOException e){
				System.out.println("支店定義ファイルが存在しません");
			}
//
//			HashMap<String, String> map = new HashMap<String, String>();
//			for(int i=0;i<100;i++){
//				if(i<10){
//					map.put("rcdfile"+i, "0000000"+i+".rcd");
//				}
//				else if((i<100)){
//					map.put("rcdfile"+i, "000000"+i+".rcd");
//				}
//			}
//			String[] strarray = new String[map.size()];
//			String[] urg = new String[map.size()];
//			for(int i=0;i<map.size();i++){
//				int n=0;
//				File file = new File(map.get("cdrfile"+i));
//				if (!file.exists()){
////					newdir
//				}
//				FileReader fr = new FileReader(file);
//				BufferedReader br = new BufferedReader(fr);
//				String s;
//				while((s = br.readLine()) != null){
//					strarray[n]=s;
//					urg[n] = strarray[n].substring(0);
//					n=n+1;
//				}
//				System.out.println(urg[i]);
//				br.close();
//			}
//			int[] val = new int[3];
//			for(int i=0;i<3;i++){
//				val[i] = Integer.parseInt(urg[2]);
//			}
//			System.out.println(val[2]);
//			//.crdファイルから送られた売上額をStringからintに変更
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
//			System.out.println(stn_code[0]);
//		} catch(IOException e){
//			System.out.println(e);
//			System.out.println("支店定義ファイルが存在しません");
//		}
	}

}
