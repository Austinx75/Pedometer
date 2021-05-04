package als48.myapplication.UI.pedometer;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Author: Austin Scott
 * Description: Saves data across life cycle and has function to get count and increment.
 */
public class PedometerViewModel extends ViewModel {

    private MutableLiveData<Integer> mCount;

    /**
     * No-arg constructor that initializes this object. Do not
     * Explicitly call this.
     */

    public PedometerViewModel(){
        mCount = new MutableLiveData<Integer>();
        mCount.setValue(0);
    }

    /**
     * Add an observer to this live data. This is a pass through method for
     * this classes MutableLiveData field of count.
     *
     * See LiveData.observe for more implementation details.
     * @param owner the LifecycleOwner which controls the observer
     *  @param observer the observer that will receive the events
     */

    public void addCountObserver(@NonNull LifecycleOwner owner,
                                 @NonNull Observer<? super Integer> observer) {
        mCount.observe(owner, observer);
    }

    /**
     * Return the current count.
     * @Return the current count
     */
    public int getCount(){
        return mCount.getValue();
    }

    /**
     * increment the current count by 1
     */
    public void increment(int values){

        mCount.setValue(mCount.getValue() + values);
    }

}
