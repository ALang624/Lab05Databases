import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DataMgr {
    /* PRIVATE MEMBER VARIABLES */
    private Connection currentConnection; //Current connection to database if not meal/arcade
    private String user; //Username for database connection
    private String pass; //Password for database connection
    // Normally not a great idea to have individually cached connections, but for the sake of this project, it's 
    // done like this.
    private Connection meal;
    private Connection arcade;
    private String database; //Database name for other connections

    /* CREATE CONNECTION */
    public DataMgr(String database) {
        Scanner userInformation = new Scanner(System.in);
        System.out.println("Enter username and password:");
        // String input
        String user = userInformation.nextLine();
        String pass = userInformation.nextLine();

        try {
            if (database.equals("MealPlanning")) {
                meal = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, pass);
            } else if (database.equals("ArcadeGames")) {
                arcade = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, pass);
            } else {
                currentConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, pass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userInformation.close();
    }

    public Connection getCurrentConnection() {
        return currentConnection;
    }

    public Connection getMeal() {
        return meal;
    }

    public Connection getArcade() {
        return arcade;
    }
}
