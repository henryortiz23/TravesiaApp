package com.softs.hn.ip.travesiaapp.ui.inicio;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.softs.hn.ip.travesiaapp.entity.Nota;
import com.softs.hn.ip.travesiaapp.database.NotasRepository;

import java.util.List;

public class InicioViewModel extends AndroidViewModel {

    private NotasRepository repository;
    private final LiveData<List<Nota>> dataSet;

    public InicioViewModel(@NonNull Application app) {
        super(app);
        this.repository= new NotasRepository(app);
        this.dataSet= repository.getDataset();
    }

    private NotasRepository getRepository(){return repository;}
    public LiveData<List<Nota>> getDataSet(){return dataSet;}

    public void insert(Nota data) {
        repository.insert(data);
    }

    public void update(Nota data) {
        repository.update(data);
    }

    public void delete(Nota data) {
        repository.delete(data);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}