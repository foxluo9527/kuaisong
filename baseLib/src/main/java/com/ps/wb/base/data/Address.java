package com.ps.wb.base.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Address {
    @PrimaryKey(autoGenerate = true)
    private int aId;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "addressDetail")
    private String addressDetail;
    @ColumnInfo(name = "doorNumber")
    private String doorNumber;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "lt")
    private double lt;
    @ColumnInfo(name = "lg")
    private double lg;

    public Address(String name, String addressDetail, String doorNumber, String phone) {
        this.name = name;
        this.addressDetail = addressDetail;
        this.doorNumber = doorNumber;
        this.phone = phone;
    }

    public Address copy() {
        Address address=new Address(name, addressDetail, doorNumber, phone);
        address.lt=lt;
        address.lg=lg;
        return address;
    }

    public int getAId() {
        return aId;
    }

    public void setAId(int aId) {
        this.aId = aId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public String getDoorNumber() {
        return doorNumber;
    }

    public void setDoorNumber(String doorNumber) {
        this.doorNumber = doorNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getLt() {
        return lt;
    }

    public void setLt(double lt) {
        this.lt = lt;
    }

    public double getLg() {
        return lg;
    }

    public void setLg(double lg) {
        this.lg = lg;
    }
}