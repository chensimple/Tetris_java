package tetris;

public class Map{
	public int[][] map=new int[15][25];
	public Map() {
		for(int i=0;i<15;i++) {
			for(int j=0;j<25;j++) {
				map[i][j]=0;
			}
		}
	}
	public int count_map()
	{
		int count=0;
		for(int i=0;i<15;i++) {
			for(int j=0;j<25;j++) {
				if(map[i][j]==1)
					count++;
			}
		}
		return count;
	}
	public int[][] copy(){
		int[][] map_copy=new int[15][25];
		for(int i=0;i<15;i++) {
			for(int j=0;j<25;j++) {
				map_copy[i][j]=map[i][j];
			}
		}
		return map_copy;
	}
}
