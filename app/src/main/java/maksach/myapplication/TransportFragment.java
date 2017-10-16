package maksach.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.support.*;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

public class TransportFragment extends Fragment implements OnClickListener {
    private static final String TAG = "TransportFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transport_fragment, container, false);

        try {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }

        Button button = (Button) view.findViewById(R.id.button_calcTransport);
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

        try {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }

        EditText mpgInput=(EditText)getView().findViewById((R.id.car_mpg));
        EditText milesInput=(EditText)getView().findViewById((R.id.miles_driven));


        try{
            Double mpg = ConvertNumber(mpgInput.getText().toString());
            Double miles=ConvertNumber(milesInput.getText().toString());
            //Double car_c02=CovertNumber(carbon.getText().toString());
            //Double lightning= CovertNumber(electric.getText().toString());
            Double total_carbon = calculateTransportation(mpg, miles);

            TextView transportResult = (TextView)getView().findViewById(R.id.transportResult);
            String stringTotalCarbon = String.format("%.2f", total_carbon);
            transportResult.setText(stringTotalCarbon + "kg");

            ImageView co2image = (ImageView)getView().findViewById(R.id.co2transport);
            co2image.setBackgroundResource(R.drawable.co2);
            //passing value start new activity
            //Intent tempIntent = new Intent(this, Result_page.class);
            //tempIntent.putExtra("Carbon_Weight",total_carbon);
            //startActivity(tempIntent);
        }
        catch (Exception e)
        {
            //new AlertDialog.Builder(TransportFragment.this).setMessage("Please Enter In Correct Number Format").show();
        }

    }

    //just if entering a number not a text empty spot will result in 0
    private Double ConvertNumber(String number)  throws Exception
    {
        if(number.equals(""))
        {
            return 0.0;
        }
        else
        {
            if(number.matches("[0-9]+"))
            {
                return Double.parseDouble(number);
            }
            else
            {
                throw  new Exception();

            }

        }

    }

    ////////////////////////////c
    //////////////////////////
    // calculation
    public Double calculateTransportation(Double mpgValue, Double milesDriven) {
        //https://www.epa.gov/sites/production/files/2016-02/documents/420f14040a.pdf
        //8.91kg of co2 emitted for a gallon of fuel if it does not have ethanol (10.15 with ethanol)
        double carbonGas= (milesDriven / mpgValue) * 8.91;
        return carbonGas;
    }
}