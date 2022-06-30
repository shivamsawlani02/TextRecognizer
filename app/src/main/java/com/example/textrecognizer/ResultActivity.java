package com.example.textrecognizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ResultActivity extends AppCompatActivity {
    TextView result;
    FloatingActionButton copy;
    String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        result = findViewById(R.id.img_res);
        copy = findViewById(R.id.fab_copy);
        result.setTextIsSelectable(true);
        res = getIntent().getStringExtra("res");
        result.setMovementMethod(new ScrollingMovementMethod());
        result.setText(res);

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", res);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(ResultActivity.this, "Text Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

    }
}