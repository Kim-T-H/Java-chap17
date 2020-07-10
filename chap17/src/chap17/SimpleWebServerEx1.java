package chap17;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleWebServerEx1 {
	public static void main(String[] args) {
		try {
			//ServerSocket : 서버.
			//8888 : 포트 번호를 OS로 부터 할당. binding이라함. 
			//IP : 컴퓨터 한개당 한개의 주소 할당.
			//PORT : 논리적인 접속 번호.
			// 1 ~ 1000 번 포트 : 공공목적.
			//  FTP : 21
			//  SSH : 22
			//  Telnet : 25 ...
			ServerSocket server = new ServerSocket(8888);
			while(true) {
				System.out.println("클라이언트 접속 대기");
				Socket client = server.accept();  //클라이언트와 접속 완료
				System.out.println("스레드 생성");
				HttpThread ht = new HttpThread(client);
				ht.start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	static class HttpThread extends Thread {
		private Socket client;
		BufferedReader br; //클라이언트의 데이터를 읽기 위한 스트림
		PrintWriter pw;    //클라이언트에 데이터를 출력하기 위한 스트림
		HttpThread(Socket client) { //생성자
			this.client = client;
			try {
				//client.getInputStream() : 클라이언트가 전송한 데이터를 입력받기위한 입력스트림
				br = new BufferedReader(new InputStreamReader(client.getInputStream()));
				//client.getOutputStream() : 클라이언트에게 전송할 데이터를 보내기 위한 출력스트림
				pw = new PrintWriter(client.getOutputStream());
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			BufferedReader fbr = null; //요청할 파일을 읽기 위한 스트림
			try {
				String line = br.readLine(); //클라이언트 데이터
				System.out.println("Http Header :" + line);// GET / HTTP/1.1
				int start = line.indexOf("/") + 1;
				int end = line.lastIndexOf("HTTP")-1;
				String fileName = line.substring(start,end);
				if(fileName.equals("")) fileName = "index.html";
				System.out.println("사용자 요청 파일:" + fileName);
				fbr = new BufferedReader(new FileReader(fileName));
				String fileLine = null;
				//크롬브라우저에서 실행 가능하도록.
				//pw.println("HTTP/1.0 200 OK\r\nContent-Type:text/html\r\n");
				while((fileLine = fbr.readLine()) != null) {
					pw.println(fileLine);
					pw.flush();
				}
			} catch(IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(fbr != null) fbr.close();
					if(br != null) br.close();
					if(pw != null) pw.close();
					if(client != null) client.close();
				} catch(IOException e) {}
			}
		}
	}
}
