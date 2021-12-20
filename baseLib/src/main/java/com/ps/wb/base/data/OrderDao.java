package com.ps.wb.base.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    Long insert(Order order);

    @Delete
    int delete(Order order);

    @Update
    int update(Order order);

    @Query("select * from `order` order by orderTime desc")
    List<Order> getAll();

    // 使用 LiveData 关联 Room
    @Query("select * from `order` where sendAddress like:key " +
            "or sendName like:key " +
            "or sendPhone like:key " +
            "or receiveAddress like:key " +
            "or receiveName like:key " +
            "or receiveName like:key " +
            "or orderNumber like:key " +
            "order by orderTime desc")
    List<Order> search(String key);

    @Query("select * from `order` where id=:id")
    Order searchByAId(int id);
}
