package edu.msoe.ncir.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Signals belong to a device
 */
@Entity(tableName = "signal_table")
public class Signal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    /*
     * The index of this button on the device
     */
    @NonNull
    @ColumnInfo(name = "device_index")
    private int deviceIndex;


    @NonNull
    @ColumnInfo(name = "device_id")
    private int deviceID;

    public Signal(@NonNull int deviceID, @NonNull String name) {
        this.deviceID = deviceID;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public int getDeviceID() {
        return this.deviceID;
    }

    public void setDeviceID(int id) {
        this.deviceID = id;
    }

    public int getDeviceIndex() {
        return this.deviceIndex;
    }

    public void setDeviceIndex(int index) {
        this.deviceIndex = index;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
