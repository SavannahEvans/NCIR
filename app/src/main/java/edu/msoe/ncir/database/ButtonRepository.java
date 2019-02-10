package edu.msoe.ncir.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import edu.msoe.ncir.models.Button;

public class ButtonRepository {


    private ButtonDao myButtonDao;
    private LiveData<List<Button>> myButtons;

    ButtonRepository(Application application) {
        RemoteRoomDatabase db = RemoteRoomDatabase.getDatabase(application);
        myButtonDao = db.buttonDao();
        myButtons = myButtonDao.getAll();
    }

    LiveData<List<Button>> getAll() {
        return myButtons;
    }

    LiveData<List<Button>> getAll(int remoteID) {
        return myButtonDao.getAll(remoteID);
    }

    // Must call this on a non-UI thread or app will crash
    public void insert(Button button) {
        new insertAsyncTask(myButtonDao).execute(button);
    }

    private static class insertAsyncTask extends AsyncTask<Button, Void, Void> {
        private ButtonDao myAsyncTaskDao;

        insertAsyncTask(ButtonDao dao) {
            myAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Button... params) {
            Log.d("ButtonRepository", params[0].getName() + " added");
            myAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
