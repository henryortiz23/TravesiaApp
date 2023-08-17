package com.softs.hn.ip.travesiaapp.ui.inicio;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.softs.hn.ip.travesiaapp.R;
import com.softs.hn.ip.travesiaapp.databinding.FragmentInicioBinding;
import com.softs.hn.ip.travesiaapp.entity.Contacto;
import com.softs.hn.ip.travesiaapp.entity.Nota;
import com.softs.hn.ip.travesiaapp.ui.acompaniantes.AcompaniantesViewModel;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InicioFragment extends Fragment implements OnItemClickListenerNotas<Nota> {

    private NotasAdapter adapter;
    private InicioViewModel viewModel;
    private FragmentInicioBinding binding;

    private StaggeredGridLayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(InicioViewModel.class);

        adapter = new NotasAdapter(new ArrayList<>(), this);

        viewModel.getDataSet().observe(getViewLifecycleOwner(), notas -> {
            adapter.setItems(notas);
            if(adapter.getItemCount()>0){
                binding.imgBack.setVisibility(View.GONE);
            }else{
                binding.imgBack.setVisibility(View.VISIBLE);
            }
        });


        binding.bFab.setOnClickListener(v -> {
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

    private void nuevaNota() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_notas);

    }

    private void DialogConfirm(Nota nota) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);
        TextView tvMsg = dialog.findViewById(R.id.tvMsg);
        String msg = getString(R.string.eliminar_nota) + " \"" + nota.getLugar() + "\"?";
        tvMsg.setText(msg);

        btnAceptar.setOnClickListener(v -> {
            viewModel.delete(nota);
            dialog.dismiss();
        });
        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }


    private void DialogShare(Nota nota) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog_shared);


        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

        RadioGroup rG = dialog.findViewById(R.id.rgAcompaniantes);
        Button btnMsm = dialog.findViewById(R.id.btnSMS);
        Button btnWatsapp = dialog.findViewById(R.id.btnWhatsapp);
        Button btnCorreo = dialog.findViewById(R.id.btnCorreo);
        MaterialButtonToggleGroup mbtg = dialog.findViewById(R.id.gShareOptions);
        Button btnCompartir = dialog.findViewById(R.id.btnCompatir);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);


        String a = nota.getAcompaniante();
        List<Contacto> lContactos;
        if (!"".equals(a)) {
            lContactos = new ArrayList<>();
            AcompaniantesViewModel viewModelAcompaniantes = new ViewModelProvider(this).get(AcompaniantesViewModel.class);

            String[] a_ids = a.split("\n");
            List<String> list_ids = new ArrayList<>(Arrays.asList(a_ids));

            for (String numero : list_ids) {
                if (!"".equals(numero)) {
                    LiveData<Contacto> contacto;
                    contacto = viewModelAcompaniantes.getDataContactoId(Integer.valueOf(numero));
                    if (contacto != null) {
                        contacto.observe(getViewLifecycleOwner(), contactoId -> {
                            if (contactoId != null) {
                                if (!lContactos.contains(contactoId)) {
                                    lContactos.add(contactoId);
                                    RadioButton r = new RadioButton(getContext());
                                    r.setTag(contactoId);
                                    r.setText(contactoId.getName());

                                    r.setOnClickListener(v -> {
                                        if (v.getTag() != null) {
                                            Contacto cTag = (Contacto) v.getTag();
                                            mbtg.clearChecked();
                                            btnCompartir.setEnabled(false);

                                            btnMsm.setVisibility(View.GONE);
                                            btnWatsapp.setVisibility(View.GONE);
                                            btnCorreo.setVisibility(View.GONE);

                                            if (!"".equals(cTag.getPhone())) {
                                                btnMsm.setVisibility(View.VISIBLE);
                                                btnWatsapp.setVisibility(View.VISIBLE);
                                            }

                                            if (!"".equals(cTag.getEmail())) {
                                                btnCorreo.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });

                                    rG.addView(r);
                                }
                            }
                        });
                    }
                }
            }
        }

        mbtg.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            btnCompartir.setEnabled(true);
        });

        btnCompartir.setOnClickListener(v -> {
            int bSelect = mbtg.getCheckedButtonId();
            RadioButton rb = dialog.findViewById(rG.getCheckedRadioButtonId());
            Contacto con = (Contacto) rb.getTag();

            if (dialog.findViewById(bSelect) == btnMsm) {
                sharedSms(nota, con.getPhone());

            } else if (dialog.findViewById(bSelect) == btnWatsapp) {
                sharedWhatsapp(nota, con.getPhone());
            } else {
                sharedEmail(nota, con.getEmail());
            }

            dialog.dismiss();
        });
        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();

    }

    private void sharedWhatsapp(Nota nota, String numero) {
        numero = numero.replace("+", "");
        String mensaje = "Lugar: " + nota.getLugar() + "\n" +
                "Fecha: " + nota.getFecha() + "\n" +
                "Comentario: " + nota.getComentario() + "\n" +
                "Ubicacion: " + nota.getLatitud() + "," + nota.getLongitud();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(mensaje, "UTF-8");
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse(url));
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocurrio un error al compartir con la app de whatsapp", Toast.LENGTH_SHORT).show();
        }
    }

    public void sharedSms(Nota nota, String numero) {
        numero = numero.replace("+", "");
        String mensaje = "Lugar: " + nota.getLugar() + "\n" +
                "Fecha: " + nota.getFecha() + "\n" +
                "Comentario: " + nota.getComentario() + "\n" +
                "Ubicacion: " + nota.getLatitud() + ", " + nota.getLongitud();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("smsto:" + numero));

            intent.putExtra("sms_body", mensaje);//Agrego el mensaje

            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocurrio un error al compartir con la app de SMS", Toast.LENGTH_SHORT).show();
        }
    }


    public void sharedEmail(Nota nota, String destinatario) {
        String asunto= "Compartir nota";
        String mensaje = "Lugar: " + nota.getLugar() + "\n" +
                "Fecha: " + nota.getFecha() + "\n" +
                "Comentario: " + nota.getComentario() + "\n" +
                "Ubicacion: " + nota.getLatitud() + ", " + nota.getLongitud();

        try {
            String uriText =
                    "mailto:"+destinatario +
                            "?subject=" + Uri.encode(asunto) +
                            "&body=" + Uri.encode(mensaje);
            Uri uri = Uri.parse(uriText);

            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(uri);
            startActivity(Intent.createChooser(sendIntent, "Compartir nota"));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocurrio un error al compartir con la app de correo", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Nota data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("nota", data);
        bundle.putInt("imagenId", data.getImg());
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_notas, bundle);
    }

    @Override
    public void onItemClickDelete(Nota data) {
        DialogConfirm(data);
    }

    @Override
    public void onItemClickShared(Nota data) {
        DialogShare(data);
    }
}