package com.ps.wb.base.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AddressDao {

    @Insert
    Long insert(Address address);

    @Delete
    void delete(Address address);

    @Update
    void update(Address address);

    @Query("select * from address")
    LiveData<List<Address>> getAll();

    // 使用 LiveData 关联 Room
    @Query("select * from address where aId=:id")
    LiveData<List<Address>> searchByAId(int id);
}
