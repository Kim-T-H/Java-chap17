package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
//숫자 야구게임 서버
//임의의 4자리 숫자를 지정하고
// 클라이언트가 숫자를 입력하면 스트라익 볼을 결정하여
// 클라이언트에게 전송하기
//누군가가 정답이면 모든 스레드를 종료시킨다. 
// 새로운 번호를 다시 지정하여, 클라이언트의 접속을 대기

public class BaseBallServerEx {
	//접속된 Socket 들의 출력스트림들의 모임
	static List<Writer> outs = new ArrayList<Writer>();
	static List<BaseBallThread> threads =new ArrayList<BaseBallThread>();
	static LinkedHashSet<Integer> answer ;
	public static void main(String[] args) throws IOException {
		ServerSocket ss = new ServerSocket(9000);
		numberSelect();
		while (true) {
			Socket s = ss.accept();
			System.out.println("클라이언트 :" + s.getInetAddress());
			List<Integer> arr = new ArrayList<Integer>(answer);
			outs.add(new OutputStreamWriter(s.getOutputStream()));
			BaseBallThread bt = new BaseBallThread(s, arr);
			threads.add(bt);
			bt.start();
		}
	}

	static void numberSelect() {
		System.out.println("새로운 야구 게임을 시작합니다.");
		answer = new LinkedHashSet<Integer>();
		int data;
		while (answer.size() < 4) {
			data = (int) (Math.random() * 10);
			answer.add(data);
		}
		System.out.println(answer);
	}
}
class BaseBallThread extends Thread {
	Socket s;
	List<Integer> answer;
	boolean able = true;
	BaseBallThread(Socket s, List<Integer> answer) {
		this.s = s;
		this.answer = answer;
	}
	public void run() {
		BufferedReader br = null;
		Writer bw = null;
		String number = null;
		try {
			br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			bw = new OutputStreamWriter(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (able) {
			try {
				number = br.readLine(); //클라이언트가 전달한 숫자
			} catch (IOException e) {
				e.printStackTrace();
			}
			char[] carr = number.toCharArray();
			int rcvData[] = new int[4];
			for (int i = 0; i < carr.length; i++)
				rcvData[i] = carr[i] - '0';
			int strike = 0;
			int ball = 0;
			int i, j;
			for (i = 0; i < rcvData.length; i++) {
				for (j = 0; j < rcvData.length; j++) {
					if (rcvData[i] == answer.get(j)) {
						if (i == j)	strike++;
						else		ball++;
					}
				}
			}
			try {
				if (strike == 4) {
					bw.write(number + "정답입니다. 게임을 종료합니다.\n");
					bw.flush();
					allClientClose();
					break;
				} else {
					bw.write(number + " : " +strike + " 스트라이크, " + ball	+ " 볼\n");
					bw.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void allClientClose() {
		Iterator<Writer> list = BaseBallServerEx.outs.iterator();
		BaseBallServerEx.numberSelect();
		while (list.hasNext()) {
			Writer w = list.next();
			try {
				w.write("다른 사용자가 이겼습니다. 게임을 종료 합니다.\n");
				w.flush();
				list.remove();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BaseBallServerEx.outs.clear();
	    for (BaseBallThread t : BaseBallServerEx.threads){
		    t.able = false;
		}
		BaseBallServerEx.threads.clear();
	}
}