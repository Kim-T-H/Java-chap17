package chap17;

import java.net.InetAddress;
import java.net.UnknownHostException;

/*
 * InetAddress 클래스 
 *   - IP 주소 정보를 저장하는 클래스
 *   - 객체생성을 위한 메서드
 *     getByName("도메인이름")
 *     getAllByName("도메인이름")
 *     getByAddress(byte[])
 *     getLocalHost()
 */
public class InetAddressEx1 {
	public static void main(String[] args) throws UnknownHostException {
		InetAddress ip = InetAddress.getByName("www.goodee.co.kr");
		System.out.println("getByName 메서드 호출");
		System.out.println("hostname:"+ip.getHostName());
		System.out.println("hostaddress:"+ip.getHostAddress()); //실제 IP 주소
		InetAddress[] ips = InetAddress.getAllByName("www.naver.com");
		System.out.println("getAllByName 메서드 호출");
		for(InetAddress add : ips) {
			System.out.println("IP 주소:" + add);
		}
		//IPv4 : 32비트 => 4바이트
		byte[] ipAddr = ip.getAddress();
		for(byte b : ipAddr) {
			System.out.print(((b < 0)?256+b:b)+".");
		}
		System.out.println();
		InetAddress ip2 = InetAddress.getByAddress(ipAddr);
		System.out.println("ip2의 주소:"+ip2);

		System.out.println("getLocalHost 메서드 호출");
		InetAddress local = InetAddress.getLocalHost();
		System.out.println("내 컴퓨터:" + local);				
	}
}
