package Toursim;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TourismModel.class}, version = 1)
public abstract class TourismDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "TourismApp";
    public abstract TourismDao tourismDao();
    public static volatile TourismDatabase INSTANCE = null;

    public static TourismDatabase getTourismInstance(Context context){
        if(INSTANCE == null){
            synchronized (TourismDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, TourismDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public static Callback callback = new Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    TourismDatabase database = INSTANCE;
                    TourismDao tourismDao = database.tourismDao();
                    tourismDao.deleteAllTourism();
                }
            });

        }
    };

}
