package com.example.medicalpharma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.medicalpharma.databaseUsers.getDatabaseUsers;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LogInActivity extends AppCompatActivity {

    RelativeLayout layout;

    EditText phoneNum, password;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        layout = findViewById(R.id.layout);

        phoneNum = (MaterialEditText) findViewById(R.id.phoneNum);
        password = (MaterialEditText) findViewById(R.id.password);

        loginBtn = (Button) findViewById(R.id.loginCheckbtn);

        //Firebase conn
        FirebaseDatabase dtb = FirebaseDatabase.getInstance();
        DatabaseReference users = dtb.getReference("User");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(LogInActivity.this);
                dialog.setMessage("Ju lutem prisni.");
                dialog.show();

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        //Kontrollo nese KEY input field (telefoni) eshte zbrazet (shmang error)
                        if(phoneNum.getText().toString().trim().length() > 0 && phoneNum.getText().toString().trim().contains("#") == false
                            && phoneNum.getText().toString().trim().contains(".") == false){
                            //Kontrollo egzistencen e userit
                            if(snapshot.child(phoneNum.getText().toString()).exists()) {
                                //Info te userit nga databaza
                                dialog.dismiss();
                                getDatabaseUsers getUser = snapshot.child(phoneNum.getText().toString()).getValue(getDatabaseUsers.class);
                                if (getUser.getPassword().equals(password.getText().toString()))
                                {
                                    Toast.makeText(LogInActivity.this, "Te dhenat jane ne rregull!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LogInActivity.this, "Te dhenat nuk jane te sakta!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
                                Toast.makeText(LogInActivity.this, "Hmmm... useri nuk egziston!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dialog.dismiss();
                            Snackbar.make(layout, "KUJDES! Numri i telefonit nuk duhet te jete i zbrazet, te permbaj simbolin # ose piken!!!", Snackbar.LENGTH_LONG)
                                    .setAction("Kuptoj", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //fokusohu mbi input field te telefonit
                                            phoneNum.requestFocus();
                                            //hape keyboard
                                            InputMethodManager phoneKeyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            phoneKeyboard.showSoftInput(phoneNum, InputMethodManager.SHOW_FORCED);
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {

                    }
                });
            }
        });
    }
}