import java.io.*;
import java.util.*;

public class ViewStudentInCourse {

    private static final String STUDENT_FILE = "csv_database/Students.csv";
    private static final String COURSE_MARKS_PATH = "csv_database/CourseMarks/";

    public static void view(String lecturerWorkId, Scanner scanner) {
        

        // 1. Get Assigned Course Code
        List<String> myCourses = ViewAssignedCourse.displayAndGetCourses(lecturerWorkId);

        if (myCourses.isEmpty()) return;
        
        System.out.println("0.     Back"); // Manual Back option
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

        // 2. Read course file to get Marks
        File courseFile = new File(COURSE_MARKS_PATH + selectedCourseCode + ".csv");
        Main.clearScreen();

        System.out.println("=================================================================================================\n");
        System.out.println("                              COURSE MARK & GRADE MANAGEMENT SYSTEM");
        System.out.println("\n=================================================================================================");
        System.out.println(">> Lecturer Menu >> View Student in a Course");
        System.out.println("-------------------------------------------------------------------------------------------------\n");
        
        if (!courseFile.exists()) {
            System.out.println("No students registered for this course yet (File not found).");
            return;
        }

        // Map to store: Key = Matric No, Value = Array [CW, Final]
        Map<String, double[]> studentMarksMap = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");
                
                // CourseMarks CSV Format: MatricNo, CW, Final
                if (data.length >= 1 && !data[0].trim().isEmpty()) {
                    String matric = data[0].trim().toUpperCase();
                    
                    double cw = 0.0;
                    double fe = 0.0;

                    // Get CW mark (index 1) if available
                    if (data.length > 1) {
                        try { cw = Double.parseDouble(data[1].trim()); } catch (NumberFormatException ignored) {}
                    }

                    // Get Final mark (index 2) if available
                    if (data.length > 2) {
                        try { fe = Double.parseDouble(data[2].trim()); } catch (NumberFormatException ignored) {}
                    }

                    studentMarksMap.put(matric, new double[]{cw, fe});
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading course file.");
            return;
        }

        if (studentMarksMap.isEmpty()) {
            System.out.println("No students found in this course file.");
            return;
        }
        
        // 3. Read Students.csv to match names and print Table
        System.out.println("List of Students in " + selectedCourseCode + ":");
        System.out.println("-------------------------------------------------------------------------------------------------");
        // Header Table
        System.out.printf("%-5s %-15s %-30s %-10s %-10s %-10s %-5s%n", 
                          "No.", "Matric No", "Student Name", "CW", "Final", "Total", "Grade");
        System.out.println("-------------------------------------------------------------------------------------------------");

        boolean foundAny = false;
        int count = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");

                // Students CSV Format: Name, MatricNo, ...
                if (data.length >= 2) {
                    String studentName = data[0].trim();
                    String studentMatric = data[1].trim().toUpperCase();

                    // Check if this student's matric exists in the marks map
                    if (studentMarksMap.containsKey(studentMatric)) {
                        double[] marks = studentMarksMap.get(studentMatric);
                        double cw = marks[0];
                        double fe = marks[1];
                        double total = cw + fe;
                        String grade = getGrade(total);

                        // Print Row
                        System.out.printf("%-5d %-15s %-30s %-10.1f %-10.1f %-10.1f %-5s%n", 
                                          count++, studentMatric, studentName, cw, fe, total, grade);
                        foundAny = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Students.csv");
            return;
        }

        if (!foundAny) {
            System.out.println("Matric numbers found in course file, but names not found in Students.csv database.");
        }
        
        System.out.println("-------------------------------------------------------------------------------------------------");
    }

    // Helper method to calculate Grade (UTM General Scheme)
    private static String getGrade(double totalMarks) {
        if (totalMarks >= 90) return "A+";
        if (totalMarks >= 80) return "A";
        if (totalMarks >= 75) return "A-";
        if (totalMarks >= 70) return "B+";
        if (totalMarks >= 65) return "B";
        if (totalMarks >= 60) return "B-";
        if (totalMarks >= 55) return "C+";
        if (totalMarks >= 50) return "C";
        if (totalMarks >= 45) return "C-";
        if (totalMarks >= 40) return "D+";
        if (totalMarks >= 35) return "D";
        if (totalMarks >= 30) return "D-";
        return "E";
    }
}