package com.example.manzreport_depa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;

    reportesTerminados reportesTerminados = new reportesTerminados();
    reportesPendientes reportesPendientes = new reportesPendientes();
    perfil perfil = new perfil();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();

        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar);
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        getActionBar().setTitle("Reportes");
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
        loadFragment(reportesTerminados);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.rTerminado_fragment:
                    loadFragment(reportesTerminados);
                    return true;
                case R.id.rPendiente_fragment:
                    loadFragment(reportesPendientes);
                    return true;
                case R.id.user_fragment:
                    loadFragment(perfil);
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