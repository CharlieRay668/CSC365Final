import java.sql.*;

class SQLConnector {
    static   Connection connect;
    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306?user=chray&password=talltree");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
