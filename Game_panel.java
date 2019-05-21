package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.InetAddress;
import java.util.Random;

//import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Game_panel extends JPanel implements KeyListener {
	private boolean is_nextblock = true;
	private boolean pause = false;
	public static boolean addline=false;
	public static int lines_moved=0;
	public static int line=0;
	public int speed = 5;
	public Timer timer;
	@SuppressWarnings("unused")
	private int game_time = 0;
	private Block a = new Block();
	private Map origin_map = new Map();
	private Map next_map = new Map();
	private int[] location = { 0, 0, 0, 0, 0, 0, 0, 0 };
	private double[] center = { 0.0, 0.0 };// 4*4方塊中心坐標
	int[][] next_block = a.create_block();
	int[][] next_todraw = a.create_block();
	
	static Label grade= new Label();
	private Random rand=new Random();//生成随机颜色
	public Game_panel() {
		setSize(500, 550);
		add(grade);
		grade.setBounds(350, 200, 80, 20);
		time_control();
		Connect_net.start();
	}
	public void addNotify() {
		super.addNotify();
		addKeyListener(this);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.drawLine(300, 0, 300, 500);
		g.drawLine(0, 500, 500, 500);
		drawmap(origin_map.map, g);// 绘制地图
		shownext(next_todraw, g);// 显示下一方块
	}

	private void drawmap(int[][] map, Graphics g) {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 25; j++) {
				if (map[i][j] == 1) {
					// TODO 七彩炫光皮皮块
					/*
					 * Color rand_color=new
					 * Color(rand.nextInt(255),rand.nextInt(255),rand.nextInt(255));
					 * g.setColor(rand_color);
					 */
					g.drawRect(20 * i + 2, 20 * j + 2, 16, 16);
				}
			}
		}
	}

	public void time_control() {
		timer = new Timer(100 * speed, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					game_time++;
					// 更新坐标
					check_isnext_orupdate();
					repaint();
			}
		});
		timer.start();
	}

	// 更新block坐標
	public void check_isnext_orupdate() {
		if (is_nextblock) {
			check_line();
			next_block = copy_block(next_todraw);
			next_todraw = a.create_block();
			add_block(next_block);
			repaint();
			if (location[1] * location[3] * location[5] * location[7] != 0) {
				mykeydown(KeyEvent.VK_DOWN);
			}

		} else
			mykeydown(KeyEvent.VK_DOWN);
	}

	void check_line() {
		//检测消行
		for (int j = 0; j < 25; j++) {
			int num = 0;
			for (int i = 0; i < 15; i++) {
				num += origin_map.map[i][j];
			}
			if (num == 15) {
				move_line(j);
				lines_moved++;
				j--;
				try {
				sendmessege.send_info("to_addline",InetAddress.getByAddress("255.255.255.255".getBytes()), 50000);
				}catch(Exception e){
					System.out.println("发送失败");
				}
				
			}
		}
		line_send();
		is_add_line();
		//游戏结束
		for (int i = 0; i < 15; i++) {
			if (origin_map.map[i][0] == 1) {
				JOptionPane.showMessageDialog(null, "游戏结束，点击确定重新开始");
				origin_map = new Map();
				lines_moved=0;
			}
		}
		
	}
	
	//反馈行数
	private void line_send() {
		for (int i = 0; i < 25; i++) {
			for (int j = 0; j < 15; j++) {
				if(origin_map.map[j][i]==1) {
					line=25-i;
					return;
				}
			}
		}
	}
	
	//添加随机方块
	private void is_add_line() {
		if(addline) {
			for(int i=0;i<15;i++) {
				for(int j=0;j<24;j++) {
					origin_map.map[i][j]=origin_map.map[i][j+1];
				}
			}
			for(int i=0;i<15;i++) {
			origin_map.map[i][24]=rand.nextInt(2);
			}
			addline=false;
		}
		
	}
	
	private void add_block(int[][] block) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (block[i][j] == 1) {
					origin_map.map[6 + i][j] = 1;
					// 获取方块形状
					int k = 0;
					for (int m = 0; m < 4; m++) {
						for (int n = 0; n < 4; n++) {
							if (block[m][n] == 1) {
								location[k] = 6 + m;
								location[k + 1] = n;
								k += 2;
							}
						}
					}
					center[0] = 7.5;
					center[1] = 1.5;
					is_nextblock = false;
				}
			}
		}
	}

	private void move_line(int line) {
		for (int j = line; j >= 0; j--) {
			for (int i = 0; i < 15; i++) {
				if (j != 0) {
					origin_map.map[i][j] = 0;
					origin_map.map[i][j] = origin_map.map[i][j - 1];
				} else
					origin_map.map[i][j] = 0;
			}
		}
	}

	private void shownext(int[][] block, Graphics g) {
		g.setColor(Color.BLUE);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (block[i][j] == 1) {
					g.drawRect(20 * i + 372, 20 * j + 52, 16, 16);
				}
			}
		}
	}

	private int[][] copy_block(int[][] block) {
		int[][] block_copy = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++)
				block_copy[i][j] = block[i][j];
		}
		return block_copy;
	}

	private void left_move() {
		for (int i = 0; i < 8; i += 2) {
			location[i]--;
		}
		for (int i = 0; i < 8; i += 2) {
			if (location[i] < 0)
				right_move();
		}
		center[0]--;
	}

	private void right_move() {
		for (int i = 0; i < 8; i += 2) {
			location[i]++;
		}
		for (int i = 0; i < 8; i += 2) {
			if (location[i] > 14)
				left_move();
		}
		center[0]++;
	}

	private void down_move() {
		for (int i = 0; i < 8; i += 2) {
			location[i + 1]++;
		}
		for (int i = 0; i < 8; i += 2) {
			if (location[i + 1] > 24) {
				up_move();
				is_nextblock = true;
			}
		}
		center[1]++;
	}

	private void up_move() {
		for (int i = 0; i < 8; i += 2) {
			location[i + 1]--;
		}
		for (int i = 0; i < 8; i += 2) {
			if (location[i + 1] < 0)
				down_move();
		}
		center[1]--;
	}

	
	@Override
	public void keyPressed(KeyEvent e) {
		if (is_nextblock)
			return;
		mykeydown(e.getKeyCode());
	}

	void mykeydown(int e) {
		next_map.map = origin_map.copy();
		switch (e) {
		case KeyEvent.VK_LEFT: {
			clear_location();
			left_move();
			draw_location();
			if (IsComplect())
				right_move();
			break;
		}
		case KeyEvent.VK_RIGHT: {
			clear_location();
			right_move();
			draw_location();
			if (IsComplect())
				left_move();
			break;
		}
		case KeyEvent.VK_UP: {
			clear_location();
			clockwise_move();
			draw_location();
			if (IsComplect())
				anticlockwise_move();
			break;
		}
		case KeyEvent.VK_DOWN: {
			clear_location();
			down_move();
			draw_location();
			if (IsComplect()) {
				up_move();
				is_nextblock = true;
			}
			break;
		}
		case KeyEvent.VK_SPACE: {
			if (pause == false) {
				timer.stop();
				pause = true;
				break;
			} else {
				timer.start();
				pause = false;
				break;
			}
		}
		case KeyEvent.VK_ESCAPE: {
			System.exit(0);
			break;
		}
		}
		repaint();
	}

	private void clockwise_move() {
		int y, x;
		if (center[0] < 13.5 && center[1] < 23.5 && center[0] >= 1.5 && center[1] >= 1.5)
			for (int i = 0; i < 8; i += 2) {
				x = location[i];
				y = location[i + 1];
				location[i] = (int) (-y + center[1] + center[0]);
				location[i + 1] = (int) (center[1] + x - center[0]);
			}
	}

	private void anticlockwise_move() {
		int x, y;
		for (int i = 0; i < 8; i += 2) {
			x = location[i];
			y = location[i + 1];
			location[i] = (int) (y - center[1] + center[0]);
			location[i + 1] = (int) (center[0] - x + center[1]);
		}
	}

	// 是否衝突
	synchronized boolean IsComplect() {
		if (origin_map.count_map() == next_map.count_map()) {
			origin_map.map = next_map.copy();
			return false;
		} else
			return true;
	}

	void clear_location() {
		for (int i = 0; i < 8; i += 2)
			next_map.map[location[i]][location[i + 1]] = 0;
	}

	void draw_location() {
		for (int i = 0; i < 8; i += 2)
			next_map.map[location[i]][location[i + 1]] = 1;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// no opreation
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// no opreation
	}
}
