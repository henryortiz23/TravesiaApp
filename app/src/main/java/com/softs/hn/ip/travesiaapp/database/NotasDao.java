package com.softs.hn.ip.travesiaapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.softs.hn.ip.travesiaapp.entity.Nota;

import java.util.List;

@Dao
public interface NotasDao {

    @Insert
    void insert(Nota data);

    @Update
    void update(Nota data);

    @Query("DELETE FROM notas_table")
    void deleteAll();

    @Delete
    void delete(Nota data);

    @Query("select * from notas_table order by id")
    LiveData<List<Nota>> getNotas();
}
