package admin;
import student.Student;

public class StudentReg {
    private Student student;
    private String session;
    private int semester;

    StudentReg(Student student, String session, int semester){
        this.student = student;
        this.session = session;
        this.semester = semester;
    }

    public Student getStudent(){
        return student;
    }
    
    public String toString(){
        return student.toString()+" - "+session+" - "+semester;
    }
}
