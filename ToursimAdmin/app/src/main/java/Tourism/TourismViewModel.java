package Tourism;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TourismViewModel extends AndroidViewModel {
    private TourismRepository repository;
    public LiveData<List<TourismModel>> tourismList;

    public TourismViewModel(@NonNull Application application) {
        super(application);

        repository = new TourismRepository(application);
        tourismList = repository.getAllTourism();
    }

    public void insert(List<TourismModel> tourism){
        repository.insert(tourism);
    }

    public LiveData<List<TourismModel>> getAllTourism(){
        return tourismList;
    }
}
