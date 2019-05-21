package tetris;

import java.awt.event.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Timer;

class Net_connect {
	  //存储所有玩家信息
	public static Map<InetAddress,String> client_table = new HashMap<InetAddress,String>();
	public static boolean is_server=false;
	public static InetAddress server=null;
	public static Timer timer2;
	
	public static void main(String[] args) throws Exception {
		
		//开启端口监听
		client_table=null;
		client_table.put(InetAddress.getLocalHost(), "0,0");
		Thread t= new Thread(new MySocket());
		t.start();
		//寻找服务器
		search_server();
		//定时发送数据
		timer2 = new Timer(1000, new sendmessege());
		timer2.start();
	}
	
	//寻找服务器
	public static void search_server() throws Exception{
		if(is_server=false) {
			sendmessege.send_info("is_have_server",InetAddress.getByAddress("255.255.255.255".getBytes()),50000);
		}
	}
}

//发送数据
class sendmessege implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		String line = Mainpanel.name+","+Game_panel.line+","+Game_panel.lines_moved;
		try {
			Net_connect.client_table.put(InetAddress.getLocalHost(),line);
			if(!Net_connect.is_server)
				//客户端发送
				send_info(line,Net_connect.server,50000);
			else {
				//TODO服务端广播发送Map
				//
			}
		}catch(Exception e1) {
			System.out.println("发送失败");
		}
	}
	
	//发送函数
	public static void send_info(String info,InetAddress netaddress,int port) {
		byte[] arr = info.getBytes();
		// 数据报包打包
		try {
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(arr, arr.length, netaddress, port);
			if(Mainpanel.name!=null) {
				socket.send(packet);
			}
				socket.close();
		} catch (Exception x) {
			System.out.println("发送失败");
		}
	}
	
}

//监听端口，处理接收数据
class MySocket implements Runnable {
	public void run() {
		try {
			@SuppressWarnings("resource")
			DatagramSocket socket=new DatagramSocket(50000);
			while (true) {
				byte[] arr = new byte[1024];
				DatagramPacket packet = new DatagramPacket(arr, arr.length);
				socket.receive(packet);
				// 此处是获取数据报的一些信息，用于打印
				String content = new String(packet.getData(), 0, packet.getLength());
				//发送方Address
				InetAddress address = packet.getAddress();
				to_handle(content,address);				
			}
		} catch (Exception e) {
			System.out.println("错误");
		}
	}

	//检查客户端是否已断开
	void check_life() {
		// 每10s检查
		Timer timer = new Timer(10000, new check());
		timer.start();
	}

	//处理数据
	void to_handle(String content,InetAddress address) throws UnknownHostException {
		//设置服务器端
		if(content.equals("server_ip")) {
			Net_connect.server=address;
		}
		else if(content.equals("is_have_server")) {
			//建立服务器
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
			if(Net_connect.is_server) {//服务器存入数据
				Net_connect.client_table.put(address,content);
			}
			else {
				//TODO 作为客户端接收数据
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