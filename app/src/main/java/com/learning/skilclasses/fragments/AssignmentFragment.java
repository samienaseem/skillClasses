package com.learning.skilclasses.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.R;
import com.learning.skilclasses.adapters.AssignmentAdapter;
import com.learning.skilclasses.models.AssignmentBean;
import com.learning.skilclasses.preferences.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AssignmentFragment extends Fragment {

    // Spinner spinner;
    // String classtype;
    TextView viewAssignment;
    List<AssignmentBean> list;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    static int count = 0;
    String category;
    String subcategory;
    static Context activity;
    String student_class;
    AssignmentAdapter adapter1;
    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/assignment.php";

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

    UserSession userSession;

    public AssignmentFragment() {
        count++;
        System.out.println(count);
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Assignmnets");
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);
        //spinner=view.findViewById(R.id.ClassType);
        activity = getActivity();
        ButterKnife.bind(this, view);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        list = new ArrayList<AssignmentBean>();
        userSession = new UserSession(activity);
        category = userSession.getUserDetails().get(UserSession.KEY_CATEGORY);
        Log.d("S_CATEGORY", category);
        subcategory = userSession.getUserDetails().get(UserSession.KEY_SUBCATEGORY);
        //  student_class = userSession.getUserDetails().get(UserSession.KEY_COURSE);
        if (activity != null) {
            group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.choice_a:
                            fetchClassDetails(student_class, null);
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
                            fetchClassDetails(student_class, toggleB.getText().toString().trim());
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
                            fetchClassDetails(student_class, toggleC.getText().toString().trim());
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
                            fetchClassDetails(student_class, toggleD.getText().toString().trim());
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
                            fetchClassDetails(student_class, toggleE.getText().toString().trim());
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
            fetchClassDetails(student_class, null);
        }
        SearchView searchView = view.findViewById(R.id.search);
        searchView.onActionViewExpanded();
        searchView.setQueryHint("Search for Assignments...");
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               if (adapter1!=null)
                adapter1.getFilter().filter(newText);
                return false;
            }
        });
        return view;
    }

    private void fetchClassDetails(String _class, String subject) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, CLASS_URL,
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
                                recyclerView.setAdapter(adapter1);
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

    @Override
    public void onStart() {
        super.onStart();
        if (getActivity() == null) return;
    }
}
