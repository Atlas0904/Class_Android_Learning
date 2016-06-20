package com.as.atlas.teaorder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by atlas on 2016/6/6.
 */
public class OrderAdapter extends BaseAdapter {

    ArrayList<Order> orders;
    LayoutInflater inflater;

    public OrderAdapter(Context ctx, ArrayList<Order> orders) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return orders.size();
    }

    @Override
    public Object getItem(int position) {
        return orders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;  // 不會用到
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if (convertView == null) {  // 如果還沒有 convertView 先 new 出來
            convertView = inflater.inflate(R.layout.list_view_order_item, null);
            TextView drinkNumberTextView = (TextView) convertView.findViewById(R.id.drinkNumberTextView);
            TextView noteTextView = (TextView) convertView.findViewById(R.id.noteTextView);
            TextView storeInfoTextView = (TextView) convertView.findViewById(R.id.storeInfoTextView);

            holder = new Holder();
            holder.drinkNumber = drinkNumberTextView;
            holder.note = noteTextView;
            holder.storeInfo = storeInfoTextView;


            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Order order = orders.get(position);
        int totalNumber = 0;
        try {
            JSONArray jsonArray = new JSONArray(order.menuResult);
            Log.d("AAA", "menu=" + order.menuResult);

            Log.d("AAA", "length=" + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Log.d("AAA", "jsonObject=" + jsonObject);
                totalNumber += (jsonObject.getInt(DrinkOrder.MEDIUM_CUP_NUM) + jsonObject.getInt(DrinkOrder.LARGE_CUP_NUM));
                Log.d("AAA", "getView: totalNumber=" + totalNumber);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.drinkNumber.setText(String.valueOf(totalNumber));
        holder.note.setText(order.note);
        holder.storeInfo.setText(order.storeInfo);

        return convertView;
    }

    // Cache
    class Holder {
        TextView drinkNumber;
        TextView note;
        TextView storeInfo;
    }
}
