package com.example.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.DetectResultActivity;
import com.example.project.R;

public class LinearHistoryItemAdapter extends RecyclerView.Adapter<LinearHistoryItemAdapter.LinearHistoryItemViewHolder> {

    private Context mContext;

    public LinearHistoryItemAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public LinearHistoryItemAdapter.LinearHistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearHistoryItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearHistoryItemAdapter.LinearHistoryItemViewHolder holder, int position) {
        holder.btn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, DetectResultActivity.class);
                mContext.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class LinearHistoryItemViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private Button btn_detail, btn_delete;

        public LinearHistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            btn_detail = itemView.findViewById(R.id.btn_detail);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
