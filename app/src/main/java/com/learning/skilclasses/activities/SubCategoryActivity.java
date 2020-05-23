package com.learning.skilclasses.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
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
import com.learning.skilclasses.adapters.SubCategoriesAdapter;
import com.learning.skilclasses.models.SubCategoriesModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubCategoryActivity extends AppCompatActivity {

    @BindView(R.id.subcategories_recycler)
    RecyclerView recyclerView;
    boolean updateCourse;
    List<SubCategoriesModel> categoriesList;
    SubCategoriesAdapter adapter;
    String category;
    String url = "http://www.digitalcatnyx.store/api/allCategory.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        ButterKnife.bind(this);
        category = getIntent().getStringExtra("category");
        updateCourse = getIntent().getBooleanExtra("update",false);
        fetchClassDetails();
    }

    private void fetchClassDetails() {
        try {
            categoriesList = new ArrayList<>();
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                categoriesList.clear();
                                Log.d("SUC_RES", response);

                                //JSONObject jsonObject = new JSONObject(response);

                                //Toast.makeText(getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                // progressBar.setVisibility(View.GONE);
                                JSONArray array = new JSONArray(response);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject detail = array.getJSONObject(i);
                                    categoriesList.add(new SubCategoriesModel(detail.getString("category"), detail.getString("price")));
                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(SubCategoryActivity.this, LinearLayoutManager.VERTICAL, false));

                                if (updateCourse)
                                adapter = new SubCategoriesAdapter(categoriesList, SubCategoryActivity.this, category,true);
                                else
                                adapter = new SubCategoriesAdapter(categoriesList, SubCategoryActivity.this, category,false);

                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERROR_MESSAGE", error.getMessage() + "");
                    //     Toast.makeText(getContext(), "Error" + error.toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<>();
                    params.put("class", category.replace(" ",""));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SubCategoryActivity.this);
            requestQueue.add(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
