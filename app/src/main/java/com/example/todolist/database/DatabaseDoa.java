package com.example.todolist.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.todolist.database.model.DatabaseModel;

import java.util.List;

@Dao
public  interface DatabaseDoa {

    @Query("select * from `Database` order by priority")
    LiveData<List<DatabaseModel>> getAllTodos();

    @Insert
    void insertTodo(DatabaseModel databaseModel);

    @Delete
    void deleteTodo(DatabaseModel databaseModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(DatabaseModel databaseModel);

    @Query("select * from `Database` where id = :id")
    LiveData<DatabaseModel> loadTodoById(int id);

}
