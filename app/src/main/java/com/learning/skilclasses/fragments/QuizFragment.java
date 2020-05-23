package com.learning.skilclasses.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.learning.skilclasses.R;
import com.learning.skilclasses.activities.QuizActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class QuizFragment extends Fragment {

    private static String CLASS_URL = "http://www.digitalcatnyx.store/api/getListOptions.php";
    List TestList;
    Object testname;

    @BindView(R.id.spinner)
    Spinner spinner;

    @OnClick(R.id.start)
    void startQuiz() {
        if (testname==spinner.getItemAtPosition(0))
        Toast.makeText(getContext(),"You have not seleted any Quiz ",Toast.LENGTH_LONG).show();
        else{
          Intent intent = new Intent(getContext(), QuizActivity.class);
          intent.putExtra("papername",testname.toString());
        startActivity(intent);
        }

    }

    public QuizFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Quiz");
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        TestList=new ArrayList();
        TestList.add("Select MCQ's Series");
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        StringRequest stringRequest= new StringRequest(Request.Method.POST, CLASS_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray testArray=new JSONArray(response);

                    for (int i=0;i<testArray.length();i++){
                        JSONObject TestName=testArray.getJSONObject(i);
                        TestList.add(TestName.getString("test_name"));


                    }
                    //Toast.makeText(getContext(),"testName"+TestList,Toast.LENGTH_LONG).show();

                    ArrayAdapter arrayAdapter=new ArrayAdapter(getContext(),android.R.layout.simple_spinner_item,TestList);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(arrayAdapter);

                } catch (JSONException e) {
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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                testname=adapterView.getItemAtPosition(i);
               // Toast.makeText(getContext(),testname.toString(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        //ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,)
    }
}
