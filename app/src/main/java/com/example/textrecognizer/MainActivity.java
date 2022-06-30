package com.example.textrecognizer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    Button camera,gallery;
    private static final int PERMISSION_REQUEST_CODE = 777;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 778;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera=findViewById(R.id.camera);
        gallery=findViewById(R.id.gallery);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent(MainActivity.this,ExtractActivity.class);
                    intent.putExtra("code",2);
                    startActivity(intent);
                }
                else
                    EasyPermissions.requestPermissions(MainActivity.this, "Allow this app to access your storage", PERMISSION_REQUEST_CODE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(EasyPermissions.hasPermissions(MainActivity.this,
                        Manifest.permission.CAMERA)){
                    Intent intent = new Intent(MainActivity.this,ExtractActivity.class);
                    intent.putExtra("code",1);
                    startActivity(intent);
                }
                else {
                    EasyPermissions.requestPermissions(MainActivity.this,
                            "ALLOW CAMERA ACCESS",
                            CAMERA_PERMISSION_REQUEST_CODE,
                            Manifest.permission.CAMERA);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,grantResults, this);
    }

}