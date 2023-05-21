package shared.model;

public enum EducationalStatus {
    STUDYING,
    GRADUATING,
    DROPPING_OUT;

    @Override
    public String toString(){
        String status = super.toString().toLowerCase();
        status = (status.charAt(0) +"").toUpperCase() + status.substring(1);
        return status;
    }

    public static EducationalStatus getEnum(String value) {
        for(EducationalStatus v : values())
            if(v.toString().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }

    public static String[] getStatuses(){
        return new String[]{STUDYING.toString() , GRADUATING.toString() , DROPPING_OUT.toString()};
    }
}
