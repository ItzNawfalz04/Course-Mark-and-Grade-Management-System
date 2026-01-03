public CourseReg {
  Course course;
  String session;
  int semester;
  Mark mark;

  CourseReg(Course course, String session, int semester, Mark mark) {
    this.course=course;
    this.semester=semester;
    this.mark=mark;
  }
}
