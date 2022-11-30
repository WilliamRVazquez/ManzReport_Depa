package com.example.manzreport_depa;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class perfil extends Fragment {
    TextView fullName, email, phone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId,newPassword;
    Button resetPassLocal, changeProfileImage, buttonsalir,sireset,noreset;
    FirebaseUser user;
    Dialog dialog;
    EditText Nombre,Email,Phone, resetPassword;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);

        dialog =new Dialog(this.getContext());
        resetPassword = new EditText(this.getContext());
        sireset = new Button(this.getContext());
        noreset = new Button(this.getContext());
        phone = root.findViewById(R.id.profilePhone);
        fullName = root.findViewById(R.id.profileName);
        email = root.findViewById(R.id.profileEmail);
        resetPassLocal = root.findViewById(R.id.resetPasswordLocal);
        buttonsalir = root.findViewById(R.id.button);
        changeProfileImage = root.findViewById(R.id.changeProfile);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        buttonsalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogcerrarsesion();
            }
        });



        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getContext(), login.class));
            onStop();
        }//sacar usuario

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this.getContext().getMainExecutor(), new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));

                } else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });


        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Resetiar Contraseña ?");
                passwordResetDialog.setMessage("Ingresa Nueva Contraseña > 6 Caracteres Minimo.");
                passwordResetDialog.setView(resetPassword);


                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(perfil.this.getContext(), "Reseteo de Contraseña Completo.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(perfil.this.getContext(), "Error al Cambiar la Contraseña.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
                Nombre = dialog.findViewById(R.id.Name);
                Email = dialog.findViewById(R.id.Email);
                Phone = dialog.findViewById(R.id.Phone);
                fStore.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@androidx.annotation.Nullable DocumentSnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if(value.exists()){
                            Nombre.setText(value.getString("fName"));
                            Email.setText(value.getString("email"));
                            Phone.setText(value.getString("phone"));
                        }
                    }
                });
            }
        });
        // Inflate the layout for this fragment
        return root;
    }

    private void openDialogcerrarsesion() {
        dialog.setContentView(R.layout.cerrarseion);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.setCancelable(false);
        Button sicerrar = dialog.findViewById(R.id.btn_yescerrar);
        Button nocerrar = dialog.findViewById(R.id.btn_nocerrar);

        sicerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(perfil.this.getContext(), login.class));
            }
        });

        nocerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();


    }

    private void openDialog() {
        dialog.setContentView(R.layout.cus);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        ImageView imageViewclose = dialog.findViewById(R.id.imageclose2);
        Button btnsave = dialog.findViewById(R.id.btn_yes);
        Button btnno = dialog.findViewById(R.id.btn_no);
        //EditText Nombre = dialog.findViewById(R.id.Name);


        imageViewclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Toast.makeText(perfil.this.getContext(), "Cerrado", Toast.LENGTH_SHORT).show();
            }
        });
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Nombre.getText().toString().isEmpty() || Email.getText().toString().isEmpty() || Phone.getText().toString().isEmpty()){
                    Toast.makeText(perfil.this.getContext(), "Uno o varios campos estan vacios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String email = Email.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("email",email);
                        edited.put("fName",Nombre.getText().toString());
                        edited.put("phone",Phone.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(perfil.this.getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
                                //onBackPressed();
                                dialog.dismiss();
                                return;
                            }
                        });
                        //Toast.makeText(EditProfile.this, "Perfil cambiado.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(perfil.this.getContext(),   e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        btnno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                Toast.makeText(perfil.this.getContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = fAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(perfil.this.getContext(), Principal.class));
        }//sacar usuario
    }
}