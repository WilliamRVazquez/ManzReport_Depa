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

public class reportes_pendientes extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportes_pendientes);

        Toolbar mToolbar= (Toolbar) findViewById(R.id.tolbar);
        setActionBar(mToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);
        getActionBar().setTitle("Reportes Pendientes");
        mToolbar.inflateMenu(R.menu.overflow);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.rpPendiente) {

                } else if (item.getItemId() == R.id.rpTerminados) {
                    Intent i = new Intent(reportes_pendientes.this, reportes_terminados.class);
                    startActivity(i);
                    finish();
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