package com.duke.z_udp_test;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duke.udp.util.UDPUtil;

import java.util.ArrayList;

/**
 * @Author: duke
 * @DateTime: 2019-05-12 19:11
 * @Description:
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ReceiveHolder> {
    private ArrayList<String> list = new ArrayList<>();

    public void addItemToLast(String text) {
        if (UDPUtil.isEmpty(text)) {
            return;
        }
        list.add(text);
        notifyItemChanged(list.size() - 1);
    }

    public void addItemToHead(String text) {
        if (UDPUtil.isEmpty(text)) {
            return;
        }
        list.add(0, text);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReceiveHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);
        return new ReceiveHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveHolder receiveHolder, int i) {
        receiveHolder.textView.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ReceiveHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ReceiveHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_text_view);
        }
    }
}
