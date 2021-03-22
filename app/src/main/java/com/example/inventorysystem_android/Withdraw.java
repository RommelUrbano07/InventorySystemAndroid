package com.example.inventorysystem_android;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Withdraw extends AppCompatActivity {
    Button back_inventory_withdraw;
    Button withdraw_item;
    Spinner spinner_withdraw;
    String value;
    DBAdapter helper;
    String val="";
    EditText quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.withdraw_item);
        helper = new DBAdapter(this);
        back_inventory_withdraw = (Button) findViewById(R.id.back_inventory_withdraw);
        withdraw_item= (Button) findViewById(R.id.withdraw_item);
        spinner_withdraw = (Spinner) findViewById(R.id.spinner_withdraw);
        quantity = (EditText) findViewById(R.id.quantity_withdraw);
        ArrayList<String> temp = new ArrayList<String>();

        Bundle extras = getIntent().getExtras();
        value="sample";
        if (extras != null) {
            value = extras.getString("accountID");
        }

        Cursor cursor = helper.getItemOnID(value);
        while(cursor.moveToNext()){
            temp.add(cursor.getString(cursor.getColumnIndex("ItemName")));
        }

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, temp);
        //set the spinners adapter to the previously created one.
        spinner_withdraw.setAdapter(adapter);

        back_inventory_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Withdraw.this, Inventory.class);
                i.putExtra("accountID", value);
                startActivity(i);
                finish();
            }
        });

        try{
            spinner_withdraw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    val=spinner_withdraw.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });

            withdraw_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(helper.subtractItem(value,val,quantity.getText().toString())>-1){
                        Toast.makeText(getApplicationContext(),"Withdraw Item SUCCESS", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Withdraw.this, Withdraw.class);
                        i.putExtra("accountID", value);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Withdraw Item FAILED", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No item to withdraw!", Toast.LENGTH_LONG).show();
        }
    }
}
