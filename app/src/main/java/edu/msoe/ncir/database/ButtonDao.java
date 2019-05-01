package edu.msoe.ncir.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.msoe.ncir.models.Button;

/**
 * DAO stands for data access object, this is where all SQL queries are built.
 * LiveData is used since these values must be updated dynamically.
 */
@Dao
public interface ButtonDao {

    @Insert
    void insert(Button button);

    @Query("DELETE FROM button_table")
    void deleteAll();

    @Query("SELECT * from button_table ORDER BY name ASC")
    LiveData<List<Button>> getAll();

    @Query("SELECT * from button_table WHERE remote_id = :remoteID ORDER BY view_index ASC")
    LiveData<List<Button>> getAll(int remoteID);

    @Query("SELECT * from button_table WHERE id = :buttonID")
    LiveData<Button> get(int buttonID);
}
