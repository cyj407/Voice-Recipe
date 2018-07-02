package fragmentPage;

public class CuisineItem {
    private String head;
    private String desc;
    private String imageUrl;

    public CuisineItem(String head, String desc, String imageUrl){
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public String getHead() {
        return head;
    }
    public String getDesc() {
        return desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
