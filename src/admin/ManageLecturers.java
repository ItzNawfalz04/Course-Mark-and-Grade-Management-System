package admin;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import main.Main;

public class ManageLecturers {

    private static final String LECTURER_FILE = "csv_database/Lecturers.csv";
    private static final String COURSE_ASSG_FILE = "csv_database/CourseAssg.csv";

    public static void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            Main.clearScreen();

            System.out.println("=======================================================\n");
            System.out.println("         COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n=======================================================");
            System.out.println(">> Admin Menu >> Manage Lecturers");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("[1] Add New Lecturer");
            System.out.println("[2] Edit Lecturer");
            System.out.println("[3] Delete Lecturer");
            System.out.println("[4] View All Lecturers");
            System.out.println("[5] View All Lecturers Assigned Courses");
            System.out.println("[6] Back to Admin Menu");
            System.out.println("\n-------------------------------------------------------");
            System.out.print("Enter your choice (1-6): ");

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
                    viewAllLecturersAssignedCourses(scanner);
                    break;
                case "6":
                    running = false;
                    Main.clearScreen();
                    break;

                default:
                    System.out.println("\nInvalid choice! Please enter 1–6.");
                    ManageStudents.pause(scanner);
            }
        }
    }

    // Add Lecturer
    private static void addNewLecturer(Scanner scanner) {
        Main.clearScreen();
        System.out.println("=======================================================\n");
        System.out.println("         COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n=======================================================");
        System.out.println(">> Admin Menu >> Manage Lecturers >> Add New Lecturer");
        System.out.println("-------------------------------------------------------\n");
        System.out.println("Type 'Exit' to cancel and return to previous menu.");
        System.out.println("\n---------------------------------------------------\n");

        System.out.print("Lecturer Name\t\t: ");
        String name = scanner.nextLine().trim();
        if (ManageStudents.isCancel(name)) return;

        System.out.print("Lecturer WorkID\t\t: ");
        String workId = scanner.nextLine().trim();
        if (ManageStudents.isCancel(workId)) return;

        System.out.print("Lecturer Username\t: ");
        String username = scanner.nextLine().trim();
        if (ManageStudents.isCancel(username)) return;

        System.out.print("Lecturer Password\t: ");
        String password = scanner.nextLine().trim();
        if (ManageStudents.isCancel(password)) return;

        System.out.println("\n---------------------------------------------------");

        // Validation
        if (name.isEmpty() || workId.isEmpty() || username.isEmpty() || password.isEmpty()) {
            System.out.println("\nError: All fields are required!");
            ManageStudents.pause(scanner);
            return;
        }

        if (ManageStudents.isUsernameExists(username)) {
            System.out.println("\nError: Username already exists!");
            ManageStudents.pause(scanner);
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(LECTURER_FILE, true))) {
            pw.println(name + "," + workId + "," + username + "," + password);
            System.out.println("Lecturer added successfully!");
        } catch (IOException e) {
            System.out.println("Error writing to Lecturers.csv");
            System.out.println("Message: " + e.getMessage());
        }

        ManageStudents.pause(scanner);
    }

    // Edit Lecturer
    private static void editLecturer(Scanner scanner) {
        Main.clearScreen();
        System.out.println("====================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n====================================================================================");
        System.out.println(">> Admin Menu >> Manage Lecturers >> Add New Lecturer");
        System.out.println("------------------------------------------------------------------------------------\n");

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
            ManageStudents.pause(scanner);
            return;
        }

        if (count == 0) {
            System.out.println("No lecturer records found.");
            ManageStudents.pause(scanner);
            return;
        }

        // Display lecturers table
        System.out.println("All available Lecturers:");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-15s %-15s%n",
                "No.", "Name", "Work ID", "Username", "Password");
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
        System.out.print("Pick lecturer to edit (1-" + count + ") or type 'Exit': ");
        String input = scanner.nextLine().trim();

        if (ManageStudents.isCancel(input)) return;

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            ManageStudents.pause(scanner);
            return;
        }

        if (choice < 1 || choice > count) {
            System.out.println("Invalid lecturer number.");
            ManageStudents.pause(scanner);
            return;
        }

        int index = choice - 1;
        String[] lecturer = lines[index].split(",");

        String oldName = lecturer[0].trim();
        String oldWorkId = lecturer[1].trim();
        String oldUsername = lecturer[2].trim();
        String oldPassword = lecturer[3].trim();

        System.out.println("\n[EDITING LECTURER]");
        System.out.println("Press 'Enter' to keep existing value or 'Exit' to cancel.");
        System.out.println("------------------------------------------------------------------------------------\n");
        System.out.print("Edit Lecturer Name (" + oldName + ") : ");
        String name = scanner.nextLine().trim();
        if (ManageStudents.isCancel(name)) return;
        if (name.isEmpty()) name = oldName;

        System.out.print("Edit Lecturer Work ID (" + oldWorkId + ") : ");
        String workId = scanner.nextLine().trim();
        if (ManageStudents.isCancel(workId)) return;
        if (workId.isEmpty()) workId = oldWorkId;

        String username;
        while (true) {
            System.out.print("Edit Lecturer Username (" + oldUsername + ") : ");
            username = scanner.nextLine().trim();
            if (ManageStudents.isCancel(username)) return;

            // Keep old username
            if (username.isEmpty()) {
                username = oldUsername;
                break;
            }

            // If changed, check uniqueness
            if (!username.equalsIgnoreCase(oldUsername) && ManageStudents.isUsernameExists(username)) {
                System.out.println("\nError: Username already exists! Please enter a different username.\n");
                continue; // stay here and re-enter
            }

            break; // valid username
        }

        System.out.print("Edit Lecturer Password (" + oldPassword + ") : ");
        String password = scanner.nextLine().trim();
        if (ManageStudents.isCancel(password)) return;
        if (password.isEmpty()) password = oldPassword;

        // Update record
        lines[index] = name + "," + workId + "," + username + "," + password;

        // Rewrite file
        try (PrintWriter pw = new PrintWriter(new FileWriter(LECTURER_FILE))) {
            for (int i = 0; i < count; i++) {
                pw.println(lines[i]);
            }
            System.out.println("\n-------------------------------------------------------------------------------------");
            System.out.println("Lecturer record updated successfully!");
        } catch (IOException e) {
            System.out.println("\n-------------------------------------------------------------------------------------");
            System.out.println("Error saving Lecturers.csv");
        }

        ManageStudents.pause(scanner);
    }

    // Delete Lecturer
    private static void deleteLecturer(Scanner scanner) {
        Main.clearScreen();

        System.out.println("====================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n====================================================================================");
        System.out.println(">> Admin Menu >> Manage Lecturers >> Delete Lecturer");
        System.out.println("------------------------------------------------------------------------------------\n");

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
            ManageStudents.pause(scanner);
            return;
        }

        if (count == 0) {
            System.out.println("No lecturer records found.");
            ManageStudents.pause(scanner);
            return;
        }

        // Display table
        System.out.println("All available Lecturers:");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-15s %-15s%n","No.", "Name", "Work ID", "Username", "Password");
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
        System.out.print("Pick lecturer to be deleted (1-" + count + ") or type 'Exit': ");
        String input = scanner.nextLine().trim();

        if (ManageStudents.isCancel(input)) return;

        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            ManageStudents.pause(scanner);
            return;
        }

        if (choice < 1 || choice > count) {
            System.out.println("Invalid lecturer number.");
            ManageStudents.pause(scanner);
            return;
        }

        int index = choice - 1;
        String[] lecturer = lines[index].split(",");

        String name = lecturer[0].trim();
        String workId = lecturer[1].trim();
        String username = lecturer[2].trim();
        String password = lecturer[3].trim();

        // Confirmation
        System.out.print("\nAre you sure you want to delete this lecturer? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("\nDelete operation cancelled.");
            ManageStudents.pause(scanner);
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
            ManageStudents.pause(scanner);
            return;
        }

        // Show deleted info
        System.out.println("\nLecturer Deleted Successfully!");
        System.out.println("[INFORMATION OF DELETED LECTURER]");
        System.out.println("Name\t\t: " + name);
        System.out.println("Work ID\t\t: " + workId);
        System.out.println("Username\t: " + username);
        System.out.println("Password\t: " + password);
        System.out.println("------------------------------------------------------------------------------------");
        ManageStudents.pause(scanner);
    }

    // View All Lecturers
    private static void viewAllLecturers(Scanner scanner) {
        Main.clearScreen();
        System.out.println("====================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n====================================================================================");
        System.out.println(">> Admin Menu >> Manage Lecturers >> View All Lecturers");
        System.out.println("------------------------------------------------------------------------------------\n");

        System.out.println("All available Lecturers:");
        System.out.println("------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s %-15s %-15s%n",
                "No.", "Name", "Work ID", "Username", "Password");
        System.out.println("------------------------------------------------------------------------------------");

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(LECTURER_FILE))) {
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
            System.out.println("Error reading Lecturers.csv");
            System.out.println("Message: " + e.getMessage());
        }

        if (count == 0) {
            System.out.println("No lecturer records found.");
        }

        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Press Enter to go back...");
        scanner.nextLine();
    }

    // View All Lecturers Assigned Courses
    private static void viewAllLecturersAssignedCourses(Scanner scanner) {
        Main.clearScreen();
        System.out.println("===================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n===================================================================================");
        System.out.println(">> Admin Menu >> Manage Lecturers >> View Lecturers Assigned Courses");
        System.out.println("-----------------------------------------------------------------------------------\n");

        // Load all lecturers
        Map<String, String> lecturerMap = new HashMap<>(); // WorkID -> Lecturer Name
        
        try (BufferedReader br = new BufferedReader(new FileReader(LECTURER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    String lecturerName = data[0].trim();
                    String workID = data[1].trim();
                    lecturerMap.put(workID, lecturerName);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Lecturers.csv");
            ManageStudents.pause(scanner);
            return;
        }

        if (lecturerMap.isEmpty()) {
            System.out.println("No lecturer records found.");
            ManageStudents.pause(scanner);
            return;
        }

        // Load all assignments grouped by lecturer WorkID
        Map<String, List<String>> assignmentsMap = new HashMap<>(); // WorkID -> List of Course Codes
        
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    String courseCode = data[0].trim();
                    String workID = data[1].trim();
                    
                    if (!assignmentsMap.containsKey(workID)) {
                        assignmentsMap.put(workID, new ArrayList<>());
                    }
                    assignmentsMap.get(workID).add(courseCode);
                }
            }
        } catch (IOException e) {
            // File might not exist yet
        }

        // Load course information for mapping
        Map<String, String[]> coursesMap = ManageCourses.loadCoursesMap();

        // Display all lecturers with their assigned courses
        int lecturerCount = 0;

        for (Map.Entry<String, String> lecturerEntry : lecturerMap.entrySet()) {
            String workID = lecturerEntry.getKey();
            String lecturerName = lecturerEntry.getValue();
            List<String> assignedCourseCodes = assignmentsMap.getOrDefault(workID, new ArrayList<>());

            lecturerCount++;
            
            // Display lecturer information
            System.out.println("Lecturer Name\t: " + lecturerName);
            System.out.println("Lecturer WorkID\t: " + workID);
            System.out.println("-----------------------------------------------------------------------------------");
            
            if (assignedCourseCodes.isEmpty()) {
                System.out.println("No courses assigned to this lecturer.");
            } else {
                System.out.printf("%-5s %-40s %-15s %-15s%n", 
                        "No.", "Course Name", "Course Code", "Credit Hour");
                System.out.println("-----------------------------------------------------------------------------------");
                
                int courseCount = 1;
                for (String courseCode : assignedCourseCodes) {
                    if (coursesMap.containsKey(courseCode)) {
                        String[] courseInfo = coursesMap.get(courseCode);
                        String courseName = courseInfo[0];
                        String creditHour = courseInfo[1];
                        
                        System.out.printf("%-5d %-40s %-15s %-15s%n",
                                courseCount++,
                                courseName,
                                courseCode,
                                creditHour);
                    } else {
                        // Course code found in assignments but not in Courses.csv
                        System.out.printf("%-5d %-40s %-15s %-15s%n",
                                courseCount++,
                                "Course Not Found in Database",
                                courseCode,
                                "N/A");
                    }
                }
            }
            
            System.out.println("-----------------------------------------------------------------------------------\n");
            
            // Add a separator between lecturers, but not after the last one
            if (lecturerCount < lecturerMap.size()) {
                System.out.println();
            }
        }
        
        System.out.println("Press Enter to go back...");
        scanner.nextLine();
    }
}