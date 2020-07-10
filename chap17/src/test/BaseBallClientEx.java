package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

public class BaseBallClientEx {
	public static void main(String[] args) throws IOException {
		Socket s = new Socket("192.168.0.164",9000);
		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader sbr = new BufferedReader(new InputStreamReader(s.getInputStream()));
		Writer sw = new OutputStreamWriter(s.getOutputStream());
		while(true) {
			System.out.println("4자리의 서로다른 숫자 입력");
			String number = sysin.readLine();
			sw.write(number + "\n");
			sw.flush();
			String data = sbr.readLine();
			System.out.println(data);
			if (data.indexOf("종료") >= 0) break;
		}
		sysin.close();		sbr.close();
		sw.close();		s.close();
	}
}