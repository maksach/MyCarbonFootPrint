package maksach.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Spinner;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import android.util.Log;
import android.widget.AdapterView.OnItemSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class ElectricityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ElectricityFragment";
    private Spinner spinner;
    private String spinnerSelection;
    private View currView;
    private String resultElectricityCarbon;
    private DataLoader loadData;

    OnDataPass dataPasser;

    public interface OnDataPass {
        public void onElectricityDataPass(String data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(String data) {
        dataPasser.onElectricityDataPass(data);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.electricity_fragment, container, false);
        currView = view;
        spinner = (Spinner) view.findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelection = parent.getItemAtPosition(position).toString();
                spinnerSelection = spinner.getSelectedItem().toString();
                loadData = new DataLoader(ElectricityFragment.this.getContext(), spinnerSelection);
                loadData.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        try {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }

        //add listener to button
        Button button = (Button) view.findViewById(R.id.button_calcElectricity);
        button.setOnClickListener(this);

        return view;
    }

    // sets the text of the electricity details on post execute
    private void setSources(String res) {
        TextView electricityDetails = (TextView) currView.findViewById(R.id.electricityDetails);
        electricityDetails.setText(res);
    }


    // calculate button is clicked
    @Override
    public void onClick(View v) {
        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }


        try {
            Double electricityCarbon = 0.0;

            EditText electricityConsumption = (EditText) getView().findViewById(R.id.electricityBill);
            Double electricity = Double.parseDouble(electricityConsumption.getText().toString());
            electricityCarbon = (loadData.getCoal() * electricity) + (loadData.getNatGas() * electricity) + (loadData.getNuclear() * electricity) + (loadData.getRenewables() * electricity) + (loadData.getOther() * electricity);
            TextView electricityResult = (TextView) getView().findViewById(R.id.electricityResult);
            resultElectricityCarbon = String.format("%.2f", electricityCarbon);
            electricityResult.setText(resultElectricityCarbon + "kg");

            passData(resultElectricityCarbon);

            ImageView co2image = (ImageView) getView().findViewById(R.id.co2transport);
            co2image.setBackgroundResource(R.drawable.co2);

        } catch (Exception e_e) {
            new AlertDialog.Builder(ElectricityFragment.this.getContext()).setMessage("Please Enter In Correct Number Format").show();
        }
    }


    class DataLoader extends AsyncTask<Void, Void, String> {
        private static final String API_KEY = "f1fede5e78fb75019a26f213ba40f77b";
        private String allFuelsResultStr = "";
        private String allFuelsCO2 = "";
        private String coalResultStr = "";
        private String coalCO2 = "";
        private String natGasResultStr = "";
        private String natGasCO2 = "";
        private String nuclearResultStr = "";
        private String nuclearCO2 = "";
        private String renewableResultStr = "";
        private String renewableCO2 = "";
        private String state = "";
        private String finalResult = "";
        private String coalResult = "";
        private String natGasResult = "";
        private String nuclearResult = "";
        private String renewableResult = "";
        private String otherResult = "";

        private Context context;

        public DataLoader(Context context, String state) {
            this.context = context;
            this.state = state;
        }

        protected String doInBackground(Void... urls) {

            try {
                URL allFuelsUrl = new URL("http://api.eia.gov/series/?api_key=" + API_KEY + "&series_id=ELEC.GEN.ALL-" + state + "-99.A");
                URL coalUrl = new URL("http://api.eia.gov/series/?api_key=" + API_KEY + "&series_id=ELEC.GEN.COW-" + state + "-99.A");
                URL natGasUrl = new URL("http://api.eia.gov/series/?api_key=" + API_KEY + "&series_id=ELEC.GEN.NG-" + state + "-99.A");
                URL nuclearUrl = new URL("http://api.eia.gov/series/?api_key=" + API_KEY + "&series_id=ELEC.GEN.NUC-" + state + "-99.A");
                URL renewableUrl = new URL("http://api.eia.gov/series/?api_key=" + API_KEY + "&series_id=ELEC.GEN.AOR-" + state + "-99.A");
                HttpURLConnection allFuelsUrlConnection = (HttpURLConnection) allFuelsUrl.openConnection();
                HttpURLConnection coalUrlConnection = (HttpURLConnection) coalUrl.openConnection();
                HttpURLConnection natGasUrlConnection = (HttpURLConnection) natGasUrl.openConnection();
                HttpURLConnection nuclearUrlConnection = (HttpURLConnection) nuclearUrl.openConnection();
                HttpURLConnection renewableUrlConnection = (HttpURLConnection) renewableUrl.openConnection();

                try {
                    // ALL FUELS DATA
                    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(allFuelsUrlConnection.getInputStream()));
                    StringBuilder stringBuilder1 = new StringBuilder();
                    String line1;
                    while ((line1 = bufferedReader1.readLine()) != null) {
                        stringBuilder1.append(line1).append("\n");
                    }
                    bufferedReader1.close();
                    allFuelsResultStr = stringBuilder1.toString();
                    JSONObject json1 = new JSONObject(allFuelsResultStr);
                    JSONArray seriesArr1 = json1.getJSONArray("series");
                    JSONObject seriesObject1 = (JSONObject) seriesArr1.get(0);
                    JSONArray dataArr1 = seriesObject1.getJSONArray("data");
                    JSONArray latestYear1 = (JSONArray) dataArr1.get(0);
                    System.out.println(latestYear1.get(1));
                    allFuelsCO2 = latestYear1.get(1).toString();


                    // COAL DATA
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(coalUrlConnection.getInputStream()));
                    StringBuilder stringBuilder2 = new StringBuilder();
                    String line2;
                    while ((line2 = bufferedReader2.readLine()) != null) {
                        stringBuilder2.append(line2).append("\n");
                    }
                    bufferedReader2.close();
                    coalResultStr = stringBuilder2.toString();
                    JSONObject json2 = new JSONObject(coalResultStr);
                    JSONArray seriesArr2 = json2.getJSONArray("series");
                    JSONObject seriesObject2 = (JSONObject) seriesArr2.get(0);
                    JSONArray dataArr2 = seriesObject2.getJSONArray("data");
                    JSONArray latestYear2 = (JSONArray) dataArr2.get(0);
                    System.out.println(latestYear2.get(1));
                    coalCO2 = latestYear2.get(1).toString();


                    // NATURAL GAS DATA
                    BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(natGasUrlConnection.getInputStream()));
                    StringBuilder stringBuilder3 = new StringBuilder();
                    String line3;
                    while ((line3 = bufferedReader3.readLine()) != null) {
                        stringBuilder3.append(line3).append("\n");
                    }
                    bufferedReader3.close();
                    natGasResultStr = stringBuilder3.toString();
                    JSONObject json3 = new JSONObject(natGasResultStr);
                    JSONArray seriesArr3 = json3.getJSONArray("series");
                    JSONObject seriesObject3 = (JSONObject) seriesArr3.get(0);
                    JSONArray dataArr3 = seriesObject3.getJSONArray("data");
                    JSONArray latestYear3 = (JSONArray) dataArr3.get(0);
                    System.out.println(latestYear3.get(1));
                    natGasCO2 = latestYear3.get(1).toString();


                    // NUCLEAR DATA
                    BufferedReader bufferedReader4 = new BufferedReader(new InputStreamReader(nuclearUrlConnection.getInputStream()));
                    StringBuilder stringBuilder4 = new StringBuilder();
                    String line4;
                    while ((line4 = bufferedReader4.readLine()) != null) {
                        stringBuilder4.append(line4).append("\n");
                    }
                    bufferedReader4.close();
                    nuclearResultStr = stringBuilder4.toString();
                    JSONObject json4 = new JSONObject(nuclearResultStr);
                    JSONArray seriesArr4 = json4.getJSONArray("series");
                    JSONObject seriesObject4 = (JSONObject) seriesArr4.get(0);
                    JSONArray dataArr4 = seriesObject4.getJSONArray("data");
                    JSONArray latestYear4 = (JSONArray) dataArr4.get(0);
                    System.out.println(latestYear4.get(1));
                    nuclearCO2 = latestYear4.get(1).toString();


                    // RENEWABLE DATA
                    BufferedReader bufferedReader5 = new BufferedReader(new InputStreamReader(renewableUrlConnection.getInputStream()));
                    StringBuilder stringBuilder5 = new StringBuilder();
                    String line5;
                    while ((line5 = bufferedReader5.readLine()) != null) {
                        stringBuilder5.append(line5).append("\n");
                    }
                    bufferedReader5.close();
                    renewableResultStr = stringBuilder5.toString();
                    JSONObject json5 = new JSONObject(renewableResultStr);
                    JSONArray seriesArr5 = json5.getJSONArray("series");
                    JSONObject seriesObject5 = (JSONObject) seriesArr5.get(0);
                    JSONArray dataArr5 = seriesObject5.getJSONArray("data");
                    JSONArray latestYear5 = (JSONArray) dataArr5.get(0);
                    System.out.println(latestYear5.get(1));
                    renewableCO2 = latestYear5.get(1).toString();

                    Double coalCalculation = (Double.parseDouble(coalCO2) / Double.parseDouble(allFuelsCO2)) * 100;
                    Double natGasCalculation = (Double.parseDouble(natGasCO2) / Double.parseDouble(allFuelsCO2)) * 100;
                    Double nuclearCalculation = (Double.parseDouble(nuclearCO2) / Double.parseDouble(allFuelsCO2)) * 100;
                    Double renewableCalculation = (Double.parseDouble(renewableCO2) / Double.parseDouble(allFuelsCO2)) * 100;
                    Double otherCalculation = 100.00 - (coalCalculation + natGasCalculation + nuclearCalculation + renewableCalculation);

                    coalResult = String.format("%.2f", coalCalculation);
                    natGasResult = String.format("%.2f", natGasCalculation);
                    nuclearResult = String.format("%.2f", nuclearCalculation);
                    renewableResult = String.format("%.2f", renewableCalculation);
                    otherResult = String.format("%.2f", otherCalculation);

                    finalResult += "Electricity by Fuel Type in " + state + ":\nCoal: " + coalResult + "%\n" + "Natural Gas: " + natGasResult + "%\n" + "Nuclear: " + nuclearResult + "%\n" + "Renewable: " + renewableResult + "%\n" + "Other: " + otherResult + "%\n";
                    return finalResult;
                }

                finally{
                    allFuelsUrlConnection.disconnect();
                    coalUrlConnection.disconnect();
                    natGasUrlConnection.disconnect();
                    nuclearUrlConnection.disconnect();
                    renewableUrlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        ProgressDialog loading_bar = new ProgressDialog(ElectricityFragment.this.getContext());


        protected void onPreExecute() {
            loading_bar.setTitle("Loading From the Server");
            loading_bar.setMessage("Searching for " + spinnerSelection + " electricity data");
            loading_bar.setCancelable(false); // disable dismiss by tapping outside of the dialog
            loading_bar.show();
        }

        protected void onPostExecute(String response) {
            setSources(finalResult);
            loading_bar.dismiss();
        }

        protected Double getCoal() {
            return Double.parseDouble(coalCO2) / Double.parseDouble(allFuelsCO2);
        }

        protected Double getNatGas() {
            return Double.parseDouble(natGasCO2) / Double.parseDouble(allFuelsCO2);
        }

        protected Double getNuclear() {
            return Double.parseDouble(nuclearCO2) / Double.parseDouble(allFuelsCO2);
        }

        protected Double getRenewables() {
            return Double.parseDouble(renewableCO2) / Double.parseDouble(allFuelsCO2);
        }

        protected Double getOther() {
            return 100.00 - (this.getCoal() + this.getNatGas() + this.getNuclear() + this.getRenewables());
        }
    }

}
