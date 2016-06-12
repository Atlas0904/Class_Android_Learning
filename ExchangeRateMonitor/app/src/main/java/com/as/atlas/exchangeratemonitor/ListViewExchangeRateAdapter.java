package com.as.atlas.exchangeratemonitor;

import android.content.Context;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by atlas on 2016/6/11.
 */
public class ListViewExchangeRateAdapter extends BaseAdapter {

    private Context mContext;
    private List rateList;
    private LayoutInflater layoutInflater;

    public ListViewExchangeRateAdapter(Context mContext, List rateList) {
        this.layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        this.rateList = rateList;
    }

    @Override
    public int getCount() {
        return rateList.size();
    }

    @Override
    public Object getItem(int position) {
        return rateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rateList.indexOf(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_exchange_rate, parent, false);
            holder = new Holder(
                    (TextView) convertView.findViewById(R.id.TextViewCurrency),
                    (TextView) convertView.findViewById(R.id.TextViewCashBuy),
                    (TextView) convertView.findViewById(R.id.TextViewCashSold),
                    (TextView) convertView.findViewById(R.id.TextViewSpotBuy),
                    (TextView) convertView.findViewById(R.id.TextViewSpotSold)
            );
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        RateItem rateItem = (RateItem) rateList.get(position);

        holder.textViewCurrency.setText(rateItem.getCurrency());
        holder.textViewCashBuy.setText(convertDoubleToString(rateItem.getCashBuyRate()));
        holder.textViewCashSold.setText(convertDoubleToString(rateItem.getCashSoldRate()));
        holder.textViewSpotBuy.setText(convertDoubleToString(rateItem.getSpotBuyRate()));
        holder.textViewSpotSold.setText(convertDoubleToString(rateItem.getSpotSoldRate()));

        return convertView;
    }

    private static class Holder {
        TextView textViewCurrency;
        TextView textViewCashBuy;
        TextView textViewCashSold;
        TextView textViewSpotBuy;
        TextView textViewSpotSold;

        public Holder(TextView textViewCurrency, TextView textViewCashBuy, TextView textViewCashSold, TextView textViewSpotBuy, TextView textViewSpotSold) {
            this.textViewCurrency = textViewCurrency;
            this.textViewCashBuy = textViewCashBuy;
            this.textViewCashSold = textViewCashSold;
            this.textViewSpotBuy = textViewSpotBuy;
            this.textViewSpotSold = textViewSpotSold;
        }
    }

    private String convertDoubleToString(double d) {
        return String.valueOf(d);
    }
}
