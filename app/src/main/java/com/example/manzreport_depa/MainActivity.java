package com.example.manzreport_depa;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.manzreport_depa.model.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore mFirestore;
    Query query;
    user users;
    String id;
    String roles = "0";
    String rol = "0";


    reportesTerminados  reportesTerminados = new reportesTerminados();
    reportesPendientes reportesPendientes = new reportesPendientes();

    perfil perfil = new perfil();



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        id = fAuth.getCurrentUser().getUid();
        mFirestore.collection("users").whereEqualTo("Id", fAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getString("Rol"));
                                //esto es para obtener en un querySnapshot algo especifico de un documento en string y asi no obtener el data
                                roles = document.getString("Rol");
                                Toast.makeText(MainActivity.this, roles, Toast.LENGTH_SHORT).show();
                                SharedPreferences prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("users", roles);
                                editor.commit();



                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        Intent intentReceived = getIntent();
        Bundle data = intentReceived.getExtras();
        if(data != null){
            rol = data.getString("ROL");
        }else{
            rol = "";
        }


        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        getActionBar().setTitle("Reportes deparatamentales");
        mToolbar.inflateMenu(R.menu.overflow);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.closeSesion){
                    fAuth.signOut();
                    startActivity(new Intent(MainActivity.this, login.class));
                }
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

        BottomNavigationView navegacion = findViewById(R.id.bottom_navigation);
        navegacion.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,reportesTerminados).commit();
        //loadFragment(reportesTerminados);



    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.rTerminado_fragment:
                    //loadFragment(reportesTerminados);

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,reportesTerminados).commit();
                    return true;
                case R.id.rPendiente_fragment:
                    //loadFragment(reportesPendientes);
                    //Bundle b = new Bundle();
                    //b.putString("users",roles);

                    //reportesPendientes.setArguments(b);

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,reportesPendientes).commit();

                    return true;
                case R.id.user_fragment:
                    //loadFragment(perfil);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,perfil).commit();

                    return true;

            }
            return false;
        }
    };

    public void loadFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container,fragment);
        transaction.commit();
    }
}