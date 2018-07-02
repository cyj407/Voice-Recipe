package fragmentPage;

public class CuisineItem {
    private String head;
    private String desc;
    private String imageUrl;
    private String recipeUrl;

    public CuisineItem(String head, String desc, String imageUrl, String recipeUrl){
        this.head = head;
        this.desc = desc;
        this.imageUrl = imageUrl;
        this.recipeUrl = recipeUrl;
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

    public String getRecipeUrl() {
        return recipeUrl;
    }
}
