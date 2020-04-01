package com.example.myapplication.vendor_ui.home;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.VendorActivity;
import com.example.myapplication.models.User;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.FactsAfricaApi;
import com.example.myapplication.utils.FactsPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateInvoice extends Fragment implements View.OnClickListener {

    private static final String TAG = "CreateInvoice";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MaterialSpinner spinner;
    private View rootView;
    private List<String> users = new ArrayList<>();

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private NotificationManagerCompat managerCompat;

    //Calender
    private Calendar myCal = Calendar.getInstance();
    private int year = myCal.get(Calendar.YEAR);
    private int month = myCal.get(Calendar.MONTH);
    private int day = myCal.get(Calendar.DAY_OF_WEEK);

    //company details
    @BindView(R.id.company_name)
    TextView mCompanyName;
    @BindView(R.id.company_email)
    TextView mCompanyEmail;
    @BindView(R.id.company_phone)
    TextView mCompanyPhone;
    @BindView(R.id.company_physical_address)
    TextView mPhysicalAddress;

    //vendor details
    @BindView(R.id.vendor_name)
    TextView mVendorName;
    @BindView(R.id.vendor_email)
    TextView mVendorEmail;
    @BindView(R.id.vendor_phone)
    TextView mVendorPhone;
    @BindView(R.id.invoice_due_date)
    TextView mInvoiceDueDate;

    private String mParam1;
    private String mParam2;

    // private OnFragmentInteractionListener mListener;

    public CreateInvoice() {
        // Required empty public constructor
    }

    public static CreateInvoice newInstance(String param1, String param2) {
        CreateInvoice fragment = new CreateInvoice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called: ");

        rootView = inflater.inflate(R.layout.fragment_create_invoice, container, false);
        initItems();
        ButterKnife.bind(this, rootView);
        spinner = rootView.findViewById(R.id.spinner);
        mInvoiceDueDate.setOnClickListener(this);
        initSpinner();
        initDate();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        DatePickerDialog dialog = new DatePickerDialog(
                getActivity(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year, month, day);
        if(v==mInvoiceDueDate){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        }
    }

    private void initDate(){
        Log.d(TAG, "initDate called: ");
        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = year + "-" + month + "-" + day;
            mInvoiceDueDate.setText(date);
            managerCompat = NotificationManagerCompat.from(rootView.getContext());

            Log.d(TAG, "initDate: "+date);
        };
    }

    private void initSpinner(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, users);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        addUserDetails();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=-1){
                    getUserById(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addUserDetails() {
        mVendorName.setText(FactsPreferences.getUserName());
        mVendorEmail.setText(FactsPreferences.getUserEmail());
        mVendorPhone.setText(FactsPreferences.getUserPhone());
    }

    private void initItems(){
        FactsAfricaApi factsAfricaApi = ApiClient.getClient().create(FactsAfricaApi.class);
        Call<User> userCall = factsAfricaApi.getUserById(3);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    users.add(response.body().getName());
                }
                else {
                    Toast.makeText(getContext(), "No Buyers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserById(int id){
        FactsAfricaApi factsAfricaApi = ApiClient.getClient().create(FactsAfricaApi.class);
        Call<User> userCall = factsAfricaApi.getUserById(id);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if(response.isSuccessful()&&response.body()!=null){
                    mCompanyName.setText(response.body().getName());
                    mCompanyEmail.setText(response.body().getEmail());
                    mCompanyPhone.setText(response.body().getPhone());
                }
                else {
                    Toast.makeText(getContext(), "No Buyers", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<User> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
