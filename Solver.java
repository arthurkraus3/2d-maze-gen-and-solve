
import java.util.ArrayList;
import java.util.List;

public class Solver {
    public static void main(String[] args)
    {
    	Generator g = new Generator(21,21);
    	//elements from Generator
    	boolean[][] maze = g.getMaze();
    	final int ROWS = g.getX();
    	final int COLS = g.getY();
    	ArrayList<int[]> s = g.getStack();//absolute indecies given from Generator
    	g.printMaze();
    	
    	//elements for AStar
    	Node initialNode = new Node(s.get(0)[0], s.get(0)[1]);
        Node finalNode = new Node(s.get(1)[0], s.get(1)[1]); //change these to reflect actual finish point
        AStar aStar = new AStar(ROWS, COLS, initialNode, finalNode);
        
        //the test "blocks" to act as obstacles
        //int[][] blocksArray = new int[][] {{1, 3}, {2, 3}, {3, 3}};
        
        aStar.setBlocks(maze);

        //aStar.printSearchArea();

        
        List<Node> path = aStar.findPath();
        
        for (Node node : path)
        {
            System.out.println(node.toString());
        }

        aStar.printSearchArea(path);

    }
    //i think we should do two different kinds of displays for this solver
    //because one way we can show the astar algorithm of checking everything and going
    //towards the exit (best first) and the other we can just show the actual path
    //or maybe start with the algorithm and once the solver completes the maze, we just
    //show the actual path
}
