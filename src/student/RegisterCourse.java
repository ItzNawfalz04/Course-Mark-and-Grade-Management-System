package student;
import java.io.*;
import java.util.*;
import main.Main;

public class RegisterCourse {

    private static final String COURSES_FILE = "csv_database/Courses.csv";
    private static final String STUDENT_FILE = "csv_database/Students.csv";

    public static void register(String matricNo, Scanner scanner) {

        matricNo = matricNo.trim().toUpperCase();

        Map<String, String[]> availableCourses = new LinkedHashMap<>();

        //Read courses
        try (BufferedReader br = new BufferedReader(new FileReader(COURSES_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);

                String[] d = line.split(",");
                if (d.length >= 3) {
                    String courseCode = d[1].trim().toUpperCase();
                    String courseName = d[0].trim();
                    String creditHour = d[2].trim();

                    availableCourses.put(courseCode, new String[]{courseName, creditHour});
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Courses.csv");
            return;
        }

        Main.clearScreen();
        System.out.println("\n\n==========================================================================");
        System.out.println("\n                           REGISTER COURSE");
        System.out.println("\n==========================================================================");
        System.out.println(">> Student Menu >> Register Course");
            System.out.println("--------------------------------------------------------------------------\n");

        System.out.printf("\n%-45s %-12s %-6s%n", "Course Name", "Course Code", "Credit");
        System.out.println("--------------------------------------------------------------------------");

        for (var e : availableCourses.entrySet()) {
            System.out.printf("%-45s %-12s %-6s%n",
                    e.getValue()[0], e.getKey(), e.getValue()[1]);
        }

        System.out.println("--------------------------------------------------------------------------");

        int numSubjects;
        System.out.print("\nHow many subjects do you want to register? (or 0 to back): ");

        try {
            numSubjects = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
            return;
        }

        if (numSubjects == 0) return;
        if (numSubjects < 0) {
            System.out.println("Invalid number!");
            return;
        }

        System.out.println("\n");        

        List<String> selectedCourses = new ArrayList<>();

        for (int i = 1; i <= numSubjects; i++) {
            while (true) {
                System.out.print(i + ". Course Code: ");
                String code = scanner.nextLine().trim().toUpperCase();

                // Validate if the course code exists
                if (!availableCourses.containsKey(code)) {
                    System.out.println("Invalid course code! Please try again.");
                    continue;
                }

                // Prevent duplicate in same attempt
                if (selectedCourses.contains(code)) {
                    System.out.println("You already entered this course. Please enter a different course.");
                    continue;
                }

                selectedCourses.add(code);
                break;
            }
        }

        // Read Students.csv and update
        List<String> lines = new ArrayList<>();
        boolean studentFound = false;
        boolean updated = false;

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length > 0 && data[0].equalsIgnoreCase("Name")) {
                    lines.add(line);
                    continue;
                }

                if (data.length >= 2 && data[1].trim().equalsIgnoreCase(matricNo)) {
                    studentFound = true;

                    if (data.length < 5) {
                        data = Arrays.copyOf(data, 5);
                        data[4] = "()";
                    }

                    String raw = data[4].replace("(", "").replace(")", "").trim();
                    Set<String> courses = new LinkedHashSet<>();

                    if (!raw.isEmpty()) {
                        for (String c : raw.split("\\|")) {
                            String cc = c.trim().toUpperCase();
                            if (!cc.isEmpty()) courses.add(cc);
                        }
                    }

                    // Add new courses and skip duplicates that already exist
                    int addedCount = 0;
                    for (String code : selectedCourses) {
                        if (courses.contains(code)) {
                            continue;
                        }
                        courses.add(code);
                        addedCount++;
                    }

                    if (addedCount == 0) {
                        System.out.println("All selected courses are already registered. No changes made.");
                    } else {
                        data[4] = "(" + String.join("|", courses) + ")";
                        updated = true;
                    }
                }

                lines.add(String.join(",", data));
            }

        } catch (IOException e) {
            System.out.println("Error reading Students.csv");
            return;
        }

        if (!studentFound) {
            System.out.println("Student not found!");
            return;
        }

        if (!updated) {
            return;
        }

        //Write back to Students.csv
        System.out.println("\n------------------------------------------");
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (String l : lines) pw.println(l);
            System.out.println("Course registered successfully!");
        } catch (IOException e) {
            System.out.println("\nError writing Students.csv");
        }
    }
}