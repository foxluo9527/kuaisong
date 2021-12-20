package com.ps.wb.base.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Order {
    //订单总状态话术 预订xxxx时间配送 待接单 待取货 配送中 已完成 已取消
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "orderNumber")
    private String orderNumber;
    @ColumnInfo(name = "money")
    private double money;

    @ColumnInfo(name = "distance")
    private String distance;
    //下单时间
    @ColumnInfo(name = "orderTime")
    private long orderTime;
    //预订时间
    @ColumnInfo(name = "bookTime")
    private long bookTime;
    //预订时间表达 2021-11-28 19:00~20:00
    @ColumnInfo(name = "bookTimeString")
    private String bookTimeString;
    //订单进度 0:待接单 1:待配送 2:待收货 3:已完成 -1:已取消
    @ColumnInfo(name = "orderState")
    private int orderState;
    @ColumnInfo(name = "sendAddress")
    private String sendAddress;
    @ColumnInfo(name = "sendPhone")
    private String sendPhone;
    @ColumnInfo(name = "sendName")
    private String sendName;
    @ColumnInfo(name = "receiveAddress")
    private String receiveAddress;
    @ColumnInfo(name = "receivePhone")
    private String receivePhone;
    @ColumnInfo(name = "receiveName")
    private String receiveName;
    @ColumnInfo(name = "mark")
    private String mark;
    @ColumnInfo(name = "type")
    private String type;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Order() {
    }

    @Ignore
    public Order(String orderNumber, double money, String distance, long orderTime, long bookTime, String bookTimeString, int orderState, String sendAddress, String sendPhone, String sendName, String receiveAddress, String receivePhone, String receiveName, String mark, String type) {
        this.orderNumber = orderNumber;
        this.money = money;
        this.distance = distance;
        this.orderTime = orderTime;
        this.bookTime = bookTime;
        this.bookTimeString = bookTimeString;
        this.orderState = orderState;
        this.sendAddress = sendAddress;
        this.sendPhone = sendPhone;
        this.sendName = sendName;
        this.receiveAddress = receiveAddress;
        this.receivePhone = receivePhone;
        this.receiveName = receiveName;
        this.mark = mark;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getBookTime() {
        return bookTime;
    }

    public void setBookTime(long bookTime) {
        this.bookTime = bookTime;
    }

    public String getBookTimeString() {
        return bookTimeString;
    }

    public void setBookTimeString(String bookTimeString) {
        this.bookTimeString = bookTimeString;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getSendAddress() {
        return sendAddress;
    }

    public void setSendAddress(String sendAddress) {
        this.sendAddress = sendAddress;
    }

    public String getSendPhone() {
        return sendPhone;
    }

    public void setSendPhone(String sendPhone) {
        this.sendPhone = sendPhone;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getReceiveAddress() {
        return receiveAddress;
    }

    public void setReceiveAddress(String receiveAddress) {
        this.receiveAddress = receiveAddress;
    }

    public String getReceivePhone() {
        return receivePhone;
    }

    public void setReceivePhone(String receivePhone) {
        this.receivePhone = receivePhone;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }
}
