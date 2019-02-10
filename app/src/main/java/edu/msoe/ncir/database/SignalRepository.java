package edu.msoe.ncir.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import edu.msoe.ncir.models.Signal;

public class SignalRepository {

    private SignalDao mySignalDao;
    private LiveData<List<Signal>> mySignals;

    SignalRepository(Application application) {
        RemoteRoomDatabase db = RemoteRoomDatabase.getDatabase(application);
        mySignalDao = db.signalDao();
        mySignals = mySignalDao.getAll();
    }

    LiveData<List<Signal>> getAll() {
        return mySignals;
    }

    LiveData<List<Signal>> getAll(int deviceID) {
        return mySignalDao.getAll(deviceID);
    }

    LiveData<Signal> get(int signalID) {
        return mySignalDao.get(signalID);
    }

    // Must call this on a non-UI thread or app will crash
    public void insert(Signal Signal) {
        new insertAsyncTask(mySignalDao).execute(Signal);
    }

    private static class insertAsyncTask extends AsyncTask<Signal, Void, Void> {
        private SignalDao myAsyncTaskDao;

        insertAsyncTask(SignalDao dao) {
            myAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Signal... params) {
            Log.d("SignalRepository", params[0].getName() + " added");
            myAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
