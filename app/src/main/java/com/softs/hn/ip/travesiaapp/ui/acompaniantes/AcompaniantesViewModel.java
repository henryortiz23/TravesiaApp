package com.softs.hn.ip.travesiaapp.ui.acompaniantes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.softs.hn.ip.travesiaapp.database.ContactosRepository;
import com.softs.hn.ip.travesiaapp.database.NotasRepository;
import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.Nota;

import java.util.List;

public class AcompaniantesViewModel extends AndroidViewModel {

    private ContactosRepository repository;
    private final LiveData<List<Contacto>> dataSet;

    public AcompaniantesViewModel(@NonNull Application app) {
        super(app);
        this.repository= new ContactosRepository(app);
        this.dataSet= repository.getDataset();
    }

    private ContactosRepository getRepository(){return repository;}
    public LiveData<List<Contacto>> getDataSet(){return dataSet;}

    public void insert(Contacto data) {
        repository.insert(data);
    }

    public void update(Contacto data) {
        repository.update(data);
    }

    public void delete(Contacto data) {
        repository.delete(data);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}