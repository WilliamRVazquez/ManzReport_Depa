package com.example.manzreport_depa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manzreport_depa.adapter.ReportAdapter;
import com.example.manzreport_depa.model.Report;
import com.example.manzreport_depa.model.user;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class reportesPendientes extends Fragment {
    public static final String TAG = "TAG";
    RecyclerView mRecycler;
    ReportAdapter mAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Query query;
    user rolesitos;
    String correo_e;
    //Bundle args = getArguments();
    //String rol = args.getString("user", "nulo");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        View root = inflater.inflate(R.layout.fragment_reportes_pendientes, container, false);
        SharedPreferences prefs = getActivity().getSharedPreferences("Preferences", 0);
        correo_e = prefs.getString("users", "");
        mRecycler =root.findViewById(R.id.recyclerViewSingle);
        if(correo_e.equals("2")){
            query = mFirestore.collection("Reportes").whereEqualTo("tiporeporte","Capdam").whereEqualTo("Aceptado", "Si");
            setUpRecyclerView();
        }else if(correo_e.equals("3")){
            query = mFirestore.collection("Reportes").whereEqualTo("tiporeporte","Proteccion civil").whereEqualTo("Aceptado", "Si");
            setUpRecyclerView();
        }else if(correo_e.equals("4")){
            query = mFirestore.collection("Reportes").whereEqualTo("tiporeporte","Jardineria").whereEqualTo("Aceptado", "Si");
            setUpRecyclerView();
        }else if(correo_e.equals("5")){
            query = mFirestore.collection("Reportes").whereEqualTo("tiporeporte","Mantenimiento publico").whereEqualTo("Aceptado", "Si");
            setUpRecyclerView();
        }


        // Inflate the layout for this fragment
        return root;
    }



    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {
        mRecycler.setLayoutManager(new WrapContentLinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL,false));
        //query = mFirestore.collection("Reportes").whereEqualTo("tiporeporte","Capdam").whereEqualTo("Aceptado", "Si");


        FirestoreRecyclerOptions<Report> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Report>().setQuery(query, Report.class).build();

        mAdapter = new ReportAdapter(firestoreRecyclerOptions, this.getActivity());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);
    }
    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
    public static class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public WrapContentLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TAG", "meet a IOOBE in RecyclerView");
            }
        }
    }

}