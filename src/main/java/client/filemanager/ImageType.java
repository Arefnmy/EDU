package client.filemanager;

public enum ImageType {
    HOME("home.png"),
    REFRESH("update.jpg");

    private final String pathName;
    ImageType(String pathName){
        this.pathName = pathName;
    }

    public String getPathName(){
        return pathName;
    }
}
