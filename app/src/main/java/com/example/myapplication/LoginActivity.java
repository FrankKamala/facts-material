package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.models.Login;
import com.example.myapplication.models.User;
import com.example.myapplication.network.ApiClient;
import com.example.myapplication.network.FactsAfricaApi;
import com.example.myapplication.utils.FactsPreferences;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FactsPreferences factsPreferences;
    @BindView(R.id.textEmailInput) TextInputLayout mUserEmail;
    @BindView(R.id.textPasswordInput) TextInputLayout mUserPassword;
    @BindView(R.id.loginButton) Button mLoginButton;
    @BindView(R.id.loginProgressBar) ProgressBar mLoginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Check if user is logged in */
        factsPreferences = new FactsPreferences(this);
        if (factsPreferences.isLoggedIn()) {
            startHomeActivity(FactsPreferences.getUserRole());
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        /* Watch for and clear email and password errors */
        mUserEmail.getEditText()
                .addTextChangedListener(createTextWatcher(mUserEmail));
        mUserPassword.getEditText()
                .addTextChangedListener(createTextWatcher(mUserPassword));
        mLoginButton.setOnClickListener(this);
    }
    /* Handle click interactions */
    @Override
    public void onClick(View v) {
        if (v == mLoginButton) {
            onLoginClicked();
        }
    }

    /* Start home activity */
    private void startHomeActivity(int userRole) {
        if (userRole == 2) {
            Intent vendorIntent = new Intent(this, VendorActivity.class);
            startActivity(vendorIntent);
        } else if (userRole == 3) {
            Intent buyerIntent = new Intent(this, BuyerActivity.class);
            startActivity(buyerIntent);
        } else if (userRole == 0) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
        }
    }

    /* Handle clicking login */
    private void onLoginClicked() {
        String email = mUserEmail.getEditText().getText().toString();
        String password = mUserPassword.getEditText().getText().toString();

        /* Validate Input */
        if (email.isEmpty()) {
            mUserEmail.setError("Email cannot be empty!");
        } else if (password.isEmpty()) {
            mUserPassword.setError("Password cannot be empty!");
        } else {
            loginCall(email, password);
        }
    }

    /*Error cleanup function*/
    private TextWatcher createTextWatcher(TextInputLayout textInputLayout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUserEmail.setError(null);
                mUserPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    /* Login api call */
    private void loginCall(String email, String password) {
        secureLogin();
        Login login = new Login(email, password);
        FactsAfricaApi service = ApiClient.getClient().create(FactsAfricaApi.class);
        Call<User> call = service.login(login);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    String token = response.body().getApiToken();
                    factsPreferences.setApiToken(token);
                    factsPreferences.setUserName(response.body().getName());
                    factsPreferences.setUserEmail(response.body().getEmail());
                    factsPreferences.setUserRole(response.body().getRole());
                    factsPreferences.setUserPhone(response.body().getPhone());
                    factsPreferences.setLoggedIn(true);
                    startHomeActivity(response.body().getRole());
                    finish();
                } else {
                    showErrorDialog();
                    secureLoginReset();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showErrorSnackbar();
                secureLoginReset();
            }
        });
    }

    /* Security checks */
    private void secureLogin() {
        mUserEmail.setEnabled(false);
        mUserPassword.setEnabled(false);
        mLoginButton.setVisibility(View.INVISIBLE);
        mLoginProgressBar.setVisibility(View.VISIBLE);
    }

    private void secureLoginReset() {
        mUserEmail.setEnabled(true);
        mUserPassword.setEnabled(true);
        mLoginButton.setVisibility(View.VISIBLE);
        mLoginProgressBar.setVisibility(View.INVISIBLE);
    }

    /* Error handling */
    private void showErrorDialog() {
        new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle("Login failed!")
                .setIcon(R.drawable.ic_error_outline_red_24dp)
                .setMessage("Invalid email or password. Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    /*Error snackbar*/
    private void showErrorSnackbar() {
        View rootView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView,
                "Network error!.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(getResources().getColor(R.color.design_default_color_error));
        snackbar.setAction("Retry", v -> {
            onLoginClicked();
            snackbar.dismiss();
        });
        snackbar.show();
    }
}
