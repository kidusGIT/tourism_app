package Hotels;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import Toursim.TourismModel;

@Dao
public interface HotelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HotelModel> toursim);

    @Query("SELECT DISTINCT * FROM Hotel WHERE ToursimId = :id ORDER BY id DESC")
    LiveData<List<HotelModel>> getHotels(int id);

    @Query("DELETE FROM Hotel")
    void deleteAllTourism();
}
