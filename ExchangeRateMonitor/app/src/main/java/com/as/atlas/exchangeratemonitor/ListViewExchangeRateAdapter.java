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

        RateItem rateItem = (RateItem) rateList.get(position);
        convertView = layoutInflater.inflate(R.layout.list_view_exchange_rate, parent, false);
        TextView textViewCurrency = (TextView) convertView.findViewById(R.id.TextViewCurrency);
        TextView textViewCashBuy = (TextView) convertView.findViewById(R.id.TextViewCashBuy);
        TextView textViewCashSold = (TextView) convertView.findViewById(R.id.TextViewCashSold);
        TextView textViewSpotBuy = (TextView) convertView.findViewById(R.id.TextViewSpotBuy);
        TextView textViewSpotSold = (TextView) convertView.findViewById(R.id.TextViewSpotSold);

        textViewCurrency.setText(rateItem.getCurrency());
        textViewCashBuy.setText(convertDoubleToString(rateItem.getCashBuyRate()));
        textViewCashSold.setText(convertDoubleToString(rateItem.getCashSoldRate()));
        textViewSpotBuy.setText(convertDoubleToString(rateItem.getSpotBuyRate()));
        textViewSpotSold.setText(convertDoubleToString(rateItem.getSpotSoldRate()));

        return convertView;
    }

    private String convertDoubleToString(double d) {
        return String.valueOf(d);
    }
}
