package com.example.grocerystoreoffers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MyAdapter extends ArrayAdapter<StateVO> {

    Offers offer = new Offers();
    private Context mContext;
    private ArrayList<StateVO> listState;
    private MyAdapter myAdapter;
    private boolean isFromView = false;

    public MyAdapter(Context context, int resource, List<StateVO> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<StateVO>) objects;
        this.myAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == 0)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }
        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                Log.d("IN THIS BITCH","TJENA");
                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                }
                if(!isChecked){
                    if (holder.mTextView.getText().equals("Vegetarian")) {
                        offer.getInstance().vegetarian = false;
                        offer.getInstance().filterCategory();
                    }
                     if (holder.mTextView.getText().equals("Vegan")) {
                        offer.getInstance().vegan = false;
                        offer.getInstance().filterCategory();
                    }
                     if (holder.mTextView.getText().equals("Meat, Poultry & Fish")) {
                        offer.getInstance().meat = false;
                        offer.getInstance().filterCategory();
                    }
                     if (holder.mTextView.getText().equals("Fruit & Vegetables")) {
                        offer.getInstance().fruit = false;
                        offer.getInstance().filterCategory();
                    }
                     if (holder.mTextView.getText().equals("Dairy, Cheese & Eggs")) {
                        offer.getInstance().dairy = false;
                        offer.getInstance().filterCategory();
                    }
                     if (holder.mTextView.getText().equals("Drink")) {
                        offer.getInstance().drink = false;
                        offer.getInstance().filterCategory();
                    }
                     if (holder.mTextView.getText().equals("Ice Cream, Sweets & Snacks")) {
                        offer.getInstance().ice = false;
                        offer.getInstance().filterCategory();
                    }
                     if (holder.mTextView.getText().equals("Bread & Cookies")) {
                        offer.getInstance().bread = false;
                        offer.getInstance().filterCategory();
                    }

                }
                else {
                    if (holder.mTextView.getText().equals("Vegetarian")) {
                        offer.getInstance().vegetarian = true;
                        offer.getInstance().filterCategory();
                    }
                    else if (holder.mTextView.getText().equals("Vegan")) {
                        offer.getInstance().vegan = true;
                        offer.getInstance().filterCategory();
                    }
                    else if (holder.mTextView.getText().equals("Meat, Poultry & Fish")) {
                        offer.getInstance().meat = true;
                        offer.getInstance().filterCategory();
                    }
                    else if (holder.mTextView.getText().equals("Fruit & Vegetables")) {
                        offer.getInstance().fruit = true;
                        offer.getInstance().filterCategory();
                    }
                    else if (holder.mTextView.getText().equals("Dairy, Cheese & Eggs")) {
                        offer.getInstance().dairy = true;
                        offer.getInstance().filterCategory();
                    }
                    else if (holder.mTextView.getText().equals("Drink")) {
                        offer.getInstance().drink = true;
                        offer.getInstance().filterCategory();
                    }
                    else if (holder.mTextView.getText().equals("Ice Cream, Sweets & Snacks")) {
                        offer.getInstance().ice = true;
                        offer.getInstance().filterCategory();
                    }
                    else if (holder.mTextView.getText().equals("Bread & Cookies")) {
                        offer.getInstance().bread = true;
                        offer.getInstance().filterCategory();
                    }
                }
            }
        });
        if(!holder.mTextView.getText().equals("Select Categories")){
            holder.mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.mCheckBox.toggle();
                }
            });
        }


        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}
