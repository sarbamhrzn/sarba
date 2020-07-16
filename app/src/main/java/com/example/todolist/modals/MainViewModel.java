package com.example.todolist.modals;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import com.example.todolist.database.model.DatabaseModel;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<DatabaseModel>> mTodos;
    TodoRepository mRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TodoRepository(application);
        mTodos = mRepository.getAllWords();
    }

    public LiveData<List<DatabaseModel>> getTodos(){
        return mTodos;
    }

    public void insert(DatabaseModel todo) { mRepository.insert(todo); }

    public void delete(DatabaseModel todo) { mRepository.delete(todo); }

    public void update(DatabaseModel todo) { mRepository.update(todo); }
}
