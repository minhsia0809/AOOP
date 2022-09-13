

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class A1083301_checkpoint7_GameFrame extends JFrame {
    // Description : Width of Frame
    private int FWidth;
    // Description : Height of Frame
    private int FHeight;
    // Description : the displaysize of the map
    public int jfScaler = 2;
    // Description : the obstacle images set. bar_id -> obstacle image
    private HashMap<Integer, Image> obstacleImg = new HashMap<>();
    // Description : the filenames of the obstacle image set. bar_id -> filename
    private HashMap<Integer, String> typeChar = new HashMap<Integer, String>();
    // Description : the obstacle location set queryed from database
    private ArrayList<Integer[]> obstacleDataStructure;
    // Description : the obstacle location set in GUI index version.
    private ArrayList<Integer[]> obstacleList;
    // Description : the object to query data.
    private A1083301_checkpoint7_QueryDB querydb;
    private static ArrayList<A1083301_checkpoint7_House> houseList = new ArrayList<A1083301_checkpoint7_House>();
    private static ArrayList<A1083301_checkpoint7_Barrack> barrackList = new ArrayList<A1083301_checkpoint7_Barrack>();
    private static ArrayList<A1083301_checkpoint7_Pyramid> pyramidList = new ArrayList<A1083301_checkpoint7_Pyramid>();
    private static int PressedX = 0;
    private static int PressedY = 0;
    private static int ReleasedX = 0;
    private static int ReleasedY = 0;
    private static int ClickedX = 0;
    private static int ClickedY = 0;
    private static int keytype = 1;
    
    //Description : the cost of sand weight;
    private final int GRASSWEIGHT = 3;
    //Description : the cost of space weight;
    private final int SAPCEWEIGHT = 1;
    // Description : The main panel.
    public A1083301_checkpoint7_GamePanel gamePanel;
    // Description : The UI panel of spawnMenu.
    public A1083301_checkpoint7_SpawnMenu spawnMenu;
    // Description : The soldier that is selected.
    public A1083301_checkpoint7_Soldier selectedSoldier;
    //Description : the map with all blocks.
    //You can get the location block you want with typing map[x][y].
    private A1083301_checkpoint7_Block[][] map;
    // Description : The route searching algorithm.
    public  int algorithm;

    private int houseCount = 0;
    private int barrackCount = 0;
    private int pyramidCount = 0;

    public A1083301_checkpoint7_GameFrame(int FWidth, int FHeight, String mapID, int jfScaler, int algorithm) throws HeadlessException {
        this.FWidth = FWidth;
        this.FHeight = FHeight;
        this.setTitle("Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FWidth, FHeight);
        this.jfScaler = jfScaler;
        this.obstacleList = new ArrayList<Integer[]>();
        this.obstacleDataStructure = new ArrayList<Integer[]>();
        this.querydb = new A1083301_checkpoint7_QueryDB();
        this.querydb.setMapID(mapID);
        this.algorithm = algorithm;
        /********************************** The TODO (Checkpoint7) ********************************
         * 
         * TODO(1): This time you need to create a map  recording  the info. of every blocks.   
         * Hint 1: You could use "createMap" after using "toGUIIdx" to create the map. 
         * 
        ********************************** The End of the TODO **************************************/
        // TODO(Past): You need to get the obstacle from database and transform it into
        // GUI index version and set your map(panel) on the frame.
        // Hint: In order to build Hashmap obstacleImg, key means the bar_type from
        // database and value equals the Image class that load from the corresponding
        // filepath.
        // Hint2: To get the obstacle set from database, we need you to realize the
        // queryData() in the object QueryDB and get the result.
        // Hint3: obstacle is transformed by obstacleDataStructure via toGUIIdx() in
        // order to let the location transformed from database to panel location.(GUI
        // index version)
        // Hint4: ObstacleDataStructure is a Integer array ([row, column, bartype]) like
        // ArrayList.
        // Obstacle is a Integer array ([x_coordinate, y_coordinate, bartype]) like
        // ArrayList.
        // TODO(Past): This time you need to add a spawnMenu at the bottom of main frame, and set the parent frame.
        // Hint 1: You could use "BorderLayout.SOUTH" to add something at the bottom of main frame.

        /********************************************************************************************
         * START OF YOUR CODE
         ********************************************************************************************/
        querydb.queryData(obstacleDataStructure, typeChar);

        for(Integer i: typeChar.keySet())
        {
            Image image = new ImageIcon(typeChar.get(i)).getImage();
            obstacleImg.put(i, image);
        }

        toGUIIdx(obstacleDataStructure, obstacleList);

        map = createMap(FHeight, FWidth);

        gamePanel = new A1083301_checkpoint7_GamePanel(houseList, barrackList, pyramidList, obstacleList, obstacleImg, jfScaler);
        add(gamePanel, BorderLayout.CENTER);

        spawnMenu = new A1083301_checkpoint7_SpawnMenu();
        add(spawnMenu, BorderLayout.SOUTH);

        /********************************************************************************************
         * END OF YOUR CODE
         ********************************************************************************************/

        this.addComponentListener(new ComponentAdapter() {
            @Override
            // Description : While resizing the windows, the evnet will be happenned(Reset
            // the location of player).
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (houseList.size() != 0) {
                    int x = gamePanel.getWidth() / 2 - gamePanel.getCenterX();
                    int y = gamePanel.getHeight() / 2 - gamePanel.getCenterY();
                    for (A1083301_checkpoint7_House house : houseList) {
                        house.setLocation(x + house.getlocationX() * gamePanel.getGridLen(),
                                y + house.getlocationX() * gamePanel.getGridLen());
                    }
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
//                System.out.println("pressed!");
                char key = e.getKeyChar();
                if (key == 'h') {
                    keytype = 1;
                } else if (key == 'b') {
                    keytype = 2;
                } else {
                    keytype = 3;
                }
            }

        });

        gamePanel.addMouseListener(new MouseAdapter() {
            // Description : the event happenned while mouse be pressed.
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                PressedX = e.getX();
                PressedY = e.getY();

            }

            // Description : the event happenned while mouse be released
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);

                ReleasedX = e.getX();
                ReleasedY = e.getY();

                int moveX = ReleasedX - PressedX;
                int moveY = ReleasedY - PressedY;

                gamePanel.setCenterX(gamePanel.getCenterX() + moveX);
                gamePanel.setCenterY(gamePanel.getCenterY() + moveY);
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                ClickedX = e.getX();
                ClickedY = e.getY();

                int locationX = (Integer)(ClickedX - (gamePanel.getCenterX() - (gamePanel.getGridLen() * 16) + 250)) / gamePanel.getGridLen();
                int locationY = (Integer)(ClickedY - (gamePanel.getCenterY() - (gamePanel.getGridLen() * 16) + 250)) / gamePanel.getGridLen();

                if (ClickedX - (gamePanel.getCenterX() - (gamePanel.getGridLen() * 16) + 250) < 0)
                    locationX = locationX - 1;
                if (ClickedY - (gamePanel.getCenterY() - (gamePanel.getGridLen() * 16) + 250) < 0)
                    locationY = locationY - 1;

                
                if (locationY < 0 || locationY > 15 || locationX < 0 || locationX > 15 && selectedSoldier != null)
                {
                    selectedSoldier.isSelected = false;
                    selectedSoldier.repaint();
                    selectedSoldier = null;
                }
                if (selectedSoldier != null && (locationX >= 0 || locationX <= 15) && (locationY >= 0 || locationY <= 15))
                {
                    synchronized (selectedSoldier)
                    {
                        selectedSoldier.setDestination(locationX, locationY);
                        selectedSoldier.repaint();
                        selectedSoldier.notify();
                    }
//                    selectedSoldier.repaint();
                }
                else if (selectedSoldier == null)
                {
                    boolean check = ClickCheckMouseLocation(ClickedX, ClickedY, true);
                    if (!check)
                    {
                        if (keytype == 1)
                        {
                            A1083301_checkpoint7_House house = new A1083301_checkpoint7_House(locationX, locationY, String.valueOf(houseCount++), jfScaler, SwingConstants.CENTER);

                            houseList.add(house);
                            gamePanel.add(house);
                            gamePanel.setHouseList(houseList);
                            gamePanel.revalidate();
                            Thread thread = new Thread(house);
                            thread.start();
                        }
                        else if (keytype == 2)
                        {
                            A1083301_checkpoint7_Barrack barrack = new A1083301_checkpoint7_Barrack(locationX, locationY, String.valueOf(barrackCount++), jfScaler, SwingConstants.CENTER);
                            barrackList.add(barrack);
                            gamePanel.add(barrack);
                            gamePanel.setBarrackList(barrackList);
                            gamePanel.revalidate();
                            Thread thread = new Thread(barrack);
                            thread.start();
                        }
                        else if (keytype == 3)
                        {
                            A1083301_checkpoint7_Pyramid pyramid = new A1083301_checkpoint7_Pyramid(locationX, locationY, String.valueOf(pyramidCount++), jfScaler, SwingConstants.CENTER);
                            pyramidList.add(pyramid);
                            gamePanel.add(pyramid);
                            gamePanel.setPyramidList(pyramidList);
                            gamePanel.revalidate();
                            Thread thread = new Thread(pyramid);
                            thread.start();
                        }
                    }
                }
                repaint();
            }
        });
        gamePanel.addMouseMotionListener(new MouseAdapter() {
            // Description : the event happenned while mouse be dragged.
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);

                int x = e.getX() - PressedX;
                int y = e.getY() - PressedY;
                gamePanel.setCenterX(gamePanel.getCenterX() + x);
                gamePanel.setCenterY(gamePanel.getCenterY() + y);
                PressedX = e.getX();
                PressedY = e.getY();
                repaint();
            }

        });
        this.setFocusable(true);
        this.requestFocusInWindow();

    }

    // Description : transform the obstacle location from database version to GUI
    // index version
    // data is the database one, and the other.
    public static void toGUIIdx(ArrayList<Integer[]> data, ArrayList<Integer[]> dataGui) {
        for (Integer[] x : data) {
            dataGui.add(new Integer[] { x[1] - 1, x[0] - 1, x[2] });
        }
    }
    /********************************** The TODO (Checkpoint7) ********************************
    * TODO(2): At this time, grass isn't an obstacles, so you have to return false at the situation.
    * 
    /********************************** The TODO (Past) ********************************
    * TODO(Past): Here you will implement the method to check if the grid location passed in is empty.
    * If the location is empty, return false, else return true. The variable "isBuilding" is to check 
    * if you need to take building's  construction scope into consideration. If the "isBuilding" is true,
    * it means that now it's going to build a building, you need to take this building's construction scope
    * into consideration. On the other hand, you only need to check that grid location is empty or not, if
    * "isBuilding" is false.
    * Rules: There are several situations that will cause the location is not empty.
    * 1. There are obstacles on the location.
    * 2. There are buildings on the location.
    * 3. There are soldiers on the location.
    * Hint 1: There are diffirent construction scope for diffirent types of building.
    * Houses are 1*1 grid; Barracks are 1*1 grid; Pyramids are 2*2 grid.
    * Hint 2: You should consider about diffirent types of buildings' situation
    * while checking if there exists obstacle or building in buildings' construction scope.
    * For example, pyramid construction scope is 2*2, In other words, there should
    * be empty in pyramid construction scope.
    ***************************************** The End of the TODO**************************************/
    private boolean locationVarify(int locationX,int locationY,boolean isBuilding){
        int[][] locationCheck = new int[16][16];
        for (int i = 0 ; i < 16 ; i++)
            for (int j = 0 ; j < 16 ; j++)
                locationCheck[i][j] = 0 ;

        for (int i = 0 ; i < 16 ; i++)
        {
            for (int j = 0 ; j < 16 ; j++)
            {
                for (int m = 0 ; m < obstacleList.size() ; m++)
                    if (obstacleList.get(m)[1] == i && obstacleList.get(m)[0] == j && obstacleList.get(m)[2] != 2)
                        locationCheck[i][j] = 1;
                if (houseList.size() > 0)
                {
                    for (int a = 0 ; a < houseList.size() ; a++)
                        if (houseList.get(a).getlocationX() == i && houseList.get(a).getlocationY() == j)
                            locationCheck[i][j] = 1;
                }
                if (barrackList.size() > 0)
                {
                    for (int a = 0 ; a < barrackList.size() ; a++)
                        if (barrackList.get(a).getlocationX() == i && barrackList.get(a).getlocationY() == j)
                            locationCheck[i][j] = 1;
                }
                if (pyramidList.size() > 0)
                {
                    for (int a = 0 ; a < pyramidList.size() ; a++)
                    {
                        if (pyramidList.get(a).getlocationX() == i && pyramidList.get(a).getlocationY() == j)
                        {
                            locationCheck[i][j] = 1;
                            locationCheck[i + 1][j] = 1;
                            locationCheck[i][j + 1] = 1;
                            locationCheck[i + 1][j + 1] = 1;
                        }
                    }
                }
                if (gamePanel.getSoldierList().size() > 0)
                {
                    for (int a = 0 ; a < gamePanel.getSoldierList().size() ; a++)
                    {
                        if (gamePanel.getSoldierList().get(a).getlocationX() == i && gamePanel.getSoldierList().get(a).getlocationY() == j)
                            locationCheck[i][j] = 1;
                    }
                }
            }
        }

        if (isBuilding == true)
        {
            if (locationX < 0 || locationY < 0 || locationX > 15 || locationY > 15)
                return true;
            if (keytype == 3 && locationX == 15 )
                return true;
            if (keytype == 3 && locationY == 15)
                return true;
            if (keytype == 3)
            {
                if (locationCheck[locationX][locationY] == 1 ||
                        locationCheck[locationX + 1][locationY] == 1 ||
                        locationCheck[locationX][locationY + 1] == 1 ||
                        locationCheck[locationX + 1][locationY + 1] == 1)
                    return true;
            }
            else if (keytype == 1 || keytype == 2)
            {
                if (locationCheck[locationX][locationY] == 1)
                    return true;
            }
        }
        else
        {
            if (locationX < 0 || locationY < 0 || locationX > 15 || locationY > 15)
                return true;
            if (locationCheck[locationX][locationY] == 1)
                return true;
        }
        return false;
    }


    public boolean ClickCheckGridLocation(int locationX, int locationY,boolean isBuilding) {
        boolean check = locationVarify(locationX, locationY, isBuilding);
        if (!check)
            return false;
        return true;
    }

    public boolean ClickCheckMouseLocation(int ClickX, int ClickY,boolean isBuilding) {

        int locationX = (Integer)(ClickX - (gamePanel.getCenterX() - (gamePanel.getGridLen() * 16) + 250)) / gamePanel.getGridLen();
        int locationY = (Integer)(ClickY - (gamePanel.getCenterY() - (gamePanel.getGridLen() * 16) + 250)) / gamePanel.getGridLen();
        if (ClickedX -
                (gamePanel.getCenterX() - (gamePanel.getGridLen() * 16) + 250) < 0)
            locationX = locationX - 1;
        if (ClickedY - (gamePanel.getCenterY() -
                (gamePanel.getGridLen() * 16) + 250) < 0)
            locationY = locationY - 1;

        boolean check = locationVarify(locationX, locationY, isBuilding);
        if (!check)
            return false;
        return true;
    }
    //Description : create the map, if each of the loaction will be tag as "grass", "obstacle" or "space".
    public A1083301_checkpoint7_Block[][] createMap(int height, int width){
       
        A1083301_checkpoint7_Block[][] map = new A1083301_checkpoint7_Block[width][height];
        for (Integer[] block: obstacleList){
            int cost = block[2] == 2 ? GRASSWEIGHT : 100;
            String type = block[2] == 2 ? "grass" : "obstacle";
            map[block[0]][block[1]] = new A1083301_checkpoint7_Block(block[0], block[1], type, cost);
        }
        for(int x = 0; x<width; x++){
            for(int y = 0; y<height; y++){
                if(map[x][y] == null){
                    map[x][y] = new A1083301_checkpoint7_Block(x,y,"space",SAPCEWEIGHT);
                }
            }
        }
        return map;
    }
    public A1083301_checkpoint7_Block[][] getMap(){
        return this.map;
    }
}
