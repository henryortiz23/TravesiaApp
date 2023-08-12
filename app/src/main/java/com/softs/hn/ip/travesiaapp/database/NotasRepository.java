package com.softs.hn.ip.travesiaapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.softs.hn.ip.travesiaapp.entity.Nota;

import java.util.List;

public class NotasRepository {
    private NotasDao dao;
    private LiveData<List<Nota>> dataset;

    public NotasRepository(Application app) {
        Database db = Database.getDatabase(app);
        this.dao = db.NotasDao();
        this.dataset = dao.getNotas();
    }

    public LiveData<List<Nota>> getDataset() {
        return dataset;
    }

    public void insert(Nota nuevo){
        //INSERTANDO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        Database.databaseWriteExecutor.execute(() -> {
            dao.insert(nuevo);
        });
    }

    public void update(Nota actualizar){
        //ACTUALIZANDO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        Database.databaseWriteExecutor.execute(() -> {
            dao.update(actualizar);
        });
    }

    public void delete(Nota borrar){
        //BORRANDO UN REGISTRO DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        Database.databaseWriteExecutor.execute(() -> {
            dao.delete(borrar);
        });
    }

    public void deleteAll(){
        //BORRANDO TODOS LOS REGISTROS DE FORMA ASINCRONA, PARA NO AFECTAR LA INTERFAZ DE USUARIO
        Database.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }
}
