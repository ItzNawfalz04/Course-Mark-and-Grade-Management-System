import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Student {
    private String name;
    private String matricNo;
    private ArrayList<CourseReg> crsReg = new ArrayList<>();
    
    public Student(String name, String matricNo) {
        this.name = name;
        this.matricNo = matricNo;
    }

    public String getmatricNo() {
        return matricNo;
    }
    
    public void showMenu(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("===================================================");
            System.out.println("                    STUDENTS MENU"); 
            System.out.println("===================================================\n");
            System.out.println("[STUDENT INFORMATION]\n");
            System.out.println("Student Name\t: " + name);
            System.out.println("Matric No.\t: " + matricNo);
            System.out.println("User Type\t: Student");
            System.out.println("\n---------------------------------------------------");
            System.out.println("1. Register Course");
            System.out.println("2. Drop Course");
            System.out.println("3. View Registered Courses");
            System.out.println("4. View Grades & CGPA");
            System.out.println("5. Logout");
            System.out.println("---------------------------------------------------");
            System.out.print("Enter your choice (1-5): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.println("\n=== Register Course ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would allow student to register for available courses.");
                    break;
                case "2":
                    System.out.println("\n=== Drop Course ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would allow student to drop currently registered courses.");
                    break;
                case "3":
                    System.out.println("\n=== View Registered Courses ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would display all courses the student is registered for.");
                    break;
                case "4":
                    System.out.println("\n=== View Grades & CGPA ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would display grades for all courses and calculate CGPA.");
                    break;
                case "5":
                    Main.clearScreen();
                    loggedIn = false;
                    break;
                default:
                    System.out.println("\nInvalid choice! Please enter a number from 1 to 5.");
            }
            
            if (!choice.equals("5")) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                Main.clearScreen();
            }
        }
    }
    
    public void loadCourseReg() {
        for(int i = 0; i<Main.crsList.size(); i++){
            try (BufferedReader br = new BufferedReader(new FileReader("csv_database/"+Main.crsList.get(i).getCode()+".csv"))){
                //Assign the course in index i from the courseList, for crsreg student later
                Course crs = Main.crsList.get(i);
                String line;
                while ((line = br.readLine()) != null) {
                    // Handle UTF-8 BOM (Byte Order Mark) if present
                    line = line.replace("\uFEFF", "").trim();
                
                    // Check if line is not empty
                    if (line.isEmpty()) continue;
                
                    String[] values = line.split(",");
                    if (values.length >= 3) {
                        //Find the equal matricNo for this student with the matricNo in the file
                        if (matricNo.equals(values[0].trim())){
                            //Add the course reg for this student with the course from [Course crs = Main.crsList.get(i)] adn the object mark from value 1 and 2 = coursework and finalexam
                            crsReg.add(new CourseReg(crs, "2025/2026", 1, new Mark(Integer.parseInt(values[1].trim()), Integer.parseInt(values[2].trim()))));
                        }
                        else {
                            System.out.println("Matric no not found in this course file" + crs.getCode());
                        }
                    }
                }
            }
            catch(IOException e) {
                System.out.println("Error reading file: ");
                System.out.println("Error message: " + e.getMessage());
            }
        }
    }
}
