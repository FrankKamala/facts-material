package com.example.myapplication.vendor_ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.VendorActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapters.InvoiceAdapter;
import com.example.myapplication.models.Invoice;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.FactsAfricaApi;
import com.example.myapplication.vendor_ui.CreateInvoiceActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
  private InvoiceAdapter invoiceAdapter;
  private List<Invoice> invoices;
  private View root;
  private static final int SORT_AMOUNT = 0;
  private static final int SORT_DATE_CREATED = 1;
  private static final int SORT_DATE_DUE = 2;
  private int currentSort = SORT_DATE_CREATED;
  @BindView(R.id.invoiceRecycler) RecyclerView mInvoiceRecycler;
  @BindView(R.id.invoiceProgress) ProgressBar mInvoiceProgressBar;
  @BindView(R.id.refresh) SwipeRefreshLayout refresh;
  @BindView(R.id.invoiceToolbar) MaterialToolbar mInvoicesToolbar;
  @BindView(R.id.invoiceSearch) androidx.appcompat.widget.SearchView mInvoiceSearch;
  @BindView(R.id.floating_action_button)
  FloatingActionButton mFloatingActionButton;

  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
    root = inflater.inflate(R.layout.fragment_home_vendor, container, false);
    ButterKnife.bind(this, root);
    ((VendorActivity) getActivity()).authListener();
    loadInvoices();
    refresh.setOnRefreshListener(this::loadInvoices);
    mFloatingActionButton.setOnClickListener(this);
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
    if(v==mFloatingActionButton){
      Intent intent = new Intent(getActivity(), CreateInvoiceActivity.class);
      startActivity(intent);
    }

  }

  /* Get all invoices */
  private void loadInvoices() {
    refresh.setRefreshing(true);
    FactsAfricaApi service = ApiClient.getClient().create(FactsAfricaApi.class);
    Call<List<Invoice>> call = service.getAllInvoices();
    call.enqueue(new Callback<List<Invoice>>() {
      @Override
      public void onResponse(Call<List<Invoice>> call, Response<List<Invoice>> response) {
        if (response.isSuccessful()) {
          refresh.setRefreshing(false);
          invoices = response.body();
          sortData(invoices);
          mInvoiceProgressBar.setVisibility(View.GONE);
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
        refresh.setRefreshing(false);
        showErrorSnackbar();
      }
    });
  }

  /* Search invoices */
  private void searchInvoices() {
    mInvoiceSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
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
    snackbar.setAction("Retry", v -> {
      mInvoiceProgressBar.setVisibility(View.VISIBLE);
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