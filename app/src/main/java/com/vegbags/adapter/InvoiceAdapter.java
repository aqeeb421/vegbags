package com.vegbags.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.vegbags.R;
import com.vegbags.ResponseModels.InvoiceModel;

import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {
    List<InvoiceModel> invoiceModelList;
    Context context;

    public InvoiceAdapter(List<InvoiceModel> invoiceModelList, Context context) {
        this.invoiceModelList = invoiceModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_item, parent, false);
        return new InvoiceAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.invoiceId.setText("#"+invoiceModelList.get(position).getInvoiceID());
        holder.invoiceTotal.setText("₹"+invoiceModelList.get(position).getTotal());
        holder.invoicePaid.setText("₹"+invoiceModelList.get(position).getPaid());
        holder.invoiceStatus.setText(invoiceModelList.get(position).getStatus());
        holder.downloadInvoice.setOnClickListener(view -> {
            String url = "http://vbag.pythonanywhere.com/api/invoice?invoice=" + invoiceModelList.get(position).getInvoiceID();
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        if (invoiceModelList.size() > 0) return invoiceModelList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView invoiceId, invoiceTotal, invoicePaid, invoiceStatus, downloadInvoice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            invoiceId = itemView.findViewById(R.id.invoiceId);
            invoiceTotal = itemView.findViewById(R.id.invoiceTotal);
            invoicePaid = itemView.findViewById(R.id.invoicePaid);
            invoiceStatus = itemView.findViewById(R.id.invoiceStatus);
            downloadInvoice = itemView.findViewById(R.id.downloadInvoice);
        }
    }
}
