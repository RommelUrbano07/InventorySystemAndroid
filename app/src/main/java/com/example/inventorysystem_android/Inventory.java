package com.example.inventorysystem_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Inventory extends AppCompatActivity {

    DrawerLayout dLayout;
    DBAdapter helper;
    TableLayout inventory_items;
    Button add;
    Button edit;
    Button delete;
    Button withdraw;
    String ItemName_val="empty";
    String ItemQuantity_val="empty";
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        inventory_items = (TableLayout) findViewById(R.id.item_view);

        Bundle extras = getIntent().getExtras();
        value="sample";
        if (extras != null) {
            value = extras.getString("accountID");
        }
        final Activity activity = this;
        activity.setTitle("Inventory");
        setNavigationDrawer();

        add= (Button) findViewById(R.id.addItem);
        edit= (Button) findViewById(R.id.editItem);
        delete= (Button) findViewById(R.id.deleteItem);
        withdraw= (Button) findViewById(R.id.subractItem);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Inventory.this, Add.class);
                i.putExtra("accountID", value);
                startActivity(i);
                finish();
            }
        });

        helper = new DBAdapter(this);

        String account_name= helper.getUserFirstName(value);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.nav_header_textView);
        navUsername.setText("Hello "+account_name+"! Welcome back!");

        generateItems(value);

    }

    private void generateItems(String ID){
        Cursor cursor = helper.getItemOnID(ID);
        while(cursor.moveToNext()){
            TableRow temp = new TableRow(this);
            temp.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            temp.setBackgroundResource(R.drawable.border);
            temp.setWeightSum(1);
            temp.setGravity(Gravity.FILL);
            TableRow.LayoutParams layout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            layout.setMargins(5,5,5,5);
            TextView ItemName =  new TextView(this);
            ItemName.setLayoutParams(layout);
            TextView ItemQuantity =  new TextView(this);
            ItemQuantity.setLayoutParams(layout);

            ItemName.setText(cursor.getString(cursor.getColumnIndex("ItemName")));
            ItemQuantity.setText(cursor.getString(cursor.getColumnIndex("ItemQuantity")));

            temp.addView(ItemName);
            temp.addView(ItemQuantity);
            inventory_items.addView(temp);
        }
    }

    public void deleteItem(View view){
        Intent i = new Intent(Inventory.this,Delete.class);
        i.putExtra("accountID",value);
        startActivity(i);
        finish();
    }

    public void updateItem(View view){
        Intent i = new Intent(Inventory.this,Edit.class);
        i.putExtra("accountID",value);
        startActivity(i);
        finish();
    }

    public void withdraw(View view){
        Intent i = new Intent(Inventory.this,Withdraw.class);
        i.putExtra("accountID",value);
        startActivity(i);
        finish();
    }

    private void setNavigationDrawer() {
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout); // initiate a DrawerLayout
        NavigationView navView = (NavigationView) findViewById(R.id.navigation); // initiate a Navigation View
        // implement setNavigationItemSelectedListener event on NavigationView
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Fragment frag = null; // create a Fragment Object
                int itemId = menuItem.getItemId(); // get selected menu item's id
                // check selected menu item's id and replace a Fragment Accordingly
                if (itemId == R.id.first) {
                    Intent i = new Intent(Inventory.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                // display a toast message with menu item's title
                Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}