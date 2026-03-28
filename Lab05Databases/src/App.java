import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


        // STEP 2 - Create Managers
        if (database.equals("MealPlanning")) {
            rm = new RecipeManager(dm.getMeal());
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
        System.out.println("5. Exit");
    }
}
