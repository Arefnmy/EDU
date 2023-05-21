package shared.model.media;

public enum MediaType {
    PDF(".pdf"),
    IMAGE(".png"),
    VOICE(".m4a"),
    VIDEO(".mp4"),
    TEXT(".txt");

    private final String name;
    MediaType(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
