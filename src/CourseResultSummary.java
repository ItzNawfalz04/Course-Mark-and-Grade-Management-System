import java.io.*;
import java.util.*;

public class CourseResultSummary {

    private static final String STUDENT_FILE = "csv_database/Students.csv";
    private static final String COURSE_MARKS_PATH = "csv_database/CourseMarks/";

    public static void viewSummary(String lecturerWorkId, Scanner scanner) {
        
        // Retrieve the list of courses
        List<String> myCourses = AssignedCourse.displayAndGetCourses(lecturerWorkId);

        if (myCourses.isEmpty()) return;

        System.out.print("\nEnter number to view summary (or 0 to back): ");
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
        
        // Processing the mark file
        File courseFile = new File(COURSE_MARKS_PATH + selectedCourseCode + ".csv");
        Main.clearScreen();
        
        if (!courseFile.exists()) {
            System.out.println("No data found for " + selectedCourseCode);
            return;
        }

        // STEP 1: Load all Student Names into a Map (Key: Matric, Value: Name)
        // This allows us to quickly look up names while reading the marks file.
        Map<String, String> studentNameMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String name = data[0].trim();
                    String matric = data[1].trim().toUpperCase(); // Normalize to uppercase
                    studentNameMap.put(matric, name);
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: Could not read Student Database to fetch names.");
        }

        // Initialize statistics variables
        double highest = 0.0;
        double lowest = 100.0; 
        double grandTotal = 0.0;
        int studentCount = 0;
        
        // List to store top scorer(s) matric numbers (and names if available)
        List<String> topScorers = new ArrayList<>(); 

        // STEP 2: Read Course Marks and Generate Table
        try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
            System.out.println("=================================================================================================\n");
            System.out.println("                               COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n=================================================================================================");
            System.out.println(">> Lecturer Menu >> View Course Result Summary");
            System.out.println("-------------------------------------------------------------------------------------------------");
            System.out.println(" Course Code : " + selectedCourseCode);
            System.out.println("-------------------------------------------------------------------------------------------------");
            
            // Student Table Header (Expanded to include Name)
            System.out.printf("%-5s %-15s %-30s %-10s %-10s %-10s %-5s%n", 
                    "No.", "Matric No", "Student Name", "CW", "FE", "Total", "Grade");
            System.out.println("-------------------------------------------------------------------------------------------------");

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");

                // CSV Format: MatricNo, CW, FE
                if (data.length >= 3) {
                    try {
                        String matric = data[0].trim().toUpperCase();
                        double cw = Double.parseDouble(data[1].trim());
                        double fe = Double.parseDouble(data[2].trim());
                        double totalScore = cw + fe;
                        
                        // Get Name from Map, default to "Unknown" if not found
                        String studentName = studentNameMap.getOrDefault(matric, "Unknown Student");

                        // Calculate Grade
                        String grade = calculateGrade(totalScore);

                        // Update Statistics Logic
                        if (totalScore > highest) {
                            highest = totalScore;
                            topScorers.clear(); 
                            topScorers.add(studentName + " (" + matric + ")");
                        } else if (totalScore == highest) {
                            topScorers.add(studentName + " (" + matric + ")");
                        }

                        if (totalScore < lowest) {
                            lowest = totalScore;
                        }

                        grandTotal += totalScore;
                        studentCount++;

                        // Display the student row immediately
                        System.out.printf("%-5d %-15s %-30s %-10.1f %-10.1f %-10.1f %-5s%n", 
                                studentCount, matric, studentName, cw, fe, totalScore, grade);

                    } catch (NumberFormatException e) {
                        continue; // Skip invalid lines
                    }
                }
            }
            System.out.println("-------------------------------------------------------------------------------------------------");

        } catch (IOException e) {
            System.out.println("Error reading file.");
            return;
        }

        // Display Statistics Summary below the table
        if (studentCount == 0) {
            System.out.println("\nNo student marks recorded yet.");
            return;
        }

        double average = grandTotal / studentCount;

        System.out.println("\n STATISTICS SUMMARY");
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.printf("%-25s : %d%n", "Total Students Graded", studentCount);
        System.out.printf("%-25s : %.2f%n", "Highest Score", highest);
        System.out.printf("%-25s : %.2f%n", "Lowest Score", lowest);
        System.out.printf("%-25s : %.2f%n", "Average Score", average);
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.print("Top Scorer(s)             : ");
        
        // Print top scorers (joined by comma if multiple)
        System.out.println(String.join(", ", topScorers));
        
        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.println("\nPress Enter to return...");
        scanner.nextLine();
    }

    // Helper method to calculate Grade (UTM Standard)
    private static String calculateGrade(double totalScore) {
        if (totalScore >= 90) return "A+";
        else if (totalScore >= 80) return "A";
        else if (totalScore >= 75) return "A-";
        else if (totalScore >= 70) return "B+";
        else if (totalScore >= 65) return "B";
        else if (totalScore >= 60) return "B-";
        else if (totalScore >= 55) return "C+";
        else if (totalScore >= 50) return "C";
        else if (totalScore >= 45) return "D+";
        else if (totalScore >= 40) return "D";
        else if (totalScore >= 30) return "E";
        else return "F";
    }
}