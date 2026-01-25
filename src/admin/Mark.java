package admin;

public class Mark {
    private int courseWork;
    private int finalExam;

    //Method for changing mark
    public void setMark (int courseWork, int finalExam) {
        this.courseWork = courseWork;
        this.finalExam = finalExam;
    }

    //Mark Constructor
    Mark(int courseWork, int finalExam){
        setMark(courseWork, finalExam);
    }

    //Method for get total mark and return it
    public int getTotalMark(){
        return courseWork + finalExam;
    }

    //Method for get grade and return it
    public String getGrade() {
        String grade = "E";
    
        if(getTotalMark() >= 90){grade = "A+";}
            else if(getTotalMark() >= 80){grade = "A";}
            else if(getTotalMark() >= 75){grade = "A-";}
            else if(getTotalMark() >= 70){grade = "B+";}
            else if(getTotalMark() >= 65){grade = "B";}
            else if(getTotalMark() >= 60){grade = "B-";}
            else if(getTotalMark() >= 55){grade = "C+";}
            else if(getTotalMark() >= 50){grade = "C";}
            else if(getTotalMark() >= 45){grade = "C-";}
            else if(getTotalMark() >= 40){grade = "D+";}
            else if(getTotalMark() >= 35){grade = "D";}
            else if(getTotalMark() >= 30){grade = "D-";}

        return grade;
  }

  //Method for get points and return it
  public double getPoints() {
    double points = 0.00;

    if(getTotalMark() >= 90){points = 4.00;}
        else if(getTotalMark() >= 80){points = 4.00;}
        else if(getTotalMark() >= 75){points = 3.67;}
        else if(getTotalMark() >= 70){points = 3.33;}
        else if(getTotalMark() >= 65){points = 3.00;}
        else if(getTotalMark() >= 60){points = 2.67;}
        else if(getTotalMark() >= 55){points = 2.33;}
        else if(getTotalMark() >= 50){points = 2.00;}
        else if(getTotalMark() >= 45){points = 1.67;}
        else if(getTotalMark() >= 40){points = 1.33;}
        else if(getTotalMark() >= 35){points = 1.00;}
        else if(getTotalMark() >= 30){points = 0.67;}

    return points;
  }
}
