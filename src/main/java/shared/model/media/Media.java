package shared.model.media;

public class Media {
    private String name;
    private byte[] bytes;
    private MediaType mediaType;

    Media(){}
    public Media(String name, byte[] bytes, MediaType mediaType){
        this.name = name;
        this.bytes = bytes;
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
