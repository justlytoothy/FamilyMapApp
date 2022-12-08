package com.example.familymapapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;
import com.example.familymapapp.MapsFragment;

import com.example.familymapapp.databinding.FragmentFirstBinding;
import com.example.familymapapp.handlers.LoginHandler;
import com.example.familymapapp.handlers.RegisterHandler;
import com.google.gson.Gson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Event;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

public class LoginFragment extends Fragment {
    private static final String TAG = "Login Fragment";
    private static final String TOTAL_SIZE_KEY = "TotalSizeKey";



    private FragmentFirstBinding binding;
    private RadioGroup gender;
    private RadioButton male;
    private RadioButton female;
    private EditText usuarioLogin;
    private EditText passLogin;
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
        View view = inflater.inflate(R.layout.fragment_first,container,false);
        setHasOptionsMenu(true);
        usuarioLogin = (EditText)view.findViewById(R.id.usuariologin);
        passLogin = (EditText)view.findViewById(R.id.passlogin);
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
        usuarioLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        passLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CheckTextEdits();
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
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

        gender.setOnCheckedChangeListener((group, checkedId) -> {
            if (RegisterFieldsFilled()) {
                register.setEnabled(true);
            }
        });

        login.setOnClickListener(v -> loginClick());

        register.setOnClickListener(v -> registerClick());

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
        return !("".equals(usuarioLogin.getText().toString()) || "".equals(passLogin.getText().toString()));
    }

    private Boolean RegisterFieldsFilled() {
        return !("".equals(usuario.getText().toString()) || "".equals(pass.getText().toString()) ||
                "".equals(nombre.getText().toString()) || "".equals(apellido.getText().toString()) ||
                "".equals(correo.getText().toString()) || (!male.isChecked() && !female.isChecked()));
    }


    private void loginClick() {
        try {
            LoginRequest loginRequest = new LoginRequest(usuarioLogin.getText().toString(),
                    passLogin.getText().toString());
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();
                    boolean success = bundle.getBoolean("success");

                    if (success) {
                        PersonResult personParsed = new Gson().fromJson(bundle.getString("ob"),PersonResult.class);
                        Person currPerson = new Person(personParsed.getAssociatedUsername(), personParsed.getPersonID(), personParsed.getFirstName(), personParsed.getLastName(), personParsed.getGender(), personParsed.getFatherID(), personParsed.getMotherID(), personParsed.getSpouseID());
                        EventResult eventParsed = new Gson().fromJson(bundle.getString("event"),EventResult.class);
                        DataCache.getInstance().setCurrPerson(currPerson);
                        DataCache.getInstance().setEvents(eventParsed.getData());
                        DataCache.getInstance().setPeople(personParsed.getData());
                        DataCache.getInstance().storePersonsEvents();
                        Toast toast = Toast.makeText(getActivity(), personParsed.getFirstName() + " " + personParsed.getLastName(), Toast.LENGTH_SHORT);
                        toast.show();
//                        NavHostFragment.findNavController(LoginFragment.this)
//                                .navigate(R.id.action_LoginFragment_to_MapsFragment);
                        MapsFragment newFragment = new MapsFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, newFragment);
                        // Commit the transaction
                        transaction.commit();
                    }
                    else {
                        Toast toast = Toast.makeText(getActivity(), bundle.getString("message"), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            };
            LoginHandler loginHandler = new LoginHandler(handler,loginRequest);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(loginHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null) {
            menu.findItem(R.id.search).setVisible(false);
            menu.findItem(R.id.settings).setVisible(false);
            menu.findItem(R.id.filter).setVisible(false);
        }
    }
    private void registerClick() {
        try {
            // Create register request
            RegisterRequest registerRequest = new RegisterRequest(usuario.getText().toString(),
                    pass.getText().toString(), correo.getText().toString(),
                    nombre.getText().toString(), apellido.getText().toString(),
                    getGenderString());
            // Set up a handler that will process messages from the task and make updates on the UI thread
            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    Bundle bundle = message.getData();
                    boolean success = bundle.getBoolean("success");

                    if (success) {
                        PersonResult personParsed = new Gson().fromJson(bundle.getString("ob"),PersonResult.class);
                        Person currPerson = new Person(personParsed.getAssociatedUsername(), personParsed.getPersonID(), personParsed.getFirstName(), personParsed.getLastName(), personParsed.getGender(), personParsed.getFatherID(), personParsed.getMotherID(), personParsed.getSpouseID());
                        EventResult eventParsed = new Gson().fromJson(bundle.getString("event"),EventResult.class);
                        DataCache.getInstance().setCurrPerson(currPerson);
                        DataCache.getInstance().setEvents(eventParsed.getData());
                        DataCache.getInstance().setPeople(personParsed.getData());
                        DataCache.getInstance().storePersonsEvents();
                        Toast toast = Toast.makeText(getActivity(), personParsed.getFirstName() + " " + personParsed.getLastName(), Toast.LENGTH_SHORT);
                        toast.show();
//                        NavHostFragment.findNavController(LoginFragment.this)
//                                .navigate(R.id.action_LoginFragment_to_MapsFragment);
                        MapsFragment newFragment = new MapsFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, newFragment);
                        // Commit the transaction
                        transaction.commit();
                    }
                    else {
                        Toast toast = Toast.makeText(getActivity(), bundle.getString("message"), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            };
            RegisterHandler registerHandler = new RegisterHandler(handler,registerRequest);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(registerHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
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




}