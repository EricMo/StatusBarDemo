package com.example.benjamin.statusbardemo.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.benjamin.statusbardemo.R;
import com.example.benjamin.statusbardemo.adapter.BaseViewHolder;

/**
 * Created by Benjamin on 2017/5/23.
 */

public class OptionViewHolder extends BaseViewHolder<String> {
    private TextView name;

    public OptionViewHolder(ViewGroup parent) {
        super(parent, R.layout.adapter_option_item);
        name = (TextView) itemView.findViewById(R.id.optionName);
    }

    @Override
    public void setData(String data) {
        name.setText(data);
    }
}
