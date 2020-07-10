package chap17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * EchoServerEx1.java 프로그램을 멀티 클라이언트가 접속가능하도록 수정하기
 * Thread 사용해야함.
 */
public class Exam2 {
	public static void main(String[] args) {
		try {
			ServerSocket server = new ServerSocket(5000);
			while (true) {
				System.out.println("클라이언트 접속 대기");
				Socket client = server.accept();
				ServerThread th = new ServerThread(client);
				th.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
class ServerThread extends Thread {
	private Socket client;
	PrintWriter out;
	BufferedReader br;
	ServerThread(Socket client) {
		this.client = client;
		System.out.println("connected ip :" + client.getInetAddress());
		System.out.println("connected port :" + client.getPort());
		try {
			out = new PrintWriter(client.getOutputStream());
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		String input;
		try {
			while ((input = br.readLine()) != null) {
				out.println(input);
				out.flush();
				System.out.println("client data:" + input);
			}
			br.close();	out.close();client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
