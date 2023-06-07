package com.example.professorfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private LinearLayout emptyState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        emptyState = findViewById(R.id.emptyState);
        progressBar = findViewById(R.id.progress_circular);
        new InfoAsyncTask().execute();


    }
    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, ArrayList<ProfessorModel>> {
        @Override
        protected ArrayList<ProfessorModel> doInBackground(Void... voids) {
            ArrayList<ProfessorModel> models = new ArrayList<>();

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://aws.connect.psdb.cloud/professorfinder", "7h1h6gwlesio1mhr2dwv", "pscale_pw_GjGHKmif5lnUFb4Q2SvRopAOJJ75ve4UePzIViJsxjc")) {
                String sql = "SELECT * FROM quick_information WHERE roomLocation != 'Vacant';";
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()){
                    String name = resultSet.getString("name");
                    String roomLocation = resultSet.getString("roomLocation");
                    String isPresent = resultSet.getString("isPresent");
                    String idNumber = resultSet.getString("idNumber");
                    String startTime = resultSet.getString("startTime");
                    String endTime = resultSet.getString("endTime");
                    String comment = resultSet.getString("comment");
                    ProfessorModel model = new ProfessorModel(name, "", roomLocation, isPresent, idNumber, startTime, endTime, comment);
                    models.add(model);
                }
                resultSet.close();
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }

            return models;
        }

        @Override
        protected void onPostExecute(ArrayList<ProfessorModel> result) {
            if (result.size() != 0){
                progressBar.setVisibility(View.GONE);
                emptyState.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                MainAdapter mainAdapter = new MainAdapter(getApplicationContext(), result);
                recyclerView.setAdapter(mainAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                return;
            }
            emptyState.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }


}