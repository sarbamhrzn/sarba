package com.example.todolist.modals;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.todolist.database.AppDatabase;


public class AddNewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mdb;
    private final int mTodoId;


    public AddNewModelFactory(AppDatabase mdb, int mTodoId) {
        this.mdb = mdb;
        this.mTodoId = mTodoId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddNewModel(mdb, mTodoId);
    }
}
