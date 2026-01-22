import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ManageStudents {

    private static final String STUDENT_FILE = "csv_database/Students.csv";

    public static void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            Main.clearScreen();

            System.out.println("=======================================================\n");
            System.out.println("         COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n=======================================================");
            System.out.println(">> Admin Menu >> Manage Students");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("[1] Add New Student");
            System.out.println("[2] Edit Student");
            System.out.println("[3] Delete Student");
            System.out.println("[4] View All Students");
            System.out.println("[5] Back to Admin Menu");
            System.out.println("\n-------------------------------------------------------");
            System.out.print("Enter your choice (1-5): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addNewStudent(scanner);
                    break;
                case "2":
                    editStudent(scanner);
                    break;
                case "3":
                    deleteStudent(scanner);
                    break;
                case "4":
                    viewAllStudents(scanner);
                    break;
                case "5":
                    running = false;
                    Main.clearScreen();
                    break;

                default:
                    System.out.println("\nInvalid choice! Please enter 1–5.");
                    pause(scanner);
            }
        }
    }

    // Add Student
    private static void addNewStudent(Scanner scanner) {
        Main.clearScreen();
        System.out.println("=======================================================\n");
        System.out.println("         COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n=======================================================");
        System.out.println(">> Admin Menu >> Manage Students >> Add New Student");
        System.out.println("-------------------------------------------------------\n");
        System.out.println("Type 'Exit' to cancel and return to previous menu.");
        System.out.println("\n-------------------------------------------------------\n");

        System.out.print("New Student Name\t: ");
        String name = scanner.nextLine().trim();
        if (isCancel(name)) return;

        System.out.print("New Student ID\t\t: ");
        String studentId = scanner.nextLine().trim();
        if (isCancel(studentId)) return;

        System.out.print("New Student Username\t: ");
        String username = scanner.nextLine().trim();
        if (isCancel(username)) return;

        System.out.print("New Student Password\t: ");
        String password = scanner.nextLine().trim();
        if (isCancel(password)) return;

        System.out.println("\n-------------------------------------------------------");

        // Validation
        if (name.isEmpty() || studentId.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("\nError: All fields are required!");
            pause(scanner);
            return;
        }

        if (isUsernameExists(username)) {
            System.out.println("\nError: Username already exists!");
            pause(scanner);
            return;
        }

        if (isStudentIdExists(studentId)) {
            System.out.println("\nError: Student ID already exists!");
            pause(scanner);
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENT_FILE, true))) {
            pw.println(name + "," + studentId + "," + username + "," + password);
            System.out.println("Student added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to Students.csv");
            System.out.println("Message: " + e.getMessage());
        }

        pause(scanner);
    }

    // Edit Student
    private static void editStudent(Scanner scanner) {
        Main.clearScreen();
        System.out.println("====================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n====================================================================================");
        System.out.println(">> Admin Menu >> Manage Students >> Edit Student");
        System.out.println("------------------------------------------------------------------------------------\n");

        String[] lines = new String[1000]; // simple storage
        int count = 0;

        // Read all students
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (!line.isEmpty()) {
                    lines[count++] = line;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Students.csv");
            pause(scanner);
            return;
        }

        if (count == 0) {
            System.out.println("No student records found.");
            pause(scanner);
            return;
        }

        // Display students table
        System.out.println("All available Students:");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-15s %-15s%n",
                "No.", "Name", "Student ID", "Username", "Password");
        System.out.println("------------------------------------------------------------------------------------");

        for (int i = 0; i < count; i++) {
            String[] data = lines[i].split(",");
            System.out.printf("%-5d %-25s %-15s %-15s %-15s%n",
                    (i + 1),
                    data[0].trim(),
                    data[1].trim(),
                    data[2].trim(),
                    data[3].trim());
        }

        System.out.println("------------------------------------------------------------------------------------");
        System.out.print("Pick student to edit (1-" + count + ") or type 'Exit': ");
        String input = scanner.nextLine().trim();

        if (isCancel(input)) return;

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            pause(scanner);
            return;
        }

        if (choice < 1 || choice > count) {
            System.out.println("Invalid student number.");
            pause(scanner);
            return;
        }

        int index = choice - 1;
        String[] student = lines[index].split(",");

        String oldName = student[0].trim();
        String oldStudentId = student[1].trim();
        String oldUsername = student[2].trim();
        String oldPassword = student[3].trim();

        System.out.println("\n[EDITING STUDENT]");
        System.out.println("Press 'Enter' to keep existing value or 'Exit' to cancel.");
        System.out.println("------------------------------------------------------------------------------------\n");
        System.out.print("Edit Student Name (" + oldName + ") : ");
        String name = scanner.nextLine().trim();
        if (isCancel(name)) return;
        if (name.isEmpty()) name = oldName;

        String studentId;
        while (true) {
            System.out.print("Edit Student ID (" + oldStudentId + ") : ");
            studentId = scanner.nextLine().trim();
            if (isCancel(studentId)) return;
            
            if (studentId.isEmpty()) {
                studentId = oldStudentId;
                break;
            }
            
            // If changed, check uniqueness
            if (!studentId.equalsIgnoreCase(oldStudentId) && isStudentIdExists(studentId)) {
                System.out.println("\nError: Student ID already exists! Please enter a different student ID.\n");
                continue;
            }
            
            break;
        }

        String username;
        while (true) {
            System.out.print("Edit Student Username (" + oldUsername + ") : ");
            username = scanner.nextLine().trim();
            if (isCancel(username)) return;

            // Keep old username
            if (username.isEmpty()) {
                username = oldUsername;
                break;
            }

            // If changed, check uniqueness
            if (!username.equalsIgnoreCase(oldUsername) && isUsernameExists(username)) {
                System.out.println("\nError: Username already exists! Please enter a different username.\n");
                continue; // stay here and re-enter
            }

            break; // valid username
        }

        System.out.print("Edit Student Password (" + oldPassword + ") : ");
        String password = scanner.nextLine().trim();
        if (isCancel(password)) return;
        if (password.isEmpty()) password = oldPassword;

        // Update record
        lines[index] = name + "," + studentId + "," + username + "," + password;

        // Rewrite file
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (int i = 0; i < count; i++) {
                pw.println(lines[i]);
            }
            System.out.println("\n-------------------------------------------------------------------------------------");
            System.out.println("Student record updated successfully!");
        } catch (IOException e) {
            System.out.println("\n-------------------------------------------------------------------------------------");
            System.out.println("Error saving Students.csv");
        }

        pause(scanner);
    }

    // Delete Student
    private static void deleteStudent(Scanner scanner) {
        Main.clearScreen();

        System.out.println("====================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n====================================================================================");
        System.out.println(">> Admin Menu >> Manage Students >> Delete Student");
        System.out.println("------------------------------------------------------------------------------------\n");

        String[] lines = new String[1000];
        int count = 0;

        // Read all students
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (!line.isEmpty()) {
                    lines[count++] = line;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Students.csv");
            pause(scanner);
            return;
        }

        if (count == 0) {
            System.out.println("No student records found.");
            pause(scanner);
            return;
        }

        // Display table
        System.out.println("All available Students:");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-15s %-15s%n","No.", "Name", "Student ID", "Username", "Password");
        System.out.println("------------------------------------------------------------------------------------");

        for (int i = 0; i < count; i++) {
            String[] data = lines[i].split(",");
            System.out.printf("%-5d %-25s %-15s %-15s %-15s%n",
                    (i + 1),
                    data[0].trim(),
                    data[1].trim(),
                    data[2].trim(),
                    data[3].trim());
        }

        System.out.println("------------------------------------------------------------------------------------");
        System.out.print("Pick student to be deleted (1-" + count + ") or type 'Exit': ");
        String input = scanner.nextLine().trim();

        if (isCancel(input)) return;

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            pause(scanner);
            return;
        }

        if (choice < 1 || choice > count) {
            System.out.println("Invalid student number.");
            pause(scanner);
            return;
        }

        int index = choice - 1;
        String[] student = lines[index].split(",");

        String name = student[0].trim();
        String studentId = student[1].trim();
        String username = student[2].trim();
        String password = student[3].trim();

        // Confirmation
        System.out.print("\nAre you sure you want to delete this student? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("\nDelete operation cancelled.");
            pause(scanner);
            return;
        }

        // Rewrite CSV excluding deleted student
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (int i = 0; i < count; i++) {
                if (i != index) {
                    pw.println(lines[i]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating Students.csv");
            pause(scanner);
            return;
        }

        // Show deleted info
        System.out.println("\nStudent Deleted Successfully!");
        System.out.println("[INFORMATION OF DELETED STUDENT]");
        System.out.println("Name\t\t: " + name);
        System.out.println("Student ID\t: " + studentId);
        System.out.println("Username\t: " + username);
        System.out.println("Password\t: " + password);
        System.out.println("------------------------------------------------------------------------------------");
        pause(scanner);
    }

    // View All Students
    private static void viewAllStudents(Scanner scanner) {
        Main.clearScreen();
        System.out.println("====================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n====================================================================================");
        System.out.println(">> Admin Menu >> Manage Students >> View All Students");
        System.out.println("------------------------------------------------------------------------------------\n");

        System.out.println("All available Students:");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-15s %-15s%n",
                "No.", "Name", "Student ID", "Username", "Password");
        System.out.println("------------------------------------------------------------------------------------");

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 4) {
                    count++;
                    System.out.printf("%-5d %-25s %-15s %-15s %-15s%n",
                            count,
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim()
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Students.csv");
            System.out.println("Message: " + e.getMessage());
        }

        if (count == 0) {
            System.out.println("No student records found.");
        }

        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Press Enter to go back...");
        scanner.nextLine();
    }

    // Username Check (checks across all user files)
    private static boolean isUsernameExists(String username) {
        return checkFileForUsername("csv_database/Admin.csv", username) ||
               checkFileForUsername("csv_database/Lecturers.csv", username) ||
               checkFileForUsername("csv_database/Students.csv", username);
    }

    // Student ID Check (checks only in Students.csv)
    private static boolean isStudentIdExists(String studentId) {
        return checkFileForStudentId("csv_database/Students.csv", studentId);
    }

    private static boolean checkFileForUsername(String filename, String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 3) {
                    if (data[2].trim().equalsIgnoreCase(username)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // File might not exist yet, which is okay
        }
        return false;
    }

    private static boolean checkFileForStudentId(String filename, String studentId) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    if (data[1].trim().equalsIgnoreCase(studentId)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // File might not exist yet, which is okay
        }
        return false;
    }

    public static void pause(Scanner scanner) {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
    
    public static boolean isCancel(String input) {
        if (input.equalsIgnoreCase("Exit")) {
            System.out.println("\nOperation cancelled. Returning to previous menu...");
            return true;
        }
        return false;
    }
}