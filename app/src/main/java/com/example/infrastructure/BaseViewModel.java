package com.example.infrastructure;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseViewModel extends AndroidViewModel {

    private DocumentReference documentReference;

    public BaseViewModel(Application application) {
        super(application);
    }

    public void initializeData() {
        PreferenceManager preferenceManager = new PreferenceManager(getApplication().getApplicationContext());
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        documentReference = database.collection(EUserField.COLLECTION_NAME.getName())
                .document(preferenceManager.getString(EUserField.USERNAME.getName()));
    }

    public void onPause() {
        documentReference.update(EUserField.AVAILABILITY.getName(), 0);
    }

    public void onResume() {
        documentReference.update(EUserField.AVAILABILITY.getName(), 1);
    }
}
