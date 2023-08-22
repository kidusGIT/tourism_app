package Hotels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class HotelViewModel extends AndroidViewModel {
    private HotelRepository hotelRepository;
    public LiveData<List<HotelModel>> hotelList;
    private int id;

    public HotelViewModel(@NonNull Application application) {
        super(application);
        hotelRepository = new HotelRepository(application, id);
        hotelList = hotelRepository.getHotelList();
    }



    public void insert(List<HotelModel> hotels){
        hotelRepository.insert(hotels);
    }

    public LiveData<List<HotelModel>> getHotelList(){
        return hotelList;
    }
}
