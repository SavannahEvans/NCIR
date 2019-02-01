package edu.msoe.ncir.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import edu.msoe.ncir.models.Device;

public class DeviceRepository {

    private DeviceDao myDeviceDao;
    private LiveData<List<Device>> myDevices;

    DeviceRepository(Application application) {
        RemoteRoomDatabase db = RemoteRoomDatabase.getDatabase(application);
        myDeviceDao = db.deviceDao();
        myDevices = myDeviceDao.getAll();
    }

    LiveData<List<Device>> getAll() {
        return myDevices;
    }

    // Must call this on a non-UI thread or app will crash
    public void insert(Device Device) {
        new insertAsyncTask(myDeviceDao).execute(Device);
    }

    private static class insertAsyncTask extends AsyncTask<Device, Void, Void> {
        private DeviceDao myAsyncTaskDao;

        insertAsyncTask(DeviceDao dao) {
            myAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Device... params) {
            Log.d("DeviceRepository", params[0].getName() + " added");
            myAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
