import java.io.*;
import java.util.*;

public class ViewGradesCGPA {

    private static final String COURSE_MARKS_FOLDER = "csv_database/CourseMarks/";
    private static final String STUDENT_FILE = "csv_database/Students.csv";
    private static final String COURSES_FILE = "csv_database/Courses.csv";


    public static void viewGradesAndCGPA(String matricNo, Scanner scanner) {

        matricNo = matricNo.trim().toUpperCase();

        Main.clearScreen();

        System.out.println("=========================================================================================\n");
        System.out.println("                          GRADES & CGPA RESULTS                                            ");
        System.out.println("\n=========================================================================================");
        System.out.println(">> Student Menu >> View Grades & CGPA");
        System.out.println("-----------------------------------------------------------------------------------------\n");

        String studentName = getStudentNameFromFile(matricNo);

        System.out.println("[STUDENT INFORMATION]");
        System.out.println("Student Name   : " + (studentName == null ? "Unknown" : studentName));
        System.out.println("Matric Number  : " + matricNo);
        System.out.println("-----------------------------------------------------------------------------------------\n");

        // Get registered courses from Students.csv
        List<String> registeredCourses = getRegisteredCoursesFromStudentsFile(matricNo);

        if (registeredCourses.isEmpty()) {
            System.out.println("You have not registered any courses yet.");
            
        }

        // Load credit hours from Courses.csv
        Map<String, Integer> creditHoursMap = loadCreditHoursFromCoursesFile();

        // Display grades and calculate CGPA
        displayGradesAndCalculateCGPA(matricNo, registeredCourses, creditHoursMap);
    }

    private static List<String> getRegisteredCoursesFromStudentsFile(String matricNo) {

        List<String> result = new ArrayList<>();

        File file = new File(STUDENT_FILE);
        if (!file.exists()) return result;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);

                String[] data = line.split(",", -1);

                if (data.length > 0 && data[0].trim().equalsIgnoreCase("Name")) continue;

                if (data.length < 2) continue;

                String m = data[1].trim().toUpperCase();
                if (!m.equals(matricNo)) continue;

                String courseData = (data.length >= 5) ? data[4].trim() : "";

                if (courseData.isEmpty() || courseData.equals("()")) return result;

                if (courseData.startsWith("(") && courseData.endsWith(")") && courseData.length() >= 2) {
                    courseData = courseData.substring(1, courseData.length() - 1).trim();
                }

                if (courseData.isEmpty()) return result;

                String[] courses = courseData.split("\\|");
                for (String c : courses) {
                    String code = c.trim().toUpperCase();
                    if (!code.isEmpty()) result.add(code);
                }

                return new ArrayList<>(new LinkedHashSet<>(result));
            }

        } catch (IOException e) {

        }


        return result;
    }


    private static Map<String, Integer> loadCreditHoursFromCoursesFile() {
        Map<String, Integer> creditMap = new HashMap<>();

        File file = new File(COURSES_FILE);
        if (!file.exists()) return creditMap;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);

                String[] data = line.split(",", -1);
                if (data.length < 3) continue;

                String courseCode = data[1].trim().toUpperCase();
                String creditString = data[2].trim();

                if (courseCode.isEmpty()) continue;

                try {
                    int creditHour = Integer.parseInt(creditString);
                    creditMap.put(courseCode, creditHour);
                } catch (NumberFormatException e) {
                    // ignore invalid credit hour
                }
            }

        } catch (IOException e) {
            
        }

        return creditMap;
    }

    private static void displayGradesAndCalculateCGPA(
            String matricNo,
            List<String> courses,
            Map<String, Integer> creditHoursMap
    ) {
        double totalQualityPoints = 0.0;
        int totalCreditHours = 0;

        System.out.println("Your Grades (Registered Courses Only):");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("%-10s %-18s %-18s %-10s %-8s %-12s%n",
                "Course", "Coursework Mark", "Final Exam Mark", "Total", "Grade", "Credit Hour");
        System.out.println("-----------------------------------------------------------------------------------------");

        boolean hasAnyMarks = false;

        for (String courseCode : courses) {

            File courseFile = new File(COURSE_MARKS_FOLDER + courseCode + ".csv");

            if (!courseFile.exists()) {
                System.out.printf("%-10s %-18s %-18s %-10s %-8s %-12s%n",
                        courseCode, "N/A", "N/A", "N/A", "N/A", "N/A");
                continue;
            }

            double[] marks = findStudentMarks(courseFile, matricNo);

            if (marks == null) {
                System.out.printf("%-10s %-18s %-18s %-10s %-8s %-12s%n",
                        courseCode, "N/A", "N/A", "N/A", "N/A", "N/A");
                continue;
            }

            hasAnyMarks = true;

            double courseworkMark = marks[0];
            double finalExamMark = marks[1];
            double total = courseworkMark + finalExamMark;

            String grade = getGrade(total);

            int creditHour = creditHoursMap.getOrDefault(courseCode, 3);

            double gradePoint = gradeToPoint(grade);
            double qualityPoints = gradePoint * creditHour;

            totalQualityPoints += qualityPoints;
            totalCreditHours += creditHour;

            System.out.printf("%-10s %-18.1f %-18.1f %-10.1f %-8s %-12d%n",
                    courseCode, courseworkMark, finalExamMark, total, grade, creditHour);
        }

        System.out.println("-----------------------------------------------------------------------------------------");

        if (!hasAnyMarks || totalCreditHours == 0) {
            System.out.println("\nYour CGPA cannot be calculated yet because no marks were found for your registered courses.");
            return;
        }

        double cgpa = totalQualityPoints / totalCreditHours;

        System.out.println("\nResult:");
        System.out.println("Total Credit Hours : " + totalCreditHours);
        System.out.printf("CGPA               : %.2f%n", cgpa);
    }

    //Read student marks
    private static double[] findStudentMarks(File courseFile, String matricNo) {

        try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);

                String[] data = line.split(",", -1);
                if (data.length < 1) continue;

                String m = data[0].trim().toUpperCase();

                if (m.equals(matricNo)) {
                    double coursework = 0.0;
                    double finalExam = 0.0;

                    if (data.length > 1) coursework = safeParseDouble(data[1]);
                    if (data.length > 2) finalExam = safeParseDouble(data[2]);

                    return new double[]{coursework, finalExam};
                }
            }

        } catch (IOException e) {

        }

        return null;
    }


    private static String getStudentNameFromFile(String matricNo) {

        File file = new File(STUDENT_FILE);
        if (!file.exists()) return null;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);

                String[] data = line.split(",", -1);
                if (data.length >= 2) {
                    String name = data[0].trim();
                    String m = data[1].trim().toUpperCase();

                    if (m.equals(matricNo)) return name;
                }
            }

        } catch (IOException e) {

        }

        return null;
    }

    // Grading

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

    private static double gradeToPoint(String grade) {
        switch (grade) {
            case "A+":
            case "A":  return 4.00;
            case "A-": return 3.67;
            case "B+": return 3.33;
            case "B":  return 3.00;
            case "B-": return 2.67;
            case "C+": return 2.33;
            case "C":  return 2.00;
            case "C-": return 1.67;
            case "D+": return 1.33;
            case "D":  return 1.00;
            case "D-": return 0.67;
            default:   return 0.00;
        }
    }

    private static double safeParseDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}