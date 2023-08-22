package Hotels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HotelRepository {
    public HotelDao hotelDao;
    public LiveData<List<HotelModel>> hotelList;
    private HotelDatabase database;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private int id;

    public HotelRepository(Application application, int id) {
        this.id = id;
        database = HotelDatabase.getHotelInstance(application);
        hotelDao = database.hotelDao();
        hotelList = hotelDao.getHotels(id);
    }

    public void insert(List<HotelModel> hotel){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                hotelDao.insert(hotel);
            }
        });
    }

    public void delete(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                hotelDao.deleteAllTourism();
            }
        });
    }

    public LiveData<List<HotelModel>> getHotelList(){
        return hotelList;
    }
}
