package com.example.manzreport_depa;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();

        Toolbar mToolbar= (Toolbar) findViewById(R.id.tolbar);
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        getActionBar().setTitle("Reportes");
        mToolbar.inflateMenu(R.menu.overflow);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.rpPendiente) {
                    Intent i = new Intent(MainActivity.this, reportes_pendientes.class);
                    startActivity(i);
                } else if (item.getItemId() == R.id.rpTerminados) {
                    Intent i = new Intent(MainActivity.this, reportes_terminados.class);
                    startActivity(i);
                } else if(item.getItemId() == R.id.cerrarSesion){
                    fAuth.signOut();
                    startActivity(new Intent(MainActivity.this, login.class));
                }
                return false;
            }
        });
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());

    }
    //Primer metodo con su objeto.
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow, menu);
        return true;
    }
}