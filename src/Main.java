import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    // User data storage
    static String currentUserName = "";
    static String currentUserFullName = "";
    static String currentUserId = "";
    static String currentUserRole = "";

    // I think better store all the data inside main too, not only the data that we will use
    static ArrayList<Course> crsList = new ArrayList<>();
    static StudentList<Student> stdList = new ArrayList<>();
    static LecturerList<Lecturer> lectList = new ArrayList<>();
    
    public static void main(String[] args) {
        clearScreen();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        loadStudents();
        loadLecturers();
        loadCourses();
        
        while (running) {
            System.out.println("===================================================\n" +
                               "\t   SCSE1224: ADVANCED PROGRAMMING\n" +
                               "\t       --- Group Project ---\n\n" +
                               "    >> COURSE MARK & GRADE MANAGEMENT SYSTEM <<\n" +
                               "===================================================\n");
            
            System.out.println("[1] User Login");
            System.out.println("[2] Exit Program");
            System.out.println("\n---------------------------------------------------");
            System.out.print("Enter your choice (1-2): ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    clearScreen();
                    showLoginPage(scanner);
                    break;
                    
                case "2":
                    System.out.println("\nExiting program. Goodbye!\n");
                    running = false;
                    break;
                    
                default:
                    System.out.println("\nInvalid choice! Please enter 1 or 2.\n");
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    clearScreen();       
            }
        }
        
        scanner.close();
    }
    
    private static void showLoginPage(Scanner scanner) {
        System.out.println("===================================================");
        System.out.println("    >> COURSE MARK & GRADE MANAGEMENT SYSTEM <<\n");
        System.out.println("\t\t--- USER LOGIN ---");
        System.out.println("===================================================\n");
        
        System.out.print(">> Enter your username and password.\n\n");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User loggedInUser = login(username, password);

        if (loggedInUser == null) {
            System.out.println("\n----------------------------------------------------------------");
            System.out.println("Error: Invalid username or password. Please try again.");
            System.out.println("----------------------------------------------------------------");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            clearScreen();
        }
        else {
            if(loggedInUser instanceof Student) {
                Student s = (Student) loggedInUser;
                s.showMenu(scanner);
            }
            else if(loggedInUser instanceof Lecturer) {
                Lecturer l = (Lecturer) loggedInUser;
                l.showMenu(scanner);
            }
        }
        // Student Authentication
        //boolean isStudent = authenticateUser("csv_database/Students.csv", username, password, "student");
        
        /*if (isStudent) {
            System.out.println("\n----------------------------------------------------------------");
            System.out.println("Login successful! Welcome Student: " + currentUserFullName + "!");
            System.out.println("----------------------------------------------------------------");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
            clearScreen();
            Student student = new Student(currentUserFullName, currentUserId);
            student.showMenu(scanner);
        } else {
            // Lecturer Authentication
            boolean isLecturer = authenticateUser("csv_database/Lecturers.csv", username, password, "lecturer");
            
            if (isLecturer) {
                System.out.println("\n----------------------------------------------------------------");
                System.out.println("Login successful! Welcome Lecturer: " + currentUserFullName + "!");
                System.out.println("----------------------------------------------------------------");
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                clearScreen();
                Lecturer lecturer = new Lecturer(currentUserFullName, currentUserId);
                lecturer.showMenu(scanner);
            } else {
                // Admin Authentication
                boolean isAdmin = authenticateUser("csv_database/Admin.csv", username, password, "admin");
                
                if (isAdmin) {
                    System.out.println("\n----------------------------------------------------------------");
                    System.out.println("Login successful! Welcome Admin: " + currentUserFullName + "!");
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    clearScreen();
                    Admin admin = new Admin(currentUserFullName, currentUserId);
                    admin.showMenu(scanner);
                } else {
                    System.out.println("\n----------------------------------------------------------------");
                    System.out.println("Error: Invalid username or password. Please try again.");
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();
                    clearScreen();
                }
            }
        }
    }
    
    /*private static boolean authenticateUser(String filename, String username, String password, String role) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Handle UTF-8 BOM (Byte Order Mark) if present
                line = line.replace("\uFEFF", "").trim();
                
                // Check if line is not empty
                if (line.isEmpty()) continue;
                
                String[] values = line.split(",");
                if (values.length >= 4) {
                    String name = values[0].trim();
                    String id = values[1].trim();
                    String fileUsername = values[2].trim();
                    String filePassword = values[3].trim();
                    
                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        currentUserName = username;
                        currentUserFullName = name;
                        currentUserId = id;
                        currentUserRole = role;
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            System.out.println("Error message: " + e.getMessage());
        }
        return false;
    }*/
    
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // If clearing fails, just print multiple newlines
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public static ArrayList<String> readCSVFile(String filename){
        ArrayList<String> dataString = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("csv_database/"+filename+".csv"))){
            String line;
            while ((line = br.readLine()) != null){
                String cleanLine = line.replace("\uFEFF", "").trim();
                if (line.isEmpty()) continue;
                dataString.add(cleanLine);
            }
        }
        catch(IOException e) {
            System.out.println("Gagal membaca file: " + filename);
            System.out.println("Error message: " + e.getMessage());
        }
        return dataString;
    }

    public static void loadStudents() {
        ArrayList<String> data = readCSVFile("Students");
        for(int i = 0; i < data.size(); i++){
            String line = data.get(i);
            String[] values = line.split(",");

            if (values.length >= 4){
                stdList.add(new Student(values[0].trim(), values[1].trim(), values[2].trim(), values[3].trim()));
                System.out.println("Loaded " + stdList.size() + " students.");
            }
        }
    }

    public static User login(String username, String password){
        for(Student s : stdList) {
            if(s.getUsername().equals(username) && s.getPassword().equals(password)){
                return s;
            }
        }
        for(Lecturer l : lectList) {
            if(l.getUsername().equals(username) && l.getPasswords().equals(password)){
                return l;
            }
        }
        return null;
    }
}
