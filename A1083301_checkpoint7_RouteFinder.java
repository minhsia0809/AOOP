
import java.util.*;

public class A1083301_checkpoint7_RouteFinder {
    //Description : The target block.
    private A1083301_checkpoint7_Block target;
    private A1083301_checkpoint7_Block root;
    //Description : The hashmap that records the parent block.
    private HashMap<A1083301_checkpoint7_Block, A1083301_checkpoint7_Block> ParentBlock;
    //Description : Record which block has been visited.
    private boolean[][] visited ;
    // Description : The root frame.
    private A1083301_checkpoint7_GameFrame parentFrame;
    //Description : the map with all blocks.
    //You can get the location block you want with typing map[x][y].
    private A1083301_checkpoint7_Block[][] map;
    //Description : record the cost if you go on the block.
    private HashMap<A1083301_checkpoint7_Block, Integer> accumulatedCost;
    // Description : The route searching algorithm.
    private int algorithm;
    private A1083301_checkpoint7_Fringe fringe;
    private static final int DFS = 0;
    private static final int BFS = 1;
    private static final int UCS = 2;
    ArrayList<Integer[]> recordDistance = new ArrayList<>();
    public A1083301_checkpoint7_RouteFinder(A1083301_checkpoint7_GameFrame parentFrame, A1083301_checkpoint7_Block target, A1083301_checkpoint7_Block root, int algorithm, A1083301_checkpoint7_Block[][] map){
        /**********************************The TODO This Time (Checkpoint7)**************************
         * 
         * TODO(1): For the TODO here, you have to implement fringe according "algorithm".
         * 
         * Hint(1): The BFS algorithm needs to use the queue to work, so we make a object named BlockQueue for BFS.
         * Hint(2): The DFS algorithm needs to use the stack to work, so we make a object named BlockStack for DFS.
         * Hint(3): The UCS algorithm needs to use the priority queue  to work, so we make a object named PriorityQueue for UCS.
         * Hint(4): These three objects all implement the fringe, and the detail description can be found 
         *          in the code of Fringe.java, BlockQueue.java, BlockStack.java, BlockPriorityQueue.java.
         * Hint(5): You have to add the root (the player current location) into fringe.
         * Hint(6): To calculate the priority, you have to implement a Comparator<block> object and make 
         *          it as an input in the constructor of BlockPriorityQueue.
         * Hint(7): Before starting the searching, you need to initialize the accumulatedCost and set the root with
         *          its cost.
         **********************************The End of the TODO**************************************/
        this.target = target;
        this.root = root;
        this.ParentBlock = new HashMap<A1083301_checkpoint7_Block, A1083301_checkpoint7_Block>();
        this.parentFrame = parentFrame;
        this.visited = new boolean[4096 / 256][4096 / 256];
        this.accumulatedCost = new HashMap<A1083301_checkpoint7_Block, Integer>();
        this.algorithm = algorithm;
        this.map = map;
        for(int x = 0 ; x < 4096 / 256; x++ ){
            for(int y = 0 ; y < 4096 / 256; y++ ){
                visited[x][y] = false;
            }
        }
        /*************************** START OF YOUR CODE ******************/
        Comparator<A1083301_checkpoint7_Block> comparator = new Comparator<A1083301_checkpoint7_Block>() {
            @Override
            public int compare(A1083301_checkpoint7_Block o1, A1083301_checkpoint7_Block o2) {
                    return Math.abs((root.getCost() - o1.getCost()) - (root.getCost() - o2.getCost()));
            }
        };

        if (algorithm == BFS)
        {
            fringe = new A1083301_checkpoint7_BlockQueue();
        }
        else if (algorithm == DFS)
        {
            fringe = new A1083301_checkpoint7_BlockStack();
        }
        else if (algorithm == UCS)
        {
            fringe = new A1083301_checkpoint7_BlockPriorityQueue(comparator);
        }

        fringe.add(root);
        visited[root.getX()][root.getY()] = true;
        accumulatedCost = new HashMap<A1083301_checkpoint7_Block, Integer>();
        accumulatedCost.put(root, root.getCost());

        /************************ END OF YOUR CODE **********************/
    }
    private A1083301_checkpoint7_Block search(){
        /*********************************The TODO (Checkpoint7)********************************
         * 
         * TODO(14.1): For the TODO here, you have to implement the searching funciton;
         * TODO(14.2): You MUST print the message of "Searching at (x, y)" in order to let us check if you sucessfully do it.
         * TODO(14.3): After you find the target, you just need to return the target block.
         * //System.out.println("Searching at ("+Integer.toString(YOURBLOCK.getX())+", "+Integer.toString(YOURBLOCK.getY())+")");
         * 
         * Hint(1): If the target can not be search you should return null(that means failure).
         * 
         * pseudo code is provided here: 
         *   1. get the block from fringe.
         *   2. print the message
         *   3. if that block equals target return it.
         *   4. if not, expand the block and insert then into fringe.
         *   5. return to 1. until the fringe does not have anyting in it.
         * 
         **********************************The End of the TODO**************************************/
//        System.out.println("search");
        A1083301_checkpoint7_Block block  = null;
        if (parentFrame.ClickCheckGridLocation(target.getX(), target.getY(), false))
            return null;
        while (!fringe.isEmpty())
        {
            block = fringe.remove();
            System.out.println("Searching at (" + block.getX()+", "+ block.getY() + ")");
            if (block.getX() == target.getX() && block.getY() == target.getY())
                return block;
            else
            {
                ArrayList<A1083301_checkpoint7_Block> blocks = expand(block, ParentBlock, visited);
                for (A1083301_checkpoint7_Block expandBlock : blocks)
                {
                    fringe.add(expandBlock);
//                    System.out.println("expandBlock " + expandBlock.getX() + " " + expandBlock.getY());
                }
            }
        }
        return null;
    }
    private ArrayList<A1083301_checkpoint7_Block> expand(A1083301_checkpoint7_Block currentBlock, HashMap<A1083301_checkpoint7_Block, A1083301_checkpoint7_Block> ParentBlock, boolean[][] visited){
        /*********************************The TODO This Time (Checkpoint7)*****************************
         * 
         * TODO(15.1): For the TODO here, you have to implement the expand funciton and return the Blocks(successor);
         * TODO(15.2): the order that you expand is North(Up) West(Left) South(Down) East(Right).
         * TODO(15.3): before adding the block into successor, you have to check if it is valid by checkBlock().
         * TODO(15.4): For the TODO here, you have to calculate the cost of the path that the player walked from 
         * root to new blocks and set it into the HashMap accumulatedCost.
         * 
         * Hint(1): While the block is valid, before you add the block into successor, 
         *        you should set its ParentBlock (We prepare a HashMap to implement this).
         *        And you should also set it is visited. (We prepare 2D boolean array for you) (the (x,y) block <--> visited[x][y] )
         **********************************The End of the TODO**************************************/

        /********************** START OF YOUR CODE ****************************/

        ArrayList<A1083301_checkpoint7_Block> successor = new ArrayList<>();

        A1083301_checkpoint7_Block expandBlock = null;
        if (!parentFrame.ClickCheckGridLocation(currentBlock.getX(), currentBlock.getY() - 1, false) && !visited[currentBlock.getX()][currentBlock.getY() - 1])
        {
            expandBlock = map[currentBlock.getX()][currentBlock.getY() - 1];
            visited[currentBlock.getX()][currentBlock.getY() - 1] = true;
            accumulatedCost.put(expandBlock, accumulatedCost.get(currentBlock) + expandBlock.getCost());
            ParentBlock.put(expandBlock, currentBlock);
            successor.add(expandBlock);
        }
        if (!parentFrame.ClickCheckGridLocation(currentBlock.getX() - 1, currentBlock.getY(), false) && !visited[currentBlock.getX() - 1][currentBlock.getY()])
        {
            expandBlock = map[currentBlock.getX() - 1][currentBlock.getY()];
            visited[currentBlock.getX() - 1][currentBlock.getY()] = true;
            accumulatedCost.put(expandBlock, accumulatedCost.get(currentBlock) + expandBlock.getCost());
            ParentBlock.put(expandBlock, currentBlock);
            successor.add(expandBlock);
        }
        if (!parentFrame.ClickCheckGridLocation(currentBlock.getX(), currentBlock.getY() + 1, false) && !visited[currentBlock.getX()][currentBlock.getY() + 1])
        {
            expandBlock = map[currentBlock.getX()][currentBlock.getY() + 1];
            visited[currentBlock.getX()][currentBlock.getY() + 1] = true;
            accumulatedCost.put(expandBlock, accumulatedCost.get(currentBlock) + expandBlock.getCost());
            ParentBlock.put(expandBlock, currentBlock);
            successor.add(expandBlock);
        }
        if (!parentFrame.ClickCheckGridLocation(currentBlock.getX() + 1, currentBlock.getY(), false) && !visited[currentBlock.getX() + 1][currentBlock.getY()])
        {
            expandBlock = map[currentBlock.getX() + 1][currentBlock.getY()];
            visited[currentBlock.getX() + 1][currentBlock.getY()] = true;
            accumulatedCost.put(expandBlock, accumulatedCost.get(currentBlock) + expandBlock.getCost());
            ParentBlock.put(expandBlock, currentBlock);
            successor.add(expandBlock);
        }

        return successor;

        /********************** END OF YOUR CODE ***********************/

    }

