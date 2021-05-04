package als48.myapplication.UI.pedometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import als48.myapplication.R;
import als48.myapplication.databinding.FragmentPedometerBinding;
import als48.myapplication.util.Accelerometer;

import static androidx.core.content.ContextCompat.getSystemService;
import static java.lang.String.valueOf;


/**
 * A simple {@link Fragment} subclass.
 * Author: Austin Scott
 * Description: This Fragment counts your total steps by utilizing an accelerometer
 *              sensor (Inaccurate, do not trust).
 */
public class PedometerFragment extends Fragment implements SensorEventListener {

    public Accelerometer accelerometer;

    SensorManager sensorManager;

    boolean running = false;


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
        PedometerViewModel model = new ViewModelProvider(getActivity()).get(PedometerViewModel.class);

        binding.textView.setText("Total Steps: 0");

        //Obtain access to the ViewModel. If this fragment object is new, the ViewModel
        //will be re/created. Note the parameter to the ViewModelProvider constructor.

        //Add an observer the the MutableLiveData - mCount.
        model.addCountObserver(getViewLifecycleOwner(), count ->
                binding.textView.setText("Total Steps: " + model.getCount()));


        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
//        accelerometer.setListener(new Accelerometer.Listener() {
//            @Override
//            public void onTranslation(float tx, float ty, float tz) {
//                if(tx > 1.0f || tx < -1.0f){
//                    model.increment();
//                }
//            }
//        });
    }

    @Override
    public void onResume(){
        super.onResume();
        //accelerometer.register();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(getActivity(), "Sensor not found!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause(){
        super.onPause();
        //accelerometer.unRegister();
        running = false;
        //sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        PedometerViewModel model = new ViewModelProvider(getActivity()).get(PedometerViewModel.class);
        if(running){
            model.increment(Integer.parseInt(valueOf(event.values[0])));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}