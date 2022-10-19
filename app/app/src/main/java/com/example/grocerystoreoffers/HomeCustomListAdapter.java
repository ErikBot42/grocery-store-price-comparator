package com.example.grocerystoreoffers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeCustomListAdapter extends ArrayAdapter<Product> {

    ArrayList<Product> products;
    Context context;
    int resource;

    public HomeCustomListAdapter(Context context, int resource, ArrayList<Product> products) {
        super(context, resource, products);
        this.products = products;
        this.context = context;
        this.resource = resource;
    }

    public void setFilteredList(ArrayList<Product> filteredList)    {
        this.products = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)    {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.home_custom_list_layout, null, true);
        }
        Product product = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewProduct);
        Picasso.with(context).load(product.getImage()).into(imageView);

        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        txtName.setText(product.getName());

        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        txtPrice.setText(product.getPrice()+" :-");

        TextView txtStore = (TextView) convertView.findViewById(R.id.txtStore);
        if (product.getStore().equals("1"))    {
            txtStore.setText("Lidl");
        }   else if (product.getStore().equals("2"))  {
            txtStore.setText("Coop Konsum Kronoparken");
        }   else if (product.getStore().equals("3"))  {
            txtStore.setText("Maxi ICA Stormarknad Bergvik Karlstad");
        }   else if (product.getStore().equals("4"))  {
            txtStore.setText("Willys Karlstad");
        }

        return convertView;
    }

}
