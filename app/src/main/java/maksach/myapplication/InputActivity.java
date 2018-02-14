package maksach.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class InputActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TransportFragment.OnDataPass, FoodFragment.OnDataPass, ElectricityFragment.OnDataPass, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "InputActivity";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private String transportCarbon;
    private String foodCarbon;
    private String electricityCarbon;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInAccount acct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        acct = getIntent().getParcelableExtra("account");
        System.out.println(acct.getDisplayName());
        System.out.println(acct.getEmail());


        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        /////////////////  float button - activities
        /// view change when last page turn to the result page
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mViewPager.getCurrentItem() == 2) {
                    double[] keyset = new double[3];
                    //preset for testing
                    keyset[0] = Double.parseDouble(formator(transportCarbon));
                    keyset[1] = Double.parseDouble(formator(foodCarbon));
                    keyset[2] = Double.parseDouble(formator(electricityCarbon));
                    //
                    //go to calculation page
                    Intent switch_result = new Intent(InputActivity.this, Page_res.class);
                    switch_result.putExtra("detail", keyset);
                    startActivity(switch_result);
                } else {
                    //go to next page
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }
            }
        });
        ///// navigation page action /////
        //seting up tool bar and navigation bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Daily Inputs");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.getHeaderView(0);
        TextView sideBarName = (TextView) navView.findViewById(R.id.name);
        sideBarName.setText(acct.getDisplayName());
        TextView sideBarEmail = (TextView) navView.findViewById(R.id.email);
        sideBarEmail.setText(acct.getEmail());
        ImageView profilePic = (ImageView) navView.findViewById(R.id.profilePic);
        //profilePic.setImageURI(acct.getPhotoUrl());
    }

    //for error handling for the input array
    private String formator(String a)
    {
        if(a==null)
        {return "0.00";}
        else
        {
            return a;
        }
    }
    //setting the 3 frags
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TransportFragment(), "Transport");
        adapter.addFragment(new FoodFragment(), "Food");
        adapter.addFragment(new ElectricityFragment(), "Electricity");
        viewPager.setAdapter(adapter);
    }
    @Override
    public void onTransportDataPass(String data) {
        transportCarbon = data;
    }

    @Override
    public void onFoodDataPass(String data) {
        foodCarbon = data;
    }

    @Override
    public void onElectricityDataPass(String data) {
        electricityCarbon = data;
    }

    // ------------------------------------------------------------------------------------------------------
    //drawer handling
    //source code from Google Project Sample

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //action after clicker the selected one// menu is  in @menu/activity_main_drawer.xml
        if (id == R.id.summary_page) {
            //switch to summary page
            Intent switch_result = new Intent(InputActivity.this, Database_page.class);
            startActivity(switch_result);
            // Handle the camera action
        } else if (id == R.id.records) {
            //switch to summary page
            //Intent switch_result = new Intent(this, InputActivity.class);
            //startActivity(switch_result);
            // Handle the camera action
        } else if(id == R.id.tips) {
            //switch to summary page
            Intent switch_result = new Intent(InputActivity.this, tips_page.class);
            startActivity(switch_result);
            // Handle the camera action
        } else if(id == R.id.settings) {
            Intent switch_result = new Intent(InputActivity.this, Settings_page.class);
            startActivity(switch_result);
        } else if (id == R.id.nav_share) {
            double[] keyset = new double[3];
            //preset for testing
            keyset[0] = Double.parseDouble(formator(transportCarbon));
            keyset[1] = Double.parseDouble(formator(foodCarbon));
            keyset[2] = Double.parseDouble(formator(electricityCarbon));
            //
            //go to calculation page
            Intent switch_result = new Intent(InputActivity.this, Page_res.class);
            switch_result.putExtra("detail",keyset);
            startActivity(switch_result);
        } else if (id == R.id.sign_out) {

            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            Intent mainIntent = new Intent(InputActivity.this, SignInActivity.class);
            startActivity(mainIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
