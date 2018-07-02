package cuisineList;

public class CuisineItem {
    private String head;
    private String imageUrl;
    private String recipeUrl;
    private String ratingStar;

    public CuisineItem(String head, String ratingStar, String imageUrl, String recipeUrl){
        this.head = head;
        this.imageUrl = imageUrl;
        this.recipeUrl = recipeUrl;
        this.ratingStar = ratingStar;
    }

    public String getHead() {
        return head;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getRecipeUrl() {
        return recipeUrl;
    }

    public String getRatingStar() {
        return ratingStar;
    }
}
