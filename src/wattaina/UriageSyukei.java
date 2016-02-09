package wattaina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class UriageSyukei {
	public static void main(String[] args) {
		try{
			String[] strarray1 = new String[6];
			int i = 0;
			String[] stn_code = new String[6];
			String[] stn_name = new String[6];
			File file1 = new File("C:\\java\\branch.lst");
			FileReader fr1 = new FileReader(file1);
			BufferedReader br1 = new BufferedReader(fr1);
			String s1;
			while((s1 = br1.readLine()) != null){
				strarray1[i]=s1;
				stn_code[i] = strarray1[i].substring(0,3);
				stn_name[i] = strarray1[i].substring(4);
				i = i +1;
			}
			br1.close();
			String[] strarray2 = new String[4];
			int j = 0;
			String[] shn_code = new String[4];
			String[] shn_name = new String[4];
			File file2 = new File("C:\\java\\commodilty.lst");
			FileReader fr2 = new FileReader(file2);
			BufferedReader br2 = new BufferedReader(fr2);
			String s2;
			while((s2 = br2.readLine()) != null){
				strarray2[j]=s2;
				shn_code[j] = strarray2[j].substring(0,8);
				shn_name[j] = strarray2[j].substring(9);
				j = j +1;
			}
			br2.close();
			String[] rcdfile = new String[3];
			String[] strarray = new String[3];
			String[] urg = new String[3];
			int[] val = new int[3];
			for(i=1;i<=3;i++){
				int n = 0;
				rcdfile[n] = "C:\\java\\0000000"+i+".rcd";
				if (Arrays.asList(rcdfile[n]).contains(null)){
					System.out.println("売り上げファイルが連番になっていません");
				}
				File file = new File(rcdfile[n]);
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String s;
				while((s = br.readLine()) != null){
					strarray[n]=s;
					urg[n] = strarray[n].substring(0);
					n=n+1;
				}
				br.close();
//				int[] parseInts(String[] ar){
//
//					  int[] val = new int[3];
//					  for(int a = 0; a < 3; a++){
//					    val[a] = Integer.parseInt(ar[a]);
//					  }
//					  return;
//					}
				for(i=0;i<3;i++){
					val[i] = Integer.parseInt(urg[2]);
				}
				System.out.println(val[2]);
			}
			//.crdファイルから送られた売上額をStringからintに変更
//			File file4 = new File("C:\\java\\branch.out");
//			FileWriter fw1 = new FileWriter(file4);
//			for(i=0;i<6;i++){
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
		} catch(IOException e){
			System.out.println(e);
			System.out.println("支店定義ファイルが存在しません");
		}
	}

}
