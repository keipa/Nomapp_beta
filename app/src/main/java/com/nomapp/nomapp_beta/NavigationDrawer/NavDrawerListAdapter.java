package com.nomapp.nomapp_beta.NavigationDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomapp.nomapp_beta.R;

/**
 * Created by antonid on 29.10.2015.
 */
public class NavDrawerListAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
   // ArrayList<Product> objects;
    //Data for out NavDrawer
    String[] items = {"�����������", "��� �������"/*,  "�������� �����",
    "�������"*/};

    int currentPosition;

    int[] images = {R.drawable.ic_local_dining_grey_18dp, R.drawable.ic_import_contacts_grey_18dp};
    int[] selImages = {R.drawable.ic_local_dining_orange_18dp,R.drawable.ic_import_contacts_orange_18dp};
    public NavDrawerListAdapter(Context context, int currentPosition) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentPosition = currentPosition;
    }

    public NavDrawerListAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        currentPosition = 100; //any incredible number
    }

    // ���-�� ���������
    @Override
    public int getCount() {
        return items.length;
    }

    // ������� �� �������
    @Override
    public Object getItem(int position) {
        return items[position];
    }

    // id �� �������
    @Override
    public long getItemId(int position) {
        return position;
    }

    // ����� ������
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ���������� ���������, �� �� ������������ view
        View view = convertView;
      //  TextView text = (TextView)convertView.findViewById(R.id.navdrawer_name_of_item);
        if (view == null){


            switch (position){
                case 0:
                     view = lInflater.inflate(R.layout.navdrawer_item, parent, false);
                    ((TextView) view.findViewById(R.id.navdrawer_name_of_item)).setText(items[position]);
                    ((ImageView) view.findViewById(R.id.navdrawer_item_icon)).
                            setImageResource(images[position]);
                    break;
                case 1:
                     view = lInflater.inflate(R.layout.navdrawer_item_with_separator, parent, false);
                    ((TextView) view.findViewById(R.id.navdrawer_name_of_item)).setText(items[position]);
                    ((ImageView) view.findViewById(R.id.navdrawer_item_icon)).
                            setImageResource(images[position]);
                    break;
                default:
                     view = lInflater.inflate(R.layout.navdrawer_additional_item, parent, false);
                    ((TextView) view.findViewById(R.id.navdrawer_name_of_additional_item)).setText(items[position]);
                    break;
            }
            if (position == currentPosition) {
                try {
                    ((ImageView) view.findViewById(R.id.navdrawer_item_icon)).
                            setImageResource(selImages[currentPosition]);
                    ((TextView) view.findViewById(R.id.navdrawer_name_of_item)).setTextColor(ctx.getResources().getColor(R.color.chosenElement));
                     view.setBackgroundColor(ctx.getResources().getColor(R.color.grey));
                } catch (Throwable t) {
                    ((TextView) view.findViewById(R.id.navdrawer_name_of_additional_item)).
                            setTextColor(ctx.getResources().getColor(R.color.chosenElement));
                    view.setBackgroundColor(ctx.getResources().getColor(R.color.grey));

                }
            }
        }

        return view;
    }

}
