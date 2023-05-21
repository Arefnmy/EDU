package shared.model;

public enum Grade{
    BACHELOR,
    MASTER,
    DOCTORATE;


    @Override
    public String toString(){
        String grade = super.toString().toLowerCase();
        grade = (grade.charAt(0) +"").toUpperCase() + grade.substring(1);
        return grade;
    }

    public static Grade getEnum(String value) {
        for(Grade v : values())
            if(v.toString().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }

    public static String[] getGrades(){
        return new String[]{BACHELOR.toString() , MASTER.toString() , DOCTORATE.toString()};
    }
}
