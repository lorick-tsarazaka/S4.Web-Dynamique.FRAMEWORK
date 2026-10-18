package mg.itu.framework.model;

public class MapFile {
    private String name;
    private String extension;
    private byte[] content;
    private String path;

    public MapFile(String name, String extension, byte[] content) {
        this.name = name;
        this.extension = extension;
        this.content = content;
    }

    public MapFile(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getExtension() {
        return extension;
    }

    public byte[] getContent() {
        return content;
    }

    public String getPath(){
        return path;
    }


}