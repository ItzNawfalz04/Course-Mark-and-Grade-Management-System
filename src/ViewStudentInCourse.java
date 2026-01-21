import java.io.*;
import java.util.*;

public class ViewStudentInCourse {

    private static final String STUDENT_FILE = "csv_database/Students.csv";
    private static final String COURSE_MARKS_PATH = "csv_database/CourseMarks/";

    public static void view(String lecturerWorkId, Scanner scanner) {
        
        System.out.println("\n\n--------------------------------------------------------------------------");
        System.out.println("\n                        VIEW STUDENTS IN COURSE                           ");
        
        // Get Assigned Course Code
        List<String> myCourses = AssignedCourse.displayAndGetCourses(lecturerWorkId);

        if (myCourses.isEmpty()) return;

        System.out.print("\nEnter number to view students (or 0 to back): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input!");
            return;
        }

        if (choice == 0) return;
        if (choice < 1 || choice > myCourses.size()) {
            System.out.println("Invalid choice!");
            return;
        }
        String selectedCourseCode = myCourses.get(choice - 1);
        System.out.println("\nSelected Course: " + selectedCourseCode);

        // Read course file
        File courseFile = new File(COURSE_MARKS_PATH + selectedCourseCode + ".csv");
        Main.clearScreen();
        if (!courseFile.exists()) {
            System.out.println("No students registered for this course yet (File not found).");
            return;
        }

        // To get corresponding name from matricno faster
        Set<String> registeredMatrics = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");
                
                // To check whether data[0] contain any matric or not
                if (data.length >= 1 && !data[0].trim().isEmpty()) {
                    registeredMatrics.add(data[0].trim().toUpperCase());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading course file.");
            return;
        }

        if (registeredMatrics.isEmpty()) {
            System.out.println("No students found in this course file.");
            return;
        }

        // 3. Read Students.csv to match retrieve student names
        // --------------------------------------------------------
        System.out.println("\nList of Students in " + selectedCourseCode + ":");
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-5s %-15s %-30s%n", "No.", "Matric No", "Student Name");
        System.out.println("--------------------------------------------------------------------------");

        boolean foundAny = false;
        int count = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");

                
                if (data.length >= 2) {
                    String studentName = data[0].trim();
                    String studentMatric = data[1].trim().toUpperCase();

                    // Check if this student's matric exists in the course list
                    if (registeredMatrics.contains(studentMatric)) {
                        System.out.printf("%-5d %-15s %-30s%n", count++, studentMatric, studentName);
                        foundAny = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Students.csv");
            return;
        }

        if (!foundAny) {
            System.out.println("Matric numbers found in course file, but not found in Students.csv database.");
        }
        
        System.out.println("--------------------------------------------------------------------------");
    }
}