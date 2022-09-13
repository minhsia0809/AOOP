

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Description : This class will implement Runnable and checkpoint7_Soldier_Movement.(may need to rename)
public class A1083301_checkpoint7_Soldier extends JLabel implements Runnable, A1083301_checkpoint7_Soldier_Movement {
    // Description : the grid location X of player.
    private int locationX;
    // Description : the grid location Y of player.
    private int locationY;
    // Description : the normal image size.
    private int originalGridLen;
    // Description : the Image the player is.
    private ImageIcon icon;
    // Description : The image of progress to grow up.
    private ImageIcon[] growingIcons;
    // Description : The root frame.
    private A1083301_checkpoint7_GameFrame parentFrame ;
    // Description : To check if this soldier is selected.
    public boolean isSelected;
    // Description : To check if this soldier is a grown up.
    private boolean isGrown;
    // Description : To store the route to get to the destination.
    private A1083301_checkpoint7_RouteLinkedList route = new A1083301_checkpoint7_RouteLinkedList();
    // Description : The grid location of destination.
    private int destinationX, destinationY;
    //Description : UCS : 2, BFS : 1, DFS : 0
    private int algorithm;


    public A1083301_checkpoint7_Soldier(int locationX, int locationY, int scaler, int horizontalAlignment, int algorithm) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.originalGridLen = 256;
        this.icon = new ImageIcon("Resource/soldier.png");
        Image img = icon.getImage();
        img = img.getScaledInstance(originalGridLen / scaler, originalGridLen / scaler, Image.SCALE_DEFAULT);
        icon.setImage(img);
        this.isSelected = false;
        this.isGrown = false;
        this.algorithm = algorithm;
        setParentFrame();

        growingIcons = new ImageIcon[6];
        for (int i = 0 ; i < 6 ; i++)
        {
            ImageIcon babyIcon = new ImageIcon("Resource/baby" + i +".png");
            growingIcons[i] = babyIcon;
            Image baby = growingIcons[i].getImage();
            baby = baby.getScaledInstance(originalGridLen / scaler, originalGridLen / scaler, Image.SCALE_DEFAULT);
            growingIcons[i].setImage(baby);
        }
        this.setIcon(growingIcons[0]);
        this.setVisible(true);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getSelf().getIcon().equals(icon))
                {
                    setParentFrame();
                    if (!isSelected)
                    {
                        resetSelectedSoldier();
                        for (A1083301_checkpoint7_Soldier soldier : parentFrame.gamePanel.getSoldierList())
                        {
                            soldier.isSelected = false;
                            soldier.repaint();
                        }
                        isSelected = true;

                        setSelectedSoldier();
                        repaint();
                    }
                    else if (isSelected)
                    {
                        isSelected = false;
                        resetSelectedSoldier();
                        repaint();
                    }
                }
            }
        });

        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setVerticalTextPosition(JLabel.CENTER);

    }


    @Override
    public void run() {

        int growNum = 0;

        while (growNum < 6)
        {
            this.setIcon(growingIcons[growNum]);
            doNothing(500);//500
            growNum++;
        }
        this.setIcon(icon);
        isGrown = true;
        try {
            while(true)
            {
                while(isGrown)
                {
                    synchronized (this){
                        while (!isSelected)
                        {
                            wait();
                        }
                        detectRoute();
//                        startMove();
                        isSelected = false;
                        resetSelectedSoldier();
                        notifyAll();
                        if (route != null)
                            startMove();
//                        notifyAll();
                    }
                }
            }
        }catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    // Description : Stop the thread in milliseconds.
    private static void doNothing(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Unexpected interruption");
            System.exit(0);
        }
    }
    /*********************************The TODO This Time (Checkpoint7)***************************
     * 
     * TODO(11): This time you have to instantiate "checkpoint7_RouteFinder" , 
     * and get "route" by method "route = routefinder.createRoute();"
     * Hint1: You could change variable "isSelected" and use method resetSelectedSoldier().
     * Hint2: You could get root block and target block from "parentFrame.getMap()".
     * 
     ********************************** The End of the TODO**************************************/
    
    @Override
    public void detectRoute() {
        /********************************************************************************************
         * START OF YOUR CODE
         ********************************************************************************************/
        A1083301_checkpoint7_Block target = null;
        A1083301_checkpoint7_Block root = null;
        root = parentFrame.getMap()[parentFrame.selectedSoldier.getlocationX()][parentFrame.selectedSoldier.getlocationY()];
        target = parentFrame.getMap()[destinationX][destinationY];

        A1083301_checkpoint7_RouteFinder routefinder = new A1083301_checkpoint7_RouteFinder(parentFrame, target, root, algorithm, parentFrame.getMap());
        route = routefinder.createRoute();

        /******************************* END OF YOUR CODE *********/
    }
    /*******************************The TODO This Time (Checkpoint7)******************************
     * 
     * TODO(12): After detecting the route, the soldier starts to move to the destination based on the route.
     * There should be a 0.5 seconds pause between each movement.
     * Hint1: You could use "ClickCheckGridLocation()" to check whether the next grid is empty. 
     * 
     ********************************** The End of the TODO**************************************/
    @Override
    // @Override
    public void startMove() {
        /************ START OF YOUR CODE ********************/
        A1083301_checkpoint7_Node node = route.getHead();
        int axis = 0;
        int direction = 0;
//        System.out.println(node.getAxis());
        while (node != null)
        {
            axis = node.getAxis();
            direction = node.getDirection();

            if (axis == 0)
            {
                if (!parentFrame.ClickCheckGridLocation(locationX + direction, locationY, false))
                    locationX = locationX + direction;
            }
            else if (axis == 1)
            {
                if (!parentFrame.ClickCheckGridLocation(locationX, locationY + direction, false))                    
                    locationY = locationY + direction;
            }
            repaint();
//            route.delete(node.getAxis(), node.getDirection());
            node = node.getNext();

            doNothing(500);
        }
        

        /************** END OF YOUR CODE ******************/
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isSelected && isGrown) //Draw a rectangle border around the selected soldier.
            g.drawRect(0, 0, getImage().getWidth(null) - 1, getImage().getHeight(null) - 1);
    }

    // Description : Set the root frame.
    public void setParentFrame() {
        this.parentFrame = (A1083301_checkpoint7_GameFrame) SwingUtilities.getWindowAncestor(this);
    }

    // Description : Set the destination in grid location format.
    public void setDestination(int destinationX, int destinationY) {
        this.destinationX = destinationX;
        this.destinationY = destinationY;
    }

    // Description : Set selected soldier.
    public void setSelectedSoldier() {
        setParentFrame();
        parentFrame.selectedSoldier = this;
    }

    // Description : Reset selected soldier.
    public void resetSelectedSoldier() {
        setParentFrame();
        parentFrame.selectedSoldier = null;
        repaint();
    }
    // Description : Return current position X.
    public int getlocationX() {
        return this.locationX;
    }
    // Description : Set current position X.
    public void setlocationX(int locationX) {
        this.locationX = locationX;
    }
    // Description : Return current position Y.
    public int getlocationY() {
        return this.locationY;
    }
    // Description : Set current position Y.
    public void setlocationY(int locationY) {
        this.locationY = locationY;
    }

    public Image getImage() {
        return this.icon.getImage();
    }
    // Description : Return self.
    public A1083301_checkpoint7_Soldier getSelf(){
        return this;
    }

}
