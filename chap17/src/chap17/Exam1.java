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
 * ������ ������ �����ϱ� : �̹������Ϸ� ó�� �ǵ��� ���α׷� �ۼ��ϱ�
 *    1. ������ ����Ʈ���Է½�Ʈ������ �б�
 *    2. Ŭ���̾�Ʈ�� ���۽� ����Ʈ����½�Ʈ��
 */
public class Exam1 {
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(8888);
			while(true) {
				System.out.println("Ŭ���̾�Ʈ ���� ���");
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
		HttpThread(Socket client) { //������
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
				System.out.println("Ŭ���̾�Ʈ ��û ����:" + fileName);
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
		} //run �޼��� ����
	}}
