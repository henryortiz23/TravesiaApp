package com.softs.hn.ip.travesiaapp.ui.inicio;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.softs.hn.ip.travesiaapp.databinding.FragmentInicioBinding;
import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.Nota;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class InicioFragment extends Fragment implements OnItemClickListenerNotas<Nota>{

    private NotasAdapter adapter;
    private InicioViewModel viewModel;
    private FragmentInicioBinding binding;

    private StaggeredGridLayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        adapter = new NotasAdapter(new ArrayList<>(), this);

        viewModel.getDataSet().observe(getViewLifecycleOwner(),notas -> {
            adapter.setItems(notas);
        });

        binding.bFab.setOnClickListener(v ->{
            nuevaNota();
        });

        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.rvNotas.setLayoutManager(layoutManager);

        binding.rvNotas.setAdapter(adapter);
    }

    private void nuevaNota(){
        Nota nNueva= new Nota("Castillo de Omoa",obtenerFecha(),"Es un castillo de la era medieval, el cual a pesar del tiempo aun se mantiene en pie","","15.778583","-88.040023",obtenerRecursoImg());
        viewModel.insert(nNueva);

    }
    public int obtenerRecursoImg(){
        Random random = new Random();
        int n = random.nextInt(17) + 1;
        return n;
    }

    private String obtenerFecha(){
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha = dateFormat.format(currentDate);

        return fecha;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Nota data) {

    }

    @Override
    public void onItemClickDelete(Nota data) {
        viewModel.delete(data);
    }

    @Override
    public void onItemClickShared(Nota data) {

    }
}