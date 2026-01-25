package admin;
import java.util.Scanner;
import main.Main;

public class Admin {
    private String name;
    private String adminId;
    
    public Admin(String name, String adminId) {
        this.name = name;
        this.adminId = adminId;
    }
    
    public void showMenu(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("=======================================================\n");
            System.out.println("         COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n=======================================================");
            System.out.println(">> Admin Menu");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("[ADMINISTRATOR INFORMATION]");
            System.out.println("Admin Name\t: " + name);
            System.out.println("Admin ID\t: " + adminId);
            System.out.println("User Type\t: Administrator");
            System.out.println("\n-------------------------------------------------------\n");
            System.out.println("[1] Manage Students");
            System.out.println("[2] Manage Lecturers");
            System.out.println("[3] Manage Courses");
            System.out.println("[4] Logout");
            System.out.println("\n-------------------------------------------------------");
            System.out.print("Enter your choice (1-4): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    Main.clearScreen();
                    ManageStudents.showMenu(scanner);
                    break;
                case "2":
                    Main.clearScreen();
                    ManageLecturers.showMenu(scanner);
                    break;
                case "3":
                    Main.clearScreen();
                    ManageCourses.showMenu(scanner);
                    break;
                case "4":
                    Main.clearScreen();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("\nInvalid choice! Please enter a number from 1 to 4.");
                    scanner.nextLine();
                    Main.clearScreen();
            }
        }
    }
}