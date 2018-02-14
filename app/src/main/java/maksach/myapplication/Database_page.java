package maksach.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class Database_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_page2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.databaseToolbar);
        toolbar.setTitle("Summary");
        setSupportActionBar(toolbar);
    }

}
