package Hotels;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Hotel", indices = @Index(value = {"id"}, unique = true))
public class HotelModel {


    @PrimaryKey@NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @ColumnInfo(name = "desc")
    @SerializedName("desc")
    private String desc;

    @ColumnInfo(name = "img")
    @SerializedName("img")
    private String img;

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    private String latitude;

    @ColumnInfo(name = "longtude")
    @SerializedName("longtude")
    private String longtude;

    @ColumnInfo(name = "ToursimId")
    @SerializedName("ToursimId")
    private String ToursimId;

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

    public void setId(int id) {
        this.id = id;
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

    public String getToursimId() {
        return ToursimId;
    }

    public void setToursimId(String toursimId) {
        ToursimId = toursimId;
    }
}
