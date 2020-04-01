package com.example.myapplication.buyer_ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.BuyerActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapters.InvoiceAdapter;
import com.example.myapplication.models.Invoice;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.FactsAfricaApi;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private InvoiceAdapter invoiceAdapter;
    private List<Invoice> invoices;
    private View root;
    private static final int SORT_AMOUNT = 0;
    private static final int SORT_DATE_CREATED = 1;
    private static final int SORT_DATE_DUE = 2;
    private int currentSort = SORT_DATE_CREATED;
    @BindView(R.id.buyerInvoiceRecycler)
    RecyclerView mInvoiceRecycler;
    @BindView(R.id.invoiceBuyerProgress) ProgressBar mBuyerInvoiceProgressBar;
    @BindView(R.id.refresh) SwipeRefreshLayout refreshBuyer;
    @BindView(R.id.invoiceToolbar)
    MaterialToolbar mInvoicesToolbar;
    @BindView(R.id.invoiceSearch) androidx.appcompat.widget.SearchView mInvoiceSearch;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home_buyer, container, false);

        ButterKnife.bind(this, root);
        ((BuyerActivity) getActivity()).authListener();
        loadInvoices();
        refreshBuyer.setOnRefreshListener(this::loadInvoices);
        mInvoicesToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.sort) {
                onSortClicked();
            }
            return false;
        });
        return root;
    }

    @Override
    public void onClick(View v) {

    }
    /* Get all invoices */
    private void loadInvoices() {
        refreshBuyer.setRefreshing(true);
        FactsAfricaApi service = ApiClient.getClient().create(FactsAfricaApi.class);
        Call<List<Invoice>> call = service.getBuyerInvoices();
        call.enqueue(new Callback<List<Invoice>>() {
            @Override
            public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
                if (response.isSuccessful()) {
                    refreshBuyer.setRefreshing(false);
                    invoices = response.body();
                    sortData(invoices);
                    mBuyerInvoiceProgressBar.setVisibility(View.GONE);
                    invoiceAdapter = new InvoiceAdapter(invoices, root.getContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
                    mInvoiceRecycler.setLayoutManager(layoutManager);
                    mInvoiceRecycler.setHasFixedSize(true);
                    mInvoiceRecycler.setAdapter(invoiceAdapter);
                    invoiceAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Invoice>> call, Throwable t) {
                refreshBuyer.setRefreshing(false);
                showErrorSnackbar();
            }
        });
    }

    /* Handle sort */
    private void onSortClicked() {
        String[] items = {"Amount", "Date Created", "Due Date"};
        new MaterialAlertDialogBuilder(root.getContext())
                .setTitle("Sort Order")
                .setSingleChoiceItems(items, currentSort, (dialog, which) -> {
                    dialog.dismiss();
                    currentSort = which;
                    loadInvoices();
                }).show();
    }
    /* Sort data */
    private void sortData(List<Invoice> invoices) {
        if (currentSort == SORT_DATE_CREATED) {
            Collections.sort(invoices, new Comparator<Invoice>() {
                @Override
                public int compare(Invoice o1, Invoice o2) {
                    return o2.getId().compareTo(o1.getId());
                }
            });
        }
        else if (currentSort == SORT_AMOUNT) {
            Collections.sort(invoices, new Comparator<Invoice>() {
                @Override
                public int compare(Invoice o1, Invoice o2) {
                    return o2.getInvoiceAmount().compareTo(o1.getInvoiceAmount());
                }
            });
        }
        else if (currentSort == SORT_DATE_DUE) {
            Collections.sort(invoices, new Comparator<Invoice>() {
                @Override
                public int compare(Invoice o1, Invoice o2) {
                    return o1.getDueDate().compareTo(o2.getDueDate());
                }
            });
        }
    }

    /* Error snackbar */
    private void showErrorSnackbar() {
        View rootView = root;
        Snackbar snackbar = Snackbar.make(rootView,
                "Network error!.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.design_default_color_error));
        snackbar.setAction("Please Retry", v -> {
            mBuyerInvoiceProgressBar.setVisibility(View.VISIBLE);
            loadInvoices();
            snackbar.dismiss();
        });
        snackbar.show();
    }

    /* Close Application */
    private void close() {
        getActivity().moveTaskToBack(true);
        getActivity().finish();
    }
}
