package edu.msoe.ncir.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import edu.msoe.ncir.models.Device;
import edu.msoe.ncir.models.Remote;

@Database(entities = {Remote.class, Device.class}, version = 1, exportSchema = false)
public abstract class RemoteRoomDatabase extends RoomDatabase {

    public abstract RemoteDao remoteDao();
    public abstract DeviceDao deviceDao();

    private static volatile RemoteRoomDatabase INSTANCE;

    static RemoteRoomDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (RemoteRoomDatabase.class) {
                if(INSTANCE == null) {
                    // Create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RemoteRoomDatabase.class, "remote_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final RemoteDao myRemoteDao;
        private final DeviceDao myDeviceDao;

        PopulateDbAsync(RemoteRoomDatabase db) {
            myRemoteDao = db.remoteDao();
            myDeviceDao = db.deviceDao();
        }

        /**
         * Runs on application startup, usually used to reset data each restart
         * WARNING: If models change the data must be deleted and recreated :)
         */
        @Override
        protected Void doInBackground(final Void... params) {
            //myRemoteDao.deleteAll();
            //myDeviceDao.deleteAll();

            //Device d = new Device("TV Remote");
            //myDeviceDao.insert(d);

            return null;
        }
    }
}
