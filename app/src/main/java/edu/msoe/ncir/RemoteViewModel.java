package edu.msoe.ncir;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

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

    LiveData<List<Remote>> getAllRemotes() {
        return myRemotes;
    }

    public void insert(Remote remote) {
        myRepository.insert(remote);
    }
}
