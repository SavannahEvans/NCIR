package edu.msoe.ncir;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Remote.class}, version = 1, exportSchema = false)
public abstract class RemoteRoomDatabase extends RoomDatabase {

    public abstract RemoteDao remoteDao();

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

        private final RemoteDao myDao;

        PopulateDbAsync(RemoteRoomDatabase db) {
            myDao = db.remoteDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            myDao.deleteAll();
            return null;
        }
    }
}
