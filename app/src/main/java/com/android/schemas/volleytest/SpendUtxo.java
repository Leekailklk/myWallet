package com.android.schemas.volleytest;

import java.io.Serializable;

public class SpendUtxo implements Serializable {
    private String type;
    private String output_id;

    public SpendUtxo() {

    }
    public SpendUtxo(String type, String output_id) {
        this.type = type;
        this.output_id = output_id;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOutput_id() {
        return output_id;
    }

    public void setOutput_id(String output_id) {
        this.output_id = output_id;
    }
}
