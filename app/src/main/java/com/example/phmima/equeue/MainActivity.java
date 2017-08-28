package com.example.phmima.equeue;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.phmima.equeue.TabFragment.tabLayout;

public class MainActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tab = tabLayout.getSelectedTabPosition();

                if (tab == 1){
                    Intent intent = new Intent(MainActivity.this, AddDestinationActivity.class);
                    MainActivity.this.startActivity(intent);
                }
                else if (tab == 2){
                    Intent intent = new Intent(MainActivity.this, AddNews.class);
                    MainActivity.this.startActivity(intent);
                }

            }
        });

        setTitle("Barker Application");
        if (Config.APP_TYPE == 2){
            fab.hide();
            setTitle("Passenger Application");
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                if (extras.containsKey("queue_message")) {
                    String queue_message = extras.getString("queue_message");
                    Toast.makeText(MainActivity.this, queue_message, Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        if (Config.APP_TYPE == 1){
            adapter.addFragment(new BarkerHomeFragment(), "Home");
            adapter.addFragment(new BarkerDestinationFragment(), "Queue");
            adapter.addFragment(new NewsFragment(), "News");
        }
        else if (Config.APP_TYPE == 2){
            adapter.addFragment(new PassengerHomeFragment(), "Home");
            adapter.addFragment(new TerminalFragment(),"Terminal");
            adapter.addFragment(new NewsFragment(), "News");
        }

        viewPager.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
