package com.example.medicalpharma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

public class SignUpActivity extends AppCompatActivity {

    RelativeLayout layout;

    MaterialEditText phoneNum, name, password;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        layout = findViewById(R.id.signUpLayout);

        phoneNum = (MaterialEditText) findViewById(R.id.signUpPhoneNum);
        name = (MaterialEditText) findViewById(R.id.signUpName);
        password = (MaterialEditText) findViewById(R.id.signUpPassword);
        signUp = (Button) findViewById(R.id.signUpBtn);

        //Firebase conn
        FirebaseDatabase dtb = FirebaseDatabase.getInstance();
        DatabaseReference users = dtb.getReference("User");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dialog = new ProgressDialog(SignUpActivity.this);
                dialog.setMessage("Ju lutem prisni.");
                dialog.show();

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        //Kontrollo nese numri i telefonit eshte ne rregull per rajonin e Kosoves dhe fillon me 04
                    if (phoneNum.getText().toString().trim().length() == 9 && phoneNum.getText().toString().trim().contains("#") == false
                            && phoneNum.getText().toString().trim().contains(".") == false && phoneNum.getText().toString().trim().charAt(0)=='0'
                            && phoneNum.getText().toString().trim().charAt(1)=='4')
                        {
                        //Kontrollo nese numri pastaj mbaron me 3,4,5,8,9
                        if (phoneNum.getText().toString().trim().charAt(2)=='3' || phoneNum.getText().toString().trim().charAt(2)=='4'
                                || phoneNum.getText().toString().trim().charAt(2)=='5'|| phoneNum.getText().toString().trim().charAt(2)=='8'
                                || phoneNum.getText().toString().trim().charAt(2)=='9')
                            {
                                //Check if user exists already
                                if (snapshot.child(phoneNum.getText().toString()).exists()) {
                                    dialog.dismiss();
                                    Toast.makeText(SignUpActivity.this, "This phone number is already " +
                                            "registered with another user!", Toast.LENGTH_LONG).show();
                                } else {
                                    dialog.dismiss();
                                    getDatabaseUsers user = new getDatabaseUsers(name.getText().toString(), password.getText().toString());
                                    users.child(phoneNum.getText().toString()).setValue(user);
                                    Toast.makeText(SignUpActivity.this, "Numri u regjistrua me sukses!!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                        } else {
                        dialog.dismiss();
                        Snackbar.make(layout, "KUJDES! Numri i telefonit nuk eshte ne rregull!!!", Snackbar.LENGTH_LONG)
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