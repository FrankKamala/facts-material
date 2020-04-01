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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.BuyerActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapters.InvoiceAdapter;
import com.example.myapplication.models.Invoice;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private InvoiceAdapter invoiceAdapter;
    private List<Invoice> invoices;
    private View root;
    private static final int SORT_AMOUNT = 0;
    private static final int SORT_DATE_CREATED = 1;
    private static final int SORT_DATE_DUE = 2;
    private int currentSort = SORT_DATE_CREATED;
    @BindView(R.id.invoiceRecycler)
    RecyclerView mInvoiceRecycler;
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
}
