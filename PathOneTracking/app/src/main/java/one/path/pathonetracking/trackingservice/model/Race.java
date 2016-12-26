package one.path.pathonetracking.trackingservice.model;

public class Race {


    private int id;
    private String name;
    private String imageUrl;
    private byte[] image;

    public Race(){

    };

    public Race(int id, String name, String imageUrl, byte[] image){
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
