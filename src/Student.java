import java.util.Scanner;

public class Student {
    private String name;
    private String matricNo;
    
    public Student(String name, String matricNo) {
        this.name = name;
        this.matricNo = matricNo;
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
}