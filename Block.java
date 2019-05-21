package tetris;

import java.util.Random;

public class Block {
	static public final int[][][] blockI={//2
		   {{0,1,0,0},
			{0,1,0,0},
			{0,1,0,0},
			{0,1,0,0}},
		   {{0,0,0,0},
			{1,1,1,1},
			{0,0,0,0},
			{0,0,0,0}}
	};
	static public final int[][][] blockL={//4
		   {{0,1,0,0},
			{0,1,0,0},
			{0,1,1,0},
			{0,0,0,0}},
		   {{0,0,0,0},
			{0,0,1,0},
			{1,1,1,0},
			{0,0,0,0}},
		   {{0,1,1,0},
			{0,0,1,0},
			{0,0,1,0},
			{0,0,0,0}},
		   {{0,0,0,0},
			{0,1,1,1},
			{0,1,0,0},
			{0,0,0,0}}
	};
	static public final int[][][] blockJ= {//4
		   {{0,0,1,0},
			{0,0,1,0},
			{0,1,1,0},
			{0,0,0,0}},
		   {{0,0,0,0},
			{0,1,0,0},
			{0,1,1,1},
			{0,0,0,0}},
		   {{0,1,1,0},
			{0,1,0,0},
			{0,1,0,0},
			{0,0,0,0}},
		   {{0,0,0,0},
			{1,1,1,0},
			{0,0,1,0},
			{0,0,0,0}}
	};
	static public final int[][][] blockT={//4
		   {{0,0,0,0},
			{1,1,1,0},
			{0,1,0,0},
			{0,0,0,0}},
		   {{0,1,0,0},
			{0,1,1,0},
			{0,1,0,0},
			{0,0,0,0}},
		   {{0,0,0,0},
			{0,1,0,0},
			{1,1,1,0},
			{0,0,0,0}},
		   {{0,1,0,0},
			{1,1,0,0},
			{0,1,0,0},
			{0,0,0,0}}
	};
	static public final int[][] blockO={//1
			{0,0,0,0},
			{0,1,1,0},
			{0,1,1,0},
			{0,0,0,0}
	};

	static public int[][][] totle_block={blockI[0],blockI[0],blockI[1],blockI[1],blockI[0],blockI[0],blockI[1],blockI[1],
			blockL[1],blockL[2],blockL[3],blockL[0],blockJ[0],blockJ[1],blockJ[2],blockJ[3],
			blockT[0],blockT[1],blockT[2],blockT[3],blockT[0],blockT[1],blockT[2],blockT[3],	
			blockO,blockO,blockO,blockO,blockO,blockO,blockO,blockO};
	static public int totle_blocknum=32;
	private Random rand=new Random();
	//随机出现一个方块
	public int[][] create_block() {
		
		int num=rand.nextInt(totle_blocknum);
		return totle_block[num];
	}
	//test_output
	public void output(int[][] block) {
		for(int i=0;i<4;i++){
			for(int j=0;j<4;j++)
				System.out.print(block[i][j]);
			System.out.print("\n");
		}
	}
}
