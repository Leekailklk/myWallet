package com.android.schemas.volleytest;

public class Utxo implements Comparable<Utxo> {
    private String account_alias;
    private String account_id;
    private String address;
    private long amount;
    private String asset_alias;
    private String asset_id;
    private boolean change;
    private long control_program_index;
    private String id;
    private String program;
    private String source_id;
    private long source_pos;
    private long valid_height;
    private boolean use_it;


    @Override
    public int compareTo(Utxo o) {
       if(this.asset_alias.compareTo(o.asset_alias)==0) {
           if(this.account_alias.compareTo(o.account_alias)==0){
          return (int)(this.getAmount()-o.getAmount());}
          else{
               return this.account_alias.compareTo(o.account_alias);
           }
       }
       else{
            return this.asset_alias.compareTo(o.asset_alias);
        }


    }


    public boolean Use_it() {
        return use_it;
    }

    public void setUse_it(boolean use_it) {
        this.use_it = use_it;
    }

    public Utxo() {

    }

    public Utxo(String id) {
        this.id = id;
    }

    public String getAccount_alias() {
        return account_alias;
    }

    public void setAccount_alias(String account_alias) {
        this.account_alias = account_alias;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
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

    public String getAsset_alias() {
        return asset_alias;
    }

    public void setAsset_alias(String asset_alias) {
        this.asset_alias = asset_alias;
    }

    public String getAsset_id() {
        return asset_id;
    }

    public void setAsset_id(String asset_id) {
        this.asset_id = asset_id;
    }

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    public long getControl_program_index() {
        return control_program_index;
    }

    public void setControl_program_index(long control_program_index) {
        this.control_program_index = control_program_index;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public long getSource_pos() {
        return source_pos;
    }

    public void setSource_pos(long source_pos) {
        this.source_pos = source_pos;
    }

    public long getValid_height() {
        return valid_height;
    }

    public void setValid_height(long valid_height) {
        this.valid_height = valid_height;
    }

    @Override
    public String toString() {
        return "Utxo{" +
                "account_alias='" + account_alias + '\'' +
                ", account_id='" + account_id + '\'' +
                ", address='" + address + '\'' +
                ", amount=" + amount +
                ", asset_alias='" + asset_alias + '\'' +
                ", asset_id='" + asset_id + '\'' +
                ", change=" + change +
                ", control_program_index=" + control_program_index +
                ", id='" + id + '\'' +
                ", program='" + program + '\'' +
                ", source_id='" + source_id + '\'' +
                ", source_pos=" + source_pos +
                ", valid_height=" + valid_height +
                '}';
    }
}
