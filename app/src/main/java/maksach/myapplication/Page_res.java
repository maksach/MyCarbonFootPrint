package maksach.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class Page_res extends AppCompatActivity {

    double[] keyset = new double[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_res);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "My FootPrint for today was " + total_Co2() + "!";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        setTitle("Daily Foot Print");
        //preset for testing

        keyset = getIntent().getDoubleArrayExtra("detail").clone();


        //set description
        des_set();
    }

    private void des_set()
    {
        // set upper description
        TextView car_text = (TextView) findViewById(R.id.car_text);
        car_text.setText(string_des("car"));
        TextView food_text = (TextView) findViewById(R.id.food_text);
        food_text.setText(string_des("food"));
        TextView elec_text = (TextView) findViewById(R.id.elec_text);
        elec_text.setText(string_des("elec"));
        //lower number
        TextView carbon_number = (TextView) findViewById(R.id.number_total);
        carbon_number.setText( total_Co2 ()+" kg");
        TextView tree_number = (TextView) findViewById(R.id.tree_number);
        tree_number.setText(cal_tree()+"");
        //lower description
        TextView tree_offset = (TextView) findViewById(R.id.off_des);
        tree_offset.setText(total_Co2()+" kg CO2 = "+ cal_tree() +" tree(s)");

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference totalCO2DB = database.getReference("total");

        totalCO2DB.setValue(total_Co2());
    }

    private String string_des(String type)
    {
        int index = 0;
        if (type.equals("food"))
       {
           index =1;
       }else if (type.equals("elec"))
       {
          index =2;
       }
       return keyset[index]+" KG";
    }


    private double total_Co2 ()
    {
        double kg = keyset[0]+keyset[1]+ keyset[2];
        DecimalFormat df = new DecimalFormat("#.##");
        kg = Double.valueOf(df.format(kg));
        return kg;
    }
    private int cal_tree()
    {

        return (int) Math.ceil(total_Co2()/22);
    }
}
