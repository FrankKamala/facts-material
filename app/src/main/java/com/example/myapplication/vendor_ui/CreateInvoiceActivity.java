package com.example.myapplication.vendor_ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.example.myapplication.R;
import com.example.myapplication.vendor_ui.home.CreateInvoice;

public class CreateInvoiceActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_invoice);
    fragmentTransactions();
  }

  public void fragmentTransactions() {
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.container_invoice, new CreateInvoice())
        .commit();
  }
}
