package Hotels;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {HotelModel.class}, version = 1)
public abstract class HotelDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "HotelApp";
    public abstract HotelDao hotelDao();
    public static volatile HotelDatabase INSTANCE = null;

    public static HotelDatabase getHotelInstance(Context context){
        if(INSTANCE == null){
            synchronized (HotelDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, HotelDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }

}
