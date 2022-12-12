package com.example.my_ecommerce;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.my_ecommerce.Aapter.ProductAdapter;
import com.example.my_ecommerce.Prevalent.Prevalent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    Fragment fragment;
    private RecyclerView.Recycler recycler ;
    private ProductAdapter productAdapter;
    private DatabaseReference databaseReference;
    private String type="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        View ovrlay=findViewById(R.id.drawer_layout);
        ovrlay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY|View.SYSTEM_UI_FLAG_FULLSCREEN);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            type=getIntent().getExtras().get("Admin").toString();
        }
        else
        {
            type="User";
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!type.equals("Admin"))
                {
                    startActivity(new Intent(HomePageActivity.this,CartActivity.class));
                }

            }
        });
         drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView=navigationView.getHeaderView(0);
        TextView UserNameView=headerView.findViewById(R.id.user_profile_name);
        CircleImageView ProileImageView=headerView.findViewById(R.id.user_profile_image);
        if(!type.equals("Admin"))
        {
            UserNameView.setText(Prevalent.onlineUsers.getName());
            Picasso.get().load(Prevalent.onlineUsers.getImage()).placeholder(R.drawable.profile).into(ProileImageView);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
     /*  mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_cart, R.id.nav_orders, R.id.nav_categories,R.id.nav_settings,R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
        fragment= new HomeFragment(type);
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.commit();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
            {
                fragment= new HomeFragment(type);
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout,fragment);
                fragmentTransaction.commit();
                break;
            }
            case R.id.nav_cart: {
                if(!type.equals("Admin"))
                {
                    startActivity(new Intent(HomePageActivity.this,CartActivity.class));
                }
                break;
            }
            case R.id.nav_search:
            {
                if(!type.equals("Admin"))
                {
                    Intent intent=new Intent(HomePageActivity.this,SearchProductsActivity.class);
                    startActivity(intent);
                }

            }
            case R.id.nav_categories: {

                if(!type.equals("Admin")){
                fragment= new CategoryFragment();
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.framelayout,fragment);
                fragmentTransaction.commit();}
                break;
            }
            case R.id.nav_settings: {
                if(!type.equals("Admin"))
                {
                    Intent intent=new Intent(HomePageActivity.this,SettingsActivity.class);
                    startActivity(intent);
                }


                break;
            }
            case R.id.nav_logout: {
                if(!type.equals("Admin"))
                {
                    Paper.book().destroy();
                    Intent intent=new Intent(HomePageActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}