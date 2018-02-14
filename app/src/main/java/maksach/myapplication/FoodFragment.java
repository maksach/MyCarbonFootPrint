package maksach.myapplication;

/**
 * Created by sachinmakaram on 10/13/17.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.support.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class FoodFragment extends Fragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {
    private static final String TAG = "FoodFragment";
    TextView dietProgress;
    double doubleProgress;

    OnDataPass dataPasser;

    public interface OnDataPass {
        public void onFoodDataPass(String data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(String data) {
        dataPasser.onFoodDataPass(data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_fragment, container, false);

        try {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }

        SeekBar seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        seekBar.setProgress(50);
        dietProgress = (TextView)view.findViewById(R.id.foodTextView);
        doubleProgress = 0.0;
        seekBar.setOnSeekBarChangeListener(this);

        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        double result = (((double) progress / 25) + 4.2);
        doubleProgress = result;
        String stringResult = String.format("%.1f", result);
        dietProgress.setText(stringResult + " kg/day");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        this.onClick(dietProgress);
    }


    @Override
    public void onClick(View view) {

        try {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }


        try{

            TextView foodResult = (TextView)getView().findViewById(R.id.foodResult);
            String stringTotalCarbonFood = String.format("%.2f", doubleProgress);
            System.out.println(stringTotalCarbonFood);

            passData(stringTotalCarbonFood);

            foodResult.setText(stringTotalCarbonFood + "kg");

            ImageView co2image = (ImageView)getView().findViewById(R.id.co2food);
            co2image.setBackgroundResource(R.drawable.co2);

        }
        catch (Exception e)
        {
            //new AlertDialog.Builder(TransportFragment.this).setMessage("Please Enter In Correct Number Format").show();
        }

    }



}
