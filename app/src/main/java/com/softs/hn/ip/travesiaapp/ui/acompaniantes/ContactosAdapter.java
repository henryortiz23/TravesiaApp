package com.softs.hn.ip.travesiaapp.ui.acompaniantes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softs.hn.ip.travesiaapp.R;
import com.softs.hn.ip.travesiaapp.databinding.ItemContactoBinding;
import com.softs.hn.ip.travesiaapp.databinding.ItemNoteBinding;
import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.Nota;
import com.softs.hn.ip.travesiaapp.ui.inicio.OnItemClickListenerNotas;

import java.util.List;
import java.util.Random;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ViewHolder> {

    private List<Contacto> dataset;
    private OnItemClickListenerNotas<Contacto> manejadorEventoClick;

    public ContactosAdapter(List<Contacto> dataset, OnItemClickListenerNotas<Contacto> manejadorEventoClick) {
        this.dataset = dataset;
        this.manejadorEventoClick = manejadorEventoClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContactoBinding binding = ItemContactoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacto contacto = dataset.get(position);

        if(contacto.getEmail().isEmpty()){
            holder.binding.tvEmail.setVisibility(View.GONE);
        }else{
            holder.binding.tvEmail.setVisibility(View.VISIBLE);
        }

        holder.binding.tvContact.setText(contacto.getName());
        holder.binding.tvTelefono.setText(contacto.getPhone());
        holder.binding.tvEmail.setText(contacto.getEmail());


        holder.setOnClickListener(contacto, manejadorEventoClick);


    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Contacto> nota){
        this.dataset = nota;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemContactoBinding binding;
        public ViewHolder(@NonNull ItemContactoBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setOnClickListener(Contacto datosContacto, OnItemClickListenerNotas<Contacto> listener) {
            this.itemView.setOnClickListener(v -> {

                listener.onItemClick(datosContacto);
            });

            //this.binding.btnDelete.setOnClickListener(v -> listener.onItemClickDelete(datosNota));
        }
    }
}
