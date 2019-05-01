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

    @ColumnInfo(name = "ipaddr")
    private String ipaddr;

    public Device(@NonNull String name, @NonNull String ipaddr) {
        this.name = name;
        this.ipaddr = ipaddr;
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

    public String getIpaddr() {
        return this.ipaddr;
    }

    public void setIpaddr(String ipaddr) {
        this.ipaddr = ipaddr;
    }
}
