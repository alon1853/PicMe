package com.me.plan.picme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.me.plan.picme.Model.Model;
import com.me.plan.picme.Model.ModelFirebase;


public class RegisterFragment extends Fragment {
    private Model model;

    public RegisterFragment() {
        model = Model.instance;
        // Required empty public constructor
    }

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_login, container, false);

        final Button signInButton = (Button) myFragmentView.findViewById(R.id.signin);
        final EditText inputEmail = (EditText) myFragmentView.findViewById(R.id.input_email);
        final EditText inputPassword = (EditText) myFragmentView.findViewById(R.id.input_password);

        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signInButton.performClick();
                    return true;
                }

                return false;
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckSignInForm(inputEmail, inputPassword);
            }
        });

        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private void CheckSignInForm(EditText inputEmail, EditText inputPassword) {
        if (inputEmail.getText().toString().equals("") || inputPassword.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getActivity(), R.string.empty_email_or_email, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            final ProgressBar progressBar = getView().findViewById(R.id.sign_in_progress_bar);
            progressBar.setVisibility(View.VISIBLE);

            ModelFirebase.SignInInterface signInInterface = new ModelFirebase.SignInInterface() {
                @Override
                public void AfterSuccessfulSignIn() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(getActivity(), R.string.signed_in_successfully, Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(getActivity(), PicsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                @Override
                public void AfterFailedSignIn() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(getActivity(), R.string.wrong_email_or_password, Toast.LENGTH_SHORT);
                    toast.show();
                }
            };

            model.SignInWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString(), signInInterface);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
