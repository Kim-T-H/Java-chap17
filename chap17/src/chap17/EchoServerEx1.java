package chap17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Echo 서버 프로그램
 */
public class EchoServerEx1 {
	public static void main(String[] args) {
		int port = 5000;
		Socket client = null;
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			client = server.accept();
			System.out.println("server:" + server);
			System.out.println("localport:" + server.getLocalPort());
			System.out.println();
			System.out.println("client ip:" + client.getInetAddress());
			System.out.println("client port:" + client.getPort());
			PrintWriter out = new PrintWriter(client.getOutputStream());
			BufferedReader br = new BufferedReader
					      (new InputStreamReader(client.getInputStream()));
			String input;
			while((input=br.readLine()) != null) {
				out.println(input);
				out.flush();
				System.out.println("client data:" + input);
			}
			br.close();out.close(); client.close(); server.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
