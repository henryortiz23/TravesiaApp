package com.softs.hn.ip.travesiaapp.ui.notas;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.net.Uri;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.softs.hn.ip.travesiaapp.R;
import com.softs.hn.ip.travesiaapp.databinding.FragmentNotasBinding;
import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.GPSLocation;
import com.softs.hn.ip.travesiaapp.entity.Nota;
import com.softs.hn.ip.travesiaapp.ui.acompaniantes.AcompaniantesViewModel;
import com.softs.hn.ip.travesiaapp.ui.inicio.InicioViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class NotasFragment extends Fragment implements LocationListener, OnItemClickListenerContactosSelect<Contacto> {

    private FragmentNotasBinding binding;

    private InicioViewModel viewModelNotas;

    private AcompaniantesViewModel viewModelAcompaniantes;

    private SelectContactosAdapter adapter;

    private Nota notaEditar;

    private List<Contacto> lSelectedContact;

    private String fecha, latitud, longitud;
    private int img;

    private static final int REQUEST_CODE_GPS = 123;
    private LocationManager locationManager;

    private GPSLocation ubicacion;

    Animation animation1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentNotasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ubicacion = null;
        lSelectedContact=new ArrayList<>();

        viewModelNotas = new ViewModelProvider(this).get(InicioViewModel.class);

        viewModelAcompaniantes = new ViewModelProvider(this).get(AcompaniantesViewModel.class);

        binding.btnGuardar.setOnClickListener(v -> {
            if(verificar()) {
                String nombre_lugar = binding.etLugar.getEditText().getText().toString();
                String comentario_lugar = binding.etComentario.getEditText().getText().toString();
                String acom = "";

                Nota nNota = notaEditar;

                for (Contacto contacto : lSelectedContact) {
                    String idContac = String.valueOf(contacto.getId());
                    acom = acom + idContac + "\n";
                    contacto.setFav((contacto.getFav() + 1));
                    viewModelAcompaniantes.update(contacto);
                }

                if (notaEditar == null) {
                    nNota = new Nota(nombre_lugar, fecha, comentario_lugar, acom, latitud, longitud, img);

                    viewModelNotas.insert(nNota);

                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.navigation_inicio);
                } else {
                    nNota.setLugar(nombre_lugar);
                    nNota.setComentario(comentario_lugar);
                    nNota.setAcompaniante(acom);
                    viewModelNotas.update(nNota);

                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.navigation_inicio);
                }

            }else{
                Snackbar.make(binding.etLugar, R.string.falta_informacion, Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.btnAcompaniantes.setOnClickListener(v -> {
            binding.lNota.setVisibility(View.GONE);
            binding.lContactos.setVisibility(View.VISIBLE);
            obtenerAcompaniantes();
        });

        binding.btnRegresar.setOnClickListener(v -> {
            binding.lContactos.setVisibility(View.GONE);
            binding.lNota.setVisibility(View.VISIBLE);
        });

        binding.btnCancelar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_inicio);
        });

        setupRecycler();

        notaEditar();

        mostrarNotaEditar();

        obtenerAcompaniantesNotaEditar();
        binding.btnMostrarContactos.setOnClickListener(v -> {
                binding.btnMostrarContactos.setVisibility(View.GONE);
                obtenerChips();
        });

        return root;
    }

    private boolean verificar(){
        return !"".equals(binding.etLugar.getEditText().getText().toString().trim()) && !"".equals(binding.etComentario.getEditText().getText().toString().trim());
    }
    private void obtenerChips() {
        binding.chipGroupAcompaniantes.removeAllViews();
        binding.chipGroupSelectAcompaniantes.removeAllViews();
        Drawable icon_contacto= requireActivity().getDrawable(R.drawable.ic_contact);
        if(lSelectedContact !=null && lSelectedContact.size()>0) {
            for (Contacto c : lSelectedContact) {
                Chip chip= new Chip(getContext());
                Chip chip2= new Chip(getContext());
                chip.setText(c.getName());
                chip2.setText(c.getName());
                chip.setTextSize(10);
                chip2.setTextSize(10);
                chip.setPadding(0,0,0,0);
                chip2.setPadding(0,0,0,0);
                chip.setChipIcon(icon_contacto);
                chip2.setChipIcon(icon_contacto);
                binding.chipGroupAcompaniantes.addView(chip);
                binding.chipGroupSelectAcompaniantes.addView(chip2);
            }
        }
    }

    private void obtenerAcompaniantesNotaEditar() {
        if (notaEditar != null) {
            String a = notaEditar.getAcompaniante();
            if (!"".equals(a)) {
                String[] a_ids = a.split("\n");
                List<String> list_ids = new ArrayList<>(Arrays.asList(a_ids));

                for (String numero : list_ids) {
                    if (!"".equals(numero)) {
                        LiveData<Contacto> contacto;
                        contacto = viewModelAcompaniantes.getDataContactoId(Integer.valueOf(numero));
                        if (contacto != null) {
                            contacto.observe(getViewLifecycleOwner(), contactoId -> {
                                if (contactoId != null) {
                                    if (!lSelectedContact.contains(contactoId)) {
                                        lSelectedContact.add(contactoId);
                                    }
                                }

                            });
                        }
                    }
                }

            }
        }
    }

    private void notaEditar() {
        try {
            notaEditar = (Nota) getArguments().getSerializable("nota");
            binding.btnCancelar.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            notaEditar = null;
            solicitarPermisosGPS(getContext());
            fecha = obtenerFecha();
            img = obtenerRecursoImg();
        }
    }

    private void mostrarNotaEditar() {
        if (notaEditar != null) {
            binding.etLugar.getEditText().setText(notaEditar.getLugar());
            binding.etComentario.getEditText().setText(notaEditar.getComentario());

            if (!"".equals(notaEditar.getAcompaniante())) {
                binding.tvSinContactosSeleccionados.setVisibility(View.GONE);
                binding.btnMostrarContactos.setVisibility(View.VISIBLE);
                binding.lSelectAcompaniantes.setVisibility(View.VISIBLE);
            } else {
                binding.tvSinContactosSeleccionados.setVisibility(View.VISIBLE);
                binding.btnMostrarContactos.setVisibility(View.GONE);
                binding.lSelectAcompaniantes.setVisibility(View.GONE);
            }

            binding.etLugar.requestFocus();
        } else {
            binding.tvSinContactosSeleccionados.setVisibility(View.VISIBLE);
            binding.btnMostrarContactos.setVisibility(View.GONE);
        }
    }

    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rvAcompaniantes.setLayoutManager(linearLayoutManager);
    }

    private void obtenerAcompaniantes() {
        LiveData<List<Contacto>> contacto1 = viewModelAcompaniantes.getDataSet();
        contacto1.observe(getViewLifecycleOwner(), contactos -> {
            if (contactos != null) {
                adapter = new SelectContactosAdapter(contactos, this, new ArrayList<>(lSelectedContact));
                binding.rvAcompaniantes.setAdapter(adapter);
            }
        });
    }


    public int obtenerRecursoImg() {
        Random random = new Random();
        int n = random.nextInt(17) + 1;
        return n;
    }

    private String obtenerFecha() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha = dateFormat.format(currentDate);

        return fecha;
    }

    private void solicitarPermisosGPS(Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            useFineLocation();
            animarObtenerUbicacion(1);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        }
    }


    private void animarObtenerUbicacion(int a) {
        animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
        if (a == 1) {
            binding.imgLoading.startAnimation(animation1);
            binding.lUbicacion.setVisibility(View.GONE);
            binding.lLoading.setVisibility(View.VISIBLE);
        } else {
            binding.imgLoading.clearAnimation();
            binding.lLoading.setVisibility(View.GONE);
            binding.lUbicacion.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GPS) {
            animarObtenerUbicacion(1);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                useFineLocation();
            } else if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                useCoarseLocation();
            }
        } else {
            Snackbar.make(binding.etLugar, R.string.permiso_denegado, Snackbar.LENGTH_SHORT).show();
        }
    }


    @SuppressLint({"ServiceCast", "MissingPermission"})
    private void useCoarseLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @SuppressLint({"ServiceCast", "MissingPermission"})
    private void useFineLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        ubicacion = new GPSLocation(location.getLatitude(), location.getLongitude());

        latitud = ubicacion.getLatitudeStr();
        longitud = ubicacion.getLongitudeStr();

        locationManager.removeUpdates(this);
        animarObtenerUbicacion(2);
    }

    @Override
    public boolean onItemClick(Contacto data) {
        boolean send = false;
        boolean exist = false;

        if (lSelectedContact != null) {
            if (lSelectedContact.size() > 0) {
                for (Contacto c : lSelectedContact) {
                    if (c.getId() == data.getId()) {
                        lSelectedContact.remove(c);
                        exist = true;
                        send = true;
                        break;
                    }
                }
            }
        }

        if (!exist) {
            if (lSelectedContact.size() < 6) {
                lSelectedContact.add(data);
                send = true;
            } else {
                Snackbar.make(binding.etLugar, R.string.companieros_maximos, Snackbar.LENGTH_SHORT).show();
            }
        }


        if (lSelectedContact.size() > 0) {
            binding.lSelectAcompaniantes.setVisibility(View.VISIBLE);
            binding.tvSinContactosSeleccionados.setVisibility(View.GONE);
        } else {
            binding.lSelectAcompaniantes.setVisibility(View.GONE);
            binding.tvSinContactosSeleccionados.setVisibility(View.VISIBLE);
        }

        obtenerChips();

        return send;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager = null;
            ubicacion = null;
        }
        binding = null;
    }


}
