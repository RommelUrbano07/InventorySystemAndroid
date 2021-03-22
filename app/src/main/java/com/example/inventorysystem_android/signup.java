package com.example.inventorysystem_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class signup extends AppCompatActivity {
    private EditText username, password,firstname,middlename,lastname,suffix;
    DBAdapter helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        username = (EditText) findViewById(R.id.Username);
        password = (EditText) findViewById(R.id.Password);
        firstname= (EditText) findViewById(R.id.FirstName);
        middlename= (EditText) findViewById(R.id.MiddleName);
        lastname= (EditText) findViewById(R.id.LastName);
        suffix= (EditText) findViewById(R.id.Suffix);
        Button back = (Button) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent i = new Intent(signup.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        helper = new DBAdapter(this);

        final Activity activity = this;
        activity.setTitle("Sign Up");

    }

    public void clearfields(View view){

        username.setText("");
        password.setText("");
        firstname.setText("");
        middlename.setText("");
        lastname.setText("");
        suffix.setText("");
    }

    public void addUser(View view)
    {
        String r1 = username.getText().toString();
        String r2= password.getText().toString();
        String r3=firstname.getText().toString();
        String r4=middlename.getText().toString();
        String r5=lastname.getText().toString();
        String r6=suffix.getText().toString();

        if(r1.isEmpty() || r2.isEmpty() || r3.isEmpty() || r4.isEmpty() || r5.isEmpty() || r6.isEmpty())
        {
            Message.message(getApplicationContext(),"Fill all required fields");
        }
        else
        {
            long id = helper.insertData(r1,r2,r3,r4,r5,r6);
            if(id<=0)
            {
                Message.message(getApplicationContext(),"Insertion Unsuccessful");

            } else
            {
                Message.message(getApplicationContext(),"Insertion Successful");
            }
            username.setText("");
            password.setText("");
            firstname.setText("");
            middlename.setText("");
            lastname.setText("");
            suffix.setText("");
        }
    }
}