package tetris;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class Mainpanel extends JFrame {
	public static String name;
	public static InetAddress localaddress;
	public static boolean is_server=false;
	JButton start = new JButton();
	JButton creat_server=new JButton();
	JTextField name_field = new JTextField();
	Label label = new Label();
	public Mainpanel() {
		this.setTitle("test_tetris(ver 2.1)");
		this.setResizable(false); // 设置界面大小可由用户调整
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 关闭
		this.setSize(500, 550); // 初始大小
		this.setLocationRelativeTo(null); // 窗口将置于屏幕的中央
		this.setVisible(true);
		this.setLayout(null);
		start.setText("开始游戏自动加入");
		start.setBounds(300, 250, 100, 40);
		creat_server.setText("创建房间并开始游戏");
		creat_server.setBounds(140, 250, 160, 40);
		name_field.setBounds(130, 200, 300, 20);
		label.setText("请输入用户名");
		label.setBounds(30, 200, 80, 20);
		this.add(start);
		this.add(creat_server);
		this.add(name_field);
		this.add(label);
		start.addActionListener(new start_game());
		creat_server.addActionListener(new creat_server());
	}

	class start_game implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			name = name_field.getText();
			remove(start);
			remove(name_field);
			remove(label);
			remove(creat_server);
			Game_panel mypanel=new Game_panel();
			add(mypanel);
			mypanel.requestFocus();
			repaint();
		};
	}

	class creat_server implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			is_server=true;
			try {
				localaddress = InetAddress.getLocalHost();
			} catch (Exception e1) {
				System.out.println("无法获取地址");
			}
			new start_game().actionPerformed(e);
		}
	}
	public static void main(String[] args) {
		new Mainpanel();
	}
}
