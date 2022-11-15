package com.example.familymapapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.familymapapp.databinding.FragmentFirstBinding;
import com.example.familymapapp.tasks.RegisterTask;

import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class LoginFragment extends Fragment implements RegisterTask.Listener {

    private FragmentFirstBinding binding;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    private EditText usuario;
    private EditText pass;
    private EditText nombre;
    private EditText apellido;
    private EditText correo;
    private Button register;
    private Button login;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
//        binding = FragmentFirstBinding.inflate(inflater, container, false);
        View view = inflater.inflate(R.layout.fragment_first,container,false);
        usuario = (EditText)view.findViewById(R.id.usuario);
        pass = (EditText)view.findViewById(R.id.pass);
        nombre = (EditText)view.findViewById(R.id.nombre);
        apellido = (EditText)view.findViewById(R.id.apellido);
        correo = (EditText)view.findViewById(R.id.correo);
        register = (Button)view.findViewById(R.id.register);
        login = (Button)view.findViewById(R.id.login);


        gender = (RadioGroup) view.findViewById(R.id.genderRadioGroup);
        male = (RadioButton) view.findViewById(R.id.male);
        female = (RadioButton) view.findViewById(R.id.female);

        login = (Button) view.findViewById(R.id.login);
        register = (Button) view.findViewById(R.id.register);

        login.setEnabled(false);
        register.setEnabled(false);

//        // Listeners for EditTexts
//        serverHostTextEntry.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                CheckTextEdits();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) { }
//        });
//
//        serverPortTextEntry.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                CheckTextEdits();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) { }
//        });

        usuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        apellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        correo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // Listener for gender RadioButtons
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (RegisterFieldsFilled()) {
                    register.setEnabled(true);
                }
            }
        });

        // Listener for login Button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginClick();
            }
        });

        // Listener for register Button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerClick();
            }
        });

        return view;
    }

    private void CheckTextEdits() {
        if (LoginFieldsFilled()) {
            login.setEnabled(true);
        }
        else {
            login.setEnabled(false);
        }
        if (RegisterFieldsFilled()) {
            register.setEnabled(true);
        }
        else {
            register.setEnabled(false);
        }
    }

    private Boolean LoginFieldsFilled() {
        return !("".equals(usuario.getText().toString()) || "".equals(pass.getText().toString()));
    }

    private Boolean RegisterFieldsFilled() {
        return !("".equals(usuario.getText().toString()) || "".equals(pass.getText().toString()) ||
                "".equals(nombre.getText().toString()) || "".equals(apellido.getText().toString()) ||
                "".equals(correo.getText().toString()) || (!male.isChecked() && !female.isChecked()));
    }


    private void loginClick() {
//        try {
//            // Create login request
//            LoginRequest loginRequest = new LoginRequest(usernameTextEntry.getText().toString(),
//                    passwordTextEntry.getText().toString());
//
//            // Execute task
//            LoginTask loginTask = new LoginTask(this, serverHostTextEntry.getText().toString(),
//                    serverPortTextEntry.getText().toString());
//            loginTask.execute(loginRequest);
//        }
//        catch (Exception e) {
//            System.out.println(e);
//        }
        System.out.println("Login");
    }

    private void registerClick() {
        try {
            // Create register request
            RegisterRequest registerRequest = new RegisterRequest(usuario.getText().toString(),
                    pass.getText().toString(), correo.getText().toString(),
                    nombre.getText().toString(), apellido.getText().toString(),
                    getGenderString());

            RegisterTask resTask = new RegisterTask(this);


        }
        catch (Exception e) {
            System.out.println(e);
        }


    }

    private String getGenderString() {
        if (male.isChecked()) {
            return "m";
        }
        else {
            return "f";
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onError(Error e) { }

    @Override
    public void registerComplete(RegisterResult regRes) {
        String errorMessage = "Register Failed\n(username already exists)";
        Boolean success = false;

        // Check success of register
        if (regRes != null) {
            errorMessage += "\n(" + regRes.getMessage() + ")";
            success = regRes.isSuccess();
        }

        // Sync data with client
//        if (success) {
//            DataSyncTask dataSyncTask = new DataSyncTask(this, serverHostTextEntry.getText().toString(), serverPortTextEntry.getText().toString());
//            dataSyncTask.execute(regRes.getAuthToken(), regRes.getPersonID());
//        }
//        else {
//            Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
    }

}