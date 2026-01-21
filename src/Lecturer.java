import java.util.Scanner;

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
            System.out.println("===================================================\n" +
                               "                  LECTURER MENU\n" + 
                               "===================================================");
            System.out.println("\n[LECTURER INFORMATION]\n");
            System.out.println("Lecturer Name\t: " + name);
            System.out.println("Work ID\t\t: " + workId);
            System.out.println("User Type\t: Lecturer");
            System.out.println("\n---------------------------------------------------");
            System.out.println("1. View Assigned Courses");
            System.out.println("2. View Students in a Course");
            System.out.println("3. Update Student Marks");
            System.out.println("4. View Course Results Summary");
            System.out.println("5. Logout");
            System.out.println("---------------------------------------------------");
            System.out.print("Enter your choice (1-5): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    System.out.println("\n=== View Assigned Courses ===");
                    ViewAssignedCourse.display(workId);
                    System.out.println("Would display all courses assigned to this lecturer.");
                    break;
                case "2":
                    System.out.println("\n=== View Students in a Course ===");
                    ViewStudentInCourse.view(workId, scanner);
                    System.out.println("Would display all students registered for a selected course.");
                    break;
                case "3":
                    System.out.println("\n=== Update Student Marks ===");
                    UpdateMarks.updateStudentMark(workId, scanner);
                    break;
                case "4":
                    System.out.println("\n=== View Course Results Summary ===");
                    ViewCourseResultSummary.viewSummary(workId, scanner);
                    System.out.println("Would display summary statistics for a course.");
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
}