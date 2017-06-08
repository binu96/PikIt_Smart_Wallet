package com.acgb.greychainlabs.pikit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btn_Register;
    private Button btn_Login;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;

    DBHelper userDB= new DBHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btn_Register = (Button) findViewById(R.id.btnRegister);
        btn_Login = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Register Button Click event
        btn_Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String nameStr = inputFullName.getText().toString().trim();
                String emailStr = inputEmail.getText().toString().trim();
                String passwordStr = inputPassword.getText().toString().trim();

                if (!nameStr.isEmpty() && !emailStr.isEmpty() && !passwordStr.isEmpty()) {
                    //registerUser(name, email, password);


                    User u = new User();
                    u.setFullname(nameStr);
                    u.setEmail(emailStr);
                    u.setPassword(passwordStr);
                    boolean check= userDB.insertRegisterData(u);
                    if (check) {
                        Toast.makeText(getApplicationContext(),
                                "Registration successful!", Toast.LENGTH_LONG)
                                .show();
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Registration unsuccessful! Please try again!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });



        // Link to Login Screen
        btn_Login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
