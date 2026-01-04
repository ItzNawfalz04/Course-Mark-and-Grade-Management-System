import java.util.ArrayList;

public class Course {
    private String name;
    private String code;
    private int credits;
    private ArrayList<StudentReg> studRegList = new ArrayList<>();
    private ArrayList<LecturerAssg> lectAssgList = new ArrayList<>();
    
    //Course Constructor
    Course (String name, String code, int credits) {
        this.name = name;
        this.code = code;
        this.credits = credits;
    }

    //Method for get course code and return it
    public String getCode(){
        return code;
    }

    //Method for get course credits and return it
    public int getCredits(){
        return credits;
    }

    //Tostring menthod
    public String toString(){
        //Example
        //DISCRETE STRUCTURE - SECI1013 - 3 Credits
        return name+" - "+code+" - "+credits+" Credits";
    }

    public void assignLecturer(LecturerAssg lectAssg){
        lectAssgList.add(lectAssg);
    }

    public void registerStudent(StudentReg studReg){
        studRegList.add(studReg);
    }

    //For Lecturer to access Student in a course
    public ArrayList<Student> getStudentArray(){
        ArrayList<Student> stdList = new ArrayList<>();
        for(StudentReg x : studRegList){
            stdList.add(x.getStudent());
        }
        return stdList;
    }
}