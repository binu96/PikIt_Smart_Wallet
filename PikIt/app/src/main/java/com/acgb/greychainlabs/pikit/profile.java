package com.acgb.greychainlabs.pikit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.acgb.greychainlabs.pikit.LoginActivity;


/**
 * Created by Binu Senevirathne on 6/8/2017.
 */

public class profile extends LoginActivity {

    private String SELECT_SQL = "SELECT * FROM register_table WHERE email =";
    private SQLiteDatabase db;
    private Cursor c;

    private TextView editTextName;
    private TextView editTextEmail;
    private Button logout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();

        LoginActivity loginActivity = new LoginActivity();
        String email = loginActivity.email_user;

        SELECT_SQL = "SELECT * FROM register_table where email = '" +email + "'";

        openDatabase();

        editTextName = (TextView) findViewById(R.id.editTextName);
        editTextEmail = (TextView) findViewById(R.id.editTextEmail);
        logout  = (Button) findViewById(R.id.butLogout);

        logout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(profile.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();
        showRecords();
    }
    protected void openDatabase() {

        db = openOrCreateDatabase("Register.db", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {
//        String id = c.getString(1);
        String name = c.getString(1);
        String add = c.getString(2);
        editTextName.setText(name);
        editTextEmail.setText(add);


    }

}
