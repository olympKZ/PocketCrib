package com.ruteam.pocketcrib;
//RUteam
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.ruteam.pocketcrib.models.User;
import com.ruteam.pocketcrib.utils.Common;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText edtName, edtPassword;
    MaterialButton btnLogin;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle(getString(R.string.login));

        edtName = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_pass);
        btnLogin = findViewById(R.id.btn_login);
        progressBar= findViewById(R.id.progressbarr);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                if (Common.isNetworkAvailable(LoginActivity.this)) {
                login(name, password);
                }else{
                    //Toast.makeText(LoginActivity.this, "Check your network connection", Toast.LENGTH_SHORT).show();
                    ////Toast.makeText(LoginActivity.this, getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(R.id.login), R.string.check_connection,
                            Snackbar.LENGTH_SHORT)
                            .show();

                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void login(String name, String password) {

        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    users.add(user);
                    if (user.getUserName().equals(name) && user.getPassword().equals(password)) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        progressBar.setVisibility(View.GONE);
                        return;
                    } else {
                        //Toast.makeText(LoginActivity.this, "Invalid username and password", Toast.LENGTH_SHORT).show();
                        //Toast.makeText(LoginActivity.this, getString(R.string.invalid_login), Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(R.id.login), R.string.invalid_login,
                                Snackbar.LENGTH_SHORT)
                                .show();

                        progressBar.setVisibility(View.GONE);
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    boolean InputTest(String n, String pass){
        if(n.equals(" ") || !n.contains("n") || n.length()<9)  // checking for  proper student id
            return false;

        if(pass.equals("")) // checking if password field entered
            return false;

        return true;

    }

}