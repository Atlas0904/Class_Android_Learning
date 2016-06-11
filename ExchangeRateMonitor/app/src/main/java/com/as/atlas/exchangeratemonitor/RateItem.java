package com.as.atlas.exchangeratemonitor;

/**
 * Created by atlas on 2016/6/8.
 */
public class RateItem {

    private String currency;
    private double cashBuyRate;
    private double cashSoldRate;
    private double spotBuyRate;
    private double spotSoldRate;

    public RateItem(String currency, double cashBuyRate, double cashSoldRate, double spotBuyRate, double spotSoldRate){

        this.currency = currency;
        this.cashBuyRate = cashBuyRate;
        this.cashSoldRate = cashSoldRate;
        this.spotBuyRate = spotBuyRate;
        this.spotSoldRate = spotSoldRate;

    }
    public RateItem(){
        this.currency = "";
        this.cashBuyRate = 0.0;
        this.cashSoldRate = 0.0;
        this.spotBuyRate = 0.0;
        this.spotSoldRate = 0.0;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCashBuyRate(double cashBuyRate) {
        this.cashBuyRate = cashBuyRate;
    }

    public double getCashBuyRate() {
        return cashBuyRate;
    }

    public void setCashSoldRate(double cashSoldRate) {
        this.cashSoldRate = cashSoldRate;
    }

    public double getCashSoldRate() {
        return cashSoldRate;
    }

    public void setSpotBuyRate(double spotBuyRate) {
        this.spotBuyRate = spotBuyRate;
    }

    public double getSpotBuyRate() {
        return spotBuyRate;
    }

    public void setSpotSoldRate(double spotSoldRate) {
        this.spotSoldRate = spotSoldRate;
    }

    public double getSpotSoldRate() {
        return spotSoldRate;
    }

    @Override
    public String toString() {
        return "RateItem: " +
                "\nCurrency: " + currency +
                "\nCashBuyRate: " + cashBuyRate +
                "\nCashSoldRate: " + cashSoldRate +
                "\nSpotBuyRate: " + spotBuyRate +
                "\nSpotSoldRate: " + spotSoldRate;
    }
}
