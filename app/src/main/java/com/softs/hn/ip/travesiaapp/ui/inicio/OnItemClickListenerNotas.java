package com.softs.hn.ip.travesiaapp.ui.inicio;

public interface OnItemClickListenerNotas<T> {
    void onItemClick(T data);

    void onItemClickDelete(T data);

    void onItemClickShared(T data);
}
