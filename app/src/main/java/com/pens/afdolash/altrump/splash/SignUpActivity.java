package com.pens.afdolash.altrump.splash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pens.afdolash.altrump.R;
import com.pens.afdolash.altrump.model.Users;
import com.pens.afdolash.altrump.navbar.MainActivity;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference db;
    EditText Namadepan, Email, Telepon, Password;
    String fname, emails, telp, passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        Namadepan = findViewById(R.id.et_namadepan);
        Email = findViewById(R.id.et_emailregister);
        Telepon = findViewById(R.id.et_telpon);
        Password = findViewById(R.id.et_passwordregister);
        TextView tv_masukdisini = findViewById(R.id.tv_masukdisini);
        RelativeLayout bt_daftar = findViewById(R.id.bt_daftar);

        tv_masukdisini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emails = Email.getText().toString();
                passwd = Password.getText().toString();
                fname = Namadepan.getText().toString();
                telp = Telepon.getText().toString();

                if (checkData(Namadepan, Email, Telepon, Password)){

                    auth.createUserWithEmailAndPassword(emails, passwd)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (!task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                                    }else {

                                        Users i = new Users(emails, fname, passwd, telp);

                                        System.out.println("email, fname, passwd, telp : " + emails + " " +  fname + " " + passwd + " " + telp);

                                        db = FirebaseDatabase.getInstance().getReference("users");
                                        String key = db.push().getKey();
                                        db.child(key).setValue(i);

                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });
                } else {
                    Toast toast = Toast.makeText(SignUpActivity.this, "You must enter all the field properly to Register", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public static boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();

        return !TextUtils.isEmpty(str);
    }

    boolean checkData(EditText a, EditText b, EditText c, EditText d) {
        return isEmpty(a) && isEmpty(b) && isEmpty(c) && isEmpty(d) && isEmail(b);
    }

    private void writeNewUsers(String fname, String email, String telepon, String password){
        Users users = new Users(fname, email, telepon, password);

        String leaf = db.getKey();

    }
}
