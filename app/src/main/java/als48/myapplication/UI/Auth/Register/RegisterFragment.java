package als48.myapplication.UI.Auth.Register;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import als48.myapplication.databinding.FragmentRegisterBinding;
import als48.myapplication.util.PasswordValidator;

import static als48.myapplication.util.PasswordValidator.checkClientPredicate;
import static als48.myapplication.util.PasswordValidator.checkExcludeWhiteSpace;
import static als48.myapplication.util.PasswordValidator.checkPwdDigit;
import static als48.myapplication.util.PasswordValidator.checkPwdLength;
import static als48.myapplication.util.PasswordValidator.checkPwdLowerCase;
import static als48.myapplication.util.PasswordValidator.checkPwdSpecialChar;
import static als48.myapplication.util.PasswordValidator.checkPwdUpperCase;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    private RegisterViewModel mRegisterModel;

    private PasswordValidator mNameValidator = checkPwdLength(1);

    private PasswordValidator mEmailValidator = checkPwdLength(2)
            .and(checkExcludeWhiteSpace())
            .and(checkPwdSpecialChar("@"));

    private PasswordValidator mPassWordValidator =
            checkClientPredicate(pwd -> pwd.equals(binding.editTextPassword1.getText().toString()))
                    .and(checkPwdLength(7))
                    .and(checkPwdSpecialChar())
                    .and(checkExcludeWhiteSpace())
                    .and(checkPwdDigit())
                    .and(checkPwdLowerCase().or(checkPwdUpperCase()));

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegisterFragmentRegister.setOnClickListener(this::attemptRegister);
        mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);
    }

    private void attemptRegister(final View button) {
        validateFirst();
    }

    private void validateFirst() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editTextFirstName.getText().toString().trim()),
                this::validateLast,
                result -> binding.editTextFirstName.setError("Please enter a first name."));
    }

    private void validateLast() {
        mNameValidator.processResult(
                mNameValidator.apply(binding.editTextLastName.getText().toString().trim()),
                this::validateEmail,
                result -> binding.editTextLastName.setError("Please enter a last name."));
    }

    private void validateEmail() {
        mEmailValidator.processResult(
                mEmailValidator.apply(binding.editTextEmail.getText().toString().trim()),
                this::validatePasswordsMatch,
                result -> binding.editTextEmail.setError("Please enter a valid Email address."));
    }

    private void validatePasswordsMatch() {
        PasswordValidator matchValidator =
                checkClientPredicate(
                        pwd -> pwd.equals(binding.editTextPassword1.getText().toString().trim()));

        mEmailValidator.processResult(
                matchValidator.apply(binding.editTextPassword.getText().toString().trim()),
                this::validatePassword,
                result -> binding.editTextPassword.setError("Passwords must match."));
    }

    private void validatePassword() {
        mPassWordValidator.processResult(
                mPassWordValidator.apply(binding.editTextPassword.getText().toString()),
                this::verifyAuthWithServer,
                result -> binding.editTextPassword.setError("Password must contain: " +
                        "\nGreater than six characters," +
                        "\nAt least one digit, " +
                        "\nOne uppercase letter," +
                        "\nOne lowercase letter, " +
                        "\nOne special character, " +
                        "\nNo spaces."));
    }

    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.editTextFirstName.getText().toString(),
                binding.editTextLastName.getText().toString(),
                binding.editTextEmail.getText().toString(),
                binding.editTextPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        // result of connect().
    }

    private void navigateToLogin() {
        RegisterFragmentDirections.ActionRegisterFragmentToLogInFragment directions =
                RegisterFragmentDirections.actionRegisterFragmentToLogInFragment();

        directions.setEmail(binding.editTextEmail.getText().toString());
        directions.setPassword(binding.editTextPassword.getText().toString());

        Navigation.findNavController(getView()).navigate(directions);

    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to SignInViewModel.
     *
     * @param response the Response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    binding.editTextEmail.setError( "Error Authenticating: "
                            + response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                navigateToLogin();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}
