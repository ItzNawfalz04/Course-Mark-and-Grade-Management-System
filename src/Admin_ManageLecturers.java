import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Admin_ManageLecturers {

    private static final String LECTURER_FILE = "csv_database/Lecturers.csv";

    public static void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            Main.clearScreen();
            System.out.println("===================================================");
            System.out.println("                 MANAGE LECTURERS"); 
            System.out.println("===================================================\n");
            System.out.println(">> Admin >> Manage Lecturers\n");
            System.out.println("---------------------------------------------------");
            System.out.println("[1] Add New Lecturer");
            System.out.println("[2] Edit Lecturer");
            System.out.println("[3] Delete Lecturer");
            System.out.println("[4] View All Lecturers");
            System.out.println("[5] Back to Admin Menu");
            System.out.println("---------------------------------------------------");
            System.out.print("Enter your choice (1-5): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    addNewLecturer(scanner);
                    break;
                case "2":
                    editLecturer(scanner);
                    break;
                case "3":
                    deleteLecturer(scanner);
                    break;
                case "4":
                    viewAllLecturers(scanner);
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

    // Add Lecturer
    private static void addNewLecturer(Scanner scanner) {
        Main.clearScreen();
        System.out.println("===================================================");
        System.out.println("                  ADD NEW LECTURER"); 
        System.out.println("===================================================\n");
        System.out.println(">> Admin >> Manage Lecturers >> Add New Lecturer");
        System.out.println("\nType 'Exit' to cancel and return to previous menu.");
        System.out.println("\n---------------------------------------------------");

        System.out.print("Lecturer Name\t\t: ");
        String name = scanner.nextLine().trim();
        if (isCancel(name)) return;

        System.out.print("Lecturer Work ID\t: ");
        String workId = scanner.nextLine().trim();
        if (isCancel(workId)) return;

        System.out.print("Lecturer Username\t: ");
        String username = scanner.nextLine().trim();
        if (isCancel(username)) return;

        System.out.print("Lecturer Password\t: ");
        String password = scanner.nextLine().trim();
        if (isCancel(password)) return;

        System.out.println("\n---------------------------------------------------");

        // Validation
        if (name.isEmpty() || workId.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("\nError: All fields are required!");
            pause(scanner);
            return;
        }

        if (isUsernameExists(username)) {
            System.out.println("\nError: Username already exists!");
            pause(scanner);
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(LECTURER_FILE, true))) {
            pw.println(name + "," + workId + "," + username + "," + password);
            System.out.println("Lecturer added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to Lecturers.csv");
            System.out.println("Message: " + e.getMessage());
        }

        pause(scanner);
    }

    // Edit Lecturer
    private static void editLecturer(Scanner scanner) {
        Main.clearScreen();
        System.out.println("=========================================================================================");
        System.out.println("                                EDIT LECTURER");
        System.out.println("=========================================================================================\n");
        System.out.println(">> Admin >> Manage Lecturers >> Edit Lecturer\n");

        String[] lines = new String[1000]; // simple storage
        int count = 0;

        // Read all lecturers
        try (BufferedReader br = new BufferedReader(new FileReader(LECTURER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (!line.isEmpty()) {
                    lines[count++] = line;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Lecturers.csv");
            pause(scanner);
            return;
        }

        if (count == 0) {
            System.out.println("No lecturer records found.");
            pause(scanner);
            return;
        }

        // Display lecturers table
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-20s %-15s%n",
                "No.", "Name", "Work ID", "Username", "Password");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int i = 0; i < count; i++) {
            String[] data = lines[i].split(",");
            System.out.printf("%-5d %-25s %-15s %-20s %-15s%n",
                    (i + 1),
                    data[0].trim(),
                    data[1].trim(),
                    data[2].trim(),
                    data[3].trim());
        }

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.print("Pick lecturer to edit (1-" + count + ") or type 'Exit': ");
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
            System.out.println("Invalid lecturer number.");
            pause(scanner);
            return;
        }

        int index = choice - 1;
        String[] lecturer = lines[index].split(",");

        String oldName = lecturer[0].trim();
        String oldWorkId = lecturer[1].trim();
        String oldUsername = lecturer[2].trim();
        String oldPassword = lecturer[3].trim();

        System.out.println("------------------------------------------------------------------------------------------\n");
        System.out.println("[EDITING LECTURER]\n");
        System.out.println("Press 'Enter' to keep existing value.");
        System.out.println("Type 'Exit' to cancel.\n");

        System.out.print("Edit Lecturer Name (" + oldName + ") : ");
        String name = scanner.nextLine().trim();
        if (isCancel(name)) return;
        if (name.isEmpty()) name = oldName;

        System.out.print("Edit Lecturer Work ID (" + oldWorkId + ") : ");
        String workId = scanner.nextLine().trim();
        if (isCancel(workId)) return;
        if (workId.isEmpty()) workId = oldWorkId;

        String username;
        while (true) {
            System.out.print("Edit Lecturer Username (" + oldUsername + ") : ");
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

        System.out.print("Edit Lecturer Password (" + oldPassword + ") : ");
        String password = scanner.nextLine().trim();
        if (isCancel(password)) return;
        if (password.isEmpty()) password = oldPassword;

        // Update record
        lines[index] = name + "," + workId + "," + username + "," + password;

        // Rewrite file
        try (PrintWriter pw = new PrintWriter(new FileWriter(LECTURER_FILE))) {
            for (int i = 0; i < count; i++) {
                pw.println(lines[i]);
            }
            System.out.println("\nLecturer record updated successfully!");
        } catch (IOException e) {
            System.out.println("Error saving Lecturers.csv");
        }

        pause(scanner);
    }

    // Delete Lecturer
    private static void deleteLecturer(Scanner scanner) {
        Main.clearScreen();
        System.out.println("=========================================================================================");
        System.out.println("                              DELETE LECTURER");
        System.out.println("=========================================================================================\n");
        System.out.println(">> Admin >> Manage Lecturers >> Delete Lecturer\n");

        String[] lines = new String[1000];
        int count = 0;

        // Read all lecturers
        try (BufferedReader br = new BufferedReader(new FileReader(LECTURER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (!line.isEmpty()) {
                    lines[count++] = line;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Lecturers.csv");
            pause(scanner);
            return;
        }

        if (count == 0) {
            System.out.println("No lecturer records found.");
            pause(scanner);
            return;
        }

        // Display table
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-20s %-15s%n",
                "No.", "Name", "Work ID", "Username", "Password");
        System.out.println("------------------------------------------------------------------------------------------");

        for (int i = 0; i < count; i++) {
            String[] data = lines[i].split(",");
            System.out.printf("%-5d %-25s %-15s %-20s %-15s%n",
                    (i + 1),
                    data[0].trim(),
                    data[1].trim(),
                    data[2].trim(),
                    data[3].trim());
        }

        System.out.println("\n------------------------------------------------------------------------------------------");
        System.out.print("Pick lecturer to be deleted (1-" + count + ") or type 'Exit': ");
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
            System.out.println("Invalid lecturer number.");
            pause(scanner);
            return;
        }

        int index = choice - 1;
        String[] lecturer = lines[index].split(",");

        String name = lecturer[0].trim();
        String workId = lecturer[1].trim();
        String username = lecturer[2].trim();
        String password = lecturer[3].trim();

        // Confirmation
        System.out.println("\nAre you sure you want to delete this lecturer? (Y/N)");
        System.out.print(">> ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("\nDelete operation cancelled.");
            pause(scanner);
            return;
        }

        // Rewrite CSV excluding deleted lecturer
        try (PrintWriter pw = new PrintWriter(new FileWriter(LECTURER_FILE))) {
            for (int i = 0; i < count; i++) {
                if (i != index) {
                    pw.println(lines[i]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error updating Lecturers.csv");
            pause(scanner);
            return;
        }

        // Show deleted info
        System.out.println("\nLecturer Deleted Successfully!");
        System.out.println("[INFORMATION OF DELETED LECTURER]");
        System.out.println("Name\t\t: " + name);
        System.out.println("Work ID\t\t: " + workId);
        System.out.println("Username\t: " + username);
        System.out.println("Password\t: " + password);

        pause(scanner);
    }

    // View All Lecturers
    private static void viewAllLecturers(Scanner scanner) {
        Main.clearScreen();
        System.out.println("=========================================================================================");
        System.out.println("                            VIEW ALL LECTURERS");
        System.out.println("=========================================================================================\n");
        System.out.println(">> Admin >> Manage Lecturers >> View All Lecturers\n");
        System.out.println("------------------------------------------------------------------------------------------");

        System.out.printf("%-5s %-25s %-15s %-20s %-15s%n",
                "No.", "Name", "Work ID", "Username", "Password");
        System.out.println("------------------------------------------------------------------------------------------");

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(LECTURER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 4) {
                    count++;
                    System.out.printf("%-5d %-25s %-15s %-20s %-15s%n",
                            count,
                            data[0].trim(), // Name
                            data[1].trim(), // Work ID
                            data[2].trim(), // Username
                            data[3].trim()  // Password
                    );
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Lecturers.csv");
            System.out.println("Message: " + e.getMessage());
        }

        if (count == 0) {
            System.out.println("No lecturer records found.");
        }

        System.out.println("\n------------------------------------------------------------------------------------------");
        System.out.println("Press anything to go back...");
        scanner.nextLine();
    }

    private static void pause(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // Username Check (same as student version - checks across all user files)
    private static boolean isUsernameExists(String username) {
        return checkFileForUsername("csv_database/Admin.csv", username) ||
               checkFileForUsername("csv_database/Lecturers.csv", username) ||
               checkFileForUsername("csv_database/Students.csv", username);
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
            System.out.println("Error reading file: " + filename);
        }
        return false;
    }

    private static boolean isCancel(String input) {
        if (input.equalsIgnoreCase("Exit")) {
            System.out.println("\nOperation cancelled. Returning to previous menu...");
            return true;
        }
        return false;
    }
}