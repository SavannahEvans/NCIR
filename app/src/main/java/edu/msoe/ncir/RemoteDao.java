package edu.msoe.ncir;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * DAO stands for data access object, this is where all SQL queries are built.
 * LiveData is used since these values must be updated dynamically.
 */
@Dao
public interface RemoteDao {

    @Insert
    void insert(Remote remote);

    @Query("DELETE FROM remote_table")
    void deleteAll();

    @Query("SELECT * from remote_table ORDER BY name ASC")
    LiveData<List<Remote>> getAll();

    @Query("SELECT * from remote_table WHERE device LIKE :deviceName ORDER BY name ASC")
    LiveData<List<Remote>> getAll(String deviceName);
}
