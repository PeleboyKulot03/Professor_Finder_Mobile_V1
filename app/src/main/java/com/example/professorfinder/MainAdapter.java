package com.example.professorfinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ProfessorModel> models;
    private String value;
    private String name;

    public MainAdapter(Context context, ArrayList<ProfessorModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.professor_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("professors");


        ProfessorModel model = models.get(position);
        if (!model.getIsPresent().equals("")){
            holder.present.setText("UPDATE");
        }
        String timeString = model.getStartTime() + " - " + model.getEndTime();
        holder.time.setText(timeString);
        holder.professorName.setText(model.getName());
        holder.roomNo.setText(model.getRoomLocation());
        holder.profImage.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.professor));
        holder.present.setOnClickListener(view -> {
            name = model.getName();
            value = holder.spinner.getSelectedItem().toString();
            holder.present.setText("UPDATE");
            new InfoAsyncTask().execute();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.marks, R.layout.spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        holder.spinner.setAdapter(adapter);

        holder.spinner.setSelection(adapter.getPosition(model.getIsPresent()));

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView professorName, roomNo, time;

        private Button present;

        private ImageView profImage;
        private Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            professorName = itemView.findViewById(R.id.professorName);
            roomNo = itemView.findViewById(R.id.roomNo);
            present = itemView.findViewById(R.id.present);
            profImage = itemView.findViewById(R.id.profImage);
            spinner = itemView.findViewById(R.id.marks_spinner);
            time = itemView.findViewById(R.id.time);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class InfoAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("TAG", "doInBackground: " + name);

            try (Connection connection = DriverManager.getConnection("jdbc:mysql://aws.connect.psdb.cloud/professorfinder", "jemivdj53akpu6jqntjz", "pscale_pw_d3cU2lZ5EJPDYt0yUSA2nqQZ6B47zyyPCQvhVJOnJ0v")) {
                String sql = "UPDATE quick_information SET isPresent = ? WHERE name = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, value);
                statement.setString(2, name);
                statement.executeUpdate();
            } catch (Exception e) {
                Log.e("InfoAsyncTask", "Error reading school information", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Toast.makeText(context, "Adding Remarks Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}