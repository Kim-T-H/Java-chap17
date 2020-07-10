package chap17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * Echo 클라이언트 프로그램
 *   키보드로 입력 받은 내용을 서버로 전송함.
 */
public class EchoClientEx1 {
	public static void main(String[] args) {
		int port = 5000;
		String ip = "192.168.0.164";
		Socket client = null;
		PrintWriter out = null;
		BufferedReader br = null;
		try {
			String input;
			//키보드 입력
			BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
			client = new Socket(ip, port);
			br = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream());
			System.out.println("local port:" + client.getLocalPort());
			System.out.println("local ip:" + client.getLocalAddress());
			System.out.println("server port:" + client.getPort());
			System.out.println("server ip:" + client.getInetAddress());
			System.out.println("메시지를 입력하세요(종료:bye)");
			while ((input = stdin.readLine()) != null) {
				if (input.equals("bye")) break;
				out.println(input);
				out.flush();
				System.out.println("server echo :" + br.readLine());
				System.out.println("메시지 입력");
			}
			out.close();
			br.close();
			stdin.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}