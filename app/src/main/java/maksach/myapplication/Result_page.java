package maksach.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Result_page extends AppCompatActivity {

    private LinearLayout ResultPage;
    private Double carbon_weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);
        //extra thing to do

        //get message from main
        ResultPage = (LinearLayout)findViewById(R.id.result);

        Intent intent = getIntent();
        carbon_weight = intent.getExtras().getDouble("Carbon_Weight");
        //draw the result
        EditText carbon = (EditText)this.findViewById(R.id.Carbon);
        carbon.setText(carbon_weight.toString());
        int trees = (int)Cal_tree(carbon_weight);
        createConclusion(carbon_weight,trees);
        createImage("tree",trees);


    }

    public void on_click_cal(View view)
    {
        //clear conclusion page
        ResultPage.removeAllViews();
        //do the calculation
        EditText carbon = (EditText)this.findViewById(R.id.Carbon);

        carbon_weight = Double.parseDouble(carbon.getText().toString());
        int trees = (int)Cal_tree(carbon_weight);


        //add textural conclusion
        createConclusion(carbon_weight,trees);
        createImage("tree",trees);
    }

    private double Cal_paper(Double kg)
    {
        return 0;
    }


    private double Cal_tree(Double kg)
    {
        return  Math.ceil(kg/22);
    }



    // create the text representation
    private void createConclusion(Double carbon, int tree)
    {
        TextView conclusion = new TextView(this);
        conclusion.setText(carbon+" of carbon emitted,"+ tree +" tree(s) needed to be planted to offset per year!");
        ResultPage.addView(conclusion);
    }

    //draw image
    private void createImage(String type_c, int number) {
        int row_num = number / 10 + 1;
        int last = number % 10;
        int temp;

        while (row_num > 0)
        {
            if (row_num == 1)
            {
                temp = last;
            } else {
                temp = 10;
            }

            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);

            while (temp > 0) {
                ImageView new_img = new ImageView(this);
                switch (type_c) {
                    case "tree":
                        new_img.setImageResource(R.drawable.tr);
                        break;
                    case "paper":
                        new_img.setImageResource(R.drawable.pa);
                        break;
                    case "wood":
                        break;
                }

                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(100, 100);
                new_img.setLayoutParams(parms);
                row.addView(new_img);
                temp--;
            }
            ResultPage.addView(row);
            row_num--;
        }
    }
}
