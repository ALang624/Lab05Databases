import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        // STEP 1 - Create Data Manager
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter Database Name:");
        String database = scan.nextLine();
        DataMgr dm = new DataMgr(database);
        RecipeManager rm = null;
        ArcadeManager am = null;
        MealPlanManager mpm = null;


        // STEP 2 - Create Managers
        if (database.equals("MealPlanning")) {
            rm = new RecipeManager(dm.getMeal());
            mpm = new MealPlanManager(dm.getMeal());
        } else if (database.equals("ArcadeGames")) {
            am = new ArcadeManager(dm.getArcade());
        }
        /*else {
            If we had a third manager set up for another database, this is where it'd go.
        }*/

        // STEP 3 - Use Managers to Get Data
        boolean running = true;

        while(running) {
            printMenu();
            System.out.print("Choice: ");
            String choice = scan.nextLine().trim();
 
            try {
                switch (choice) {
                        case "1": {
                        System.out.println("\n--- General Statement ---");
                        ResultSet rs;
                        if (database.equals("MealPlanning")) {
                            rs = rm.getAllRecipes();
                            while (rs.next()) {
                                System.out.println(rs.getString("RecipeName"));
                            }
                        } else {
                            rs = am.getAllGames();
                            while (rs.next()) {
                                System.out.println(rs.getString("GameName"));
                            }
                        }
                        break;
                    }
                    case "2": {
                        System.out.println("\n--- Prepared Statement ---");
                        System.out.print("Enter Developer/Cookbook Name: ");
                        String input = scan.nextLine().trim();
                        ResultSet rs;
                        if (database.equals("MealPlanning")) {
                            rs = rm.getRecipeByBook(input);
                            while (rs.next()) {
                                System.out.println(rs.getString("RecipeName"));
                            }
                        } else {
                            rs = am.getGamesByDeveloper(input);
                            while (rs.next()) {
                                System.out.println(rs.getString("GameName"));
                            }
                        }
                        break;
                    }
                    case "3": {
                        System.out.println("\n--- Call Stored Procedure ---");
                        System.out.print("Enter Procedure Name: ");
                        String procedure = scan.nextLine().trim();
                        System.out.print("Enter Input: ");
                        String input = scan.nextLine().trim();
                        ResultSet rs;
                        if (database.equals("MealPlanning")) {
                            rs = rm.callStored(procedure, input);
                            while (rs.next()) {
                                System.out.println(rs.getString("RecipeName"));
                            }
                        } else {
                            rs = am.callStored(procedure, input);
                            while (rs.next()) {
                                System.out.println(rs.getString("GameName"));
                            }
                        }
                        break;
                    }
                    case "4": {
                        System.out.println("\n--- Input Custom Query ---");
                        System.out.print("Enter Query: ");
                        String query = scan.nextLine().trim();
                        ResultSet rs;
                        if (database.equals("MealPlanning")) {
                            rs = rm.getInfo(query);
                            while (rs.next()) {
                                System.out.println(rs.getString(1));
                            }
                        } else {
                            rs = am.getInfo(query);
                            while (rs.next()) {
                                System.out.println(rs.getString(1));
                            }
                        }
                        break;
                    }
                    case "5": {
                        // Check for correct database
                        if (!database.equals("MealPlanning")) {
                            System.out.println("Meal planning is only available for the MealPlanning database.");
                            break;
                        }

                        System.out.println("\n--- Plan Meals ---");

                        // Get family size
                        int familySize = -1;
                        while (familySize == -1) {
                            System.out.print("Enter family size (1-6): ");
                            String sizeInput = scan.nextLine().trim();
                            familySize = mpm.validateFamilySize(sizeInput); //Make sure it's a legal number
                            if (familySize == -1) {
                                System.out.println("Invalid size. Please enter a whole number between 1 and 6.");
                            }
                        }

                        List<String> recipes = mpm.getAvailableRecipeNames();
                        if (recipes.isEmpty()) {
                            System.out.println("No recipes found in the database. Cannot build a meal plan.");
                            break;
                        }

                        // Print recipes
                        System.out.println("\nAvailable recipes:");

                        for (int i = 0; i < recipes.size(); i++) {
                            System.out.printf("  %d. %s%n", i + 1, recipes.get(i));
                        }
 
                        // Check each recipe against the database, accept if in it
                        String[] slots     = {"breakfast", "lunch", "dinner"};
                        String[] selections = new String[3];
 
                        for (int i = 0; i < slots.length; i++) {
                            String chosen = null;
                            while (chosen == null) {
                                System.out.printf("Enter recipe name for %s: ", slots[i]);
                                String recipeInput = scan.nextLine().trim();
                                chosen = mpm.findRecipeByName(recipeInput);
                                if (chosen == null) {
                                    System.out.println("Recipe not found. Please choose from the list above.");
                                }
                            }
                            selections[i] = chosen;
                        }

                        // Print meal plan
                        MealPlanManager.MealPlan plan = mpm.buildMealPlan(familySize, selections[0], selections[1], selections[2]);
                        System.out.println(plan);
                        break;
                    }
                    case "6": {
                        running = false;
                        break;
                    }
            }
        } catch (SQLException e) {
                System.out.println("An error occurred while processing your request. Please try again.");
            }
        }
        scan.close();
    }

    private static void printMenu() {
        System.out.println("1. General Statement");
        System.out.println("2. Prepared Statement");
        System.out.println("3. Call Stored Procedure");
        System.out.println("4. Input Custom Query");
        System.out.println("5. Plan Meals");
        System.out.println("6. Exit");
    }
}
