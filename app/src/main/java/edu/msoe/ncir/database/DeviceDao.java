package edu.msoe.ncir.database;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import edu.msoe.ncir.models.Device;

/**
 * DAO stands for data access object, this is where all SQL queries are built.
 * LiveData is used since these values must be updated dynamically.
 */
@Dao
public interface DeviceDao {

    @Insert
    void insert(Device device);

    @Query("DELETE FROM device_table")
    void deleteAll();

    @Query("SELECT * from device_table ORDER BY name ASC")
    LiveData<List<Device>> getAll();

    @Query("SELECT * from device_table WHERE id LIKE :deviceID")
    LiveData<Device> get(int deviceID);

    @Query("SELECT * from device_table WHERE name LIKE :deviceName")
    LiveData<Device> get(String deviceName);
}
