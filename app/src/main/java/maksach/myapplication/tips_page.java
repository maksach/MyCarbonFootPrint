package maksach.myapplication;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class tips_page extends AppCompatActivity {



    //prototype
    double[] keyset = new double[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //present
        preset();

        String[] monthName = { "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December" };

        Calendar cal = Calendar.getInstance();
        String month = monthName[cal.get(Calendar.MONTH)];

        //set tittle
        toolbar.setTitle(month);
        //toolbar.setBackground();
        setSupportActionBar(toolbar);

        set_view();
        //loading tips
        loading_tips();
        //set_up tips
        set_up_tips();
        set_listener();

    }
    //////this is what needed to be passing in
    private void preset()
    {
        //month number
        keyset[0]=11.00;
        //month average
        keyset[1]=200.4;
        //month average of other for comparing
        keyset[2]= 123;
    }

    private void set_view()
    {
        TextView Cons = (TextView)  findViewById(R.id.su_cons);
        //set monthly used value
        Double used = formatdouble(keyset[1]);
        Cons.setText(used.toString());

        //compare average with announced value
        TextView compare_text = (TextView)  findViewById(R.id.su_average);
        TextView pt = (TextView)  findViewById(R.id.su_percent);
        AppBarLayout as =  (AppBarLayout)  findViewById(R.id.app_bar);

        if(used<keyset[2])
        {
            //less co2
            Double percent    = formatdouble((1 - keyset[1]/keyset[2])*100 );
            pt.setText(percent.toString()+"%");
            compare_text.setText("Below Average");
            as.setBackgroundResource(R.drawable.bk_green);
        }
        else
        {
            //more co2
            Double percent    = formatdouble(( keyset[1]/keyset[2]-1)*100 );
            pt.setText(percent.toString()+"%");
            compare_text.setText( "Above Average");
            as.setBackgroundResource(R.drawable.bk_exceed);
        }
    }

    private double formatdouble (Double db)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(db));
    }

    //Set Month key words
    private String GetMonth(Double a)
    {
        String[] str = {"January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"};
        return str[a.intValue()-1];
    }

    ArrayList<String> tips  = new ArrayList<String>();
    private void loading_tips()
    {

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("tips.txt")));
            String tmp;
            while ((tmp = reader.readLine()) != null)
            {
                tips.add(tmp);
            }
        } catch (IOException e) {
        }
    }

    int index =0;
    private void last()
    {
        if(index==0)
        {
            index = tips.size()-1;
        }
        else
        {
            index --;
        }
        set_up_tips();
    }
    private void next()
    {
        if(index==(tips.size()-1))
        {
            index =0;
        }
        else
        {
            index ++;
        }
        set_up_tips();
    }

    private void set_up_tips()
    {
        TextView tp = (TextView)  findViewById(R.id.tip_box);
        tp.setText(tips.get(index));
    }

    private void set_listener()
    {
        Button last = (Button)  findViewById(R.id.last_but);
        Button next = (Button)  findViewById(R.id.nex_but);

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                last();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
    };
}

