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

public class Edit extends AppCompatActivity {
    Button back_inventory_edit;
    Button edit_item;
    Spinner spinner_edit;
    String value;
    DBAdapter helper;
    String val="";
    EditText quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);
        helper = new DBAdapter(this);
        back_inventory_edit = (Button) findViewById(R.id.back_inventory_edit);
        edit_item= (Button) findViewById(R.id.edit_item);
        spinner_edit = (Spinner) findViewById(R.id.spinner_edit);
        quantity = (EditText) findViewById(R.id.quantity_edit);
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
        spinner_edit.setAdapter(adapter);

        back_inventory_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Edit.this, Inventory.class);
                i.putExtra("accountID", value);
                startActivity(i);
                finish();
            }
        });

        try{
            spinner_edit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    val=spinner_edit.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });

            edit_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(helper.updateItem(value,val,quantity.getText().toString())>-1){
                        Toast.makeText(getApplicationContext(),"Edit Item SUCCESS", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Edit.this, Edit.class);
                        i.putExtra("accountID", value);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Edit Item FAILED", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No item to Edit!", Toast.LENGTH_LONG).show();
        }
    }
}
