package com.t4.sqliteexample.SubFeatureOfMainActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.t4.sqliteexample.Model.Clazz;
import com.t4.sqliteexample.R;
import com.t4.sqliteexample.SimpleSQLiteDbHelper;

import java.util.ArrayList;
import java.util.List;

public class InsertRowToTableClazz extends AppCompatActivity implements View.OnClickListener {
    private String id = "";
    private String name = "";
    private ArrayAdapter<String> rowsAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_row_to_table);

        ListView viewer = (ListView) findViewById(R.id.insert_table_viewer);
        this.rowsAdapter = new ArrayAdapter<>(this ,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                clazzAsStringList());
        viewer.setAdapter(this.rowsAdapter);


        View button = findViewById(R.id.insert_table_finish);
        button.setOnClickListener(this);
    }
    private List<String> clazzAsStringList(){
        List<Clazz> classes = SimpleSQLiteDbHelper.getHelper(this).getAll();
        List<String> rs = new ArrayList<>();
         int i = 0;
        for(Clazz c : classes){
            rs.add(i++ + "\t" + c.id + "\t " + c.name);
        }
        return rs;
    }

    @Override
    public void onClick(View view) {
        if(R.id.insert_table_finish == view.getId()){
            this.id = ((EditText)findViewById(R.id.insert_table_id)).getText().toString();
            this.name = ((EditText)findViewById(R.id.insert_table_name)).getText().toString();
            SimpleSQLiteDbHelper.getHelper(this).insertNewClazz(new Clazz(this.id , this.name));
            this.rowsAdapter.clear();
            this.rowsAdapter.addAll(clazzAsStringList());
            this.rowsAdapter.notifyDataSetChanged();
        }
    }
}
