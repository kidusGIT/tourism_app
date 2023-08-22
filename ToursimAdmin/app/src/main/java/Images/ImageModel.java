package Images;

import com.google.gson.annotations.SerializedName;

public class ImageModel {
//     "img_url":"deleted img.jpg",
//     "ToursimId":1
    @SerializedName("img_url")
    private String imgUrl;

    @SerializedName("ToursimId")
    private String tourismId;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTourismId() {
        return tourismId;
    }

    public void setTourismId(String tourismId) {
        this.tourismId = tourismId;
    }
}
