import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Admin_ManageCourses {

    private static final String COURSES_FILE = "csv_database/Courses.csv";
    private static final String COURSE_ASSG_FILE = "csv_database/CourseAssg.csv";
    private static final String LECTURERS_FILE = "csv_database/Lecturers.csv";

    public static void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            Main.clearScreen();
            System.out.println("===================================================");
            System.out.println("                 MANAGE COURSES"); 
            System.out.println("===================================================\n");
            System.out.println(">> Admin Menu >> Manage Courses\n");
            System.out.println("---------------------------------------------------");
            System.out.println("[1] View All Courses");
            System.out.println("[2] View Assign Course");
            // [3] Assign Courses Lecturer
            System.out.println("[3] Back to Admin Menu");
            System.out.println("---------------------------------------------------");
            System.out.print("Enter your choice (1-3): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAllCourses(scanner);
                    break;
                case "2":
                    viewAssignCourses(scanner);
                    break;
                case "3":
                    running = false;
                    Main.clearScreen();
                    break;
                default:
                    System.out.println("\nInvalid choice! Please enter 1-3.");
                    pause(scanner);
            }
        }
    }

    // View All Courses from Courses.csv
    private static void viewAllCourses(Scanner scanner) {
        Main.clearScreen();
        System.out.println("===================================================================================");
        System.out.println("                          VIEW ALL COURSES");
        System.out.println("===================================================================================\n");
        System.out.println(">> Admin Menu >> Manage Courses >> View All Courses\n");
        System.out.println("-----------------------------------------------------------------------------------");

        // Print table header
        System.out.printf("%-5s %-40s %-15s %-15s%n",
                "No.", "Course Name", "Course Code", "Credit Hour");
        System.out.println("-----------------------------------------------------------------------------------");

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 3) {
                    count++;
                    String courseName = data[0].trim();
                    String courseCode = data[1].trim();
                    String creditHour = data[2].trim();
                    
                    System.out.printf("%-5d %-40s %-15s %-15s%n",
                            count,
                            courseName,
                            courseCode,
                            creditHour);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Courses.csv");
            System.out.println("Message: " + e.getMessage());
        }

        if (count == 0) {
            System.out.println("No course records found.");
        }

        System.out.println("\n-----------------------------------------------------------------------------------");
        System.out.println("Press anything to go back...");
        scanner.nextLine();
    }

    // View Assign Courses - Shows CourseAssg.csv with additional info from Courses.csv and Lecturers.csv
    private static void viewAssignCourses(Scanner scanner) {
        Main.clearScreen();
        System.out.println("===========================================================================================");
        System.out.println("                         VIEW ASSIGN COURSES");
        System.out.println("===============================================================================\n");
        System.out.println(">> Admin Menu >> Manage Courses >> View Assign Courses\n");
        System.out.println("-------------------------------------------------------------------------------------------");

        // Load course data from Courses.csv
        Map<String, String[]> coursesMap = loadCoursesMap();
        
        // Load lecturer data from Lecturers.csv
        Map<String, String> lecturerMap = loadLecturerMap();

        // Print table header
        System.out.printf("%-5s %-40s %-15s %-10s %-15s%n",
                "No.", "Course Name", "Course Code", "WorkID", "Lecturer Name");
        System.out.println("-------------------------------------------------------------------------------------------");

        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    count++;
                    String courseCode = data[0].trim();
                    String workID = data[1].trim();
                    
                    // Get course name from coursesMap
                    String courseName = "Not Found";
                    if (coursesMap.containsKey(courseCode)) {
                        courseName = coursesMap.get(courseCode)[0]; // Course name is at index 0
                    }
                    
                    // Get lecturer name from lecturerMap
                    String lecturerName = "Not Found";
                    if (lecturerMap.containsKey(workID)) {
                        lecturerName = lecturerMap.get(workID);
                    }
                    
                    System.out.printf("%-5d %-40s %-15s %-10s %-15s%n",
                            count,
                            courseName,
                            courseCode,
                            workID,
                            lecturerName);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CourseAssg.csv");
            System.out.println("Message: " + e.getMessage());
        }

        if (count == 0) {
            System.out.println("No course assignment records found.");
        }

        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println("Press anything to go back...");
        scanner.nextLine();
    }

    // Helper method to load courses into a map (key: course code, value: [course name, credit hour])
    private static Map<String, String[]> loadCoursesMap() {
        Map<String, String[]> coursesMap = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 3) {
                    String courseName = data[0].trim();
                    String courseCode = data[1].trim();
                    String creditHour = data[2].trim();
                    
                    // Store as array: [course name, credit hour]
                    coursesMap.put(courseCode, new String[]{courseName, creditHour});
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Courses.csv for mapping");
        }
        
        return coursesMap;
    }

    // Helper method to load lecturers into a map (key: work ID, value: lecturer name)
    private static Map<String, String> loadLecturerMap() {
        Map<String, String> lecturerMap = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(LECTURERS_FILE))) {
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
            System.out.println("Error reading Lecturers.csv for mapping");
        }
        
        return lecturerMap;
    }

    private static void pause(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}