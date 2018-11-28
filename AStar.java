
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStar
{
    private static int DEFAULT_HV_COST = 10; //horizontal - vertical cost
    private static int DEFAULT_DIAGONAL_COST = 14;
    private int hvCost;
    private int diagonalCost;
    private Node[][] searchArea;
    private PriorityQueue<Node> openList;
    private List<Node> closedList;
    private Node initialNode;
    private Node finalNode;

    //constructors
    public AStar(int rows, int cols, Node initialNode, Node finalNode, int hvCost, int diagonalCost)
    {
        this.hvCost = hvCost;
        this.diagonalCost = diagonalCost;
        setInitialNode(initialNode);
        setFinalNode(finalNode);
        this.searchArea = new Node[rows][cols];
        this.openList = new PriorityQueue<Node>(new Comparator<Node>() {
		            @Override
		            public int compare(Node node0, Node node1)
		            {
		                return node0.getF() < node1.getF() ? -1 : node0.getF() > node1.getF() ? 1 : 0;
		            }
		        });
        setNodes();
        this.closedList = new ArrayList<Node>();
    }

    public AStar(int rows, int cols, Node initialNode, Node finalNode)
    {
        this(rows, cols, initialNode, finalNode, DEFAULT_HV_COST, DEFAULT_DIAGONAL_COST);
    }

    //getters and setters
    public int getHvCost()
    {
        return hvCost;
    }

    public void setHvCost(int hvCost)
    {
        this.hvCost = hvCost;
    }

    private int getDiagonalCost()
    {
        return diagonalCost;
    }

    private void setDiagonalCost(int diagonalCost)
    {
        this.diagonalCost = diagonalCost;
    }
    public Node getInitialNode()
    {
        return initialNode;
    }

    public void setInitialNode(Node initialNode)
    {
        this.initialNode = initialNode;
    }

    public Node getFinalNode()
    {
        return finalNode;
    }

    public void setFinalNode(Node finalNode)
    {
        this.finalNode = finalNode;
    }

    private void setNodes()
    {
        for (int i = 0; i < searchArea.length; i++)
        {
            for (int j = 0; j < searchArea[0].length; j++)
            {
                Node node = new Node(i, j);
                node.calculateHeuristic(getFinalNode());
                this.searchArea[i][j] = node;
            }
        }
    }
		//Jack WILL set this to private, just in case I don't need it.
    private void setBlock(int row, int col)
    {
        this.searchArea[row][col].setBlock(true);
    }

    public void setBlocks(boolean[][] maze)
    {
        for (int i = 0; i < maze.length; i++) {
        	for(int j = 0; j < maze[i].length; j++) {
        		if((maze[i][j] == false))setBlock(i, j); //if maze[i][j] is false then setBlock equal to false
        	}
        }
    }

    public Node[][] getSearchArea()
    {
        return searchArea;
    }

    public void setSearchArea(Node[][] searchArea)
    {
        this.searchArea = searchArea;
    }

    public PriorityQueue<Node> getOpenList()
    {
        return openList;
    }

    public void setOpenList(PriorityQueue<Node> openList)
    {
        this.openList = openList;
    }

    public List<Node> getClosedList()
    {
        return closedList;
    }

    public void setClosedList(List<Node> closedList)
    {
        this.closedList = closedList;
    }

    public List<Node> findPath()
    {
        openList.add(initialNode);
        while (!isEmpty(openList))
        {
            Node currentNode = openList.poll();
            closedList.add(currentNode);
           
            if (isFinalNode(currentNode))
            {
                return getPath(currentNode);//calls getPath(finalNode)
            }
            else
            {
                addAdjacentNodes(currentNode);
            }
        }
        return new ArrayList<Node>();
    }
    private List<Node> getPath(Node currentNode)
    {
        List<Node> path = new ArrayList<Node>();
        path.add(currentNode);
        Node parent;
        while ((parent = currentNode.getParent()) != null)
        {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }

    //methods that add components
    private void addAdjacentNodes(Node currentNode)
    {
        addAdjacentUpperRow(currentNode);
        addAdjacentMiddleRow(currentNode);
        addAdjacentLowerRow(currentNode);
    }

    private void addAdjacentLowerRow(Node currentNode)
    {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int lowerRow = row + 1;
        if (lowerRow < getSearchArea().length)
        {
            if (col - 1 >= 0)
            {
                //checkNode(currentNode, col - 1, lowerRow, getDiagonalCost());
                //commented because not sure if diagonal movements are allowed
            }
            if (col + 1 < getSearchArea()[0].length)
            {
                //checkNode(currentNode, col + 1, lowerRow, getDiagonalCost());
                //commented for the same reason as previously
            }
            checkNode(currentNode, col, lowerRow, getHvCost());
        }
    }

    private void addAdjacentMiddleRow(Node currentNode)
    {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int middleRow = row;
        if (col - 1 >= 0)
        {
            checkNode(currentNode, col - 1, middleRow, getHvCost());
        }
        if (col + 1 < getSearchArea()[0].length) {
            checkNode(currentNode, col + 1, middleRow, getHvCost());
        }
    }

    private void addAdjacentUpperRow(Node currentNode)
    {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int upperRow = row - 1;
        if (upperRow >= 0)
        {
            if (col - 1 >= 0)
            {
                //checkNode(currentNode, col - 1, upperRow, getDiagonalCost());
                // commented for same reason as previously stated
            }
            if (col + 1 < getSearchArea()[0].length)
            {
                //checkNode(currentNode, col + 1, upperRow, getDiagonalCost());
                //can we go diagonal??
            }
            checkNode(currentNode, col, upperRow, getHvCost());
        }
    }

    //check methods
    private void checkNode(Node currentNode, int col, int row, int cost)
    {
        Node adjacentNode = getSearchArea()[row][col];
        if (!adjacentNode.isBlock() && !getClosedList().contains(adjacentNode))
        {
            if (!getOpenList().contains(adjacentNode))
            {
                adjacentNode.setNodeData(currentNode, cost);
                getOpenList().add(adjacentNode);
            }
            else
            {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed)
                {
                    //remove and add the changed node, so that the Priority Queue can re-sort
                    getOpenList().remove(adjacentNode);
                    getOpenList().add(adjacentNode);
                }
            }
        }
    }

    private boolean isFinalNode(Node currentNode)
    {
        return currentNode.equals(finalNode);
    }

    private boolean isEmpty(PriorityQueue<Node> openList)
    {
        return openList.size() == 0;
    }
    
    
    public void printSearchArea(List<Node> path) {
    	System.out.println("This is the Search Area: ");
    	
    	for(int i = 0; i < searchArea.length; i++) {
    		for(int j = 0; j < searchArea[i].length; j++) {
    			if(!searchArea[i][j].isBlock()) {
    				if(path.contains(searchArea[i][j])) {
    					System.out.print("<>");
    				} else {
    					System.out.print("  "); //two whitespaces
    				}
    			} else {
       				System.out.print("\u2588\u2588");//two fullblock black spaces    				
    			}
    		}
    		System.out.println();
    	}
    }

}
