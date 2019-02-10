package edu.msoe.ncir.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Buttons belong to a specific remote
 */
@Entity(tableName = "button_table")
public class Button {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    /*
     * The index of this button on the device (not unique)
     */
    @NonNull
    @ColumnInfo(name = "device_index")
    private int deviceIndex;

    /*
     * The view index is the location on the remote grid where this button sits
     */
    @NonNull
    @ColumnInfo(name = "view_index")
    private int viewIndex;

    @NonNull
    @ColumnInfo(name = "remote_id")
    private int remoteID;

    public Button(@NonNull int remoteID, @NonNull String name) {
        this.remoteID = remoteID;
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
    public int getRemoteID() {
        return this.remoteID;
    }

    public void setRemoteID(int id) {
        this.remoteID = id;
    }

    public int getDeviceIndex() {
        return this.deviceIndex;
    }

    public void setDeviceIndex(int index) {
        this.deviceIndex = index;
    }

    public int getViewIndex() {
        return this.viewIndex;
    }

    public void setViewIndex(int index) {
        this.viewIndex = index;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
