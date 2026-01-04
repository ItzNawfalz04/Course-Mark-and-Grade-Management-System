import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Lecturer {
    private String name;
    private String workId;
    private ArrayList<CourseAssg> crsAssgList = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    
    public Lecturer(String name, String workId) {
        this.name = name;
        this.workId = workId;
    }
    
    public void showMenu(Scanner scanner) {
        boolean loggedIn = true;
        loadCourseAssignment();
        
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
                    viewAssignedCourse();
                    System.out.println("Would display all courses assigned to this lecturer.");
                    break;
                case "2":
                    System.out.println("\n=== View Students in a Course ===");
                    viewStudentinCourse(scanner);
                    System.out.println("Would display all students registered for a selected course.");
                    break;
                case "3":
                    System.out.println("\n=== Update Student Marks ===");
                    System.out.println("Functionality coming soon!");
                    System.out.println("Would allow lecturer to enter/update marks for students.");
                    break;
                case "4":
                    System.out.println("\n=== View Course Results Summary ===");
                    System.out.println("Functionality coming soon!");
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

    public void viewAssignedCourse(){
        System.out.println("Assigned Courses: ");
        for(int i = 0; i < crsAssgList.size(); i++){
            System.out.println((i+1)+". "+crsAssgList.get(i).toString());
        }
    }

    public void viewStudentinCourse(Scanner scanner){
        for(int i = 0; i < crsAssgList.size(); i++) {
            System.out.println((i + 1) +". "+ crsAssgList.get(i).toString());
        }
        System.out.print("Choose course");
        int choice = scanner.nextInt()-1;
        scanner.nextLine();
        String courseCode;
        if(choice >= 0 && choice <= crsAssgList.size()){
            courseCode = crsAssgList.get(choice).getCourse().getCode();
            ArrayList<Student> stdList = crsAssgList.get(choice).getCourse().getStudentArray();
            ArrayList<String> stdMatric = new ArrayList<>();
            System.out.println("Course :"+courseCode);
            System.out.println("Lecturer :"+name);
            System.out.println("\nName - MatricID");
            ArrayList<String> crsDataCSVLine = readCSVFile("csv_database/CourseMarks/"+courseCode+".csv");
            for(int j = 0; j < crsDataCSVLine.size(); j++){
                String crsData = crsDataCSVLine.get(j);
                String[] crsDataValues = crsData.split(",");
                if(crsDataValues.length >= 2){
                    stdMatric.add(crsDataValues[0].trim());
                }
            }
            ArrayList<String> stdCSVLine = readCSVFile("csv_database/Students.csv");
            for(int j = 0; j < stdCSVLine.size(); j++){
                String stdData = stdCSVLine.get(j);
                String[] crsValues = stdData.split(",");
                if(crsValues.length >= 3){
                    String stdMatricData = crsValues[1].trim();
                    String stdNameData = crsValues[0].trim();
                    for(int k = 0; k < stdMatric.size(); k++){
                        if(stdMatric.get(k).equals(stdMatricData)){
                            stdList.add(new Student(stdNameData, stdMatricData));
                        }
                    }
                }
            }
            for(int j = 0; j < stdList.size(); j++){
                System.out.println((j+1)+". " +stdList.get(j).getName()+" - "+stdList.get(j).getMatric());
            }
        }
    }

    public void loadCourseAssignment(){
        ArrayList<String> crsData = readCSVFile("csv_database/Courses.csv");
        ArrayList<Course> crsDataList = new ArrayList<>();
        for(int i = 0; i < crsData.size(); i++){
            String crsLine = crsData.get(i);
            String[] crsValues = crsLine.split(",");
            if(crsValues.length >= 3){
                String crsName = crsValues[0].trim();
                String crsCode = crsValues[1].trim();
                int crsCredits = Integer.parseInt(crsValues[2].trim());             
                crsDataList.add(new Course(crsName, crsCode, crsCredits));
            }
        }
        ArrayList<String> crsAssgData = readCSVFile("csv_database/CourseAssg.csv");
        for(int i = 0; i < crsAssgData.size(); i++){
            String crsAssgLine = crsAssgData.get(i);
            String[] crsAssgValues = crsAssgLine.split(",");

            if(crsAssgValues.length >= 2){
                String crsAssgCode = crsAssgValues[0];
                String crsAssgWorkId = crsAssgValues[1];
                String matchCrsCode;
                for(int j = 0; j < crsDataList.size(); j++){
                    matchCrsCode = crsDataList.get(j).getCode();
                    if(crsAssgCode.equals(matchCrsCode) && crsAssgWorkId.equals(workId)){
                        crsAssgList.add(new CourseAssg(crsDataList.get(j), "2025/2025", 1));
                    }
                } 
            }
        }   
    }

    public static ArrayList<String> readCSVFile(String filename){
        ArrayList<String> line = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String data;
            while ((data = br.readLine()) != null){
                // Handle UTF-8 BOM (Byte Order Mark) if present
                data = data.replace("\uFEFF", "").trim();
                line.add(data);
            }
        }
        catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            System.out.println("Error message: " + e.getMessage());
        }
        return line;
    }
}