package tetris;

import java.awt.event.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

class Net_connect {
	  //�洢���������Ϣ
	public static Map<InetAddress,String> client_table = new HashMap<InetAddress,String>();
	public static boolean is_server=false;
	public static InetAddress server=null;
	public static Timer timer2;
	
	public static void main(String[] args) throws Exception {
		
		//�����˿ڼ���
		client_table=null;
		client_table.put(InetAddress.getLocalHost(), "0,0");
		Thread t= new Thread(new MySocket());
		t.start();
		//Ѱ�ҷ�����
		search_server();
		//��ʱ��������
		timer2 = new Timer(1000, new sendmessege());
		timer2.start();
	}
	
	//Ѱ�ҷ�����
	public static void search_server() throws Exception{
		if(is_server=false) {
			sendmessege.send_info("is_have_server",InetAddress.getByAddress("255.255.255.255".getBytes()),50000);
		}
	}
}

//��������
class sendmessege implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		String line = Mainpanel.name+","+Game_panel.line+","+Game_panel.lines_moved;
		try {
			Net_connect.client_table.put(InetAddress.getLocalHost(),line);
			if(!Net_connect.is_server)
				//�ͻ��˷���
				send_info(line,Net_connect.server,50000);
			else {
				//TODO����˹㲥����Map
				//
			}
		}catch(Exception e1) {
			System.out.println("����ʧ��");
		}
	}
	
	//���ͺ���
	public static void send_info(String info,InetAddress netaddress,int port) {
		byte[] arr = info.getBytes();
		// ���ݱ������
		try {
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(arr, arr.length, netaddress, port);
			if(Mainpanel.name!=null) {
				socket.send(packet);
			}
				socket.close();
		} catch (Exception x) {
			System.out.println("����ʧ��");
		}
	}
	
}

//�����˿ڣ������������
class MySocket implements Runnable {
	public void run() {
		try {
			@SuppressWarnings("resource")
			DatagramSocket socket=new DatagramSocket(50000);
			while (true) {
				byte[] arr = new byte[1024];
				DatagramPacket packet = new DatagramPacket(arr, arr.length);
				socket.receive(packet);
				// �˴��ǻ�ȡ���ݱ���һЩ��Ϣ�����ڴ�ӡ
				String content = new String(packet.getData(), 0, packet.getLength());
				//���ͷ�Address
				InetAddress address = packet.getAddress();
				to_handle(content,address);				
			}
		} catch (Exception e) {
			System.out.println("����");
		}
	}

	//���ͻ����Ƿ��ѶϿ�
	void check_life() {
		// ÿ10s���
		Timer timer = new Timer(10000, new check());
		timer.start();
	}

	//��������
	void to_handle(String content,InetAddress address) throws UnknownHostException {
		//���÷�������
		if(content.equals("server_ip")) {
			Net_connect.server=address;
		}
		else if(content.equals("is_have_server")) {
			//����������
			if(Net_connect.is_server) {
				sendmessege.send_info("server_ip", address, 50000);
			}
			else {
				Net_connect.is_server=true;
				sendmessege.send_info("server_ip"+","+InetAddress.getLocalHost().getHostAddress(), address, 50000);
			}
		}
		else if(content.equals("to_addline"))
			Game_panel.addline=true;
		else {
			if(Net_connect.is_server) {//��������������
				Net_connect.client_table.put(address,content);
			}
			else {
				//TODO ��Ϊ�ͻ��˽�������
			}
		}
	}
}

class check implements ActionListener{
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO 
		
	}
}