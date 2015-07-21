package com.nomapp.nomapp_beta;

/**
 * Created by antonid on 20.07.2015.
 */import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class MyArrayAdapter extends ArrayAdapter<String> {

    private static final String TABLE_NAME = "Ingridients";
    private static final String INGRIDIENT_ID = "_id";
    private static final String INGRIDIENT_NAME = "name";
    private static final String IS_CHECKED = "checked";

    private final Context context;
  ///  private final String[] values;
    private final ArrayList<String> forIngridients;

    public MyArrayAdapter(Context context, ArrayList<String> forIngridients) {
        super(context, R.layout.adding_ingridients_listview, forIngridients);
        this.context = context;
        this.forIngridients = forIngridients;
        //this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.adding_ingridients_listview, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(forIngridients.get(position));

        Cursor cursor =  Database.getDatabase().getIngridients().query(TABLE_NAME,
                new String[]
                        {INGRIDIENT_ID, INGRIDIENT_NAME, IS_CHECKED},
                null, null, null, null
                , null);

        cursor.moveToFirst();
        cursor.moveToPosition(position);
        int isChecked = cursor.getInt(2);
        if (isChecked == 1) {
            textView.setBackgroundColor(context.getResources().getColor(R.color.chosenElement)); // ������ �������
          //  textView.setTextColor(context.getResources().getColor(R.color.white));
            Log.d("MY_TAG", "in arrayAdapter");
        }/* else{
            textView.setBackgroundColor(context.getResources().getColor(R.color.white)); // ������ �������
            Log.d("MY_TAG", "in arrayAdapter");
        }*/
        return rowView;
    }


}
