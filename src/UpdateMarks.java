import java.io.*;
import java.util.*;

public class UpdateMarks {

    private static final String CSV_PATH = "csv_database/CourseMarks/";

    public static void updateStudentMark(String lecturerWorkId, Scanner scanner) {
        
        System.out.println("\n\n--------------------------------------------------------------------------");
        System.out.println("\n                           UPDATE STUDENT MARKS                           ");
        
        // 1. Call ViewAssignedCourse
        List<String> myCourses = AssignedCourse.displayAndGetCourses(lecturerWorkId);

        if (myCourses.isEmpty()) return;

        // 2. Select Course
        System.out.print("\nEnter number to select course (or 0 to back): ");
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

        // 3. Open File: csv_database/CourseMarks/[Code].csv
        File courseFile = new File(CSV_PATH + selectedCourseCode + ".csv");
        
        if (!courseFile.exists()) {
            System.out.println("Data file not found at: " + courseFile.getPath());
            return;
        }

        System.out.print("Enter Student Matric No: ");
        String matricNo = scanner.nextLine().trim().toUpperCase();

        ArrayList<String> Newlines = new ArrayList<>();
        boolean studentFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                
                String[] data = line.split(",");
                
                if (data.length < 1) { Newlines.add(line); continue; }

                // Match the selected matric No
                if (data[0].trim().equalsIgnoreCase(matricNo)) {
                    studentFound = true;
                    
                    // Store the mark; if data length is insufficient, assume 0
                    String currentCW = (data.length > 1) ? data[1] : "0";
                    String currentFinal = (data.length > 2) ? data[2] : "0";

                    System.out.println("\nStudent Found (Matric No): " + data[0].trim());
                    System.out.println("Current Marks -> CW: " + currentCW + " | Final: " + currentFinal);
                    System.out.println("-----------------------------------");

                    // Get new mark
                    System.out.print("Enter New Course Work Mark (0-60): ");
                    String newCW = scanner.nextLine().trim();
                    
                    System.out.print("Enter New Final Exam Mark (0-40): ");
                    String newFinal = scanner.nextLine().trim();

                    // Store it in updated row
                    ArrayList<String> updatedRowData = new ArrayList<>(Arrays.asList(data));
                    
                    // Pad the row with "0" if CW or Final columns are missing
                    while (updatedRowData.size() <= 2) updatedRowData.add("0");

                    updatedRowData.set(1, newCW);    // Index 1 = CW
                    updatedRowData.set(2, newFinal); // Index 2 = FE

                    Newlines.add(String.join(",", updatedRowData));
                } else {
                    Newlines.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
            return;
        }

        if (!studentFound) {
            System.out.println("Student with Matric No " + matricNo + " not found!");
            return;
        }

        // Save File
        try (PrintWriter pw = new PrintWriter(new FileWriter(courseFile))) {
            for (String l : Newlines) pw.println(l);
            System.out.println("SUCCESS! Marks saved to " + courseFile.getName());
        } catch (IOException e) {
            System.out.println("Error writing file.");
        }
    }
}