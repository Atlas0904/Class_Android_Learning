package com.as.atlas.teaorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

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
            TextView drinkNameTextView = (TextView) convertView.findViewById(R.id.drinkNameTextView);
            TextView noteNameTextView = (TextView) convertView.findViewById(R.id.noteTextView);

            holder = new Holder();
            holder.drinkName = drinkNameTextView;
            holder.note = noteNameTextView;

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Order order = orders.get(position);

        holder.drinkName.setText(order.drinkName);
        holder.note.setText(order.note);

        return convertView;
    }

    // Cache
    class Holder {
        TextView drinkName;
        TextView note;
    }
}
