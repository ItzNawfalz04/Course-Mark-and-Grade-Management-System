import java.io.*;
import java.util.*;

public class ViewCourseResultSummary {

    private static final String COURSE_MARKS_PATH = "csv_database/CourseMarks/";

    public static void viewSummary(String lecturerWorkId, Scanner scanner) {

        
        //Use class AssignedCourse to get List of the courses
        List<String> myCourses = ViewAssignedCourse.displayAndGetCourses(lecturerWorkId);

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
        
        // Processing the mark
        File courseFile = new File(COURSE_MARKS_PATH + selectedCourseCode + ".csv");
        Main.clearScreen();
        if (!courseFile.exists()) {
            System.out.println("No data found for " + selectedCourseCode);
            return;
        }

        // Initialization statistic variable
        double highest = 0.0;
        double lowest = 100.0; // max value
        double grandTotal = 0.0;
        int studentCount = 0;
        
        // To store topscorer mark
        List<String> topScorers = new ArrayList<>(); 

        try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
            System.out.println("==========================================================================\n");
            System.out.println("                  COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n==========================================================================");
            System.out.println(">> Lecturer Menu >> View Course Result Summary");
            System.out.println("--------------------------------------------------------------------------\n");
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");

                // Format: MatricNo, CW, FE
                if (data.length >= 3) {
                    try {
                        String matric = data[0].trim();
                        double cw = Double.parseDouble(data[1].trim());
                        double fe = Double.parseDouble(data[2].trim());
                        double totalScore = cw + fe;

                        // Update Logic
                        if (totalScore > highest) {
                            highest = totalScore;
                            topScorers.clear(); // Reset list to add new top scorer
                            topScorers.add(matric);
                        } else if (totalScore == highest) {
                            topScorers.add(matric); // Add if have same score with the new highest found
                        }

                        if (totalScore < lowest) {
                            lowest = totalScore;
                        }

                        grandTotal += totalScore;
                        studentCount++;

                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
            return;
        }

        // Print the result
        if (studentCount == 0) {
            System.out.println("\nNo student marks recorded yet.");
            return;
        }

        double average = grandTotal / studentCount;

        System.out.println(" STATISTICS FOR " + selectedCourseCode);
        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("%-25s : %d%n", "Total Students Graded", studentCount);
        System.out.printf("%-25s : %.2f%n", "Highest Score", highest);
        System.out.printf("%-25s : %.2f%n", "Lowest Score", lowest);
        System.out.printf("%-25s : %.2f%n", "Average Score", average);
        System.out.println("--------------------------------------------------------------------------");
        System.out.print("Top Scorer(s)             : ");
        System.out.println(String.join(", ", topScorers));
        System.out.println("--------------------------------------------------------------------------");
    }
}