package com.example.bluefireradio;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseUser user;

    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        AssetManager am = MainActivity.this.getApplicationContext().getAssets();
        TextView appName = (TextView) headerView.findViewById(R.id.app_name);
        Typeface typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "timeburnerbold.ttf"));
        appName.setTypeface(typeface);
        TextView displayName = (TextView) headerView.findViewById(R.id.display_name);
        TextView email = (TextView) headerView.findViewById(R.id.email);

        if(FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            user = FirebaseAuth.getInstance().getCurrentUser();
            displayName.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);

            displayName.setText(user.getDisplayName());
            email.setText(user.getEmail());
        }
        else
        {
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        switch(id) {
            case R.id.nav_home:
                fragmentTransaction.replace(R.id.container, HomeFragment.newInstance());
                break;
            case R.id.nav_browse_songs:
                fragmentTransaction.replace(R.id.container, MusicFragment.newInstance());
                break;
            case R.id.nav_playlists:
                fragmentTransaction.replace(R.id.container, PlaylistFragment.newInstance());
                break;
            case R.id.nav_settings:
                Toast.makeText(this, "You selected settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_log_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }

        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
