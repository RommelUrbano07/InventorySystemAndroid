package com.example.inventorysystem_android;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DBAdapter helper;
    private Button loginButton;
    private Button signup;
    private EditText username;
    private EditText password;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton= (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signup= (Button) findViewById(R.id.signup);

        helper = new DBAdapter(this);

        final Activity activity = this;
        activity.setTitle("Log In");

        signup.setOnClickListener(new View.OnClickListener(){
                  @Override
                  public void onClick (View v){
                      Intent i = new Intent(MainActivity.this, signup.class);
                      startActivity(i);
                      finish();
                  }
            });
    }

    public void loginTrigger(View view){
        int ID = checkIfAccountExists(username.getText().toString() , password.getText().toString());
        if( ID > 0 ){
            Intent i = new Intent(MainActivity.this, Inventory.class);
            i.putExtra("accountID",ID+"");
            startActivity(i);
            finish();
        }else{
            Toast.makeText(getApplicationContext(),"Credentials does not match!",Toast.LENGTH_LONG).show();
        }
    }

    public int checkIfAccountExists(String username, String password){
        int cond = helper.getUserData(username,password);
        return cond;
    }


}