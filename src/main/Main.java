package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import student.Student;

public class Main {

    public static String currentUserName = "";
    public static String currentUserFullName = "";
    public static String currentUserId = "";
    public static String currentUserRole = "";

    // ✅ Use Paths so file path works nicely across OS
    private static final String STUDENTS_FILE  = Paths.get("csv_database", "Students.csv").toString();
    private static final String LECTURERS_FILE  = Paths.get("csv_database", "Lecturers.csv").toString();
    private static final String ADMINS_FILE    = Paths.get("csv_database", "Admin.csv").toString();

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
        boolean isStudent = authenticateUser(STUDENTS_FILE, username, password, "student");

        if (isStudent) {
            System.out.println("Login successful!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            clearScreen();

            Student student = new Student(currentUserFullName, currentUserId);
            student.showMenu(scanner);
            return;
        }

        // Lecturer Authentication (enable later when your lecturer package exists)
        boolean isLecturer = authenticateUser(LECTURERS_FILE, username, password, "lecturer");
        if (isLecturer) {
            System.out.println("Login successful!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            clearScreen();

            // Lecturer lecturer = new Lecturer(currentUserFullName, currentUserId);
            // lecturer.showMenu(scanner);

            System.out.println("Lecturer module not connected yet (package not set in this step).");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            clearScreen();
            return;
        }

        // Admin Authentication (enable later when your admin package exists)
        boolean isAdmin = authenticateUser(ADMINS_FILE, username, password, "admin");
        if (isAdmin) {
            System.out.println("Login successful!");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            clearScreen();

            // Admin admin = new Admin(currentUserFullName, currentUserId);
            // admin.showMenu(scanner);

            System.out.println("Admin module not connected yet (package not set in this step).");
            System.out.println("Press Enter to continue...");
            scanner.nextLine();
            clearScreen();
            return;
        }

        System.out.println("Error: Invalid username or password. Please try again.");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
        clearScreen();
    }

    private static boolean authenticateUser(String filename, String username, String password, String role) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {

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
