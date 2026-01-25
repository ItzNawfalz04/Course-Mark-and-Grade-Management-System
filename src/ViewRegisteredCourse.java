import java.io.*;

public class ViewRegisteredCourse {

    private static final String STUDENT_FILE = "csv_database/Students.csv";

    public static void view(String matricNo) {

        matricNo = matricNo.trim().toUpperCase();
        boolean studentFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {

                // Remove BOM if any
                if (line.startsWith("\uFEFF")) line = line.substring(1);

                String[] data = line.split(",", -1);

                if (data.length > 0 && data[0].trim().equalsIgnoreCase("Name")) {
                    continue;
                }

                if (data.length < 2) continue;

                String csvMatric = data[1].trim().toUpperCase();

                if (csvMatric.equals(matricNo)) {
                    studentFound = true;

                    String courseData = (data.length >= 5) ? data[4].trim() : "";

                    Main.clearScreen();
                    System.out.println("\n\n==========================================================================");
                    System.out.println("\n                           REGISTERED COURSE                                  ");
                    System.out.println("\n==========================================================================");
                    System.out.println(">> Student Menu >> View Registered Courses");
                    System.out.println("---------------------------------------------------------------------------\n");

                    // If empty or () means no course
                    if (courseData.isEmpty() || courseData.equals("()")) {
                        System.out.println("You have not registered any courses yet.");
                        System.out.println("---------------------------------------------------------------------------");
                        return;
                    }

                    if (courseData.startsWith("(") && courseData.endsWith(")") && courseData.length() >= 2) {
                        courseData = courseData.substring(1, courseData.length() - 1).trim();
                    }

                    if (courseData.isEmpty()) {
                        System.out.println("You have not registered any courses yet.");
                        System.out.println("---------------------------------------------------------------------------");
                        return;
                    }

                    String[] courses = courseData.split("\\|");

                    int count = 0;
                    for (int i = 0; i < courses.length; i++) {
                        String c = courses[i].trim();
                        if (!c.isEmpty()) {
                            count++;
                            System.out.println(count + ". " + c);
                        }
                    }

                    if (count == 0) {
                        System.out.println("You have not registered any courses yet.");
                    }

                    System.out.println("---------------------------------------------------------------------------");
                    return;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading Students.csv");
            return;
        }

        if (!studentFound) {
            System.out.println("Student record not found.");
        }
    }
}