import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String currentUserName = "";
    static String currentUserFullName = "";
    static String currentUserId = "";
    static String currentUserRole = "";
    
    public static void main(String[] args) {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            System.out.println("=======================================================\n");
            System.out.println("         COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n=======================================================");
            System.out.println(">> Main Menu");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("[1] User Login");
            System.out.println("[2] Exit Program");
            System.out.println("\n-------------------------------------------------------");
            System.out.print("Enter your choice (1-2): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    clearScreen();
                    showLoginPage(scanner);
                    break;
                    
                case "2":
                    System.out.println("\nExiting program. Goodbye!\n");
                    running = false;
                    break;
                    
                default:
                    System.out.println("\nInvalid choice! Please enter 1 or 2.\n");
                    System.out.println("Press Enter to continue...");
                    scanner.nextLine();
                    clearScreen();       
            }
        }
        
        scanner.close();
    }
    
    private static void showLoginPage(Scanner scanner) {
        System.out.println("=======================================================\n");
        System.out.println("         COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n=======================================================");
        System.out.println(">> Main Menu >> User Login");
        System.out.println("-------------------------------------------------------\n");
        System.out.println("[*] Enter your username and Password");
        System.out.print("\nUsername : ");
        String username = scanner.nextLine();
        System.out.print("\nPassword : ");
        String password = scanner.nextLine();
        System.out.println("\n-------------------------------------------------------");
        
        // Student Authentication
        boolean isStudent = authenticateUser("csv_database/Students.csv", username, password, "student");
        
        if (isStudent) {
            System.out.println("Login successful!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            clearScreen();
            Student student = new Student(currentUserFullName, currentUserId);
            student.showMenu(scanner);
        } else {
            // Lecturer Authentication
            boolean isLecturer = authenticateUser("csv_database/Lecturers.csv", username, password, "lecturer");
            
            if (isLecturer) {
                System.out.println("Login successful!");
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                clearScreen();
                Lecturer lecturer = new Lecturer(currentUserFullName, currentUserId);
                lecturer.showMenu(scanner);
            } else {
                // Admin Authentication
                boolean isAdmin = authenticateUser("csv_database/Admin.csv", username, password, "admin");
                
                if (isAdmin) {
                    System.out.println("Login successful!");
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    clearScreen();
                    Admin admin = new Admin(currentUserFullName, currentUserId);
                    admin.showMenu(scanner);
                } else {
                    System.out.println("Error: Invalid username or password. Please try again.");
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    clearScreen();
                }
            }
        }
    }
    
    private static boolean authenticateUser(String filename, String username, String password, String role) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Handle UTF-8 BOM (Byte Order Mark) if present
                line = line.replace("\uFEFF", "").trim();
                
                if (line.isEmpty()) continue;
                
                String[] values = line.split(",");
                if (values.length >= 4) {
                    String name = values[0].trim();
                    String id = values[1].trim();
                    String fileUsername = values[2].trim();
                    String filePassword = values[3].trim();
                    
                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        currentUserName = username;
                        currentUserFullName = name;
                        currentUserId = id;
                        currentUserRole = role;
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            System.out.println("Error message: " + e.getMessage());
        }
        return false;
    }
    
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}