package com.android.schemas.volleytest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class UtxoAdapter extends ArrayAdapter<Utxo> {
    private int resourceId;
    public UtxoAdapter(Context context, int textViewResourceId,
                        List<Utxo> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Utxo utxo = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.useIt=(CheckBox) view.findViewById(R.id.utxo_use);
            viewHolder.utxoAlias= (TextView) view.findViewById(R.id.utxo_alias);
            viewHolder.utxoAmount=(TextView) view.findViewById(R.id.utxo_amount);
            viewHolder.accountAlias=(TextView) view.findViewById(R.id.account_alias);
            view.setTag(viewHolder);// 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }

        viewHolder.useIt.setChecked(utxo.Use_it());
        viewHolder.utxoAlias.setText(utxo.getAsset_alias());
        viewHolder.utxoAmount.setText("      "+utxo.getAmount());
        viewHolder.accountAlias.setText("  "+utxo.getAccount_alias());
        return view;
    }
    class ViewHolder {
        CheckBox useIt;
        TextView utxoAlias;
        TextView utxoAmount;
        TextView accountAlias;
    }

}
