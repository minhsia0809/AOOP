

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

public class A1083301_checkpoint7_House extends JLabel implements Runnable {
    // Description : the grid location X of player.
    private int locationX;
    // Description : the grid location Y of player.
    private int locationY;
    // Description : the normal image size.
    private int originalGridLen;
    // Description : the Image the player is.
    private ImageIcon icon;
    private boolean understructure;
    private int jfScaler;
    private int maximum;
    // Description : The number of this type of building.
    private String produced_num;
    // Description : The image of under constructing.
    private ImageIcon constructingIcon;
    // Description : The root frame.
    A1083301_checkpoint7_GameFrame parentFrame;

    @Override
    public void run() {
        int i = 0;
        while (i < 100)
        {
            doNothing(500);
            i = i + 25;
            this.setText(i + "%");
        }
        this.setIcon(icon);
        this.setText(produced_num);
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

    /********************************** The TODO (Past) ********************************
     * 
     * TODO(Past6): Here you need to add a mouse listener to detect mouse click. When the house is clicked ,
     * you need to open the spawn menu and set this house's location as the base for spawning soldier.
     * Noted that a under construction house can't response to mouse click.
     * TODO(Past): Aside from the "house.png", you also need to read in the
     * "constructing.png". You need to set the text of this building to "0%" when it
     * is built. Hint : The size of "constructing.png" has to be the same size of "house.png".
     * Description: this is the player's constructor, we set icon of player here and calculate the distance every step.
     * Hint(Past) text : "player", horizontalAlignment : SwingConstants.CENTER
     * Hint1: Use spawnMenu.setBase() to set base.
     * 
     ********************************** The End of the TODO **************************************/
    
    public A1083301_checkpoint7_House(int locationX, int locationY, String text, int scaler, int horizontalAlignment) {
        super(text, horizontalAlignment);
        this.locationX = locationX;
        this.locationY = locationY;
        this.understructure = true;
        this.jfScaler = scaler;
        this.originalGridLen = 256;
        this.maximum = 5;
        this.produced_num = text;
        understructure = true;

        this.icon = new ImageIcon("Resource/house.png");
        Image img = icon.getImage();
        img = img.getScaledInstance(originalGridLen / scaler, originalGridLen / scaler, Image.SCALE_DEFAULT);
        icon.setImage(img);

        this.constructingIcon = new ImageIcon("Resource/constructing.png");
        Image imgC = constructingIcon.getImage();
        imgC = imgC.getScaledInstance(originalGridLen / scaler, originalGridLen / scaler, Image.SCALE_DEFAULT);
        constructingIcon.setImage(imgC);

        this.setText("0%");
        this.setIcon(constructingIcon);


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (A1083301_checkpoint7_House.this.getIcon().equals(icon))
                {
                    openSpawnMenu();
                    parentFrame.spawnMenu.setBase(getlocationX(), getlocationY());
                }
            }
        });

        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setVerticalTextPosition(JLabel.CENTER);
        this.setVisible(true);

    }

    public void setParentFrame() {
        this.parentFrame = (A1083301_checkpoint7_GameFrame) SwingUtilities.getWindowAncestor(this);
    }

    public void openSpawnMenu() {
        setParentFrame();
        parentFrame.spawnMenu.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public int getlocationX() {
        return this.locationX;
    }

    public void setlocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getlocationY() {
        return this.locationY;
    }

    public void setlocationY(int locationY) {
        this.locationY = locationY;
    }

    public Image getImage() {
        return this.icon.getImage();
    }

}
