package com.vegbags.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.vegbags.ResponseModels.InvoiceModel;
import com.vegbags.adapter.InvoiceAdapter;
import com.vegbags.database.Api;
import com.vegbags.database.RetrofitClientService;
import com.vegbags.databinding.FragmentInvoiceBinding;
import com.vegbags.utils.CommonUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvoiceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

    public InvoiceFragment() {
        // Required empty public constructor
    }

    private FragmentInvoiceBinding invoiceBinding;
    private InvoiceAdapter invoiceAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        invoiceBinding = FragmentInvoiceBinding.inflate(inflater, container, false);
        invoiceBinding.shimmerFrameLayout.startShimmerAnimation();

        GetInvoices();


        return invoiceBinding.getRoot();
    }

    private void GetInvoices() {
        String token = CommonUtils.getToken();

        Api uploadData = RetrofitClientService.getClient().create(Api.class);
        Call<List<InvoiceModel>> modelCall = uploadData.getInvoices(token);

        modelCall.enqueue(new Callback<List<InvoiceModel>>() {
            @Override
            public void onResponse(Call<List<InvoiceModel>> call, Response<List<InvoiceModel>> response) {
                invoiceBinding.shimmerFrameLayout.stopShimmerAnimation();
                invoiceBinding.shimmerFrameLayout.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body().size() > 0) {
                    List<InvoiceModel> invoiceModelList = response.body();
                    setAdapter(invoiceModelList);
                } else {
                    invoiceBinding.noDara.setVisibility(View.VISIBLE);
                    invoiceBinding.invoiceRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<InvoiceModel>> call, Throwable t) {
                invoiceBinding.shimmerFrameLayout.stopShimmerAnimation();
                invoiceBinding.shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter(List<InvoiceModel> invoiceModelList) {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        invoiceBinding.invoiceRecyclerView.setLayoutManager(manager);
        invoiceAdapter = new InvoiceAdapter(invoiceModelList, getContext());
        invoiceBinding.invoiceRecyclerView.setHasFixedSize(true);
        invoiceBinding.invoiceRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        invoiceBinding.invoiceRecyclerView.setAdapter(invoiceAdapter);
    }
}