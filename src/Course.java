public class Course {
  String name;
  String code;
  int credits;

  Course(String name, String code, int credits){
    this.name = name;
    this.code = code;
    this.credits = credits;
  }

  public String getCode() {
    return code;
  }

  public int getCredits() {
    return credits;
  }
}
