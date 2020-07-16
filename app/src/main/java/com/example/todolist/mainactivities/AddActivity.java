package com.example.todolist.mainactivities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.todolist.AppExecutors;
import com.example.todolist.R;
import com.example.todolist.database.AppDatabase;
import com.example.todolist.database.model.DatabaseModel;
import com.example.todolist.modals.AddNewModel;
import com.example.todolist.modals.AddNewModelFactory;
import com.example.todolist.modals.MainViewModel;


import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extraTaskId";
    public static final String INSTANCE_TASK_ID = "instanceTaskId";
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    private static final int DEFAULT_TODO_ID = -1;
    private static final String TAG = AddActivity.class.getSimpleName();
    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;
    Button delete;

    private int mTodoId = DEFAULT_TODO_ID;

    private AppDatabase mDb;
    private MainViewModel mViewModel;
    TimePicker timePicker;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
         timePicker = (TimePicker) findViewById(R.id.time_picker);
        initViews();

        mDb = AppDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTodoId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TODO_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            mButton.setText("Update");
            if (mTodoId == DEFAULT_TODO_ID) {
                mTodoId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TODO_ID);

                        AddNewModelFactory factory = new AddNewModelFactory(mDb, mTodoId);
                        final AddNewModel viewModel = ViewModelProviders.of(this, factory).get(AddNewModel.class);

                        viewModel.getTodo().observe(AddActivity.this, new Observer<DatabaseModel>() {
                            @Override
                            public void onChanged(@Nullable DatabaseModel todoEntry) {
                                viewModel.getTodo().removeObserver(this);
                                Log.d(TAG, "Receiving database update from LiveData");
                                populateUI(todoEntry);
                            }
                        });
            }
        }
        delete = findViewById(R.id.deleteBtn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = mEditText.getText().toString();
                int priority = getPriorityFromViews();
                Date date = new Date();

                // TODO (4) Make todo final so it is visible inside the run method
                final  DatabaseModel todo = new DatabaseModel(description, priority, date);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if(mTodoId == DEFAULT_TODO_ID)
                            mDb.todoDao().deleteTodo(todo);
                        else{
                            todo.setId(mTodoId);
                            mDb.todoDao().deleteTodo(todo);
                        }
                    }
                });

                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTodoId);
        super.onSaveInstanceState(outState);
    }

    private void initViews() {
        mEditText = findViewById(R.id.editTextTaskDescription);
        mRadioGroup = findViewById(R.id.radioGroup);

        mButton = findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    private void populateUI(DatabaseModel todo) {
        if(todo == null)
            return;
        mEditText.setText( todo.getDescription());
        setPriorityInViews(todo.getPriority());


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onSaveButtonClicked() {
        String description = mEditText.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());// for 6 hour
        calendar.set(Calendar.MINUTE, timePicker.getMinute());// for 0 min
        calendar.set(Calendar.SECOND, 0);// for 0 sec


        Toast.makeText(this, ""+calendar.getTime(), Toast.LENGTH_SHORT).show();
        // TODO (4) Make todo final so it is visible inside the run method
       final  DatabaseModel todo = new DatabaseModel(description, priority, calendar.getTime());
       AppExecutors.getInstance().diskIO().execute(new Runnable() {
           @Override
           public void run() {
               if(mTodoId == DEFAULT_TODO_ID)
                mDb.todoDao().insertTodo(todo);
               else{
                   todo.setId(mTodoId);
                   mDb.todoDao().update(todo);
               }
           }
       });
        Intent intent = new Intent(getBaseContext(), ReceiverClass.class);
        intent.putExtra("title", description);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
        finish();
    }

    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }


    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }


}
