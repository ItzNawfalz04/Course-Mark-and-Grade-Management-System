package lecturer;
import java.io.*;
import java.util.*;
import main.Main;

public class UpdateMarks {

    private static final String CSV_PATH = "csv_database/CourseMarks/";
    private static final String STUDENT_FILE = "csv_database/Students.csv"; 

    public static void updateStudentMark(String lecturerWorkId, Scanner scanner) {
        
        // 1. Retrieve list of courses assigned to the lecturer        
        List<String> myCourses = AssignedCourse.displayAndGetCourses(lecturerWorkId);

        if (myCourses.isEmpty()) return;

        // 2. Process course selection input
        System.out.print("\nEnter number to select course (or 0 to back): ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
            return;
        }

        if (choice == 0) return;
        if (choice < 1 || choice > myCourses.size()) {
            System.out.println("Invalid choice!");
            return;
        }
        
        String selectedCourseCode = myCourses.get(choice - 1);

        // 3. Verify if the course data file exists
        File courseFile = new File(CSV_PATH + selectedCourseCode + ".csv");
        
        if (!courseFile.exists()) {
            System.out.println("Data file not found at: " + courseFile.getPath());
            System.out.println("No students registered specifically for this course yet.");
            return;
        }
        
        // 4. Begin loop for updating marks
        while (true) {
            Main.clearScreen(); 
            System.out.println("=========================================================================================\n");
            System.out.println("                          COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n=========================================================================================");
            System.out.println(">> Lecturer Menu >> Update Student Marks");
            System.out.println("-----------------------------------------------------------------------------------------\n");

            System.out.println("Selected Course: " + selectedCourseCode);
            
            // Display the table in every iteration
            displayCurrentMarksTable(courseFile); 

            System.out.println("\n(Refer to the table above)");
            System.out.print("Enter Student Matric No to Update (or 0 to cancel): ");
            String matricNo = scanner.nextLine().trim().toUpperCase();

            // Exit condition
            if (matricNo.equals("0")) return;

            ArrayList<String> Newlines = new ArrayList<>();
            boolean studentFound = false;
            boolean validMapInput = true; 

            // 5. Read file line by line to process update
            try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("\uFEFF")) line = line.substring(1);
                    
                    String[] data = line.split(",");
                    if (data.length < 1) { 
                        Newlines.add(line); 
                        continue; 
                    }

                    // Check if Matric No matches
                    if (data[0].trim().equalsIgnoreCase(matricNo)) {
                        studentFound = true;
                        
                        String currentCW = (data.length > 1) ? data[1] : "0";
                        String currentFinal = (data.length > 2) ? data[2] : "0";

                        System.out.println("\n>>> UPDATING: " + data[0].trim());
                        System.out.println("Current Marks -> CW: " + currentCW + " | Final: " + currentFinal);
                        
                        // Request new marks input
                        System.out.print("Enter New Course Work Mark (0-70): ");
                        String newCW = scanner.nextLine().trim();
                        
                        System.out.print("Enter New Final Exam Mark (0-30): ");
                        String newFinal = scanner.nextLine().trim();

                        // Validate numerical input to prevent data corruption
                        if (!isNumeric(newCW) || !isNumeric(newFinal)) {
                            System.out.println("-----------------------------------------------------");
                            System.out.println(" ERROR: Input must be a valid number!"); 
                            System.out.println(" Update cancelled for this student.");
                            System.out.println("-----------------------------------------------------");
                            validMapInput = false; 
                            Newlines.add(line); // Keep original data
                        } 
                        else {
                            // Apply updates
                            ArrayList<String> updatedRowData = new ArrayList<>(Arrays.asList(data));
                            while (updatedRowData.size() <= 2) updatedRowData.add("0"); 

                            updatedRowData.set(1, newCW);    
                            updatedRowData.set(2, newFinal); 

                            Newlines.add(String.join(",", updatedRowData));
                        }
                    }
                    else {
                        // Keep other students' data unchanged
                        Newlines.add(line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file database.");
                return;
            }

            // 6. Handle Error Scenarios (Student Not Found or Invalid Input)
            if (!studentFound) {
                System.out.println("\n-----------------------------------------------------");
                System.out.println(" ERROR: Student '" + matricNo + "' NOT FOUND in this course.");
                System.out.println("-----------------------------------------------------");
                
                System.out.print("Press Enter to try again...");
                scanner.nextLine(); 
                
                continue; // Restart loop (Clears screen -> Shows table again)
            }

            if (!validMapInput) {
                System.out.print("Press Enter to try again...");
                scanner.nextLine();
                continue; // Restart loop (Clears screen -> Shows table again)
            }

            // 7. Save Changes (Only runs if everything is valid)
            try (PrintWriter pw = new PrintWriter(new FileWriter(courseFile))) {
                for (String l : Newlines) pw.println(l);
                
                System.out.println("\nSUCCESS! Marks updated for " + matricNo);
                break; // Exit loop and return to Lecturer Menu
                
            } catch (IOException e) {
                System.out.println("Error writing to file.");
                break;
            }
        } 
    }

    // ================= HELPER METHODS =================
    
    private static void displayCurrentMarksTable(File courseFile) {
        // Load course marks into memory map for lookup
        Map<String, double[]> studentMarksMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(courseFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");
                if (data.length >= 1 && !data[0].trim().isEmpty()) {
                    String m = data[0].trim().toUpperCase();
                    double cw = 0, fe = 0;
                    if (data.length > 1) try { cw = Double.parseDouble(data[1].trim()); } catch(Exception e){}
                    if (data.length > 2) try { fe = Double.parseDouble(data[2].trim()); } catch(Exception e){}
                    studentMarksMap.put(m, new double[]{cw, fe});
                }
            }
        } catch (IOException e) { return; }

        // Display Table Header
        System.out.println("Current Class List:");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("%-15s %-30s %-8s %-8s %-8s %-5s%n", "Matric No", "Name", "CW", "Final", "Total", "Grade");
        System.out.println("-----------------------------------------------------------------------------------------");

        // Read Student Names and Display Combined Data
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("\uFEFF")) line = line.substring(1);
                String[] data = line.split(",");
                if (data.length >= 2) {
                    String name = data[0].trim();
                    String matric = data[1].trim().toUpperCase();

                    if (studentMarksMap.containsKey(matric)) {
                        double[] marks = studentMarksMap.get(matric);
                        double total = marks[0] + marks[1];
                        String grade = getGrade(total);
                        System.out.printf("%-15s %-30s %-8.1f %-8.1f %-8.1f %-5s%n", 
                            matric, name, marks[0], marks[1], total, grade);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading student names.");
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }

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

    private static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}