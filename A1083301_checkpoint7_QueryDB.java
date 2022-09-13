

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class A1083301_checkpoint7_QueryDB {
    // //Description : the driver description of mysql
    // private static final String driver = "com.mysql.cj.jdbc.Driver";
    // //Description : the protocol description of mysql
    // private static final String protocol = "jdbc:mysql://140.127.220.220:3306/";
    // Description : the obstacle set queried from database.
    private static ArrayList<Integer[]> data = new ArrayList<Integer[]>();
    // Description : the filename of obstacle image queried from database.
    private static HashMap<Integer, String> typeChar = new HashMap<Integer, String>();
    // Description : the primary key of map in database.
    private static String mapID = "0";

    public static void queryData(ArrayList<Integer[]> data, HashMap<Integer, String> typeChar) {
        // TODO(Past): Querying the barrier location from the server, and set it into
        // Arraylist data.
        // TODO(Past): Querying the bar_type and the corresponding file_name from the
        // server, and set it into HashMap.
        /********************************************************************************************
         * START OF YOUR CODE
         ********************************************************************************************/
        final String driver = "org.postgresql.Driver";
        final String DATABASE_URL = "jdbc:postgresql://140.127.220.226:5432/oopiickp";

        try
        {
            Class.forName(driver).newInstance();
        }
        catch(Exception err)
        {
            System.err.println("Unable to load the mysql driver.");
            err.printStackTrace(System.err);
            System.exit(0);
        }

        try(
                Connection connection = DriverManager.getConnection(DATABASE_URL , "fallckp" ,"2021OOPIIpwd");
                Statement statement = connection.createStatement() )
        {
            final String SELECT_OBSTACLE_INFO_QUERY = "SELECT obstacle_info.x_coordinate,obstacle_info.y_coordinate,obstacle_info.obstacle_type FROM obstacle_info WHERE obstacle_info.map_id=" + mapID;
            final String SELECT_OBSTACLE_STYLE_QUERY = "SELECT obstacle_type,filename FROM obstacle_style";

            ResultSet obstacleInfoResultSet = statement.executeQuery(SELECT_OBSTACLE_INFO_QUERY);
            Integer[] obstacle = new Integer[3];
            while(obstacleInfoResultSet.next())
            {
                obstacle[0] = (Integer)obstacleInfoResultSet.getObject(1);
                obstacle[1] = (Integer)obstacleInfoResultSet.getObject(2);
                obstacle[2] = (Integer)obstacleInfoResultSet.getObject(3);
                data.add(obstacle);
                obstacle = new Integer[3];
            }
            obstacleInfoResultSet.close();

            ResultSet obstacleStyleResultSet = statement.executeQuery(SELECT_OBSTACLE_STYLE_QUERY);
            while(obstacleStyleResultSet.next())
            {
                String imgFilename = "";// "Resource/" + obstacleStyleResultSet.getObject(2).toString();
                if (obstacleStyleResultSet.getObject(2) == null)
                    typeChar.put((Integer)obstacleStyleResultSet.getObject(1), "null");
                else
                {
                    imgFilename = "Resource/" + obstacleStyleResultSet.getObject(2).toString();
                    typeChar.put((Integer)obstacleStyleResultSet.getObject(1), imgFilename);

                }
            }
            obstacleStyleResultSet.close();

            statement.close();
            connection.close();
        }
        catch(SQLException err)
        {
            System.err.println("SQL error.");
            err.printStackTrace(System.err);
            System.exit(0);
        }
        /********************************************************************************************
         * END OF YOUR CODE
         ********************************************************************************************/

    }

    public ArrayList getObstacle() {
        return this.data;
    }

    public void setObstacle(ArrayList<Integer[]> data) {
        this.data = data;
    }

    public String getMapID() {
        return this.mapID;
    }

    public void setMapID(String mapID) {
        this.mapID = mapID;
    }

    public HashMap getObstacleImg() {
        return this.typeChar;
    }

    public void setObstacleImg(HashMap<Integer, String> typeChar) {
        this.typeChar = typeChar;
    }
}
