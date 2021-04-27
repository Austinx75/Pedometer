package als48.myapplication.UI.Home;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.auth0.android.jwt.JWT;

import als48.myapplication.R;
import als48.myapplication.UI.model.UserInfoViewModel;
import als48.myapplication.databinding.FragmentHomeBinding;


/**
 * A simple {@link Fragment} subclass.
 * Author: Austin Scott
 * Description: This is the home page fo the application.
 *              It is a nice simple greeting using the email that was used to sign in.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //SuccessFragmentArgs args = SuccessFragmentArgs.fromBundle(getArguments());
        //binding.textView.setText("Hello " + args.getJwt());

        //Note argument sent to the ViewModelProvider constructor. It is the Activity that
        //holds this fragment.
        UserInfoViewModel model = new ViewModelProvider(getActivity()).get(UserInfoViewModel.class);
        FragmentHomeBinding.bind(getView()).textView.setText("Hello " + model.getEmail());


    }


}