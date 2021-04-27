package als48.myapplication.UI.pedometer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import als48.myapplication.R;
import als48.myapplication.databinding.FragmentPedometerBinding;
import als48.myapplication.util.Accelerometer;

/**
 * A simple {@link Fragment} subclass.
 * Author: Austin Scott
 * Description: This Fragment counts your total steps by utilizing an accelerometer
 *              sensor (Inaccurate, do not trust).
 */
public class PedometerFragment extends Fragment {

    public Accelerometer accelerometer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pedometer, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity();

        accelerometer = new Accelerometer(context);

        FragmentPedometerBinding binding = FragmentPedometerBinding.bind(getView());
        binding.textView.setText("Total Steps: 0");

        //Obtain access to the ViewModel. If this fragment object is new, the ViewModel
        //will be re/created. Note the parameter to the ViewModelProvider constructor.
        PedometerViewModel model = new ViewModelProvider(getActivity()).get(PedometerViewModel.class);

        //Add an observer the the MutableLiveData - mCount.
        model.addCountObserver(getViewLifecycleOwner(), count ->
                binding.textView.setText("Total Steps: " + model.getCount()));

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float tx, float ty, float tz) {
                if(tx > 1.0f || tx < -1.0f){
                    model.increment();
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        accelerometer.register();
    }

    @Override
    public void onPause(){
        super.onPause();
        accelerometer.unRegister();
    }

}