package lecturer;
import java.util.Scanner;
import main.Main;

public class Lecturer {
    private String name;
    private String workId;
    
    public Lecturer(String name, String workId) {
        this.name = name;
        this.workId = workId;
    }
    
    public void showMenu(Scanner scanner) {
        boolean loggedIn = true;
        
        while (loggedIn) {
            System.out.println("====================================================\n");
            System.out.println("       COURSE MARK & GRADE MANAGEMENT SYSTEM");
            System.out.println("\n====================================================");
            System.out.println(">> Lecturer Menu");
            System.out.println("----------------------------------------------------\n");
            System.out.println("[LECTURER INFORMATION]");
            System.out.println("Lecturer Name\t: " + name);
            System.out.println("Work ID\t\t: " + workId);
            System.out.println("User Type\t: Lecturer");
            System.out.println("\n---------------------------------------------------\n");
            System.out.println("[1] View Assigned Courses");
            System.out.println("[2] View Students in a Course");
            System.out.println("[3] Update Student Marks");
            System.out.println("[4] View Course Results Summary");
            System.out.println("[5] Logout");
            System.out.println("\n---------------------------------------------------");
            System.out.print("Enter your choice (1-5): ");
            
            String choice = scanner.nextLine();
            Main.clearScreen();

            switch (choice) {
                case "1":
                    AssignedCourse.displayAndGetCourses(workId);
                    break;
                case "2":
                    StudentInCourse.view(workId, scanner);
                    break;
                case "3":
                    UpdateMarks.updateStudentMark(workId, scanner);
                    break;
                case "4":
                    CourseResultSummary.viewSummary(workId, scanner);
                    break;
                case "5":
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
}