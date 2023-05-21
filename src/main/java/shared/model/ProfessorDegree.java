package shared.model;

public enum ProfessorDegree {
    ASSISTANT_PROFESSOR,
    ASSOCIATE_PROFESSOR,
    FULL_PROFESSOR;

    @Override
    public String toString(){
        String degree = super.toString().toLowerCase();
        int index = degree.indexOf("professor");
        degree = degree.substring(0 , index-1) + " " + degree.substring(index);
        degree = (degree.charAt(0) +"").toUpperCase() + degree.substring(1);
        return degree;
    }
    public static ProfessorDegree getEnum(String value) {
        for(ProfessorDegree v : values())
            if(v.toString().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }

    public static String[] getDegrees(){
        return new String[]{ASSOCIATE_PROFESSOR.toString() , ASSISTANT_PROFESSOR.toString() , FULL_PROFESSOR.toString()};
    }
}
