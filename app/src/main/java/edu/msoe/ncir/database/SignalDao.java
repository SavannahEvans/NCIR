package edu.msoe.ncir.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.msoe.ncir.models.Signal;

/**
 * DAO stands for data access object, this is where all SQL queries are built.
 * LiveData is used since these values must be updated dynamically.
 */
@Dao
public interface SignalDao {

    @Insert
    void insert(Signal signal);

    @Query("DELETE FROM signal_table")
    void deleteAll();

    @Query("SELECT * from signal_table ORDER BY name ASC")
    LiveData<List<Signal>> getAll();

    @Query("SELECT * from signal_table WHERE device_id = :deviceID ORDER BY id ASC")
    LiveData<List<Signal>> getAll(int deviceID);

    @Query("SELECT * from signal_table WHERE id = :signalID")
    LiveData<Signal> get(int signalID);
}
