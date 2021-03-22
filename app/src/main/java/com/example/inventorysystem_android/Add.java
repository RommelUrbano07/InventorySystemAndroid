package com.example.inventorysystem_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Add  extends AppCompatActivity {

    Button add_item;
    Button backbutton;
    DBAdapter helper;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        add_item = (Button) findViewById(R.id.add_item);
        backbutton = (Button) findViewById(R.id.back_inventory_delete);
        helper = new DBAdapter(this);
        EditText name = (EditText) findViewById(R.id.item_name_add);
        EditText number = (EditText) findViewById(R.id.item_quantity_add);
        Bundle extras = getIntent().getExtras();
        value="sample";
        if (extras != null) {
            value = extras.getString("accountID");
        }

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Add.this, Inventory.class);
                i.putExtra("accountID", value);
                startActivity(i);
                finish();
            }
        });

        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(helper.insertItemData(value,name.getText().toString(),number.getText().toString())>-1){
                    Toast.makeText(getApplicationContext(),"Adding Item SUCCESS", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Adding Item FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
