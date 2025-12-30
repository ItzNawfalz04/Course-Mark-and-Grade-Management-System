import java.util.Scanner;

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
            System.out.println("===================================================\n" +
                               "                    ADMIN MENU\n" + 
                               "===================================================");
            System.out.println("\n[ADMIN INFORMATION]\n");
            System.out.println("Admin Name\t: " + name);
            System.out.println("Admin ID\t: " + adminId);
            System.out.println("User Type\t: Administrator");
            System.out.println("\n---------------------------------------------------");
            System.out.println("[1] Manage Students");
            System.out.println("[2] Manage Lecturers");
            System.out.println("[3] Logout");
            System.out.println("---------------------------------------------------");
            System.out.print("Enter your choice (1-3): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    clearScreen();
                    Admin_ManageStudents.showMenu(scanner);
                    break;
                case "2":
                    System.out.println("\n=== Manage Lecturers ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would allow admin to add, remove, or modify lecturer accounts.");
                    scanner.nextLine();
                    clearScreen();
                    break;
                case "3":
                    clearScreen();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("\nInvalid choice! Please enter a number from 1 to 2.");
                    scanner.nextLine();
                    clearScreen();
            }
        }
    }
    
    private void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing fails, just print multiple newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}