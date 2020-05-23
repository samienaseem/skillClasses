package com.learning.skilclasses.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.learning.skilclasses.R;
import com.learning.skilclasses.adapters.AssignmentAdapter;
import com.learning.skilclasses.adapters.VideoAdapter2;
import com.learning.skilclasses.models.AssignmentBean;
import com.learning.skilclasses.models.Video;
import com.learning.skilclasses.preferences.UserSession;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {


    @BindView(R.id.group_choices)
    RadioGroup group;

    @BindView(R.id.choice_a)
    RadioButton toggleA;

    @BindView(R.id.choice_b)
    RadioButton toggleB;

    @BindView(R.id.choice_c)
    RadioButton toggleC;

    @BindView(R.id.choice_d)
    RadioButton toggleD;

    @BindView(R.id.choice_e)
    RadioButton toggleE;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.recycler_view1)
    RecyclerView recyclerView1;

    List<AssignmentBean> list;

    VideoAdapter2 videoAdapter;
    List<Video> videoList;
    static Context activity;
    UserSession userSession;
    String category;
    String subcategory;
    CarouselView carouselView;
    List<String> images;
    ImageListener imageListener;
    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/class_detail.php";
    private static String CLASS_URL1 = "http://www.digitalcatnyx.store/api/assignment.php";
    private String carouselURL = "http://www.digitalcatnyx.store/api/corousel_fetch.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Skil Classes");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        images = new ArrayList<>();
        carouselView = view.findViewById(R.id.carouselView);
        try {
            StringRequest request = new StringRequest(Request.Method.POST, carouselURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.d("CAROUSEL_IMAGE", response + "");
                                JSONArray array = new JSONArray(response);
                                Log.d("A_RESPONSE", array + "");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject detail = array.getJSONObject(i);
                                    images.add("http://www.digitalcatnyx.store/Coaching/admin/uploads/crousel/" + detail.getString("img"));
                                    Log.d("C_IMAGEA", images.get(i));
                                    carouselView.setImageListener(imageListener);
                                    carouselView.setPageCount(images.size());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //   Toast.makeText(getContext(), "Login Error" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Toast.makeText(getContext(), "Login Error" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageListener = (position, imageView) -> {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Log.d("IMGAES", "" + images.get(0) + "");
            Glide.with(getActivity()).load(images.get(position)).into(imageView);
        };
        activity = getActivity();
        list = new ArrayList<AssignmentBean>();
        userSession = new UserSession(activity);
        category = userSession.getUserDetails().get(UserSession.KEY_CATEGORY);
        Log.d("S_CATEGORY", category + "");
        subcategory = userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY);
        // category = userSession.getUserDetails().get(UserSession.KEY_COURSE);
