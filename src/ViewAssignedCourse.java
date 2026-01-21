import java.io.*;
import java.util.*;

public class ViewAssignedCourse {

    private static final String COURSES_FILE = "csv_database/Courses.csv";
    private static final String COURSE_ASSG_FILE = "csv_database/CourseAssg.csv";

    public static void display(String lecturerWorkId) {

        System.out.println("\n\n--------------------------------------------------------------------------");
        System.out.println("\n                        LIST OF ASSIGNED COURSES                          ");
        System.out.println("\n--------------------------------------------------------------------------");

        // 1. PREPARE COURSE DATA (NAME & CREDIT) FROM COURSES.CSV
        // Map for easy lookup: Key = CourseCode, Value = Array [Name, Credit]
        Map<String, String[]> courseDetails = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1); // Remove BOM
                
                String[] data = line.split(",");
                // Format: CourseName, CourseCode, Credit
                if (data.length >= 3) {
                    String name = data[0].trim();
                    String code = data[1].trim().toUpperCase();
                    String credit = data[2].trim();

                    courseDetails.put(code, new String[]{name, credit});
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Courses.csv");
            return;
        }

        // 2. READ COURSE ASSG AND MATCH WORK ID
        boolean found = false;
        
        // Table Header
        System.out.printf("%-5s %-15s %-40s %-10s%n", "No.", "Code", "Course Name", "Credit");
        System.out.println("--------------------------------------------------------------------------");

        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_ASSG_FILE))) {
            String line;
            int count = 1;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                
                String[] data = line.split(",");
                // Format: CourseCode, WorkID, ...
                if (data.length >= 2) {
                    String assignedCode = data[0].trim().toUpperCase();
                    String assignedWorkId = data[1].trim();

                    // MAIN LOGIC: Match Work ID
                    if (assignedWorkId.equalsIgnoreCase(lecturerWorkId)) {
                        found = true;
                        // Get name & credit details from the Map created in step 1
                        String[] details = courseDetails.get(assignedCode);
                        String name = (details != null) ? details[0] : "Unknown Course";
                        String credit = (details != null) ? details[1] : "-";

                       // Print Row
                        System.out.printf("%-5d %-15s %-40s %-10s%n", count++, assignedCode, name, credit);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CourseAssg.csv");
            return;
        }

        if (!found) {
            System.out.println("\nNo courses assigned to you yet.");
        }
        
        System.out.println("--------------------------------------------------------------------------");
    }
}