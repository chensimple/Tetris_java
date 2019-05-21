package tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;

public class Connect_net {
	public static Map<InetAddress,String> client_table = new HashMap<InetAddress,String>();
	public static InetAddress server_address=null;
	public static Timer timer;
	public static void start() {
		if(!Mainpanel.is_server) {
			//启动客户端服务
			client local_client=new client();
			local_client.start_client();
		}
		else {
			//启动服务端服务
			server server_service=new server();
			server_service.start_server();
		}
	}
}

//client
class client implements send_receive,Runnable{
	public  void start_client() {
		//寻找服务器
		if(Connect_net.server_address==null) {
			try {
				Connect_net.server_address=InetAddress.getByName("255.255.255.255");
				send_info("is_have_server,"+InetAddress.getLocalHost().getHostAddress(),Connect_net.server_address,50001);
				//System.out.println("服务器在哪");
			}catch(Exception e) {
				System.out.println("寻找服务器失败");
			}
		}
		
		Thread t1= new Thread(this);
		t1.start();
		//发送数据
		Connect_net.timer=new Timer(1000, new sendmessege());
		Connect_net.timer.start();
		//接收服务器发送数据
		
	}
	
	//send data
	class sendmessege implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String line = Mainpanel.name+","+Game_panel.line+","+Game_panel.lines_moved;
			try {
				Connect_net.client_table.put(Mainpanel.localaddress,line);
				send_info(line,Connect_net.server_address,50001);
			}catch(Exception e1) {
				System.out.println("发送失败");
			}
		}
	}
	
	@Override
	public void to_handle(String content, InetAddress address) {
		if(content.equals("server_ip")) {
			Connect_net.server_address=address;
			send_info("我收到了",address,50001);
			System.out.println("服务器地址为"+address);
		}
		else {
			//TODO 修改为反序列化为Map
			Connect_net.client_table.put(address, content);
			System.out.println("处理map");
		}
	}

	@Override
	public void run() {
		System.out.println("客户端开始接收");
		receive(50000);
	}


	@Override
	public void send_map(Map<InetAddress, String> map, InetAddress netaddress, int port) {
		// no operation
		
	}
	
}

//server
class server implements send_receive,Runnable,Serializable{
	private static final long serialVersionUID = 1L;
	public void start_server() {
		Thread t2=new Thread(this);
		t2.start();
		Timer timer2=new Timer(1000, new sendallmessege());
		timer2.start();
		
	}

	class sendallmessege implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			try {
				//序列化
				//send_map(Connect_net.client_table,InetAddress.getByName("255.255.255.255"),50000);
			}catch(Exception e1) {
				System.out.println("发送失败");
			}
		}
	}
	
	@Override
	public void to_handle(String content, InetAddress address) {
		String[] info=content.split(",");
		if(info[0].equals("is_have_server"))
			try {
				send_info("server_ip",java.net.InetAddress.getByName(info[1]),50000);
				//System.out.println("我是服务器"+address);
			}catch(Exception e) {
				System.out.println("回应失败");
			}
		else {
			Connect_net.client_table.put(address, content);
		}
	}
	@Override
	public void run() {
		//System.out.println("服务器启动");
		receive(50001);
	}

	@Override
	public void send_map(Map<InetAddress, String> map, InetAddress netaddress, int port) {
		try {
			Socket socket = new Socket(netaddress,port);
			OutputStream os=socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(os);
			out.writeObject(Connect_net.client_table);
            out.close();
            socket.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

interface send_receive{
	//send
	public default void send_info(String info,InetAddress netaddress,int port) {
		byte[] arr = info.getBytes();
		// 数据报包打包
		try {
			DatagramSocket socket = new DatagramSocket();
			DatagramPacket packet = new DatagramPacket(arr, arr.length, netaddress, port);
			socket.send(packet);
			socket.close();
		} catch (Exception x) {
			System.out.println("发送失败");
		}
	}
	
	//reeceive 监听某端口
	public default void receive(int port) {
		try {
			@SuppressWarnings("resource")
			DatagramSocket socket=new DatagramSocket(port);
			while (true) {
				byte[] arr = new byte[1024];
				DatagramPacket packet = new DatagramPacket(arr, arr.length);
				socket.receive(packet);
				String content = new String(packet.getData(), 0, packet.getLength());
				InetAddress address = packet.getAddress();
				to_handle(content,address);	
				System.out.println("处理"+content);
			}
		} catch (Exception e) {
			System.out.println("错误");
		}
	}
	public abstract void to_handle(String content,InetAddress address);
	public abstract void send_map(Map<InetAddress,String> map,InetAddress netaddress,int port);
}
