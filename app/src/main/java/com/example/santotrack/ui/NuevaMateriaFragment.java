package com.example.santotrack.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.santotrack.MainActivity;
import com.example.santotrack.R;
import com.example.santotrack.auth.SessionManager;
import com.example.santotrack.db.AsignaturaDao;

/**
 * Fragmento para el registro de nuevas asignaturas asociadas al usuario en sesión.
 *
 * Reglas de negocio:
 * - Si no hay texto en el campo de nombre, el botón de agregar no se habilitará/funcionará.
 * - Permite seleccionar un color identificador entre las opciones disponibles.
 * - Guarda la asignatura en SQLite con id_usuario, id_color, nombre y nota_aprobacion.
 * - Al guardar o cancelar, retorna a la vista general de asignaturas.
 */
public class NuevaMateriaFragment extends Fragment {

    private EditText etNombre;
    private Button btnGuardar;
    private Button btnCancelar;

    private FrameLayout[] layoutColors = new FrameLayout[5];
    private ImageView[] ivChecks = new ImageView[5];
    private int selectedColorId = 1; // Por defecto Color 1 (Azul)

    private AsignaturaDao asignaturaDao;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nueva_materia, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        asignaturaDao = new AsignaturaDao(requireContext());
        sessionManager = new SessionManager(requireContext());

        initViews(view);
        setupColorPickers();
        setupValidationAndListeners();
    }

    private void initViews(View view) {
        etNombre = view.findViewById(R.id.et_nombre_asignatura);
        btnGuardar = view.findViewById(R.id.btn_guardar_asignatura);
        btnCancelar = view.findViewById(R.id.btn_cancelar_asignatura);

        layoutColors[0] = view.findViewById(R.id.layout_color_1);
        layoutColors[1] = view.findViewById(R.id.layout_color_2);
        layoutColors[2] = view.findViewById(R.id.layout_color_3);
        layoutColors[3] = view.findViewById(R.id.layout_color_4);
        layoutColors[4] = view.findViewById(R.id.layout_color_5);

        ivChecks[0] = view.findViewById(R.id.iv_check_1);
        ivChecks[1] = view.findViewById(R.id.iv_check_2);
        ivChecks[2] = view.findViewById(R.id.iv_check_3);
        ivChecks[3] = view.findViewById(R.id.iv_check_4);
        ivChecks[4] = view.findViewById(R.id.iv_check_5);
    }

    private void setupColorPickers() {
        for (int i = 0; i < layoutColors.length; i++) {
            final int colorId = i + 1;
            final int index = i;
            if (layoutColors[i] != null) {
                layoutColors[i].setOnClickListener(v -> selectColor(colorId, index));
            }
        }
    }

    private void selectColor(int colorId, int index) {
        selectedColorId = colorId;
        for (int i = 0; i < ivChecks.length; i++) {
            if (ivChecks[i] != null) {
                ivChecks[i].setVisibility(i == index ? View.VISIBLE : View.GONE);
            }
        }
    }

    private void setupValidationAndListeners() {
        // Inicialmente el botón está deshabilitado si no hay texto
        btnGuardar.setEnabled(false);

        etNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s != null ? s.toString().trim() : "";
                btnGuardar.setEnabled(!text.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnGuardar.setOnClickListener(v -> guardarAsignatura());

        btnCancelar.setOnClickListener(v -> volverAAsignaturas());
    }

    private void guardarAsignatura() {
        String nombre = etNombre.getText() != null ? etNombre.getText().toString().trim() : "";

        // Regla: si no hay texto, el botón no funcionará
        if (nombre.isEmpty()) {
            Toast.makeText(getContext(), R.string.subject_error_empty_name, Toast.LENGTH_SHORT).show();
            return;
        }

        int userId = sessionManager.getUserId();
        if (userId <= 0) {
            Toast.makeText(getContext(), "Error: Sesión no válida", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar en la base de datos SQLite
        long result = asignaturaDao.insertar(userId, selectedColorId, nombre, 4.0);

        if (result > 0) {
            Toast.makeText(getContext(), R.string.subject_saved_success, Toast.LENGTH_SHORT).show();
            etNombre.setText("");
            volverAAsignaturas();
        } else {
            Toast.makeText(getContext(), "Error al guardar la asignatura", Toast.LENGTH_SHORT).show();
        }
    }

    private void volverAAsignaturas() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToTab(R.id.nav_asignaturas);
        }
    }
}
