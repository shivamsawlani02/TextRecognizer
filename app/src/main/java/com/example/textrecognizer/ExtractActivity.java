package com.example.textrecognizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pub.devrel.easypermissions.EasyPermissions;

public class ExtractActivity extends AppCompatActivity {
    FloatingActionButton right,wrong;
    ImageView picture;
    Uri mImageUri;
    Bitmap photo;
    int code;
    private static final int GALLERY_REQUEST_CODE = 107;
    private static final int CAMERA_REQUEST = 108;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extract);

        right = findViewById(R.id.fab_tick);
        wrong = findViewById(R.id.fab_cross);
        picture = findViewById(R.id.picture);

        code = getIntent().getIntExtra("code",1);
        if(code == 1)
            opencamera();
        if(code == 2)
            openGallery();
        picture.setImageBitmap(photo);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                Log.d("BITMAP",photo+"");
                Frame imageFrame = new Frame.Builder()
                        .setBitmap(photo)              // your image bitmap
                        .build();

                String imageText = "";


                SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

                for (int i = 0; i < textBlocks.size(); i++) {
                    TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                    imageText = imageText + textBlock.getValue();// return string

                }
                Intent intent = new Intent(ExtractActivity.this, ResultActivity.class);
                intent.putExtra("res",imageText);
                startActivity(intent);
            }
        });

        wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExtractActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void opencamera()
    {
        Log.i("FUNCTION", "openCamera() started");
        code = 0;
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }
    private void openGallery() {
        code = 0;
        Log.i("FUNCTION", "openGallery() started");
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Picasso.get().load(data.getData()).into(image);//picasso directly displays images from its uri.
            mImageUri=data.getData();
            Log.i("PATH",mImageUri.getPath());

            Glide.with(this)
                    .asBitmap()
                    .load(mImageUri)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            picture.setImageBitmap(resource);
                            photo = resource;
                            Log.d("BITMAP GLIDE",resource+"");
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
        }
        else if (requestCode == CAMERA_REQUEST &&
                resultCode == RESULT_OK &&
                data != null &&
                data.getExtras() != null) {

            photo = (Bitmap) data.getExtras().get("data");
            mImageUri=data.getData();
            picture.setImageBitmap(photo);

        }
        else {
            Intent intent = new Intent(ExtractActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}