package com.softs.hn.ip.travesiaapp.ui.inicio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.softs.hn.ip.travesiaapp.R;
import com.softs.hn.ip.travesiaapp.entity.Nota;
import com.softs.hn.ip.travesiaapp.databinding.ItemNoteBinding;

import java.util.List;
import java.util.Random;

public class NotasAdapter extends RecyclerView.Adapter<NotasAdapter.ViewHolder> {

    private List<Nota> dataset;
    private OnItemClickListenerNotas<Nota> manejadorEventoClick;

    public NotasAdapter(List<Nota> dataset, OnItemClickListenerNotas<Nota> manejadorEventoClick) {
        this.dataset = dataset;
        this.manejadorEventoClick = manejadorEventoClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemNoteBinding binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Nota nota = dataset.get(position);


        holder.binding.tvLugar.setText(nota.getLugar());
        holder.binding.tvFecha.setText(nota.getFecha());
        holder.binding.tvNota.setText(nota.getComentario());
        int img = holder.itemView.getResources().getIdentifier("cap" + nota.getImg(), "drawable", holder.itemView.getContext().getPackageName());
        holder.binding.fotografia.setImageResource(img);

        holder.setOnClickListener(nota, manejadorEventoClick);


    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Nota> nota){
        this.dataset = nota;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemNoteBinding binding;
        public ViewHolder(@NonNull ItemNoteBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setOnClickListener(Nota datosNota, OnItemClickListenerNotas<Nota> listener) {
            this.itemView.setOnClickListener(v -> {

                listener.onItemClick(datosNota);
            });

            this.binding.btnDelete.setOnClickListener(v -> listener.onItemClickDelete(datosNota));
        }
    }
}
