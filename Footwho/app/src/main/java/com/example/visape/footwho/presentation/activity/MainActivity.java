package com.example.visape.footwho.presentation.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.visape.footwho.presentation.fragment.MyCompetitionsFragment;
import com.example.visape.footwho.R;
import com.example.visape.footwho.presentation.fragment.MyTeamsFragment;
import com.example.visape.footwho.presentation.fragment.OtherCompetitionsFragment;
import com.example.visape.footwho.presentation.fragment.OtherTeamsFragment;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private static final int MY_TEAMS = 0;
    private static final int OTHER_TEAMS = 1;
    private static final int MY_COMPETITIONS = 2;
    private static final int OTHER_COMPETITIONS = 3;
    private static final int SETTINGS = 4;
    private static final int ABOUT = 5;

    private static int currentFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create new fragment
        Fragment myTeamsFragment = new MyTeamsFragment();

        //Show the fragment
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, myTeamsFragment).commit();
        currentFragment = MY_TEAMS;

        //References
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_navigation);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(!item.isChecked()) item.setChecked(true);

                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.nav_my_teams:
                        Fragment myTeamsFragment = new MyTeamsFragment();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, myTeamsFragment).commit();
                        currentFragment = MY_TEAMS;
                        break;
                    case R.id.nav_other_teams:
                        Fragment otherTeamsFragment = new OtherTeamsFragment();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, otherTeamsFragment).commit();
                        currentFragment = OTHER_TEAMS;
                        break;
                    case R.id.nav_my_competitions:
                        Fragment myCompetitionsFragment = new MyCompetitionsFragment();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, myCompetitionsFragment).commit();
                        currentFragment = MY_COMPETITIONS;
                        break;
                    case R.id.nav_other_competitions:
                        Fragment otherCompetitionsFragment = new OtherCompetitionsFragment();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, otherCompetitionsFragment).commit();
                        currentFragment = OTHER_COMPETITIONS;
                        break;
                    case R.id.nav_settings:
                        //TODO: create fragment and replace
                        currentFragment = SETTINGS;
                        break;
                    case R.id.nav_about:
                        //TODO: create fragment and replace
                        currentFragment = ABOUT;
                        break;
                    default:
                }
                return false;
            }
        });
    }


    //Open lateral menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawers();
                } else {
                    navigationView.setCheckedItem(navigationView.getMenu().getItem(currentFragment).getItemId());
                    drawerLayout.openDrawer(navigationView);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
