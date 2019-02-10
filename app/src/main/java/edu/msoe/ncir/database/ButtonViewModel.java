package edu.msoe.ncir.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import edu.msoe.ncir.models.Button;

/**
 * Provides data to the UI from the repository
 */
public class ButtonViewModel extends AndroidViewModel {

    private ButtonRepository myRepository;

    private LiveData<List<Button>> myButtons;

    public ButtonViewModel(Application application) {
        super(application);
        myRepository = new ButtonRepository(application);
        myButtons = myRepository.getAll();
    }

    public LiveData<List<Button>> getAllRemotes() {
        return myButtons;
    }

    public LiveData<List<Button>> getButtons(int remoteID) {
        return myRepository.getAll(remoteID);
    }

    public void insert(Button button) {
        myRepository.insert(button);
    }
}
