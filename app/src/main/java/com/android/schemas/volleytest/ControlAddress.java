package com.android.schemas.volleytest;

import java.io.Serializable;

public class ControlAddress implements Serializable {
    private String address;
    private long amount;
    private String type;
    private String asset_alias;

    public ControlAddress(String address, long amount, String type, String asset_alias) {
        this.address = address;
        this.amount = amount;
        this.type = type;
        this.asset_alias = asset_alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAsset_alias() {
        return asset_alias;
    }

    public void setAsset_alias(String asset_alias) {
        this.asset_alias = asset_alias;
    }
}
