package com.example.user.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.BR;
import com.example.infrastructure.EUserField;
import com.example.user.User;
import com.example.user.signup.SignUpActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LoginViewModel extends BaseObservable {

    private static final String TAG = LoginViewModel.class.getSimpleName();

    private final Context context;

    private User user;

    private String successMessage = "Login successful";
    private String errorMessage = "Email or Password is not valid";

    @Bindable
    private String toastMessage = null;

    public String getToastMessage() {
        return toastMessage;
    }

    private void setToastMessage(String toastMessage) {
        this.toastMessage = toastMessage;
        notifyPropertyChanged(BR.toastMessage);
    }

    @Bindable
    public String getUserEmail() {
        return user.getEmail();
    }

    public void setUserEmail(String email) {
        user.setEmail(email);
        notifyPropertyChanged(BR.userEmail);
    }

    @Bindable
    public String getUserPassword() {
        return user.getHashedPass();
    }

    public void setUserPassword(String password) {
        user.setHashedPass(password);
        notifyPropertyChanged(BR.userPassword);
    }

    public LoginViewModel(Context context) {
        this.context = context;
        user = new User();

        addDataToFirestore();
    }

    private void addDataToFirestore() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(EUserField.USERNAME.getName(), "vanannek");
        user.put(EUserField.HASHED_PASS.getName(), "");
        user.put(EUserField.FULL_NAME.getName(), "Van An");
        user.put(EUserField.EMAIL.getName(), "vanne@gmail.com");
        user.put(EUserField.GENDER.getName(), User.EGender.MALE);
        user.put(EUserField.IS_DELETED.getName(), false);
        user.put(EUserField.AVAILABILITY.getName(), 0);

        database.collection(EUserField.COLLECTION_NAME.getName())
                .add(user)
                .addOnSuccessListener(df -> {
                    Toast.makeText(context, "Data Inserted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(context,exception.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void onButtonClicked() {
        Log.i(TAG, "Login button clicked");
        if (isValid())
            setToastMessage(successMessage);
        else
            setToastMessage(errorMessage);
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(getUserEmail()) && Patterns.EMAIL_ADDRESS.matcher(getUserEmail()).matches()
                && getUserPassword().length() > 5;
    }

    public void onSignUpTextClick(Context context) {
        Log.i(TAG, "Sign Up clicked");

        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }
}