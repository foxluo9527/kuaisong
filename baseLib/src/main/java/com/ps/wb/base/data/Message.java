package com.ps.wb.base.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Message {
    public static final int TYPE_SYS = 1;
    public static final int TYPE_ORDER_MESSAGE = 2;
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "type")
    private int type;
    //订单时间
    @ColumnInfo(name = "time")
    private long time;
    //小红点
    @ColumnInfo(name = "point")
    private boolean point;
    @ColumnInfo(name = "orderId")
    private int orderId;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "message")
    private String message;

    public Message(int type, String title, int orderId, String message) {
        this.type = type;
        this.time = new Date().getTime();
        this.point = true;
        this.orderId = orderId;
        this.message = message;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isPoint() {
        return point;
    }

    public void setPoint(boolean point) {
        this.point = point;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}