package com.example.benjamin.statusbardemo.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.example.benjamin.statusbardemo.viewholder.OptionViewHolder;

/**
 * Created by Benjamin on 2017/5/23.
 */

public class OptionAdapter extends RecyclerArrayAdapter<String> {
    public OptionAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new OptionViewHolder(parent);
    }
}
