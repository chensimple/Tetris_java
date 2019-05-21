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
		this.setResizable(false); // ���ý����С�����û�����
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �ر�
		this.setSize(500, 550); // ��ʼ��С
		this.setLocationRelativeTo(null); // ���ڽ�������Ļ������
		this.setVisible(true);
		this.setLayout(null);
		start.setText("��ʼ��Ϸ�Զ�����");
		start.setBounds(300, 250, 100, 40);
		creat_server.setText("�������䲢��ʼ��Ϸ");
		creat_server.setBounds(140, 250, 160, 40);
		name_field.setBounds(130, 200, 300, 20);
		label.setText("�������û���");
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
				System.out.println("�޷���ȡ��ַ");
			}
			new start_game().actionPerformed(e);
		}
	}
	public static void main(String[] args) {
		new Mainpanel();
	}
}
