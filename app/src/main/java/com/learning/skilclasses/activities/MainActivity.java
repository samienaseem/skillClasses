package com.learning.skilclasses.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.learning.skilclasses.R;
import com.learning.skilclasses.Utilities.ApiUrl;
import com.learning.skilclasses.authentication.LoginActivity;
import com.learning.skilclasses.fragments.AssignmentFragment;
import com.learning.skilclasses.fragments.DoubtsFragment;
import com.learning.skilclasses.fragments.HomeFragment;
import com.learning.skilclasses.fragments.QuizFragment;
import com.learning.skilclasses.fragments.VideosFragment;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.ibrahimsn.lib.OnItemReselectedListener;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemReselectedListener {

    View view;
    Fragment doubtFragment;
    BottomNavigationView bottomNavigationView;
    NavController navController;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    ConnectivityManager.NetworkCallback networkCallback;
    private GoogleSignInClient mGoogleSignInClient;
    private UserSession userSession;
    private OkHttpClient okHttpClient;
    private HashMap<String, String> userDetails;
    private GoogleSignInOptions gso;
    /*String[] type= { "MCQ Question", "TEST SERIES"};

    Spinner spinner;*/

    @BindView(R.id.bottomBar)
    SmoothBottomBar bottomBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout, new HomeFragment());
        fragmentTransaction.commit();
        /*spinner= (Spinner) bottomNavigationView.getMenu().findItem(R.id.quiz).getActionView();
        spinner.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,type));*/

        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {

                switch (i) {
                    case 0:
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
                        fragmentTransaction.commit();
                        break;
                    case 1:
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.frame_layout, new VideosFragment());
                        fragmentTransaction1.commit();
                        break;
                    case 2:
                        FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction4.replace(R.id.frame_layout, new AssignmentFragment());
                        fragmentTransaction4.commit();
                        break;
                    case 3:

                        /*PopupMenu popupMenu=new PopupMenu(MainActivity.this,bottomBar);
                        popupMenu.getMenuInflater().inflate(R.menu.test_type, popupMenu.getMenu());

                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });

                        popupMenu.show();*///showing popup menu
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame_layout, new QuizFragment());
                        fragmentTransaction2.commit();
                        break;
                    case 4:
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.frame_layout, new DoubtsFragment());
                        fragmentTransaction3.commit();
                        break;
                }
                return false;
            }
        });
        bottomBar.setOnItemReselectedListener(new OnItemReselectedListener() {
            @Override
            public void onItemReselect(int i) {
                /*if (i==3){
                    PopupMenu popupMenu=new PopupMenu(MainActivity.this,bottomBar.);
                    popupMenu.getMenuInflater().inflate(R.menu.test_type, popupMenu.getMenu());

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });

                    popupMenu.show();
                }*/

            }
        });

        if (savedInstanceState != null) {
            doubtFragment = getSupportFragmentManager().getFragment(savedInstanceState, "DoubtFragment");
        }


        userSession = new UserSession(this);
        userDetails = userSession.getUserDetails();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        userSession = new UserSession(this);
        drawerLayout = findViewById(R.id.my_drawer);
        navigationView = findViewById(R.id.navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Skil Classes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        view = navigationView.getHeaderView(0);
//        AsyncTask.execute(() -> {
//            try {
//                displayPicture();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        try {
//            displayPicture();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        ((TextView) view.findViewById(R.id.name)).setText(userDetails.get(UserSession.KEY_NAME));
        (view.findViewById(R.id.open_profile)).setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        navController = Navigation.findNavController(this, R.id.main_fragment);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);
//        bottomNavigationView.setOnNavigationItemReselectedListener(this);
//        Bundle bundle = navController.saveState();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.my_content:
                Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
                startActivity(intent);
                break;
            case R.id.setting:

                break;
            case R.id.share:

                break;
            case R.id.logout:
                signOut();
                break;
            case R.id.Test_Item:
                Intent intent1 = new Intent(MainActivity.this, TestActivty.class);
                startActivity(intent1);
                break;

        }
        return true;
    }


    protected void signOut() {
        String b = userDetails.get(UserSession.KEY_LOGIN);
        if (b.equals("false")) {
            try {
                HashMap<String, String> userDetails = userSession.getUserDetails();
                String statusUpdate = updateLoginStatus(ApiUrl.UPDATE_LOGIN_STATUS, userDetails.get(UserSession.KEY_ID), "false");
                Log.d("update", statusUpdate);
                JSONObject jsonObject1 = new JSONObject(statusUpdate);
                if (jsonObject1.getString("id").equals(userDetails.get(UserSession.KEY_ID))) {
                    Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
                    userSession.logoutUser();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finishAffinity();
                } else {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    try {
                        String statusUpdate = updateLoginStatus(ApiUrl.UPDATE_LOGIN_STATUS, userDetails.get(UserSession.KEY_ID), "false");
                        Log.d("update", statusUpdate);
                        JSONObject jsonObject1 = new JSONObject(statusUpdate);
                        if (jsonObject1.getString("id").equals(userDetails.get(UserSession.KEY_ID))) {
                            //    Toast.makeText(MainActivity.this, "Google Logged Out", Toast.LENGTH_SHORT).show();
                            userSession.logoutUser();
                            Intent i = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                            finishAffinity();
                        } else {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String updateLoginStatus(String url, String id, String loginStatus) throws Exception {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", id)
                .addFormDataPart("login", loginStatus)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

//    public String getUserProfilePicture(String url, String id) throws Exception {
//        if (okHttpClient == null) {
//            okHttpClient = new OkHttpClient();
//        }
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("id", id)
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .build();
//        try (Response response = okHttpClient.newCall(request).execute()) {
//            return response.body().string();
//        }
//    }

//    public void displayPicture() {
//        runOnUiThread(() -> {
//            try {
//                String picture_url = getUserProfilePicture(ApiUrl.GET_USER_PROFILE_PIC, userSession.getUserDetails().get(UserSession.KEY_ID));
//                JSONObject jsonObject = new JSONObject(picture_url);
//                Glide.get(this).clearMemory();
//                if (jsonObject.getBoolean("status")) {
//                    Glide.with(this).load(jsonObject.getString("image")).into(((ImageView) view.findViewById(R.id.profile_image)));
//                } else {
//                    Glide.with(this).load(R.drawable.user).into(((ImageView) view.findViewById(R.id.profile_image)));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        return;
    }

    public void openPDF(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.digitalcatnyx.store/Coaching/admin/uploads/assignments/" + url)
            );
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
