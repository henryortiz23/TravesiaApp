package com.softs.hn.ip.travesiaapp.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.Nota;

import java.util.List;

@Dao
public interface ContactosDao {

    @Insert
    void insert(Contacto data);

    @Update
    void update(Contacto data);

    @Query("DELETE FROM contactos_table")
    void deleteAll();

    @Delete
    void delete(Contacto data);

    @Query("select * from contactos_table order by id")
    LiveData<List<Contacto>> getContactos();
}
