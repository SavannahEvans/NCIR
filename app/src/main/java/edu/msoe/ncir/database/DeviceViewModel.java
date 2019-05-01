package edu.msoe.ncir.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import edu.msoe.ncir.models.Device;

/**
 * Provides data to the UI from the repository
 */
public class DeviceViewModel extends AndroidViewModel {

    private DeviceRepository myRepository;

    private LiveData<List<Device>> myDevices;

    public DeviceViewModel(Application application) {
        super(application);
        myRepository = new DeviceRepository(application);
        myDevices = myRepository.getAll();
    }

    public LiveData<List<Device>> getAllDevices() {
        return myDevices;
    }

    public LiveData<Device> getDevice (int id) {
        return myRepository.getDevice(id);
    }

    public void insert(Device device) {
        myRepository.insert(device);
    }
}
