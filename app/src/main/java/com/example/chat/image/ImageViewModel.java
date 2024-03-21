package com.example.chat.image;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.infrastructure.BaseViewModel;
import com.example.infrastructure.Utils;

public class ImageViewModel extends BaseViewModel {
    private static final String TAG = ImageViewModel.class.getSimpleName();

    private final MutableLiveData<Bitmap> imagePickedUrl = new MutableLiveData<>(null);

    public ImageViewModel(String imageUrl) {
        Bitmap b = Utils.decodeImage(imageUrl);
        imagePickedUrl.postValue(b);
    }

    public LiveData<Bitmap> getImagePickedUrl() {
        return imagePickedUrl;
    }
}
