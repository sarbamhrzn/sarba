package com.example.todolist.mainactivities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.todolist.R;

public class Alarm extends AppCompatActivity {

    TextView textView;
    Button ok;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        ok = findViewById(R.id.ok);
        textView = findViewById(R.id.alarmTitle);
        final Intent intent = getIntent();
        int resID= this.getResources().getIdentifier("alarm", "raw", this.getPackageName());
        final MediaPlayer mediaPlayer= (MediaPlayer) MediaPlayer.create(this,resID);
        mediaPlayer.start();
        textView.setText(intent.getStringExtra("intent"));
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
               finish();
            }
        });
    }

}
