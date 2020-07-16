package com.example.todolist.modals;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.todolist.AppExecutors;
import com.example.todolist.database.AppDatabase;
import com.example.todolist.database.DatabaseDoa;
import com.example.todolist.database.model.DatabaseModel;


import java.util.List;

public class TodoRepository {

    private DatabaseDoa todoDao;
    private LiveData<List<DatabaseModel>> mAllTodos;

    TodoRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        todoDao = db.todoDao();
        mAllTodos = todoDao.getAllTodos();
    }

    LiveData<List<DatabaseModel>> getAllWords() {
        return mAllTodos;
    }

    public void insert (final DatabaseModel todo) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                todoDao.insertTodo(todo);
            }
        });
    }

    public void delete(final DatabaseModel todo)  {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                todoDao.deleteTodo(todo);
            }
        });
    }

    public void update(final DatabaseModel todo)  {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                todoDao.update(todo);
            }
        });
    }

}
