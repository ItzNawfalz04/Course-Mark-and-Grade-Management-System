package admin;

public class CourseAssg {
    private Course course;
    private String session;
    private int semester;

    CourseAssg(Course course, String session, int semester){
        this.course = course;
        this.session = session;
        this.semester = semester;
    }

    public Course getCourse(){
        return course;
    }

    public String toString(){
        //Example
        //DISCRETE STRUCTURE - SECI1013 - 3 Credits - Session 2025/2026 - Semester 1
        return course.toString() + " - Session " + session + " - Semester " + semester;
    }
}