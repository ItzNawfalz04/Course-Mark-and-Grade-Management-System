package lecturer;
import java.io.*;
import java.util.*;

public class AssignedCourse {

    private static final String COURSES_FILE = "csv_database/Courses.csv";
    private static final String COURSE_ASSG_FILE = "csv_database/CourseAssg.csv";

    public static List<String> displayAndGetCourses(String lecturerWorkId) {
        
        List<String> foundCourses = new ArrayList<>(); // List to store course codes

        System.out.println("==========================================================================\n");
        System.out.println("                  COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n==========================================================================");
        System.out.println(">> Lecturer Menu >> View Assigned Course");
        System.out.println("--------------------------------------------------------------------------\n");

        // 1. PREPARE COURSE DATA
        Map<String, String[]> courseDetails = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");
                if (data.length >= 3) {
                    courseDetails.put(data[1].trim().toUpperCase(), new String[]{data[0].trim(), data[2].trim()});
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Courses.csv");
            return foundCourses;
        }

        // 2. READ COURSE ASSIGNMENTS & DISPLAY
        System.out.printf("%-5s %-15s %-40s %-10s%n", "No.", "Code", "Course Name", "Credit");
        System.out.println("--------------------------------------------------------------------------");

        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSG_FILE))) {
            String line;
            int count = 1;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String assignedCode = data[0].trim().toUpperCase();
                    String assignedWorkId = data[1].trim();

                    if (assignedWorkId.equalsIgnoreCase(lecturerWorkId)) {
                        String[] details = courseDetails.get(assignedCode);
                        String name = (details != null) ? details[0] : "Unknown Course";
                        String credit = (details != null) ? details[1] : "-";

                        System.out.printf("%-5d %-15s %-40s %-10s%n", count++, assignedCode, name, credit);
                        
                        // Add code to the list
                        foundCourses.add(assignedCode);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CourseAssg.csv");
        }

        if (foundCourses.isEmpty()) {
            System.out.println("No courses assigned to you yet.");
        }

        System.out.println("--------------------------------------------------------------------------");
        return foundCourses; // RETURN LIST TO CALLER
    }
}