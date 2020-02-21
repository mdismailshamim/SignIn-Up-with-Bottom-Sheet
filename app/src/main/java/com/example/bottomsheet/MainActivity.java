package com.example.bottomsheet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private Button loginButton,registerButton;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseDatabase = FirebaseDatabase.getInstance();
        loginButton = findViewById(R.id.signInBtn);
        registerButton = findViewById(R.id.signUpBtn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.signin_bottom_sheet);
                bottomSheetDialog.setCanceledOnTouchOutside(false);
                final EditText phoneNumberEt,passwordEt;
                Button login;

                phoneNumberEt = bottomSheetDialog.findViewById(R.id.phoneNumberEt);
                passwordEt = bottomSheetDialog.findViewById(R.id.passwordEt);
                login = bottomSheetDialog.findViewById(R.id.logInBtn);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //String phoneNumber = phoneNumberEt.getText().toString();
                        //String password = passwordEt.getText().toString();
                        //checkToDB(new User(phoneNumber,password));
                        if (phoneNumberEt.getText().toString().equals("1234" )&& passwordEt.getText().toString().equals("1234")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setTitle("Login Success");
                            builder.setMessage("Welcome back");
                            builder.setNegativeButton("Done", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                        else {
                            Toast.makeText(v.getContext(), "Login failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                bottomSheetDialog.show();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.signup_bottom_sheet);
                bottomSheetDialog.setCanceledOnTouchOutside(false);

                final EditText nameEt,et_phoneNumber,et_password;
                Button register;
                nameEt = bottomSheetDialog.findViewById(R.id.nameEt);
                et_phoneNumber = bottomSheetDialog.findViewById(R.id.et_phoneNumber);
                et_password = bottomSheetDialog.findViewById(R.id.et_password);
                register = bottomSheetDialog.findViewById(R.id.btnRegister);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name= nameEt.getText().toString();
                        String phoneNumber = et_phoneNumber.getText().toString();
                        String password = et_password.getText().toString();
                        saveToDB(new User(name,phoneNumber,password));
                    }
                });
                bottomSheetDialog.show();

            }
        });

    }

    private void checkToDB(final User user) {
        final DatabaseReference userDB = firebaseDatabase.getReference().child(user.getUserId());
        userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String phoneNumber= dataSnapshot.child("phoneNumber").getValue().toString();
                String password = dataSnapshot.child("password").getValue().toString();
                Toast.makeText(getApplicationContext(),phoneNumber + "/n" +password,Toast.LENGTH_SHORT);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Database error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void saveToDB(User user){
        DatabaseReference userDB =firebaseDatabase.getReference().child("userList");
        String userid = userDB.push().getKey();
        user.setUserId(userid);
        userDB.child(userid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Successfully saved", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
