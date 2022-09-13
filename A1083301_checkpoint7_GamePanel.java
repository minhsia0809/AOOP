

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class A1083301_checkpoint7_GamePanel extends JPanel {
    // Description : the obstacle location set in GUI index version.
    private ArrayList<Integer[]> obstacleList;
    // Description : the obstacle images set. bar_id -> obstacle image
    private HashMap<Integer, Image> obstacleImg = new HashMap<>();
    // Description : the image object of the map.
    private Image mapImg = new ImageIcon("Resource/map.png").getImage();
    // Description : the displaysize of the map
    private int scaler;
    private ArrayList<A1083301_checkpoint7_House> houseList = new ArrayList<A1083301_checkpoint7_House>();
    private ArrayList<A1083301_checkpoint7_Barrack> barrackList = new ArrayList<A1083301_checkpoint7_Barrack>();
    private ArrayList<A1083301_checkpoint7_Pyramid> pyramidList = new ArrayList<A1083301_checkpoint7_Pyramid>();
    private ArrayList<A1083301_checkpoint7_Soldier> soldierList = new ArrayList<A1083301_checkpoint7_Soldier>();
    // Description : the normal image size.
    // Hint : while the mapsize is not normal size, you have to think of the
    // displaysize.
    private int originalGridLen = 256;
    // Description : the image displaysize.
    private int gridLen;
    // Description : the map center point x-axis location.
    // Hint : While dragging the map, you may need to set the map location via this.
    private Integer centerX = 0;
    // Description : the map center point y-axis location.
    // Hint : While dragging the map, you may need to set the map location via this.
    private Integer centerY = 0;

    public A1083301_checkpoint7_GamePanel(ArrayList<A1083301_checkpoint7_House> houseList, ArrayList<A1083301_checkpoint7_Barrack> barrackList,
                                          ArrayList<A1083301_checkpoint7_Pyramid> pyramidList, ArrayList<Integer[]> obstacleList, HashMap<Integer, Image> obstacleImg, int scaler) {
        this.obstacleList = obstacleList;
        this.scaler = scaler;
        this.obstacleImg = obstacleImg;
        this.houseList = houseList;
        this.barrackList = barrackList;
        this.pyramidList = pyramidList;
        gridLen = originalGridLen / scaler;
        centerX = (4096 / scaler) / 2;
        centerY = (4096 / scaler) / 2;
    }

    // Description : While painting this JPanel, we draw map on the given location and other obstacles.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        int mapX = 0;
        int mapY = 0;
        int imageX = 0;
        int imageY = 0;
        int width = 0;
        int height = 0;
        Integer obstacleType;

        width = gridLen * 16;
        height = gridLen * 16;
        mapX = centerX - (gridLen * 16) + 250;
        mapY = centerY - (gridLen * 16) + 250;
        g.drawImage(mapImg, mapX, mapY, width, height, this);

        for(int i = 0 ; i < obstacleList.size() ; i++)
        {
            imageX = (obstacleList.get(i)[1] * gridLen) + mapX; //obstacleList.get(i)[1] = x
            imageY = (obstacleList.get(i)[0] * gridLen) + mapY; //obstacleList.get(i)[0] = y
            obstacleType = obstacleList.get(i)[2];
            Image obstacleImage = obstacleImg.get(obstacleType);
            g.drawImage(obstacleImage, imageX, imageY, gridLen, gridLen, this);
        }

        for (int i = 0 ; i < houseList.size() ; i++)
        {
            imageX = (houseList.get(i).getlocationX() * gridLen) + mapX;
            imageY = (houseList.get(i).getlocationY() * gridLen) + mapY;
            houseList.get(i).setLocation(imageX, imageY);
        }

        for (int i = 0 ; i < barrackList.size() ; i++)
        {
            imageX = (barrackList.get(i).getlocationX() * gridLen) + mapX;
            imageY = (barrackList.get(i).getlocationY() * gridLen) + mapY;
            barrackList.get(i).setLocation(imageX, imageY);
        }

        for (int i = 0 ; i < pyramidList.size() ; i++)
        {
            imageX = (pyramidList.get(i).getlocationX() * gridLen) + mapX;
            imageY = (pyramidList.get(i).getlocationY() * gridLen) + mapY;
            pyramidList.get(i).setLocation(imageX, imageY);
        }

        for (int i = 0 ; i < soldierList.size() ; i++)
        {
            imageX = (soldierList.get(i).getlocationX() * gridLen) + mapX;
            imageY = (soldierList.get(i).getlocationY() * gridLen) + mapY;
            soldierList.get(i).setLocation(imageX, imageY);
        }
    }

    public Integer getCenterX() {
        return this.centerX;
    }

    public void setCenterX(Integer centerX) {
        this.centerX = centerX;
    }

    public Integer getCenterY() {
        return this.centerY;
    }

    public void setCenterY(Integer centerY) {
        this.centerY = centerY;
    }

    public Integer getGridLen() {
        return this.gridLen;
    }

    public void setHouseList(ArrayList<A1083301_checkpoint7_House> houseList) {
        this.houseList = houseList;
    }

    public void setBarrackList(ArrayList<A1083301_checkpoint7_Barrack> barrackList) {
        this.barrackList = barrackList;
    }

    public void setPyramidList(ArrayList<A1083301_checkpoint7_Pyramid> pyramidList) {
        this.pyramidList = pyramidList;
    }

    public void addToSoldierList(A1083301_checkpoint7_Soldier soldier){
        this.soldierList.add(soldier);
    }
    public ArrayList<A1083301_checkpoint7_Soldier> getSoldierList(){
        return soldierList;
    }

}
