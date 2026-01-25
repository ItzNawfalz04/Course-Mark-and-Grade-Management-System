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

public class ManageCourses {

    private static final String COURSES_FILE = "csv_database/Courses.csv";
    private static final String COURSE_ASSG_FILE = "csv_database/CourseAssg.csv";
    private static final String LECTURERS_FILE = "csv_database/Lecturers.csv";

    public static void showMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            Main.clearScreen();
            System.out.println("=======================================================\n");
            System.out.println("         COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n=======================================================");
            System.out.println(">> Admin Menu >> Manage Courses");
            System.out.println("-------------------------------------------------------\n");
            System.out.println("[1] View All Courses");
            System.out.println("[2] View Lecturers for All Courses");
            System.out.println("[3] Assign Lecturer to Course");
            System.out.println("[4] Unassign Lecturer from Course");
            System.out.println("[5] Back to Admin Menu");
            System.out.println("\n-------------------------------------------------------");
            System.out.print("Enter your choice (1-5): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAllCourses(scanner);
                    break;
                case "2":
                    viewAssignedCourses(scanner);
                    break;
                case "3":
                    assignCourseLecturer(scanner);
                    break;
                case "4":
                    unassignCourseLecturer(scanner);
                    break;
                case "5":
                    running = false;
                    Main.clearScreen();
                    break;
                default:
                    System.out.println("\nInvalid choice! Please enter 1-5.");
                    ManageStudents.pause(scanner);
            }
        }
    }

    // View All Courses (only from Courses.csv)
    private static void viewAllCourses(Scanner scanner) {
        Main.clearScreen();
        System.out.println("===================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n===================================================================================");
        System.out.println(">> Admin Menu >> Manage Courses >> View All Courses");
        System.out.println("-----------------------------------------------------------------------------------\n");
        System.out.println("All available courses:");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("%-5s %-40s %-15s %-15s%n",
                "No.", "Course Name", "Course Code", "Credit Hour");
        System.out.println("-----------------------------------------------------------------------------------");

        List<String[]> coursesList = new ArrayList<>();
        int courseCount = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 3) {
                    courseCount++;
                    coursesList.add(data);
                    
                    System.out.printf("%-5d %-40s %-15s %-15s%n",
                            courseCount,
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Courses.csv");
            System.out.println("Message: " + e.getMessage());
        }

        if (courseCount == 0) {
            System.out.println("No course records found.");
        }
        
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Total Courses: " + courseCount);
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Press Enter to go back...");
        scanner.nextLine();
    }

    // View Assigned Courses (with lecturer information)
    private static void viewAssignedCourses(Scanner scanner) {
        Main.clearScreen();
        System.out.println("===================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n===================================================================================");
        System.out.println(">> Admin Menu >> Manage Courses >> View Lecturers for All Courses");
        System.out.println("-----------------------------------------------------------------------------------\n");
        
        // Load lecturer data
        Map<String, String> lecturerMap = loadLecturerMap(); // WorkID -> Lecturer Name
        
        // Group assignments by course code
        Map<String, List<String>> assignmentsMap = new HashMap<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    String courseCode = data[0].trim();
                    String workID = data[1].trim();
                    
                    if (!assignmentsMap.containsKey(courseCode)) {
                        assignmentsMap.put(courseCode, new ArrayList<>());
                    }
                    assignmentsMap.get(courseCode).add(workID);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CourseAssg.csv");
            System.out.println("Message: " + e.getMessage());
        }
    
        if (assignmentsMap.isEmpty()) {
            System.out.println("No courses have assigned lecturers yet.");
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("\nPress Enter to go back...");
            scanner.nextLine();
            return;
        }

        // Load course information
        Map<String, String[]> coursesMap = loadCoursesMap(); // Course Code -> [Course Name, Credit Hour]
        
        int courseCount = 0;
        int totalAssignments = 0;
        
        for (Map.Entry<String, List<String>> entry : assignmentsMap.entrySet()) {
            String courseCode = entry.getKey();
            List<String> assignedWorkIDs = entry.getValue();
            
            String courseName = "Not Found";
            String creditHour = "N/A";
            
            if (coursesMap.containsKey(courseCode)) {
                String[] courseInfo = coursesMap.get(courseCode);
                courseName = courseInfo[0];
                creditHour = courseInfo[1];
            }
            
            courseCount++;
            totalAssignments += assignedWorkIDs.size();
            // Display assigned courses    
            System.out.println("Course Name\t: " + courseName);
            System.out.println("Course Code\t: " + courseCode);
            System.out.println("Credit Hour\t: " + creditHour);
            System.out.println("-------------------------------------------------------");
            System.out.printf("%-5s %-20s %-10s%n", "No.", "Lecturer Name", "WorkID");
            System.out.println("-------------------------------------------------------");
            
            if (!assignedWorkIDs.isEmpty()) {
                int lecturerCount = 1;
                for (String workID : assignedWorkIDs) {
                    String lecturerName = lecturerMap.getOrDefault(workID, "Not Found");
                    System.out.printf("%-5d %-20s %-10s%n", 
                            lecturerCount++, 
                            lecturerName, 
                            workID);
                }
                System.out.println("-------------------------------------------------------");
            } else {
                System.out.println("No lecturer currently assigned.");
            }
            
            System.out.println();
        }
        
        System.out.println("[SUMMARY]");
        System.out.println("Total courses with lecturer\t: " + courseCount);
        System.out.println("Total lecturer assigned\t\t: " + totalAssignments);
        System.out.println("-------------------------------------------------------");
        System.out.println("Press Enter to go back...");
        scanner.nextLine();
    }
    
    // Assign Lecturer to Course
    private static void assignCourseLecturer(Scanner scanner) {
        Main.clearScreen();
        System.out.println("===================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n===================================================================================");
        System.out.println(">> Admin Menu >> Manage Courses >> View All Courses");
        System.out.println("-----------------------------------------------------------------------------------\n");
        System.out.println("All available courses:");
        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("%-5s %-40s %-15s %-15s%n",
                "No.", "Course Name", "Course Code", "Credit Hour");
        System.out.println("-----------------------------------------------------------------------------------");

        List<String[]> coursesList = new ArrayList<>();
        int courseCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 3) {
                    courseCount++;
                    coursesList.add(data);
                    
                    System.out.printf("%-5d %-40s %-15s %-15s%n",
                            courseCount,
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Courses.csv");
            ManageStudents.pause(scanner);
            return;
        }

        if (courseCount == 0) {
            System.out.println("No course records found.");
            ManageStudents.pause(scanner);
            return;
        }

        System.out.println("-------------------------------------------------------------------------------");
        System.out.print("Pick course to assign lecturer (1-" + courseCount + ") or type 'Exit': ");
        String courseInput = scanner.nextLine().trim();

        if (ManageStudents.isCancel(courseInput)) return;

        int courseChoice;
        try {
            courseChoice = Integer.parseInt(courseInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            ManageStudents.pause(scanner);
            return;
        }

        if (courseChoice < 1 || courseChoice > courseCount) {
            System.out.println("Invalid course number.");
            ManageStudents.pause(scanner);
            return;
        }

        // Get selected course details
        String[] selectedCourse = coursesList.get(courseChoice - 1);
        String selectedCourseName = selectedCourse[0].trim();
        String selectedCourseCode = selectedCourse[1].trim();
        String selectedCreditHour = selectedCourse[2].trim();

        // Step 2: Show course information
        System.out.println("\n\n[COURSE INFORMATION & ASSIGNED LECTURER]");
        System.out.println("Course Name   : " + selectedCourseName);
        System.out.println("Course Code   : " + selectedCourseCode);
        System.out.println("Credit Hour   : " + selectedCreditHour);

        // Check current assignments for this course
        List<String> currentLecturerWorkIDs = getCurrentLecturersForCourse(selectedCourseCode);
        
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-5s %-20s %-10s%n", 
        "No.", "Lecturer Name", "WorkID");
        System.out.println("-------------------------------------------------------");
        
        if (!currentLecturerWorkIDs.isEmpty()) {
            int currentCount = 1;
            for (String workID : currentLecturerWorkIDs) {
                String lecturerName = getLecturerName(workID);
                System.out.printf("%-5d %-20s %-10s%n", 
                        currentCount++, 
                        lecturerName, 
                        workID);
            }
            System.out.println("-------------------------------------------------------");
        } else {
            System.out.println("No lecturer currently assigned.");
        }

        // Step 3: Load all lecturers EXCEPT those already assigned to this course
        System.out.println("\n\n[ASSIGNING LECTURER]");
        System.out.println("Select a new lecturer to assign for this course:");
        System.out.println("(Note: Lecturers already assigned are not shown)");
        System.out.println("-------------------------------------------------------");
        System.out.printf("%-5s %-20s %-10s%n", "No.", "Lecturer Name", "WorkID");
        System.out.println("-------------------------------------------------------");

        List<String[]> availableLecturersList = new ArrayList<>();
        int availableLecturerCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(LECTURERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    String lecturerName = data[0].trim();
                    String workID = data[1].trim();
                    
                    // Check if this lecturer is already assigned to the course
                    if (!currentLecturerWorkIDs.contains(workID)) {
                        // Only add lecturers who are NOT already assigned
                        availableLecturerCount++;
                        availableLecturersList.add(data);
                        
                        System.out.printf("%-5d %-20s %-10s%n",
                                availableLecturerCount,
                                lecturerName,
                                workID);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Lecturers.csv");
            ManageStudents.pause(scanner);
            return;
        }

        if (availableLecturerCount == 0) {
            System.out.println("\nNo available lecturers to assign.");
            System.out.println("All lecturers are already assigned to this course.");
            ManageStudents.pause(scanner);
            return;
        }

        System.out.println("-------------------------------------------------------");
        System.out.print("Pick lecturer to be assigned (1-" + availableLecturerCount + ") or type 'Exit': ");
        String lecturerInput = scanner.nextLine().trim();

        if (ManageStudents.isCancel(lecturerInput)) return;

        int lecturerChoice;
        try {
            lecturerChoice = Integer.parseInt(lecturerInput);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            ManageStudents.pause(scanner);
            return;
        }

        if (lecturerChoice < 1 || lecturerChoice > availableLecturerCount) {
            System.out.println("Invalid lecturer number.");
            ManageStudents.pause(scanner);
            return;
        }

        // Get selected lecturer details
        String[] selectedLecturer = availableLecturersList.get(lecturerChoice - 1);
        String selectedLecturerName = selectedLecturer[0].trim();
        String selectedLecturerWorkID = selectedLecturer[1].trim();

        // Step 4: Add new assignment to CourseAssg.csv
        boolean success = addCourseAssignment(selectedCourseCode, selectedLecturerWorkID);
        
        if (success) {
            System.out.println("\n\n[ASSIGNMENT SUCCESSFUL]");
            System.out.println("Course Name\t: " + selectedCourseName);
            System.out.println("Course Code\t: " + selectedCourseCode);
            System.out.println("Assigned to\t: " + selectedLecturerName + " (WorkID: " + selectedLecturerWorkID + ")");
            System.out.println("-------------------------------------------------------");
        } else {
            System.out.println("\nError adding course assignment.");
        }

        ManageStudents.pause(scanner);
    }

    // Unassign Lecturer from Course
    private static void unassignCourseLecturer(Scanner scanner) {
        Main.clearScreen();
        System.out.println("============================================================================================\n");
        System.out.println("                       COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n============================================================================================");
        System.out.println(">> Admin Menu >> Manage Courses >> Unassign Lecturer from Course");
        System.out.println("--------------------------------------------------------------------------------------------\n");

        // Load all assignments with course and lecturer names
        List<String[]> assignmentsList = new ArrayList<>();
        Map<String, String[]> coursesMap = loadCoursesMap(); // Course Code -> [Course Name, Credit Hour]
        Map<String, String> lecturerMap = loadLecturerMap(); // WorkID -> Lecturer Name
        
        System.out.println("Current Course Assignments:");
        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.printf("%-5s %-40s %-15s %-10s %-30s%n",
                "No.", "Course Name", "Course Code", "WorkID", "Lecturer Name");
        System.out.println("--------------------------------------------------------------------------------------------");
        
        int assignmentCount = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    assignmentCount++;
                    assignmentsList.add(data);
                    
                    String courseCode = data[0].trim();
                    String workID = data[1].trim();
                    
                    // Get course name
                    String courseName = "Not Found";
                    if (coursesMap.containsKey(courseCode)) {
                        courseName = coursesMap.get(courseCode)[0];
                    }
                    
                    // Get lecturer name
                    String lecturerName = "Not Found";
                    if (lecturerMap.containsKey(workID)) {
                        lecturerName = lecturerMap.get(workID);
                    }
                    
                    System.out.printf("%-5d %-40s %-15s %-10s %-30s%n",
                            assignmentCount,
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

        if (assignmentCount == 0) {
            System.out.println("No course assignments found.");
            ManageStudents.pause(scanner);
            return;
        }

        System.out.println("--------------------------------------------------------------------------------------------");
        System.out.print("Pick assignment to unassign (1-" + assignmentCount + ") or type 'Exit': ");
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

        if (choice < 1 || choice > assignmentCount) {
            System.out.println("Invalid assignment number.");
            ManageStudents.pause(scanner);
            return;
        }

        // Get selected assignment details
        String[] selectedAssignment = assignmentsList.get(choice - 1);
        String courseCode = selectedAssignment[0].trim();
        String workID = selectedAssignment[1].trim();
        
        String courseName = "Not Found";
        if (coursesMap.containsKey(courseCode)) {
            courseName = coursesMap.get(courseCode)[0];
        }
        
        String lecturerName = "Not Found";
        if (lecturerMap.containsKey(workID)) {
            lecturerName = lecturerMap.get(workID);
        }

        // Confirmation
        System.out.println("\n\n[CONFIRMATION]");
        System.out.println("Are you sure you want to unassign this lecturer from the course?");
        System.out.println("Course Name\t: " + courseName);
        System.out.println("Course Code\t: " + courseCode);
        System.out.println("Lecturer\t: " + lecturerName + " (WorkID: " + workID + ")");
        System.out.print("\nConfirm unassignment? (Y/N): ");
        
        String confirm = scanner.nextLine().trim();
        
        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("\nUnassignment cancelled.");
            ManageStudents.pause(scanner);
            return;
        }

        // Remove the assignment
        boolean success = removeCourseAssignment(courseCode, workID);
        
        if (success) {
            System.out.println("\n\n[UNASSIGNMENT SUCCESSFUL]");
            System.out.println("Lecturer " + lecturerName + " has been unassigned from " + courseName + " (" + courseCode + ")");
            System.out.println("--------------------------------------------------------------------------------------------");
        } else {
            System.out.println("\nError removing course assignment.");
        }

        ManageStudents.pause(scanner);
    }

    // ============================================================
    // HELPER METHODS
    // ============================================================

    // Helper method to get current lecturers for a course (returns List of WorkIDs)
    private static List<String> getCurrentLecturersForCourse(String courseCode) {
        List<String> lecturerWorkIDs = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2 && data[0].trim().equals(courseCode)) {
                    lecturerWorkIDs.add(data[1].trim());
                }
            }
        } catch (IOException e) {
            // File might not exist or is empty
        }
        return lecturerWorkIDs;
    }

    // Helper method to get lecturer name from WorkID
    private static String getLecturerName(String workID) {
        if (workID == null || workID.isEmpty()) {
            return "Not Assigned";
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(LECTURERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2 && data[1].trim().equals(workID)) {
                    return data[0].trim();
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Lecturers.csv");
        }
        return "Unknown Lecturer";
    }

    // Helper method to add a new course assignment (doesn't replace, just adds)
    private static boolean addCourseAssignment(String courseCode, String lecturerWorkID) {
        // Append new assignment to the file
        try (PrintWriter pw = new PrintWriter(new FileWriter(COURSE_ASSG_FILE, true))) {
            pw.println(courseCode + "," + lecturerWorkID);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing to CourseAssg.csv");
            System.out.println("Message: " + e.getMessage());
            return false;
        }
    }

    // Helper method to remove a course assignment
    private static boolean removeCourseAssignment(String courseCode, String lecturerWorkID) {
        List<String[]> remainingAssignments = new ArrayList<>();
        boolean found = false;
        
        // Read all assignments
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;

                String[] data = line.split(",");
                if (data.length >= 2) {
                    String currentCourseCode = data[0].trim();
                    String currentWorkID = data[1].trim();
                    
                    // Skip the assignment to be removed
                    if (currentCourseCode.equals(courseCode) && currentWorkID.equals(lecturerWorkID)) {
                        found = true;
                        continue;
                    }
                    
                    remainingAssignments.add(data);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CourseAssg.csv");
            return false;
        }
        
        // Write back remaining assignments
        try (PrintWriter pw = new PrintWriter(new FileWriter(COURSE_ASSG_FILE))) {
            for (String[] assignment : remainingAssignments) {
                pw.println(assignment[0] + "," + assignment[1]);
            }
            return found; // Return true if the assignment was found and removed
        } catch (IOException e) {
            System.out.println("Error writing to CourseAssg.csv");
            System.out.println("Message: " + e.getMessage());
            return false;
        }
    }

    // Helper method to load courses into a map (key: course code, value: [course name, credit hour])
    public static Map<String, String[]> loadCoursesMap() {
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
}