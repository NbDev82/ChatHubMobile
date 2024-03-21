package com.example.chat.image;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.R;
import com.example.databinding.ActivityImageBinding;

public class ImageActivity extends AppCompatActivity {
    private ImageViewModel viewModel;
    private ActivityImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_image);

        String imageUrl = getIntent().getStringExtra("imageClickedUrl");

        viewModel = new ImageViewModel(imageUrl);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        setObservers();
    }

    private void setObservers() {
        viewModel.getImagePickedUrl().observe(this, bitmap -> binding.imageView.setImageBitmap(bitmap));
    }
}