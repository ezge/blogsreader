package com.ezge.blogsreader;

// LoginActivity class validates credentials

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    String email;
    String password;
    Button loginButton;
    EditText passwordText;
    EditText emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        setTitle( R.string.labelLoginActivity );

        loginButton = (Button) findViewById( R.id.btn_login );
        passwordText = (EditText) findViewById( R.id.input_password );
        emailText = (EditText) findViewById( R.id.input_email );

        if (!isOnline()){
            Toast.makeText( getBaseContext(), "No internet connection!", Toast.LENGTH_LONG ).show();
            finish();
        }

        loginButton.setOnClickListener( new OnClickListener() {

            @Override
            public void onClick(View v) { login(); }});
    }

    public void login() {

        if (!validate()) {
            Toast.makeText( getBaseContext(), "Login failed", Toast.LENGTH_LONG ).show();
            return;
        }

        onLogin();

    }

    private void onLogin(){
        Credentials credentials = null;
        JSONfunctions json = null;
        boolean login = false;
        int responseCode;

        try{
            credentials = new Credentials();
            credentials.setEmail( email );
            credentials.setPassword( password );

            json = new JSONfunctions( getBaseContext() );
            responseCode = json.postJSONCredentials( getString( R.string.login_url ), credentials );

            if (responseCode == 200) {
                Intent intent = new Intent( LoginActivity.this, BlogListActivity.class );
                startActivity( intent );
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean validate() {
        boolean valid = true;
        SpannableStringBuilder spannable = null;


        email = emailText.getText().toString().trim();
        password = passwordText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher( email ).matches()) {
            spannable = setErrorColor( "enter a valid email" );
            emailText.setError(spannable );
            emailText.requestFocus();
            valid = false;
        } else {
            emailText.setError( null );
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            spannable = setErrorColor( "enter a valid password" );
            passwordText.setError( spannable );
            passwordText.requestFocus();
            valid = false;
        } else {
            emailText.setError( null );
        }


        return valid;
    }

    private SpannableStringBuilder setErrorColor(String message){
        int errorColor;
        final int version = Build.VERSION.SDK_INT;
        SpannableStringBuilder spannableStringBuilder = null;

        if (version > 22) {
            errorColor = ContextCompat.getColor(getApplicationContext(), R.color.white);
        } else {
            errorColor = getResources().getColor(R.color.white);
        }

        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(errorColor);

        spannableStringBuilder = new SpannableStringBuilder(message);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, message.length(), 0);

        return spannableStringBuilder;

    }

    private boolean isOnline() {
        boolean wifi = false;
        boolean mobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = cm.getAllNetworkInfo();

        for (NetworkInfo nf : networkInfo) {
            if (nf.getTypeName().equalsIgnoreCase("WIFI"))
                if (nf.isConnected())
                    wifi = true;
            if (nf.getTypeName().equalsIgnoreCase("MOBILE"))
                if (nf.isConnected())
                    mobile = true;
        }
        return wifi || mobile;
    }
}