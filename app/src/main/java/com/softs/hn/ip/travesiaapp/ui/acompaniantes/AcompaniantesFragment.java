package com.softs.hn.ip.travesiaapp.ui.acompaniantes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.softs.hn.ip.travesiaapp.R;
import com.softs.hn.ip.travesiaapp.databinding.FragmentAcompaniantesBinding;
import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.Contacto2;
import com.softs.hn.ip.travesiaapp.ui.inicio.InicioViewModel;
import com.softs.hn.ip.travesiaapp.ui.inicio.NotasAdapter;
import com.softs.hn.ip.travesiaapp.ui.inicio.OnItemClickListenerNotas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AcompaniantesFragment extends Fragment implements OnItemClickListenerNotas<Contacto> {

    private FragmentAcompaniantesBinding binding;

    private ContactosAdapter adapter;

    AcompaniantesViewModel aContactos;

    private int op = 1;

    private static final int PERMISSION_REQUEST_READ_CONTACT = 100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAcompaniantesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        aContactos = new ViewModelProvider(this).get(AcompaniantesViewModel.class);

        adapter = new ContactosAdapter(new ArrayList<>(), this);

        actualizarInformacion();
        binding.bFabAction.setOnClickListener(v -> {
            if (adapter.getItemCount() > 0) {
                aContactos.deleteAll();
            } else {
                binding.bFabAction.setVisibility(View.GONE);

                binding.tvLoading.setText(getString(R.string.importando_contactos));
                binding.tvLoading.setVisibility(View.VISIBLE);
                solicitudPermisoContactos(this.getContext());
            }
        });

        setupRecyclerView();
        return root;
    }


    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        binding.rvAcompaniantes.setLayoutManager(linearLayoutManager);

        binding.rvAcompaniantes.setAdapter(adapter);
    }

    private void solicitudPermisoContactos(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACT);
        } else {
            getContacts(context);
        }
    }

    @SuppressLint("Range")
    private void getContacts(Context context) {
        List<Contacto2> contactos = new ArrayList<>();

        Contacto2 cOld = new Contacto2("", "", "");
        AcompaniantesViewModel aContactos = new ViewModelProvider(this).get(AcompaniantesViewModel.class);

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " > 0",
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phone.replaceAll(" +|-|\\(|\\)", "");
                String correo = "";

                @SuppressLint("Range") Cursor cursorCorreo = resolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))},
                        null
                );

                if (cursorCorreo != null && cursorCorreo.moveToNext()) {
                    correo = cursorCorreo.getString(cursorCorreo.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
                    cursorCorreo.close();
                }
                correo = correo.trim();

                Contacto2 nuevo2 = new Contacto2(name, phone, correo);

                //if (!contactos.contains(nuevo2)) {
                String n1, n2, p1, p2, e1, e2;
                n1 = cOld.getName();
                n2 = nuevo2.getName();
                p1 = cOld.getPhone();
                p2 = nuevo2.getPhone();
                e1 = cOld.getEmail();
                e2 = nuevo2.getEmail();

                if (!n1.equals(n2) || !p1.equals(p2) || !e1.equals(e2)) {
                    cOld = nuevo2;
                    contactos.add(nuevo2);
                    Contacto nuevo = new Contacto(name, phone, correo);
                    aContactos.insert(nuevo);
                }

            }

            cursor.close();
        }

        binding.bFabAction.setVisibility(View.VISIBLE);

    }

    private void actualizarInformacion() {
        aContactos.getDataSet().observe(getViewLifecycleOwner(), contactos -> {
            adapter.setItems(contactos);
            if (adapter.getItemCount() > 0) {
                if (op == 1) {
                    op = 2;
                    binding.tvLoading.setVisibility(View.GONE);
                    Drawable dIcon = getResources().getDrawable(R.drawable.ic_delete_24dp);
                    binding.bFabAction.setIcon(dIcon);
                    binding.bFabAction.setText(R.string.eliminar_contactos);
                }
            } else {
                if (op == 2) {
                    op = 1;
                    Drawable dIcon = getResources().getDrawable(R.drawable.ic_companiero_24dp);
                    binding.bFabAction.setIcon(dIcon);
                    binding.tvLoading.setText(getString(R.string.sin_contactos));
                    binding.bFabAction.setText(R.string.importar_contactos);

                    binding.tvLoading.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_CONTACT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts(requireContext());
                if (adapter.getItemCount() == 0) {
                    binding.tvLoading.setVisibility(View.GONE);
                }
            } else {
                if (adapter.getItemCount() == 0) {
                    binding.tvLoading.setText(getString(R.string.sin_contactos));
                    binding.tvLoading.setVisibility(View.VISIBLE);
                }
                Toast.makeText(getContext(), "PERMISO DENEGADO", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Contacto data) {

    }

    @Override
    public void onItemClickDelete(Contacto data) {

    }

    @Override
    public void onItemClickShared(Contacto data) {

    }
}