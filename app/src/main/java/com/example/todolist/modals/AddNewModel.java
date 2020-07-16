package com.example.todolist.modals;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.todolist.database.AppDatabase;
import com.example.todolist.database.model.DatabaseModel;


public class AddNewModel extends ViewModel {

    private LiveData<DatabaseModel> todo;

    public AddNewModel(AppDatabase database, int todoId){
        todo = database.todoDao().loadTodoById(todoId);
    }

    public LiveData<DatabaseModel> getTodo() {
        return todo;
    }


}
