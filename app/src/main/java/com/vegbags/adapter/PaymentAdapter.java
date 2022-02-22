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
import com.vegbags.ResponseModels.Payment;
import com.vegbags.view.activity.CreditLedgerActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private List<Payment> paymentList;
    private Context context;

    public PaymentAdapter(List<Payment> paymentList, CreditLedgerActivity creditLedgerActivity) {
        this.paymentList = paymentList;
        this.context = creditLedgerActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_item, parent, false);
        return new PaymentAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.refId.setText(paymentList.get(position).getRefId());
        holder.tvDesc.setText(paymentList.get(position).getDescription());
        holder.tvMoney.setText("₹ " + paymentList.get(position).getAmount());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //2021-08-09T15:16:49.652210Z
        @SuppressLint("SimpleDateFormat") SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM, KK:mm aa");
        Date date = null;
        try {
            date = inputFormat.parse(paymentList.get(position).getTransactionDateTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            String formattedDate = outputFormat.format(date);
            System.out.println(formattedDate);

            holder.tvDate.setText(formattedDate);
        } else {
            holder.tvDate.setText("01 JAN, 10:00 AM");
        }
    }

    @Override
    public int getItemCount() {
        if (paymentList.size() > 0)
            return paymentList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDesc, tvDate, tvMoney,refId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            refId = itemView.findViewById(R.id.refId);
            tvDesc = itemView.findViewById(R.id.tvDesc);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMoney = itemView.findViewById(R.id.tvMoney);
        }
    }
}
