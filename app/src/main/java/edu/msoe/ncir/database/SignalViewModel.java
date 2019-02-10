package edu.msoe.ncir.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import edu.msoe.ncir.models.Signal;

/**
 * Provides data to the UI from the repository
 */
public class SignalViewModel extends AndroidViewModel {

    private SignalRepository myRepository;

    private LiveData<List<Signal>> mySignals;

    public SignalViewModel(Application application) {
        super(application);
        myRepository = new SignalRepository(application);
        mySignals = myRepository.getAll();
    }

    public LiveData<List<Signal>> getAllSignals() {
        return mySignals;
    }

    public LiveData<List<Signal>> getSignals(int deviceID) {
        return myRepository.getAll(deviceID);
    }

    public LiveData<Signal> getSignal(int signalID) {
        return myRepository.get(signalID);
    }

    public void insert(Signal signal) {
        myRepository.insert(signal);
    }
}
