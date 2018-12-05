package com.example.user.lul;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

public class Title extends AppCompatActivity {

    private ImageButton startbtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.title);
        startbtn = (ImageButton) findViewById(R.id.startbtn);
        startbtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Title.this, MainActivity.class);
                startActivity(intent);
                Title.this.finish();
            }
        });

    }
}