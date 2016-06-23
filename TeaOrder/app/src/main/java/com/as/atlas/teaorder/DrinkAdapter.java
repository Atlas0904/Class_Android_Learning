package com.as.atlas.teaorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atlas on 2016/6/13.
 */
public class DrinkAdapter extends BaseAdapter {

    List<Drink> drinks;
    LayoutInflater inflater;

    public DrinkAdapter(Context context, List<Drink> drinks) {
        this.inflater = LayoutInflater.from(context);
        this.drinks = drinks;
    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Object getItem(int position) {
        return drinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        if (convertView == null) {  // 如果還沒有 convertView 先 new 出來
            convertView = inflater.inflate(R.layout.list_view_drink_item, null);

            holder = new Holder();
            holder.textViewDrinkName = (TextView) convertView.findViewById(R.id.textViewDrinkName);
            holder.textViewMediumCupPrice = (TextView) convertView.findViewById(R.id.textViewMediumPrice);
            holder.textViewLargeCupPrice = (TextView) convertView.findViewById(R.id.textViewLargePrice);
            holder.imageViewDrinkImage = (ImageView) convertView.findViewById(R.id.imageViewDrinkImage);
//            holder.editTextDrinkNum = (EditText) convertView.findViewById(R.id.editTextDrinkNum);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Drink drink = drinks.get(position);
        holder.textViewDrinkName.setText(drink.getName());
        holder.textViewMediumCupPrice.setText(String.valueOf(drink.getMediumCupPrice()));
        holder.textViewLargeCupPrice.setText(String.valueOf(drink.getLargeCupPrice()));
//        holder.editTextDrinkNum.setText(String.valueOf(drink.num));
        //holder.imageViewDrinkImage.setImageResource(drink.imageId);

        Picasso.with(inflater.getContext()).load(drink.getImage().getUrl()).into(holder.imageViewDrinkImage);

        return convertView;
    }

    class Holder {
        TextView textViewDrinkName;
        TextView textViewMediumCupPrice;
        TextView textViewLargeCupPrice;
        ImageView imageViewDrinkImage;
//        EditText editTextDrinkNum;
    }
}
