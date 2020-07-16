package com.example.todolist.mainactivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReceiverClass extends BroadcastReceiver {
    Intent intent;
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,Alarm.class);
        intent1.putExtra("intent", intent.getStringExtra("title"));
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent1);
        Toast.makeText(context, "alarm", Toast.LENGTH_SHORT).show();
    }
}