    public A1083301_checkpoint7_RouteLinkedList createRoute(){
        /******************************The TODO This Time (Checkpoint7)*****************************
         * 
         * TODO(16): For the TODO here, you have to trace back the route and return the route;
         * 
         * Hint1: You can get the parent block of target by HashMap ParentBlock, thus you can calculate
         * the last step of the route. And then you get the parent block of  target, 
         * you can calculate the backward step and so on. 
         * 
         * presudo code is provided here:
         *      1. get parent block
         *      2. calculate the delta location
         *      3. insert into head
         *      4. make the target equals its parent block and so on.
         * 
         **********************************The End of the TODO**************************************/

        /*************************** START OF YOUR CODE ***************************/
        A1083301_checkpoint7_RouteLinkedList routeLinkedList = new A1083301_checkpoint7_RouteLinkedList();
        ArrayList<A1083301_checkpoint7_Node> record = new ArrayList<>();

        if (search() == null)
            return null;
        
        A1083301_checkpoint7_Node node = null;
        A1083301_checkpoint7_Block block = ParentBlock.get(target);
        A1083301_checkpoint7_Block sonBlock = target;

        while (!block.equals(root))
        {

            for (A1083301_checkpoint7_Block parentBlockblock : ParentBlock.keySet())
            {
                if (parentBlockblock.equals(block))
                {
                    int distanceX = sonBlock.getX() - block.getX();
                    int distanceY = sonBlock.getY() - block.getY();
//                    System.out.println("distanceX " + distanceX + " distanceY " + distanceY);
                    if (distanceY < 0)
                        node = new A1083301_checkpoint7_Node(-1, 1);
                    else if (distanceX < 0)
                        node = new A1083301_checkpoint7_Node(-1, 0);
                    else if (distanceY > 0)
                        node = new A1083301_checkpoint7_Node(1, 1);
                    else if (distanceX > 0)
                        node = new A1083301_checkpoint7_Node(1, 0);
                    record.add(node);

                    sonBlock = block;
                    block = ParentBlock.get(block);

                }
            }
        }

                int distanceX = sonBlock.getX() - block.getX();
                int distanceY = sonBlock.getY() - block.getY();
//                System.out.println("distanceX " + distanceX + " distanceY " + distanceY);
                if (distanceY < 0)
                    node = new A1083301_checkpoint7_Node(-1, 1);
                else if (distanceX < 0)
                    node = new A1083301_checkpoint7_Node(-1, 0);
                else if (distanceY > 0)
                    node = new A1083301_checkpoint7_Node(1, 1);
                else if (distanceX > 0)
                    node = new A1083301_checkpoint7_Node(1, 0);
                record.add(node);

        int count = 0;
        while(record.size() > 0)
        {
            node = record.get(record.size() - 1);

            if (count == 0)
            {
                routeLinkedList.setHead(node);
                record.remove(record.size() - 1);
                count++;
            }
            else
            {
                routeLinkedList.append(node.getAxis(), node.getDirection());
                record.remove(record.size() - 1);
            }
//            System.out.println("route " + node.getAxis() + " " + node.getDirection());
        }

        return routeLinkedList;
        /************************ END OF YOUR CODE ************************/
    }
}
