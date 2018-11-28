import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;

public class Generator {
	private final int x;
	private final int y;
	private boolean[][] mainMaze; //mainMaze maze of yes/no values for passable vs. not passible objects
	private ArrayList<int[]> stack = new ArrayList<int[]>();//stack for nodes that will be popped during the backtracking process
	
	public Generator(int x, int y) {
		this.x = x;
		this.y = y;
		mainMaze = new boolean[this.x][this.y]; //maze size
		
		int[] start = new int[] {1,1};//fixed starting index 
		int[] padding = new int[] {x-3,y-2};//to allow for a generation that includes a consistent finish point
		int[] finish = new int[] {x-2, y-2};//finish point
		
		mainMaze[start[0]][start[1]] = true;//border at 0,0-> Start at {1,1} or random index {a,b}
		mainMaze[finish[0]][finish[1]] = true;//border at 0,0-> Start at {1,1} or random index {a,b}
			mainMaze[padding[0]][padding[1]] = true;//(only for generation) include the padding to the maze
		
		stack.add(start);
		stack.add(finish);

		System.out.println("Starting Index is ["+stack.get(0)[0]+", "+stack.get(0)[1]+"]");
		System.out.println("Finish Point is ["+stack.get(1)[0]+", "+stack.get(1)[1]+"]");
		
		generate(start[0],start[1]);
	}
	
	//from https://rosettacode.org/wiki/Maze_generation#Java 
	private static boolean between(int v, int upper) {
		return (v >= 0) && (v < upper);
	}
    
    private enum DIR{
    	//directions 1-4
    	N(0,2,0,1), S(0,-2,0,-1), E(2,0,1,0), W(-2,0,-1,0);
    	private final int dx;
        private final int dy;
        private final int wx;
        private final int wy;
               
        private DIR(int dx, int dy, int wx, int wy) {
        	this.dx = dx;
        	this.dy = dy;
        	this.wx = wx;
        	this.wy = wy;
        }
        
    }
    
    public void generate(int cx, int cy) { //curr = index of the stack with which the currentCell resides
    	DIR[] dirs = DIR.values();
    	Collections.shuffle(Arrays.asList(dirs));
    	
    	for(DIR dir: dirs) {
    		int nx = cx + dir.dx;
    		int ny = cy + dir.dy;
    		int nwx = cx + dir.wx;             
    		int nwy = cy + dir.wy;
    		if(between(nx, x) && between(ny, y) && (mainMaze[nx][ny] == false)) {//all of isAvail in one line
    			//set the next and the wall to the next equal to true.
    			mainMaze[nx][ny] = true;
    			mainMaze[nwx][nwy] = true;//set wall equal to true.
    			generate(nx,ny);
    		}
    	
    	}
    }
  
    public void printMaze(){ 
    	System.out.println("This is what the Generator Class Created:");
    	for(int i = 0; i < mainMaze.length; i++) {
    		for(int j = 0; j < mainMaze[i].length; j++) {
    			if(mainMaze[i][j]) {
    				System.out.print("  "); //two whitespaces
    			} else {
       				System.out.print("\u2588\u2588");//two fullblock black spaces    				
    			}
    		}
    		System.out.println();
    	}
    } 
 
    
    //in order to get the mainMaze Object
    public boolean[][] getMaze() {
    	return mainMaze;
    }
    public int getX (){
    	return x;
    }
    public int getY (){
    	return y;
    }
    public ArrayList<int[]> getStack(){
    	return stack;
    }
}
