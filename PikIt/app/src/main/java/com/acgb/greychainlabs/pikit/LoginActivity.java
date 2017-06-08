package com.acgb.greychainlabs.pikit;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btn_Login;
    private Button btn_Register;
    private EditText et_Email;
    private EditText et_Password;
    private ProgressDialog pDialog;

    private LinearLayout Profile_section;
    private SignInButton Google_Signin;
    private Button SignOut_Button;
    private TextView Name,Email;
    private ImageView Profile_pic;
    private GoogleApiClient googleApiClient;
    private static final int REQUEST_CODE = 9001;

    public static String email_user;


    LoginButton loginbutton;
    CallbackManager callbackManager;
    TextView textView;

    DBHelper userDB= new DBHelper(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        Profile_section = (LinearLayout) findViewById(R.id.profile_section);
        SignOut_Button = (Button) findViewById(R.id.google_logout);
        Google_Signin = (SignInButton)findViewById(R.id.google_login);
        Name = (TextView) findViewById(R.id.name);
        Email = (TextView) findViewById(R.id.email_id);
        Profile_pic = (ImageView) findViewById(R.id.profile_pic);
        Profile_section.setVisibility(View.GONE);
        Google_Signin.setOnClickListener(this);
        SignOut_Button.setOnClickListener(this);
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


//        Google_Signin.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//
//                sign_in();
//            }
//            });

        loginbutton = (LoginButton) findViewById(R.id.fb_login);
        callbackManager = CallbackManager.Factory.create();
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                textView.setText("Login Success \n" + loginResult.getAccessToken().getUserId()+"\n"+loginResult.getAccessToken().getToken());
                Intent intent = new Intent(LoginActivity.this,MainActivityOnPikIt.class);
                startActivity(intent);


            }

            @Override
            public void onCancel() {
                textView.setText("Login Cancelled");

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



        et_Email = (EditText) findViewById(R.id.email);
        et_Password = (EditText) findViewById(R.id.password);
        btn_Login = (Button) findViewById(R.id.btnLogin);
        btn_Register = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // Login button Click Event
        btn_Login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String emailStr = et_Email.getText().toString().trim();
                String passwordStr = et_Password.getText().toString().trim();


                //String password_1= db.searchPass(emailStr,passwordStr);
                boolean b= userDB.verifyLogin(emailStr,passwordStr);
                if (b){

                    email_user = emailStr;

                    et_Email.setText("");
                    et_Password.setText("");

                    Toast t1= Toast.makeText(LoginActivity.this,"Successful login",Toast.LENGTH_SHORT);
                    t1.show();


                    Intent intent = new Intent(LoginActivity.this,MainActivityOnPikIt.class);
                    intent.putExtra("email", email_user);
                    startActivity(intent);
                }
                else{
                    et_Email.setText("");
                    et_Password.setText("");
                    Toast t2= Toast.makeText(LoginActivity.this,"Password and username don't match",Toast.LENGTH_SHORT);
                    t2.show();
                }


            }

        });

        // Link to Register Screen
        btn_Register.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode,resultCode,data);

        if (requestCode==REQUEST_CODE){

            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handle_result(result);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.google_login:
                sign_in();
                break;
            case R.id.google_logout:
                sign_out();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void sign_in(){

        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQUEST_CODE);

    }

    private void sign_out(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void handle_result(GoogleSignInResult result){

        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            String imgURL = account.getPhotoUrl().toString();
            Name.setText(name);
            Email.setText(email);
            Glide.with(this).load(imgURL).into(Profile_pic);
            updateUI(true);
        }
        else{
            updateUI(false);
        }
    }

    private void updateUI(boolean isLogin){

        if (isLogin){
            Profile_section.setVisibility(View.VISIBLE);
            Google_Signin.setVisibility(View.GONE);
        }else{
            Profile_section.setVisibility(View.GONE);
            Google_Signin.setVisibility(View.VISIBLE);
        }

    }

    public String retuenMail(){
        return email_user;
    }

}
