package com.example.manzreport_depa;
import android.content.Intent;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Sing_up extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mFullName,mEmail,mPassword,mPhone,Register_codeempresa;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;


    // Contraseña de 8-20 caracteres que requiere números y letras de ambos casos
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z]).{8,20}$";

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        mFullName   = findViewById(R.id.Register_Nombre_Completo);
        mEmail      = findViewById(R.id.Register_Correo);
        mPassword   = findViewById(R.id.Register_Contraseña);
        mPhone      = findViewById(R.id.Register_telefono);
        mRegisterBtn= findViewById(R.id.button_Register);
        mLoginBtn   = findViewById(R.id.txtv_inisesion_btn);
        Register_codeempresa = findViewById(R.id.Register_codeempresa);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                return;
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);



        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            //codes: capdam = C002,proteccioncivil = P003,
            // jardineria = J004,Mantenimiento Publico = M005
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String[] rol = new String[1];
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();
                final EditText Codee = findViewById(R.id.Register_codeempresa);
                String code = Codee.getText().toString();


                if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Requiere Nombre");
                    mFullName.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(email)){
                    mEmail.setError("Requiere Correo");
                    mEmail.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(password)){
                    mPassword.setError("Requiere Contraseña",null);
                    mPassword.requestFocus();
                    return;
                }else if(password.length() < 8){
                    mPassword.setError("La contraseña debe ser mayor a 8 caracteres y contener una Mayuscula y una Minuscula",null);
                    mPassword.requestFocus();
                    return;
                }else if(TextUtils.isEmpty(phone)){
                    mPhone.setError("Requiere Telefono");
                    mPhone.requestFocus();
                    return;
                }else if (TextUtils.isEmpty(code)){
                    Register_codeempresa.setError("Requiere Codido de Empresa");
                    Register_codeempresa.requestFocus();
                }else if(PASSWORD_PATTERN.matcher(password).matches()){
                    progressBar.setVisibility(View.VISIBLE);

                    // register the user in firebase
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(code.equals("C002")) {
                                task.isSuccessful();
                                rol[0] = "2";
                                FirebaseUser fuser = fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Sing_up.this, "Se ha enviado una Verificacion a tu correo, aceptalo para poder ingresar", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });
                                Toast.makeText(Sing_up.this, "Capdam", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                String id = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Id", id);
                                user.put("fName", fullName);
                                user.put("email", email);
                                user.put("phone", phone);
                                user.put("Rol", rol[0]);
                                user.put("password",(password));
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), login.class));
                                finish();

                            }else if(code.equals("P003")){
                                task.isSuccessful();
                                rol[0] = "3";
                                FirebaseUser fuser = fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Sing_up.this, "Se ha enviado una Verificacion a tu correo, aceptalo para poder ingresar", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });

                                Toast.makeText(Sing_up.this, "Proteccion Civil", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                String id = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Id", id);
                                user.put("fName", fullName);
                                user.put("email", email);
                                user.put("phone", phone);
                                user.put("Rol", rol[0]);
                                user.put("password",(password));
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), login.class));
                                finish();





                            }else if(code.equals("J004")){
                                task.isSuccessful();
                                rol[0] = "4";
                                FirebaseUser fuser = fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Sing_up.this, "Se ha enviado una Verificacion a tu correo, aceptalo para poder ingresar", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });


                                Toast.makeText(Sing_up.this, "Jardineria", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                String id = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Id", id);
                                user.put("fName", fullName);
                                user.put("email", email);
                                user.put("phone", phone);
                                user.put("Rol", rol[0]);
                                user.put("password",(password));
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), login.class));
                                finish();


                            }else if(code.equals("M005")){
                                task.isSuccessful();
                                rol[0] = "5";
                                FirebaseUser fuser = fAuth.getCurrentUser();
                                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Sing_up.this, "Se ha enviado una Verificacion a tu correo, aceptalo para poder ingresar", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                    }
                                });


                                Toast.makeText(Sing_up.this, "Mantenimiento Publico", Toast.LENGTH_SHORT).show();
                                userID = fAuth.getCurrentUser().getUid();
                                String id = fAuth.getCurrentUser().getUid();
                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Id", id);
                                user.put("fName", fullName);
                                user.put("email", email);
                                user.put("phone", phone);
                                user.put("Rol", rol[0]);
                                user.put("password",(password));
                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "onSuccess: user Profile is created for " + userID);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: " + e.toString());
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(), login.class));
                                finish();



                            } else {
                                Toast.makeText(Sing_up.this, "Error ! El Correo ya esta registrado", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }else{
                    mPassword.setError("La contraseña no cumple con los requerimientos.");
                    mPassword.requestFocus();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        //FirebaseUser user = fAuth.getCurrentUser();
        //if (user != null){
        //   startActivity(new Intent(Sing_up.this, MainActivity.class));
        //}//sacar usuario
    }

}