package edu.msoe.ncir.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import edu.msoe.ncir.models.Remote;

public class RemoteRepository {

    private RemoteDao myRemoteDao;
    private LiveData<List<Remote>> myRemotes;

    RemoteRepository(Application application) {
        RemoteRoomDatabase db = RemoteRoomDatabase.getDatabase(application);
        myRemoteDao = db.remoteDao();
        myRemotes = myRemoteDao.getAll();
    }

    LiveData<List<Remote>> getAll() {
        return myRemotes;
    }

    LiveData<List<Remote>> getAll(int deviceID) {
        return myRemoteDao.getAll(deviceID);
    }

    // Must call this on a non-UI thread or app will crash
    public void insert(Remote remote) {
        new insertAsyncTask(myRemoteDao).execute(remote);
    }

    private static class insertAsyncTask extends AsyncTask<Remote, Void, Void> {
        private RemoteDao myAsyncTaskDao;

        insertAsyncTask(RemoteDao dao) {
            myAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Remote... params) {
            Log.d("RemoteRepository", params[0].getName() + " added with device id " + params[0].getDeviceID());
            myAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
