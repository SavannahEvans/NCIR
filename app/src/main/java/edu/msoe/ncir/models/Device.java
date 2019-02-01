package edu.msoe.ncir.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "device_table")
public class Device {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "name")
    private String name;

    public Device(@NonNull String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
