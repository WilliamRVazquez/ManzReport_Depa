package com.example.manzreport_depa;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class login extends AppCompatActivity {
    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn,forgotTextLink;
    ProgressBar progressBar;
    FirebaseFirestore mFirestore;
    String id;
    String rolesitos;
    FirebaseUser user;



    FirebaseAuth fAuth;
    int REQUEST_CODE = 200;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        fAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        verificarPermisos();


        mEmail = findViewById(R.id.LogIn_Correo);
        mPassword = findViewById(R.id.LogIn_Contraseña);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.button_sesion);
        mCreateBtn = findViewById(R.id.txtv_register_and_btn);
        forgotTextLink = findViewById(R.id.Click_aqui_contraseña);



        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Correo requerido");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Contraseña requerida",null);
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("la contraseña debe tener >= 6 caracteres",null);
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseFirestore mFirestore;
                            mFirestore = FirebaseFirestore.getInstance();
                            fAuth = FirebaseAuth.getInstance();
                            user = fAuth.getCurrentUser();
                            if (!user.isEmailVerified()) {
                                Toast.makeText(login.this, "Debes de verificar el correo", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                fAuth.signOut();

                            }else if (user.isEmailVerified()){
                                mFirestore.collection("users").whereEqualTo("Id", fAuth.getCurrentUser().getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        String rol = document.getString("Rol");
                                                        if(rol.equals("1")){
                                                            progressBar.setVisibility(View.GONE);
                                                            fAuth.signOut();
                                                            Toast.makeText(login.this, "No perteneces a ningun departamento", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(login.this, "Logeo completado!", Toast.LENGTH_SHORT).show();
                                                            Intent intent1 = new Intent (login.this, MainActivity.class);
                                                            Bundle data1 = new Bundle();
                                                            data1.putString("ROL",rolesitos);
                                                            intent1.putExtras(data1);
                                                            startActivity(intent1);
                                                        }

                                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                                    }
                                                } else {
                                                    Log.w(TAG, "Error getting documents.", task.getException());
                                                }
                                            }
                                        });
                            }
                        }else {
                            Toast.makeText(login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Sing_up.class));
            }
        });

        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Resetear contraseña");
                passwordResetDialog.setMessage("Escribe tu correo para enviar un link para reseteo contraseña");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this, "Link Enviado al Correo Ingresado.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "Error ! El Link no Pudo ser Enviado" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });


    }
    public void onBackPressed() {
        moveTaskToBack(true); finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();
        if (user != null){
            if(user.isEmailVerified()){
                startActivity(new Intent(login.this, MainActivity.class));
            }
        }
    }
    // apartado para verificar permisos y pedirlos
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        int  permiso_location_precisa = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if( permiso_location_precisa == PackageManager.PERMISSION_GRANTED){
            //metodo de mandar mensajes
            //Toast.makeText(this, "Consedido", Toast.LENGTH_SHORT).show();
        }else{
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        }
    }
}