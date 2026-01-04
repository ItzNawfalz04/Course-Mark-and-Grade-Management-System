public class LecturerAssg {
    Lecturer lecturer;
    String session;
    int semester;

    LecturerAssg(Lecturer lecturer, String session, int semester){
        this.lecturer = lecturer;
        this.session = session;
        this.semester = semester;
    }

    public String toString(){
        return lecturer.toString(); //Not fully completed cause its not in my job
    }
}
