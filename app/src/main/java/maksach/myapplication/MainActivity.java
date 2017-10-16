package maksach.myapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import android.widget.*;

import java.util.InputMismatchException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Button signOutButton = (Button) findViewById(R.id.sign_out_button);
        /*OnClickListener signOutListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signOutIntent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signOutIntent);
                SignInActivity.mGoogleApiClient.connect();
                Auth.GoogleSignInApi.signOut(SignInActivity.mGoogleApiClient);
            }
        };*/
        //findViewById(R.id.sign_out_button).setOnClickListener(signOutListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();
        String result="";

        switch(view.getId()) {
            case R.id.radio_meat:
                if (checked)
                    result="meat eater";
                break;
            case R.id.radio_vegetables:
                if (checked)
                    result="vegetarian";
                break;
        }
    }



    public void onCalculateClicked(View view){
        EditText mpgInput=(EditText)findViewById((R.id.car_mpg));
        EditText milesInput=(EditText)findViewById((R.id.miles_driven));
        //EditText carbonInput =(EditText)findViewById((R.id.c02));
        //EditText electric=(EditText)findViewById((R.id.electricity));


    try{
        Double mpg = CovertNumber(mpgInput.getText().toString());
        Double miles=CovertNumber(milesInput.getText().toString());
        //Double car_c02=CovertNumber(carbon.getText().toString());
        //Double lightning= CovertNumber(electric.getText().toString());
        Double total_carbon = calculateTransportation(mpg, miles);

        //passing value start new activity
        Intent tempIntent = new Intent(this, Result_page.class);
        tempIntent.putExtra("Carbon_Weight",total_carbon);
        startActivity(tempIntent);
    }
    catch (Exception e)
    {
        new AlertDialog.Builder(MainActivity.this).setMessage("Please Enter In Correct Number Format").show();
    }

    }

    //just if entering a number not a text empty spot will result in 0
    private Double CovertNumber(String number)  throws Exception
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

    public Double calculateFood(Double carbonFood) {
        //http://www.greeneatz.com/foods-carbon-footprint.html
        //meat lover = 8.2 kg/day
        //average = 6.2 kg/day
        //vegetarian = 4.2 kg/day
        return carbonFood;
    }

    public Double calculateElectricity() {
        double carbonElectricity = 0.0;
        return carbonElectricity;
    }
}
