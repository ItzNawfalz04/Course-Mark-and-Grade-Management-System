public class Course {
  String name;
  String code;
  int credits;
  ArrayList<LecturerAssg> lectAssgList = new ArrayList<>();
  ArrayList<StudentReg> studRegList = new ArrayList<>();

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

  /*public void insertlectAssgList() {
    try (BufferedReader br = new BufferedReader(new FileReader("csv_database/CoursesAssg.csv"))){
      String line;
      while ((line = br.readLine()) != null) {
        // Handle UTF-8 BOM (Byte Order Mark) if present
        line = line.replace("\uFEFF", "").trim();
                
        // Check if line is not empty
        if (line.isEmpty()) continue;
                
        String[] values = line.split(",");
        if (values.length >= 3) {
          crsList.add(new Course(values[0].trim(), values[1].trim(), Integer.parseInt(values[2].trim())));
        }
      }
    }
  }*/
}
