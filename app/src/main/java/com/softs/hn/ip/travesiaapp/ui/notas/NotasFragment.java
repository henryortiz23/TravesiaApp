package com.softs.hn.ip.travesiaapp.ui.notas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.softs.hn.ip.travesiaapp.databinding.FragmentNotasBinding;

public class NotasFragment extends Fragment {

    private FragmentNotasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotasViewModel notasViewModel =
                new ViewModelProvider(this).get(NotasViewModel.class);

        binding = FragmentNotasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}