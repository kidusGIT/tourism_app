package Toursim;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TourismDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TourismModel> tourism);

    @Query("SELECT DISTINCT * FROM Tourism ORDER BY id DESC")
    LiveData<List<TourismModel>> getTourism();

    @Query("DELETE FROM Tourism")
    void deleteAllTourism();

}
