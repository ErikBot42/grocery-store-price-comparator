package com.example.grocerystoreoffers;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<Product> {

    ArrayList<Product> products;
    Context context;
    int resource;

    public CustomListAdapter(Context context, int resource, ArrayList<Product> products) {
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
            convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);
        }
        Product product = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewProduct);
        if(product.getImage().isEmpty()){
            Picasso.with(context).load(R.drawable.ic_baseline_person_24);
        }
        else {
            Picasso.with(context).load(product.getImage()).into(imageView);
        }

        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        txtName.setText(product.getName());

        TextView txtPriceKg = (TextView) convertView.findViewById(R.id.txtPriceKg);
        if(product.getPricekg().equals("-1.0") && product.getPriceL().equals("-1.0")){
            txtPriceKg.setText("Pris/kg/l: N/A");
        }
        else if(product.getPricekg().equals("-1.0")){
            txtPriceKg.setText("Pris/l: "+product.getPriceL());
        }
        else{
            txtPriceKg.setText("Pris/kg: "+product.getPricekg());
        }

        TextView txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
        if(!product.getPrice().equals("-1.0"))  {
            txtPrice.setText("Pris: "+product.getPrice()+" :-");
        }
        else{
            txtPrice.setText("Pris: Se kilopris");
        }

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