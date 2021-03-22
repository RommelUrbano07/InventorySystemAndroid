package com.example.inventorysystem_android;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Delete extends AppCompatActivity {
    String value;
    DBAdapter helper;
    Button delete_item;
    Button backbutton;
    String val="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_item);

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner);
        //create a list of items for the spinner.
        ArrayList <String> temp = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();
        delete_item = (Button) findViewById(R.id.delete_item);
        backbutton = (Button) findViewById(R.id.back_inventory_delete);
        value="sample";
        if (extras != null) {
            value = extras.getString("accountID");
        }

        helper = new DBAdapter(this);

        Cursor cursor = helper.getItemOnID(value);
        while(cursor.moveToNext()){
            temp.add(cursor.getString(cursor.getColumnIndex("ItemName")));
        }

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, temp);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Delete.this, Inventory.class);
                i.putExtra("accountID", value);
                startActivity(i);
                finish();
            }
        });

        try{
            dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    val=dropdown.getSelectedItem().toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // TODO Auto-generated method stub
                }
            });

            delete_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(helper.delete(value,val)>-1){
                        Toast.makeText(getApplicationContext(),"Deleting Item SUCCESS", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Delete.this, Delete.class);
                        i.putExtra("accountID", value);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),"Deleting Item FAILED", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No item to delete!", Toast.LENGTH_LONG).show();
        }
    }
}
