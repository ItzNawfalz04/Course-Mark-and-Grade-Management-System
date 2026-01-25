package student;
import java.io.*;
import java.util.*;
import main.Main;

public class DropCourse {

    private static final String STUDENT_FILE = "csv_database/Students.csv";

    public static void drop(String matricNo, Scanner scanner) {

        matricNo = matricNo.trim().toUpperCase();

        List<String[]> students = new ArrayList<>();
        boolean studentFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                students.add(line.split(",", -1)); // keep empty columns too
            }
        } catch (IOException e) {
            System.out.println("Error reading Students.csv");
            return;
        }

        for (int r = 0; r < students.size(); r++) {

            String[] row = students.get(r);

            if (row.length < 2) continue;

            // Match student by matric
            if (row[1].trim().equalsIgnoreCase(matricNo)) {
                studentFound = true;

                // Ensure registered course column exists
                if (row.length < 5) {
                    row = Arrays.copyOf(row, 5);
                    row[4] = "()";
                    students.set(r, row);
                }

                String coursesData = row[4].trim();

                Main.clearScreen();
                System.out.println("\n\n==========================================================================");
                System.out.println("\n                             DROP COURSE");
                System.out.println("\n==========================================================================");
                System.out.println(">> Student Menu >> Drop Course");
                System.out.println("--------------------------------------------------------------------------\n");

                if (coursesData.equals("()") || coursesData.isEmpty()) {
                    System.out.println("You have no registered courses.");
                    return;
                }

                coursesData = coursesData.substring(1, coursesData.length() - 1).trim();
                List<String> courses = new ArrayList<>();

                if (!coursesData.isEmpty()) {
                    for (String c : coursesData.split("\\|")) {
                        String cc = c.trim().toUpperCase();
                        if (!cc.isEmpty()) courses.add(cc);
                    }
                }

                System.out.println("Registered Courses:");
                System.out.println("--------------------------------------------------------------------------");
                for (int i = 0; i < courses.size(); i++) {
                    System.out.println((i + 1) + ". " + courses.get(i));
                }
                System.out.println("--------------------------------------------------------------------------");

                System.out.print("Enter course code to drop (or 0 to back): ");
                String dropCode = scanner.nextLine().trim().toUpperCase();

                if (dropCode.equals("0")) return;

                // Validate course exists
                if (!courses.contains(dropCode)) {
                    System.out.println("Course not found in your registration!");
                    return;
                }

                // Remove course
                courses.remove(dropCode);

                // Update back to CSV format
                if (courses.isEmpty()) {
                    row[4] = "()";
                } else {
                    row[4] = "(" + String.join("|", courses) + ")";
                }

                // Save updated row back into list
                students.set(r, row);

                System.out.println("\n------------------------------------------");
                System.out.println("Course " + dropCode + " dropped successfully!");
                break;
            }
        }

        if (!studentFound) {
            System.out.println("Student not found!");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (String[] row : students) {
                pw.println(String.join(",", row));
            }
        } catch (IOException e) {
            System.out.println("Error saving Students.csv");
        }
    }
}