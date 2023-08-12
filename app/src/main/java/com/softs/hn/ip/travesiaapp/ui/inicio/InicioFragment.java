package com.softs.hn.ip.travesiaapp.ui.inicio;

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
import com.softs.hn.ip.travesiaapp.entity.Nota;

import java.util.ArrayList;

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

        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.rvNotas.setLayoutManager(layoutManager);

        //binding.rvNotas.setLayoutManager(linearLayoutManager);
        binding.rvNotas.setAdapter(adapter);
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