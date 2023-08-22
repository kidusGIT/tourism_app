package hotels;

import com.google.gson.annotations.SerializedName;

public class HotelModel {
//    "name":"Test Three Hotel",
//    "desc":"Hotels is Test",
//    "ToursimId":3

//     "img": "",
//     "latitude": 0,
//     "longtude": 0,

    private String name;
    private String desc;

    @SerializedName("id")
    private int id;

    @SerializedName("img")
    private String img;

    private String latitude;
    private String longtude;


    @SerializedName("ToursimId")
    private String ToursimId;

    public void setId(int id) {
        this.id = id;
    }

    public String getToursimId() {
        return ToursimId;
    }

    public void setToursimId(String toursimId) {
        ToursimId = toursimId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtude() {
        return longtude;
    }

    public void setLongtude(String longtude) {
        this.longtude = longtude;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }


}
