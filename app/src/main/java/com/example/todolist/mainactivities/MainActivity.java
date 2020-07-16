package com.example.todolist.mainactivities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.todolist.R;
import com.example.todolist.adapter.Adapter;
import com.example.todolist.database.model.DatabaseModel;
import com.example.todolist.modals.MainViewModel;

import java.util.List;

import static android.support.v7.widget.RecyclerView.VERTICAL;

public class MainActivity extends AppCompatActivity implements Adapter.ItemClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private MainViewModel mViewModel;
    Button addBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.recyclerViewTasks);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new Adapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(addTaskIntent);
            }
        });

        setUpViewModel();
    }

    protected  void onResume(){
        super.onResume();
    }

    private void setUpViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mViewModel.getTodos().observe(this, new Observer<List<DatabaseModel>>() {
            @Override
            public void onChanged(@Nullable List<DatabaseModel> todos) {
                mAdapter.setTodoList(todos);
            }
        });


    }


    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        intent.putExtra(AddActivity.EXTRA_TASK_ID, itemId);
        startActivity(intent);
    }
}
