package com.example.project.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.DetectResultActivity;
import com.example.project.MainActivity;
import com.example.project.R;
import com.example.project.fragment.HistoryFragment;
import com.example.project.utils.DataManageUtil;
import com.example.project.utils.UIUtil;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

public class LinearHistoryItemAdapter extends RecyclerView.Adapter<LinearHistoryItemAdapter.LinearHistoryItemViewHolder> {

    private Context mContext;
    private ArrayList<File> dates;
    private RecyclerView recyclerView;
    //private static int c = 4;

    public boolean sortFromBefore = false;
    private HistoryFragment historyFrag;
    public LinearHistoryItemAdapter(Context mContext, HistoryFragment historyFragment) {
        this.mContext = mContext;
        this.dates = DataManageUtil.getImgDirNameList(mContext);
        this.recyclerView = historyFragment.getRecords();
        this.historyFrag = historyFragment;
    }

    @NonNull
    @Override
    public LinearHistoryItemAdapter.LinearHistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearHistoryItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearHistoryItemAdapter.LinearHistoryItemViewHolder holder, int position) {
        View.OnClickListener detailPageTrigger = v -> {
            UIUtil.goToResultPage(mContext, dates.get(position));
        };
        holder.btn_detail.setOnClickListener(detailPageTrigger);
        holder.preview_img.setOnClickListener(detailPageTrigger);

        holder.date.setText(UIUtil.getDateWithTimeStamp(dates.get(position).getName()));
        try {
            holder.preview_img.setImageBitmap(DataManageUtil.getBitmapByFile(new File(dates.get(position), "processed.png")));
        } catch (FileNotFoundException e) {
            historyFrag.getActivity().runOnUiThread(() -> UIUtil.
                    indefiniteSnackbar(historyFrag.snackbar_bottom, "无法加载图片:" + dates.get(position).getAbsolutePath()));
            e.printStackTrace();
        }
        holder.btn_delete.setOnClickListener(v -> {
            ((Activity) mContext).runOnUiThread(() -> new MaterialAlertDialogBuilder(mContext).setTitle(R.string.dialog_notice)
                    .setMessage(R.string.history_frag_del_dialog_msg)
                    .setPositiveButton(R.string.history_frag_del_dialog_posi, (dialog, which) -> removeItem(position))
                    .setNegativeButton(R.string.history_frag_del_dialog_nega, (dialog, which) -> dialog.dismiss())
                    .show());
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    class LinearHistoryItemViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private Button btn_detail, btn_delete;
        private ImageView preview_img;
        public LinearHistoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            preview_img = itemView.findViewById(R.id.preview_img);
            btn_detail = itemView.findViewById(R.id.btn_detail);
            btn_delete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public void removeItem(int position){
        if (position >= dates.size() || position < 0) return;
        ((MainActivity)historyFrag.getActivity()).notifyHistoryDeleted(dates.get(position));
        DataManageUtil.deleteFilesByDirectory(dates.get(position));

        dates.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position, dates.size() - position); //刷新被删除数据，以及其后面的数据
        historyFrag.refreshHistoryAmount();
    }

    public void insertItem(int position, File imgDir){
        if (position > dates.size() || position < 0) return;
        dates.add(position, imgDir);
        notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
        notifyItemRangeChanged(position, dates.size() - position);
        historyFrag.refreshHistoryAmount();

    }

    public void reverseItems() {
        sortFromBefore = !sortFromBefore;
        Collections.reverse(dates);
        notifyDataSetChanged();
    }

}
