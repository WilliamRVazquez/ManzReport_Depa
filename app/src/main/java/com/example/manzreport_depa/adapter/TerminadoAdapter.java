package com.example.manzreport_depa.adapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import com.example.manzreport_depa.Detalle_Reportes;
import com.example.manzreport_depa.R;
import com.example.manzreport_depa.model.terminado;
import com.example.manzreport_depa.ubicacion_reporte;

public class TerminadoAdapter extends FirestoreRecyclerAdapter<terminado, TerminadoAdapter.ViewHolder>{
    Activity activity;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TerminadoAdapter(@NonNull FirestoreRecyclerOptions<terminado> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int position, @NonNull terminado terminado) {
        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id = documentSnapshot.getId();

        viewHolder.tititulo.setText(terminado.getTitulo());//
        viewHolder.ubicacion.setText(terminado.getUbicacion());
        viewHolder.date.setText(terminado.getDate().toDate().toString());


        viewHolder.btn_detalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, Detalle_Reportes.class);
                i.putExtra("id_Reportes", id);
                activity.startActivity(i);
            }
        });
        viewHolder.btn_ubicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ubicacion_reporte.class);
                i.putExtra("id_Ubicacion", id);
                activity.startActivity(i);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.terminado_single, parent, false);

        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tititulo, ubicacion, date;
        ImageView btn_detalles,btn_ubicacion;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tititulo = itemView.findViewById(R.id.titulo);
            ubicacion = itemView.findViewById(R.id.ubicacion);
            date = itemView.findViewById(R.id.date);
            btn_detalles = itemView.findViewById(R.id.btn_detail);
            btn_ubicacion = itemView.findViewById(R.id.btn_location);


        }
    }
}
