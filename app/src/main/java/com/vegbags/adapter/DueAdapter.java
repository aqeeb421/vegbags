package com.vegbags.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vegbags.R;
import com.vegbags.ResponseModels.Due;
import com.vegbags.view.activity.CreditLedgerActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DueAdapter extends RecyclerView.Adapter<DueAdapter.ViewHolder> {
    List<Due> dueList;
    private Context context;

    public DueAdapter(List<Due> dueList, CreditLedgerActivity creditLedgerActivity) {
        this.context = creditLedgerActivity;
        this.dueList = dueList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_item, parent, false);
        return new DueAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDesc.setText(dueList.get(position).getDescription());
        holder.tvMoney.setText("₹ " + dueList.get(position).getAmount());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, KK:mm aa");
        Date date = null;
        try {
            date = inputFormat.parse(dueList.get(position).getTransactionDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
       if (date != null){
           String formattedDate = outputFormat.format(date);
           System.out.println(formattedDate);

           holder.tvDate.setText(formattedDate);
       }else {
           holder.tvDate.setText("01 JAN, 10:00 AM");
       }
    }

    @Override
    public int getItemCount() {
        if (dueList.size() > 0)
            return dueList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDesc, tvDate, tvMoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }
    }
}
