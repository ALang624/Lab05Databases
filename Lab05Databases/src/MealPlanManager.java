import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * MealPlanManager handles the daily meal planning workflow.
 * It lets the user select one recipe per meal slot (breakfast, lunch, dinner)
 * for a family of 1–6 members, then displays the final plan.
 */
public class MealPlanManager {

    private Connection conn;

    public static class MealPlan {
        public final int familySize;
        public final String breakfast;
        public final String lunch;
        public final String dinner;

        // Meal plan object for making the print-out
        public MealPlan(int familySize, String breakfast, String lunch, String dinner) {
            this.familySize  = familySize;
            this.breakfast = breakfast;
            this.lunch = lunch;
            this.dinner = dinner;
        }

        @Override
        public String toString() {
            return String.format(
                "\n=== Daily Meal Plan (Family of %d) ===%n" +
                "  Breakfast : %s%n" +
                "  Lunch     : %s%n" +
                "  Dinner    : %s%n",
                familySize, breakfast, lunch, dinner
            );
        }
    }

    // Actual constructor down here.
    public MealPlanManager(Connection conn) {
        this.conn = conn;
    }

    public int validateFamilySize(String input) {
        try {
            int size = Integer.parseInt(input.trim());
            if (size >= 1 && size <= 6) {
                return size;
            }
        } catch (NumberFormatException ignored) {
            // fall through
        }
        return -1;
    }

    // Get every recipe into one list and then send it through.
    public List<String> getAvailableRecipeNames() throws SQLException {
        List<String> names = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT RecipeName FROM Recipe ORDER BY RecipeName");
        while (rs.next()) {
            names.add(rs.getString("RecipeName"));
        }
        return names;
    }

    // Search for the recipe name in the database, return it if found, otherwise return null.
    public String findRecipeByName(String recipeName) throws SQLException {
        String query = "SELECT RecipeName FROM Recipe WHERE RecipeName = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, recipeName.trim());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("RecipeName");
        }
        return null; // recipe not found
    }

    // Build the meal plan object with the given family size and recipe names.
    public MealPlan buildMealPlan(int familySize, String breakfast, String lunch, String dinner) {
        return new MealPlan(familySize, breakfast, lunch, dinner);
    }
}
