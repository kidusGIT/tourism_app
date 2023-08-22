package Toursim;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TourismRepository {
    public TourismDao tourismDao;
    public LiveData<List<TourismModel>> tourismList;
    private TourismDatabase database;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public TourismRepository(Application application) {
        database = TourismDatabase.getTourismInstance(application);
        tourismDao = database.tourismDao();
        tourismList = tourismDao.getTourism();
    }

    public void insert(List<TourismModel> tourism){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                tourismDao.insert(tourism);
            }
        });
    }

    public void delete(){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                tourismDao.deleteAllTourism();
            }
        });
    }

    public LiveData<List<TourismModel>> getAllTourism(){
        return tourismList;
    }
}
