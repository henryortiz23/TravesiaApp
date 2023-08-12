package com.softs.hn.ip.travesiaapp.ui.contactosfrecuentes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softs.hn.ip.travesiaapp.databinding.FragmentContactsFrecuentesBinding;


public class ContactosFrecuentesFragment extends Fragment {

   private FragmentContactsFrecuentesBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentContactsFrecuentesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}