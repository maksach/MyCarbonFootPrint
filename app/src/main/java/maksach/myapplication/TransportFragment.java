package maksach.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.*;
import android.widget.EditText;
import android.widget.*;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TransportFragment extends Fragment implements OnClickListener {
    //info stack
    //info stack
    private static final String TAG = "TransportFragment";
    private View current_view;
    String temp_url ="";
    String base_url ="http://www.fueleconomy.gov/ws/rest/vehicle/menu/";
    String[] stack = new String[3];
    private ArrayList<String> year_list;
    private ArrayList<String> car_list;
    private ArrayList<String> model_list;
    private String stringTotalCarbon;

    OnDataPass dataPasser;

    public interface OnDataPass {
        public void onTransportDataPass(String data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

    public void passData(String data) {
        dataPasser.onTransportDataPass(data);
    }

    //run when create the page
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transport_fragment, container, false);
        current_view = view;

        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }
        //add listener to button
        Button button = (Button) view.findViewById(R.id.button_calcTransport);
        button.setOnClickListener(this);


        //setup  years list for choices
        year_list = new ArrayList<>();
        loadingXMLFromUrl a = new loadingXMLFromUrl();
        temp_url= base_url+"year";
        a.execute();

        year_list = a.head();
        year_list.add("Select");

        init_year_list(view);

        return view;
    }


    // UI construction--------------------------------------------------------------------------------------------
    private void init_year_list(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.car_year);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransportFragment.this.getContext(), android.R.layout.simple_spinner_item, year_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new YearOnItemSelectedListener());

    }
    private void init_car_list(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.car_make);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransportFragment.this.getContext(), android.R.layout.simple_spinner_item, car_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MakeItemSelectedListener());

    }
    private void init_model_list(View view) {
        Spinner spinner = (Spinner) view.findViewById(R.id.car_model);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TransportFragment.this.getContext(), android.R.layout.simple_spinner_item, model_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new set_MGP_ItemSelectedListener());

    }

    private void setMPG(String a)
    {
        EditText mpg_box = (EditText) current_view.findViewById(R.id.car_mpg);
        mpg_box.setText(a);
    }
//------------------------------------------------------------------------------------------------------------------------------
///    Calculation
    //calculate button to calu
    @Override
    public void onClick(View view) {

        try {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }

        EditText mpgInput = (EditText) getView().findViewById((R.id.car_mpg));
        EditText milesInput = (EditText) getView().findViewById((R.id.miles_driven));


        try {
            Double mpg = ConvertNumber(mpgInput.getText().toString());
            Double miles = ConvertNumber(milesInput.getText().toString());
            //Double car_c02=CovertNumber(carbon.getText().toString());
            //Double lightning= CovertNumber(electric.getText().toString());
            Double total_carbon = calculateTransportation(mpg, miles);

            TextView transportResult = (TextView) getView().findViewById(R.id.transportResult);
            stringTotalCarbon = String.format("%.2f", total_carbon);
            transportResult.setText(stringTotalCarbon + "kg");

            passData(stringTotalCarbon);

            ImageView co2image = (ImageView) getView().findViewById(R.id.co2transport);
            co2image.setBackgroundResource(R.drawable.co2);

        } catch (Exception e_e) {
            new AlertDialog.Builder(TransportFragment.this.getContext()).setMessage("Please Enter In Correct Number Format").show();
        }

    }

    public String getTransportCarbon() {
        return stringTotalCarbon;
    }

    //just if entering a number not a text empty spot will result in 0
    private Double ConvertNumber(String number) throws Exception {
        if (number.equals("")) {
            return 0.0;
        } else {
            if (number.matches("[0-9]+")) {
                return Double.parseDouble(number);
            } else {
                throw new Exception();

            }

        }

    }

    ////////////////////////////c
    //////////////////////////
    // calculation
    public Double calculateTransportation(Double mpgValue, Double milesDriven) {
        //https://www.epa.gov/sites/production/files/2016-02/documents/420f14040a.pdf
        //8.91kg of co2 emitted for a gallon of fuel if it does not have ethanol (10.15 with ethanol)
        double carbonGas = (milesDriven / mpgValue) * 8.91;
        return carbonGas;
    }



