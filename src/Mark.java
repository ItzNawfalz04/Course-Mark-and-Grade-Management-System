public class Mark {
  int courseWork;
  int finalExam;

  Mark(int courseWork, int finalExam) {
    this.courseWork = courseWork;
    this.finalExam = finalExam;
  }

  public void setMark(int courseWork, int finalExam) {
    this.courseWork = courseWork;
    this.finalExam = finalExam;
  }

  public int totalMark() {
    return courseWork+finalExam;
  }

  public String grade() {
    String grde = "E";
    
    if(this.totalMark >= 90){grde = "A+"}
    else if(this.totalMark >= 80){grade = "A"}
    else if(this.totalMark >= 75){grade = "A-"}
    else if(this.totalMark >= 70){grade = "B+"}
    else if(this.totalMark >= 65){grade = "B"}
    else if(this.totalMark >= 60){grade = "B-"}
    else if(this.totalMark >= 55){grade = "C+"}
    else if(this.totalMark >= 50){grade = "C"}
    else if(this.totalMark >= 45){grade = "C-"}
    else if(this.totalMark >= 40){grade = "D+"}
    else if(this.totalMark >= 35){grade = "D"}
    else if(this.totalMark >= 30){grade = "D-"}

    return grde;
  }

  public double points() {
    double pnt = 0.00;

    if(this.totalMark >= 90){pnt = 4.00}
    else if(this.totalMark >= 80){pnt = 4.00}
    else if(this.totalMark >= 75){pnt = 3.67}
    else if(this.totalMark >= 70){pnt = 3.33}
    else if(this.totalMark >= 65){pnt = 3.00}
    else if(this.totalMark >= 60){pnt = 2.67}
    else if(this.totalMark >= 55){pnt = 2.33}
    else if(this.totalMark >= 50){pnt = 2.00}
    else if(this.totalMark >= 45){pnt = 1.67}
    else if(this.totalMark >= 40){pnt = 1.33}
    else if(this.totalMark >= 35){pnt = 1.00}
    else if(this.totalMark >= 30){pnt = 0.67}

    return pnt;
  }
}