//        try {
//            carouselView = view.findViewById(R.id.carouselView);
//            carouselView.setPageCount(images.size());
//            carouselView.setImageListener((position, imageView) -> {
//                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                Glide.with(this).load(images.get(position)).into(imageView);
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (activity != null) {
            getVideosList(CLASS_URL, category, subcategory, null);
            fetchClassDetailsAssignment(category, subcategory, null);
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.choice_a:
                            //fetchClassDetails(category, subcategory, null);
                            toggleA.setBackground(activity.getDrawable(R.drawable.checked_grade_background1));
                            toggleA.setTextColor(Color.WHITE);
                            toggleB.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleB.setTextColor(Color.BLACK);
                            toggleC.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleC.setTextColor(Color.BLACK);
                            toggleD.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleD.setTextColor(Color.BLACK);
                            toggleE.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleE.setTextColor(Color.BLACK);
                            break;
                        case R.id.choice_b:
                            //fetchClassDetails(category, subcategory, toggleB.getText().toString().trim());
                            toggleA.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleA.setTextColor(Color.BLACK);
                            toggleC.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleC.setTextColor(Color.BLACK);
                            toggleD.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleD.setTextColor(Color.BLACK);
                            toggleE.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleE.setTextColor(Color.BLACK);
                            toggleB.setBackground(activity.getDrawable(R.drawable.checked_grade_background1));
                            toggleB.setTextColor(Color.WHITE);
                            break;
                        case R.id.choice_c:
                            //fetchClassDetails(category, subcategory, toggleC.getText().toString().trim());
                            toggleA.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleA.setTextColor(Color.BLACK);
                            toggleC.setBackground(activity.getDrawable(R.drawable.checked_grade_background1));
                            toggleC.setTextColor(Color.WHITE);
                            toggleD.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleD.setTextColor(Color.BLACK);
                            toggleE.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleE.setTextColor(Color.BLACK);
                            toggleB.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleB.setTextColor(Color.BLACK);
                            break;
                        case R.id.choice_d:
                            //fetchClassDetails(category, subcategory, toggleD.getText().toString().trim());
                            toggleA.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleA.setTextColor(Color.BLACK);
                            toggleC.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleC.setTextColor(Color.BLACK);
                            toggleE.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleE.setTextColor(Color.BLACK);
                            toggleB.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleB.setTextColor(Color.BLACK);
                            toggleD.setBackground(activity.getDrawable(R.drawable.checked_grade_background1));
                            toggleD.setTextColor(Color.WHITE);
                            break;
                        case R.id.choice_e:
                            //fetchClassDetails(category, subcategory, toggleE.getText().toString().trim());
                            toggleA.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleA.setTextColor(Color.BLACK);
                            toggleC.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleC.setTextColor(Color.BLACK);
                            toggleD.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleD.setTextColor(Color.BLACK);
                            toggleB.setBackground(activity.getDrawable(R.drawable.message_layout_background));
                            toggleB.setTextColor(Color.BLACK);
                            toggleE.setBackground(activity.getDrawable(R.drawable.checked_grade_background1));
                            toggleE.setTextColor(Color.WHITE);
                            break;
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() == null) return;
    }

    public void getVideosList(String classUrl, String s, String s1, String sub) {

        StringRequest str = new StringRequest(Request.Method.POST, classUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    videoList = new ArrayList<>();
                    videoList.clear();
                    JSONArray jarray = new JSONArray(response);
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject detail = jarray.getJSONObject(i);
                        getLink(detail.getString("id"),
                                detail.getString("class"),
                                detail.getString("category"),
                                detail.getString("subcategory"),
                                detail.getString("video_descp"),
                                detail.getString("video_file"),
                                detail.getString("file_extension"),
                                detail.getString("date")
                        );
                        // Toast.makeText(activity,detail.getString("video_file"),Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("category", s);
                params.put("subcategory", s1);
                if (sub != null) {
                    params.put("subject", sub.toLowerCase());

                }
                return params;
            }

            ;

        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(str);

    }

    public void getLink(String id, String aClass, String category, String subcategory, String video_descp, String video_file, String file_extension, String date) {

        video_file = video_file.replace("https://vimeo.com/", "https://player.vimeo.com/video/");
        video_file += "/config";
        String videoFile = video_file;
        //Toast.makeText(activity,""+video_file,Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, video_file, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Map<String, String> urls = new HashMap<String, String>();

                try {
                    JSONObject josn = new JSONObject(response);
                    JSONObject json1 = josn.getJSONObject("request");
                    JSONObject json2 = json1.getJSONObject("files");
                    JSONArray videos = json2.getJSONArray("progressive");

                    for (int i = 0; i < videos.length(); i++) {
                        JSONObject vurl2 = videos.getJSONObject(i);
                        urls.put(vurl2.getString("quality"), vurl2.getString("url"));
                    }
                    JSONObject vurl = videos.getJSONObject(1);
                    String videoUrl = vurl.getString("url");

                    JSONObject thumbnail = josn.getJSONObject("video");
                    JSONObject thumbImg = thumbnail.getJSONObject("thumbs");

                    //Toast.makeText(activity,""+thumbImg.getString("640"),Toast.LENGTH_LONG).show();

                    videoList.add(new Video(id, aClass, category, subcategory, video_descp, videoFile, file_extension, date, videoUrl, thumbImg.getString("640"), urls));

                    recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    videoAdapter = new VideoAdapter2(videoList, activity);
                    videoAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(videoAdapter);
                    videoAdapter.notifyDataSetChanged();


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


    private void fetchClassDetailsAssignment(String category, String subcategory, String subject) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, CLASS_URL1,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                list.clear();
                                JSONArray array = new JSONArray(response);
                                Log.d("A_RESPONSE", array + "");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject detail = array.getJSONObject(i);
                                    list.add(new AssignmentBean(detail.getString("id"),
                                            detail.getString("class"),
                                            detail.getString("category"),
                                            detail.getString("subcategory"),
                                            detail.getString("assignment_descp"),
                                            detail.getString("assign_file"),
                                            detail.getString("file_extension"),
                                            detail.getString("date")));
                                }
                                AssignmentAdapter adapter1 = new AssignmentAdapter(getContext(), list);
                                recyclerView1.setAdapter(adapter1);
                                recyclerView1.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //   Toast.makeText(getContext(), "Login Error" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // Toast.makeText(getContext(), "Login Error" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("category", category);
                    params.put("subcategory", subcategory);
                    if (subject != null) {
                        params.put("subject", subject.toLowerCase());

                    }
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
