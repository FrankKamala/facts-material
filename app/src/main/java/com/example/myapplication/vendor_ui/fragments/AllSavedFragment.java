package com.example.myapplication.vendor_ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.example.myapplication.VendorActivity;
import com.example.myapplication.adapters.SavedInvoiceAdapter;
import com.example.myapplication.models.Invoice;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.FactsAfricaApi;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSavedFragment extends Fragment {
    private SavedInvoiceAdapter savedInvoiceAdapter;
    @BindView(R.id.savedRecycler) RecyclerView mInvoiceRecycler;
    @BindView(R.id.refresh) SwipeRefreshLayout refresh;
    @BindView(R.id.savedProgress) ProgressBar mSavedProgress;
    private View root;
    private List<Invoice> invoices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_all_saved, container, false);
        ButterKnife.bind(this, root);
        ((VendorActivity) getActivity()).authListener();
        refresh.setOnRefreshListener(this::loadInvoices);
        loadInvoices();
        return root;
    }

    private void loadInvoices() {
        refresh.setRefreshing(true);
        FactsAfricaApi service = ApiClient.getClient().create(FactsAfricaApi.class);
        Call<List<Invoice>> call = service.getSavedInvoices();
        call.enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                if (response.isSuccessful()) {
                    refresh.setRefreshing(false);
                    invoices = response.body();
                    mSavedProgress.setVisibility(View.GONE);
                    savedInvoiceAdapter = new SavedInvoiceAdapter(invoices, root.getContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
                    mInvoiceRecycler.setLayoutManager(layoutManager);
                    mInvoiceRecycler.setHasFixedSize(true);
                    mInvoiceRecycler.setAdapter(savedInvoiceAdapter);
                    savedInvoiceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {
                refresh.setRefreshing(false);
                showErrorSnackbar();
            }
        });
    }

    /* Error snackbar */
    private void showErrorSnackbar() {
        View rootView = root;
        Snackbar snackbar = Snackbar.make(rootView,
                "Network error!.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.design_default_color_error));
        snackbar.setAction("Retry", v -> {
            mSavedProgress.setVisibility(View.VISIBLE);
            loadInvoices();
            snackbar.dismiss();
        });
        snackbar.show();
    }
}
