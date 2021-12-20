package com.ps.wb.base.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    Long insert(Message message);

    @Delete
    int delete(Message message);

    @Update
    void update(Message message);

    @Query("select * from message order by type,time desc")
    List<Message> getAll();

    @Query("select * from message where orderId=:orderId")
    List<Message> searchByOrderId(int orderId);

    @Query("select * from message where id=:id")
    Message searchById(int id);
}
