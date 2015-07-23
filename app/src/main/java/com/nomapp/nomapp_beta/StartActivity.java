package com.nomapp.nomapp_beta;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class StartActivity extends Activity implements View.OnClickListener {

    private static final String TABLE_NAME = "Ingridients";

    ListView selectedIngridients;

    Button addIngridients;
    Button showSelectedIngridients;

    ArrayList<String> forSelectedIngridients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        selectedIngridients = (ListView) findViewById(R.id.selectedList);

        addIngridients = (Button) findViewById(R.id.addIngBtn);
        addIngridients.setOnClickListener(this);
        showSelectedIngridients = (Button) findViewById(R.id.show);
        showSelectedIngridients.setOnClickListener(this);

        FloatingActionButton fabButton = new FloatingActionButton.Builder(this)
                .withDrawable(getResources().getDrawable(R.drawable.ieon))
                .withButtonColor(Color.WHITE)
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.setOnClickListener(onCircleButtonCliclListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addIngBtn:
                Intent intent = new Intent(StartActivity.this, AddIngridientsActivity.class);
                startActivity(intent);
                break;
            case R.id.show:
                fillSelectedIngridients();
                setUpList();
                break;
            default:

                  break;
        }
    }

    View.OnClickListener onCircleButtonCliclListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(StartActivity.this, AddIngridientsActivity.class);
            startActivity(intent);
        }
    };

    void setUpList(){
        selectedIngridients.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, forSelectedIngridients));
    }

    private void fillSelectedIngridients() {
        forSelectedIngridients = new ArrayList<String>();
        Cursor cursor =  Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {Database.getIngridientId(), Database.getIngridientName(),
                                Database.getIngridientIsChecked()},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                if(cursor.getInt(2) != 0) {
                    String name = cursor.getString(1);
                    forSelectedIngridients.add(name);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

}