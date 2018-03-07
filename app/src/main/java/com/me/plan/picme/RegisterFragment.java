package com.me.plan.picme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_register, container, false);

        final Button finishRegisterButton = (Button) myFragmentView.findViewById(R.id.finish_register);
        final EditText inputFullName = (EditText) myFragmentView.findViewById(R.id.input_full_name);
        final EditText inputEmail = (EditText) myFragmentView.findViewById(R.id.input_email);
        final EditText inputPassword = (EditText) myFragmentView.findViewById(R.id.input_password);

        inputPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    finishRegisterButton.performClick();
                    return true;
                }

                return false;
            }
        });

        finishRegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckRegisterForm(inputFullName, inputEmail, inputPassword);
            }
        });

        return myFragmentView;
    }

    private void CheckRegisterForm(EditText inputFullName, EditText inputEmail, EditText inputPassword) {
        if (inputFullName.getText().toString().equals("") ||
            inputEmail.getText().toString().equals("") ||
            inputPassword.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getActivity(), R.string.empty_full_name_or_email_or_email, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            final ProgressBar progressBar = getView().findViewById(R.id.register_progress_bar);
            progressBar.setVisibility(View.VISIBLE);

            ModelFirebase.RegisterInterface registerInterface = new ModelFirebase.RegisterInterface() {
                @Override
                public void AfterSuccessfulRegister() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(getActivity(), R.string.registered_successfully, Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent = new Intent(getActivity(), PicsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                @Override
                public void AfterFailedRegister() {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast toast = Toast.makeText(getActivity(), R.string.wrong_email_or_password, Toast.LENGTH_SHORT);
                    toast.show();
                }
            };

            model.createUserWithEmailAndPassword(inputFullName.getText().toString(),
                    inputEmail.getText().toString(),
                    inputPassword.getText().toString(),
                    registerInterface);
        }
    }
}