//server manipulaation-----------------------------------------------------------------------------------------------------------
   // get year and make and model form server
    private class loadingXMLFromUrl extends
            AsyncTask<Void, Void, ArrayList<String>> {
        ArrayList<String> years = new ArrayList<>();

        @Override
        protected ArrayList<String> doInBackground(
                Void... url_str) {

            try {
                URL url;
                url = new URL(temp_url);
                URLConnection conn = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputStream a = conn.getInputStream();
                Document doc = builder.parse(a);
                NodeList nodes = doc.getElementsByTagName("menuItem");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Element element = (Element) nodes.item(i);
                    years.add(element.getFirstChild().getTextContent());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return years;
        }

    ProgressDialog loading_bar = new ProgressDialog(TransportFragment.this.getContext());
    @Override
    protected void onPreExecute()
    {

        loading_bar.setTitle("Loading");
        loading_bar.setMessage("Searching for selected Info");
        loading_bar.setCancelable(false); // disable dismiss by tapping outside of the dialog
        loading_bar.show();
    }
    //update UI when done
    @Override
    protected void onPostExecute( ArrayList<String> file_url)
    {

        loading_bar.dismiss();
    }


        public ArrayList<String> head() {
            return years;
        }
    }

    //load MPG from server
    private class MPG_loader extends
            AsyncTask<Void, Void, String> {
        String str ="";
        @Override
        protected String doInBackground(
                Void... url_str) {

            try {
                URL url;
                url = new URL(temp_url);
                URLConnection conn = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputStream a = conn.getInputStream();
                Document doc = builder.parse(a);
                //different rule
                NodeList nodes = doc.getElementsByTagName("vehicle");
                Element element = (Element) nodes.item(0);
                NodeList temp = element.getElementsByTagName("city08");
                Element element2= (Element) temp.item(0);
                str = element2.getTextContent();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        }
        ProgressDialog loading_bar = new ProgressDialog(TransportFragment.this.getContext());
        @Override
        protected void onPreExecute()
        {

            loading_bar.setTitle("Loading From the Server");
            loading_bar.setMessage("Searching for "+stack[0]+" "+stack[1]+" "+stack[2]);
            loading_bar.setCancelable(false); // disable dismiss by tapping outside of the dialog
            loading_bar.show();
        }
        //update UI when done
        @Override
        protected void onPostExecute(String file_url)
        {
            setMPG(str);
            loading_bar.dismiss();
        }

        public String head() {
            return str;
        }


    }




    ////Listeners --------------------------------------------------------------------------------------------------------------------
    //listener for the first list
    public class YearOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            String selected=parent.getItemAtPosition(pos).toString();

        if(selected!="Select")
        {

            temp_url= base_url + "make?year="+selected;
           stack[0]=selected;
            loadingXMLFromUrl car_makers = new loadingXMLFromUrl();
            car_makers.execute();
            car_list = car_makers.head();
            car_list.add("Select");
            init_car_list(current_view);
            //a new thread
        }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
//listener for the second list
    public class MakeItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            String selected=parent.getItemAtPosition(pos).toString();

            if(selected!="Select")
            {
                temp_url= base_url + "model?year="+stack[0]+"&make="+selected;
                stack[1]=selected;

                loadingXMLFromUrl model_lister = new loadingXMLFromUrl();
                model_lister.execute();
                model_list = model_lister.head();
                model_list.add("Select");
                init_model_list(current_view);
                //a new thread
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }
 //listener for thired list
    public class set_MGP_ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
        {
            String selected=parent.getItemAtPosition(pos).toString();
            if(selected!="Select")
            {
                stack[2]=selected;
                if(selected.contains(" "))
                {
                    selected.replaceAll(" ","%20");
                }
                temp_url= "http://www.fueleconomy.gov/ws/rest/ympg/shared/vehicles?make="+stack[1]+"&model="+stack[2];
                //loading xml with different rules
                MPG_loader mp = new MPG_loader();
                mp.execute();
                setMPG(mp.head());
                //a new thread
            }
        }

        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
        }
    }





}