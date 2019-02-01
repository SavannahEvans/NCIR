package edu.msoe.ncir.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import edu.msoe.ncir.models.Remote;

/**
 * Provides data to the UI from the repository
 */
public class RemoteViewModel extends AndroidViewModel {

    private RemoteRepository myRepository;

    private LiveData<List<Remote>> myRemotes;

    public RemoteViewModel(Application application) {
        super(application);
        myRepository = new RemoteRepository(application);
        myRemotes = myRepository.getAll();
    }

    public LiveData<List<Remote>> getAllRemotes() {
        return myRemotes;
    }

    public LiveData<List<Remote>> getRemotes(int deviceID) {
        return myRepository.getAll(deviceID);
    }

    public void insert(Remote remote) {
        myRepository.insert(remote);
    }
}
