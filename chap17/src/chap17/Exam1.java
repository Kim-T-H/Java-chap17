package chap17;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import chap17.SimpleWebServerEx1.HttpThread;
/*
 * 간단한 웹서버 구현하기 : 이미지파일로 처리 되도록 프로그램 작성하기
 *    1. 파일을 바이트형입력스트림으로 읽기
 *    2. 클라이언트에 전송시 바이트형출력스트림
 */
public class Exam1 {
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(8888);
			while(true) {
				System.out.println("클라이언트 접속 대기");
				Socket client = server.accept();
				HttpThread ht = new HttpThread(client);
				ht.start();
			} 
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	static class HttpThread extends Thread {
		private Socket client;
		BufferedReader br;
		OutputStream pw; 
		HttpThread(Socket client) { //생성자
			this.client = client;
			try {
				br = new BufferedReader
				(new InputStreamReader(client.getInputStream()));
				pw = client.getOutputStream();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		public void run() {
			FileInputStream fbr = null;
			try {
				String line = br.readLine();
				System.out.println(line);
				int start = line.indexOf("/") + 1;
				int end = line.lastIndexOf("HTTP")-1;
				String fileName = line.substring(start,end);
				if(fileName.equals(""))
					fileName = "index.html";
				System.out.println("클라이언트 요청 파일:" + fileName);
				fbr = new FileInputStream(fileName);
				int len = 0;
				byte[] buf=new byte[1024];
				while((len = fbr.read(buf)) != -1) {
					pw.write(buf,0,len);
					pw.flush();
				}
			}catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(fbr != null) fbr.close();
					if(br != null) br.close();
					if(pw != null) pw.close();
					if(client != null) client.close();
				} catch (IOException e) {}
			}
		} //run 메서드 종료
	}}
