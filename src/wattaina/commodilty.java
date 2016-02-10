package wattaina;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class commodilty {
	public static void main(String[] args) {
		try{
			String[] strarray = new String[4];
			int i = 0;
			File file2 = new File("C:\\java\\commodilty.lst");
			FileReader fr2 = new FileReader(file2);
			BufferedReader br2 = new BufferedReader(fr2);
			String s2;
			while((s2 = br2.readLine()) != null){
				System.out.println(s2);
				strarray[i]=s2;
				i = i +1;
			}
			br2.close();
		} catch(IOException e){
			System.out.println(e+ "商品定義ファイルが存在しません");
		}
	}
}
