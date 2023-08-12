package com.softs.hn.ip.travesiaapp.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.Nota;

import java.util.List;

public class ContactosRepository {
    private ContactosDao dao;
    private LiveData<List<Contacto>> dataset;

    public ContactosRepository(Application app) {
        Database db = Database.getDatabase(app);
        this.dao = db.ContactosDao();
        this.dataset = dao.getContactos();
    }

    public LiveData<List<Contacto>> getDataset() {
        return dataset;
    }

    public void insert(Contacto data){
        Database.databaseWriteExecutor.execute(() -> {
            dao.insert(data);
        });
    }

    public void update(Contacto data){
        Database.databaseWriteExecutor.execute(() -> {
            dao.update(data);
        });
    }

    public void delete(Contacto data){
        Database.databaseWriteExecutor.execute(() -> {
            dao.delete(data);
        });
    }

    public void deleteAll(){
        Database.databaseWriteExecutor.execute(() -> {
            dao.deleteAll();
        });
    }
}
