package com.t4.sqliteexample.SubFeatureOfMainActivity;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.t4.sqliteexample.Model.Clazz;
import com.t4.sqliteexample.Model.Student;
import com.t4.sqliteexample.R;
import com.t4.sqliteexample.SimpleSQLiteDbHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InsertRowToTableStudent extends AppCompatActivity implements View.OnClickListener , AdapterView.OnItemSelectedListener {
    private String id = "";
    private String name = "";
    private String classId = "";
    private ArrayAdapter<String> rowsAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_row_to_table_student);

        ListView viewer = (ListView) findViewById(R.id.insert_table_student_viewer);
        this.rowsAdapter = new ArrayAdapter<>(this ,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                studentAsListString());
        viewer.setAdapter(this.rowsAdapter);


        View button = findViewById(R.id.insert_table_student_finish);
        button.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.dropdown_pick_clazz);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getExistedClasses());
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private List<String> getExistedClasses() {
        List<Clazz> classes = SimpleSQLiteDbHelper.getHelper(this).getAll();
        List<String> rs = new ArrayList<>();
        for(Clazz c : classes)
            rs.add(c.id);
        return rs;
    }

    private List<String> studentAsListString(){
        List<Student> students = SimpleSQLiteDbHelper.getHelper(this).getAllStudent();
        List<String> rs = new ArrayList<>();
        int i = 0;
        for(Student s : students){
            rs.add(i++ + "\t" + s.id + "\t " + s.name + "\t " + s.classId);
        }
        return rs;
    }

    @Override
    public void onClick(View view) {
        if(R.id.insert_table_student_finish == view.getId()){
            this.id = ((EditText)findViewById(R.id.insert_table_student_id)).getText().toString();
            this.name = ((EditText)findViewById(R.id.insert_table_student_name)).getText().toString();
            SimpleSQLiteDbHelper.getHelper(this).insertNewStudent(new Student(this.id , this.name, this.classId));
            this.rowsAdapter.clear();
            this.rowsAdapter.addAll(studentAsListString());
            this.rowsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(adapterView.getId() == R.id.dropdown_pick_clazz){
            this.classId = adapterView.getItemAtPosition(i).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
