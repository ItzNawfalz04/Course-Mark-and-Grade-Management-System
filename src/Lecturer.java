import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Lecturer {
    private String name;
    private String workId;
    ArrayList<CourseAssg> crsAssgList = new ArrayList<>();
    
    public Lecturer(String name, String workId) {
        this.name = name;
        this.workId = workId;
    }
    
    public void showMenu(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("===================================================\n" +
                               "                  LECTURER MENU\n" + 
                               "===================================================");
            System.out.println("\n[LECTURER INFORMATION]\n");
            System.out.println("Lecturer Name\t: " + name);
            System.out.println("Work ID\t: " + workId);
            System.out.println("User Type\t: Lecturer");
            System.out.println("\n---------------------------------------------------");
            System.out.println("1. View Assigned Courses");
            System.out.println("2. View Students in a Course");
            System.out.println("3. Update Student Marks");
            System.out.println("4. View Course Results Summary");
            System.out.println("5. Load Assignment Course from file");
            System.out.println("6. Logout");
            System.out.println("---------------------------------------------------");
            System.out.print("Enter your choice (1-5): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.println("\n=== View Assigned Courses ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would display all courses assigned to this lecturer.");
                    break;
                case "2":
                    System.out.println("\n=== View Students in a Course ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would display all students registered for a selected course.");
                    break;
                case "3":
                    System.out.println("\n=== Update Student Marks ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would allow lecturer to enter/update marks for students.");
                    break;
                case "4":
                    System.out.println("\n=== View Course Results Summary ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would display summary statistics for a course.");
                    break;
                case "5":
                    System.out.println("\n=== Loading Course Assignment from file ===");
                    loadCourseAssg();
                    System.out.println("");
                    break;
                case "6":
                    Main.clearScreen();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("\nInvalid choice! Please enter a number from 1 to 5.");
            }
            
            if (!choice.equals("6")) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                Main.clearScreen();
            }
        }
    }

    /*public void loadCourseAssg() {
        try (BufferedReader br = new BufferedReader(new FileReader("csv_database/CourseAssg.csv"))){
            String line;
            while ((line = br.readLine()) != null) {
                // Handle UTF-8 BOM (Byte Order Mark) if present
                line = line.replace("\uFEFF", "").trim();
                
                // Check if line is not empty
                if (line.isEmpty()) continue;
                
                String[] values = line.split(",");
                if (values.length >= 2) {
                    Course crs = Main.crsList.stream().filter(c -> c.getCode().equals(values[0].trim())).findFirst().orElse(null);
                    if (workId.equals(values[1].trim())){csrAssgList.add(new CourseAssg(crs, "2025/2026", 1));}
                }
            }
            System.out.println("Data Loaded Successfully!");
        }
        catch (IOException e) {
                System.out.println("Error reading file: ");
                System.out.println("Error message: " + e.getMessage());
        }
    }*/     
}
