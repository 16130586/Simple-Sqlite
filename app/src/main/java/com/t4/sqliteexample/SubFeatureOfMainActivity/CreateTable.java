package com.t4.sqliteexample.SubFeatureOfMainActivity;

import android.app.Activity;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.t4.sqliteexample.R;
import com.t4.sqliteexample.SimpleSQLiteDbHelper;

import java.util.ArrayList;
import java.util.List;

public class CreateTable extends Activity implements OnItemSelectedListener, View.OnClickListener {

    private String targetDbName = "";
    private String tableName = "";
    private String columnTypeSelected = "";
    private String columnName = "";
    private ArrayAdapter<String> dynamicArrayAdapter = null;
    private List<String> needToCreatingUIColumns = new ArrayList<>();
    private List<String> needToCreatingDBColumns = new ArrayList<>();
    private List<String> currentDbs = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_table);
        initComponents();
    }
    private void initComponents() {
        this.initDropDownDBNames(SimpleSQLiteDbHelper.getExistedDbs());
        this.initDropDownColumnTypes(null);
        this.initSpecialFunctionalButtons();
        this.initViewers(this.needToCreatingUIColumns);
    }

    private void initViewers(List<String> needToCreatingColumns) {
        ListView listView = (ListView) findViewById(R.id.create_table_dynamic_viewer);
        this.dynamicArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, needToCreatingColumns);
        listView.setAdapter(this.dynamicArrayAdapter);
    }

    private void initSpecialFunctionalButtons() {
        Button add = findViewById(R.id.create_table_add);
        add.setOnClickListener(this);
        Button finish = findViewById(R.id.create_table_finish);
        finish.setOnClickListener(this);
    }

    private void initDropDownDBNames(String[] existedDBs) {
        if (null == existedDBs || existedDBs.length <= 0) {
            existedDBs = new String[]{"Test Db1 ", "Test Db2", "Test db3"};
        }
        Spinner spinner = (Spinner) findViewById(R.id.dropdown_pick_db);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, existedDBs);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void initDropDownColumnTypes(String[] existedColumnTypes) {
        if (null == existedColumnTypes || existedColumnTypes.length <= 0) {
            existedColumnTypes = new String[]{"TEXT", "NUMERIC",
                    "INTEGER", "REAL", "BLOB"};
        }
        Spinner spinner = (Spinner) findViewById(R.id.dropdown_pick_column_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, existedColumnTypes);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (R.id.dropdown_pick_db == parent.getId()) {
            String dbSelected = (String) parent.getItemAtPosition(pos);
            this.targetDbName = dbSelected;
        }
        if (R.id.dropdown_pick_column_type == parent.getId()) {
            String columnTypeSelected = (String) parent.getItemAtPosition(pos);
            this.columnTypeSelected = columnTypeSelected;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_table_add:
                EditText inputCLName = (EditText) findViewById(R.id.input_create_column_name);
                this.columnName = inputCLName.getText().toString();
                if(null == this.columnName  || "".equals(this.columnName)){
                    Toast.makeText(this, "Column name cannot empty", Toast.LENGTH_LONG).show();
                    return;
                }
                // build and adding to display columns view
                // one for ui
                this.needToCreatingUIColumns.add(this.columnName + "\t"  + this.columnTypeSelected);
                // one for real data
                this.needToCreatingDBColumns.add(this.columnName + " " + this.columnTypeSelected);
                // notify
                this.dynamicArrayAdapter.notifyDataSetChanged();
                break;
            case R.id.create_table_finish:
                EditText inputTableName = (EditText) findViewById(R.id.input_create_table_name);
                this.tableName = inputTableName.getText().toString();
                if(null == this.tableName  || "".equals(this.tableName)){
                    Toast.makeText(this, "Table name cannot empty", Toast.LENGTH_LONG).show();
                    return;
                }
                // build query from input data and run query
                String query = this.buildCreateTableQuery(this.needToCreatingDBColumns);
                boolean result = SimpleSQLiteDbHelper.excuteQuery(this.targetDbName , query);
                if(result){
                    Toast.makeText(this, "Creating success!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Cannot create by some causes!", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    private String buildCreateTableQuery(List<String> columnDefinitions) {
        String rs = "CREATE TABLE " + this.tableName + "(";
        for(int i = 0 ; i < columnDefinitions.size() - 1 ; i++){
            rs += columnDefinitions.get(i) + ",";
        }
        rs+=columnDefinitions.get(columnDefinitions.size() - 1);
        rs+=")";
        return rs;
    }
}
