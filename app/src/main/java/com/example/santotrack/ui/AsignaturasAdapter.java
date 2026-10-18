package com.example.santotrack.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.santotrack.R;
import com.example.santotrack.model.Asignatura;
import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador de RecyclerView para mostrar las asignaturas creadas por el usuario.
 */
public class AsignaturasAdapter extends RecyclerView.Adapter<AsignaturasAdapter.AsignaturaViewHolder> {

    public interface OnDeleteClickListener {
        void onDelete(Asignatura asignatura);
    }

    private List<Asignatura> asignaturas = new ArrayList<>();
    private final OnDeleteClickListener deleteClickListener;

    public AsignaturasAdapter(OnDeleteClickListener deleteClickListener) {
        this.deleteClickListener = deleteClickListener;
    }

    public void setAsignaturas(List<Asignatura> lista) {
        this.asignaturas = lista != null ? lista : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AsignaturaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asignatura, parent, false);
        return new AsignaturaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AsignaturaViewHolder holder, int position) {
        Asignatura item = asignaturas.get(position);
        holder.bind(item, deleteClickListener);
    }

    @Override
    public int getItemCount() {
        return asignaturas.size();
    }

    static class AsignaturaViewHolder extends RecyclerView.ViewHolder {
        private final View viewColor;
        private final TextView tvNombre;
        private final TextView tvNota;
        private final ImageButton btnDelete;

        public AsignaturaViewHolder(@NonNull View itemView) {
            super(itemView);
            viewColor = itemView.findViewById(R.id.view_item_color);
            tvNombre = itemView.findViewById(R.id.tv_item_nombre);
            tvNota = itemView.findViewById(R.id.tv_item_nota);
            btnDelete = itemView.findViewById(R.id.btn_item_delete);
        }

        public void bind(Asignatura asignatura, OnDeleteClickListener listener) {
            tvNombre.setText(asignatura.getNombre());
            tvNota.setText(String.format("Nota de aprobación: %.1f", asignatura.getNotaAprobacion()));

            try {
                int parsedColor = Color.parseColor(asignatura.getColorHex());
                viewColor.setBackgroundColor(parsedColor);
            } catch (Exception e) {
                viewColor.setBackgroundColor(Color.parseColor("#3525CD"));
            }

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDelete(asignatura);
                }
            });
        }
    }
}
