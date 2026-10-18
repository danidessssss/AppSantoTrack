package com.example.santotrack.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.santotrack.MainActivity;
import com.example.santotrack.R;
import com.example.santotrack.auth.SessionManager;
import com.example.santotrack.db.AsignaturaDao;
import com.example.santotrack.model.Asignatura;
import java.util.List;

/**
 * Vista general de asignaturas del usuario autenticado.
 *
 * Comportamiento:
 * - Carga únicamente las asignaturas pertenecientes al usuario en sesión.
 * - Si no tiene asignaturas, muestra el estado vacío original ("Aún no tienes asignaturas").
 * - Si tiene asignaturas, oculta el estado vacío y muestra la lista de ramos del usuario.
 * - Permite eliminar asignaturas mediante diálogo de confirmación.
 */
public class AsignaturasFragment extends Fragment {

    private LinearLayout layoutEmptyState;
    private LinearLayout layoutContentState;
    private TextView tvHeaderCount;
    private RecyclerView rvAsignaturas;
    private AsignaturasAdapter adapter;

    private AsignaturaDao asignaturaDao;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_asignaturas, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        asignaturaDao = new AsignaturaDao(requireContext());
        sessionManager = new SessionManager(requireContext());

        layoutEmptyState = view.findViewById(R.id.layout_empty_state);
        layoutContentState = view.findViewById(R.id.layout_content_state);
        tvHeaderCount = view.findViewById(R.id.tv_header_count);
        rvAsignaturas = view.findViewById(R.id.rv_asignaturas);

        Button btnEmptyAdd = view.findViewById(R.id.btn_empty_add_materia);
        View btnContentAdd = view.findViewById(R.id.btn_content_add_materia);

        // Configuración de navegación para agregar asignaturas
        View.OnClickListener goToAddSubjectListener = v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToTab(R.id.nav_nueva_materia);
            }
        };

        if (btnEmptyAdd != null) {
            btnEmptyAdd.setOnClickListener(goToAddSubjectListener);
        }
        if (btnContentAdd != null) {
            btnContentAdd.setOnClickListener(goToAddSubjectListener);
        }

        // Configuración del RecyclerView
        rvAsignaturas.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AsignaturasAdapter(this::mostrarDialogoEliminar);
        rvAsignaturas.setAdapter(adapter);

        cargarAsignaturas();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar al regresar al fragmento para reflejar cambios inmediatos
        cargarAsignaturas();
    }

    private void cargarAsignaturas() {
        if (!isAdded()) return;

        int userId = sessionManager.getUserId();
        if (userId <= 0) return;

        List<Asignatura> lista = asignaturaDao.obtenerPorUsuario(userId);

        if (lista.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            layoutContentState.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            layoutContentState.setVisibility(View.VISIBLE);
            tvHeaderCount.setText(getString(R.string.my_subjects_subtitle, lista.size()));
            adapter.setAsignaturas(lista);
        }
    }

    private void mostrarDialogoEliminar(Asignatura asignatura) {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.dialog_delete_subject_title)
                .setMessage(getString(R.string.dialog_delete_subject_msg, asignatura.getNombre()))
                .setPositiveButton(R.string.action_delete, (dialog, which) -> {
                    int userId = sessionManager.getUserId();
                    boolean eliminada = asignaturaDao.eliminar(asignatura.getIdAsignatura(), userId);
                    if (eliminada) {
                        Toast.makeText(requireContext(), R.string.subject_deleted_success, Toast.LENGTH_SHORT).show();
                        cargarAsignaturas();
                    } else {
                        Toast.makeText(requireContext(), "No fue posible eliminar la asignatura", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show();
    }
}
