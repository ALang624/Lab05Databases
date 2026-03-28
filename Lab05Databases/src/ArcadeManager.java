import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ArcadeManager {
    private Connection conn; // The connection to the ArcadeGames database

    // Constructor for the manager
    public ArcadeManager(Connection conn) {
        this.conn = conn;
    }

    // General Statement - Get All Games
    public ResultSet getAllGames() throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM Game");
    }

    // Prepared - Get Only Games from a Certain Developer
    public ResultSet getGamesByDeveloper(String developer) throws SQLException {
        String query = "SELECT * FROM Game WHERE DeveloperName = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, developer);
        return ps.executeQuery();
    }

    // Call Stored Procedure - Call Existing Procedure
    public ResultSet callStored(String procedure, String input) throws SQLException {
        String call = "{CALL " + procedure + "(?)}";
        PreparedStatement ps = conn.prepareStatement(call);
        ps.setString(1, input);
        return ps.executeQuery();
    }

    // Input-Based - User Inputted Query
    public ResultSet getInfo(String query) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }
}
