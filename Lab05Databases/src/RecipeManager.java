import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RecipeManager {
    private Connection conn; // The connection to the MealPlanning database

    // Constructor for the manager
    public RecipeManager(Connection conn) {
        this.conn = conn;
    }

    // General Statement - Get All Recipes
    public ResultSet getAllRecipes() throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM Recipe");
    }

    // Prepared - Get Only Book Recipes
    public ResultSet getRecipeByBook(String bookName) throws SQLException {
        String query = "SELECT * FROM Recipe WHERE CookbookName = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, bookName);
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
