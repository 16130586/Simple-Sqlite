package com.t4.sqliteexample.SubFeatureOfMainActivity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.t4.sqliteexample.R;
import com.t4.sqliteexample.SimpleSQLiteDbHelper;

import java.util.List;

public class DeleteTable extends AppCompatActivity implements AdapterView.OnItemSelectedListener , View.OnClickListener {
    private String targetDbName = "";
    private String targetTableName = "";
    private ArrayAdapter<String> dynamicDbNameArrayAdapter = null;
    private ArrayAdapter<String> dynamicTableNameArrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_table);
        initComponents();
        initButtonClicks();
    }

    private void initButtonClicks() {
        View btnFinish = findViewById(R.id.delete_table_delete_table_finish);
        btnFinish.setOnClickListener(this);
    }

    private void initComponents() {
        this.initDropDownDBNames(SimpleSQLiteDbHelper.getExistedDbs());
        this.initDropDownTableNames(SimpleSQLiteDbHelper.getAllTableNames(this.targetDbName));
    }

    private void initDropDownTableNames(List<String> tables) {
        Spinner spinner = (Spinner) findViewById(R.id.delete_table_dropdown_pick_table);
        // Create an ArrayAdapter using the string array and a default spinner layout
        this.dynamicTableNameArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                tables);
        // Specify the layout to use when the list of choices appears
        dynamicTableNameArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(dynamicTableNameArrayAdapter);
        spinner.setOnItemSelectedListener(this);
        this.targetTableName = tables.size() > 0 ? tables.get(0) : "";
    }

    private void initDropDownDBNames(String[] existedDBs) {
        if (null == existedDBs || existedDBs.length <= 0) {
            existedDBs = new String[]{"Test Db1 ", "Test Db2", "Test db3"};
        }
        Spinner spinner = (Spinner) findViewById(R.id.delete_table_dropdown_pick_db);
        // Create an ArrayAdapter using the string array and a default spinner layout
        dynamicDbNameArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, existedDBs);
        // Specify the layout to use when the list of choices appears
        dynamicDbNameArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(dynamicDbNameArrayAdapter);
        spinner.setOnItemSelectedListener(this);
        this.targetDbName = existedDBs.length > 0 ? existedDBs[0] : "";
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (R.id.delete_table_dropdown_pick_db == parent.getId()) {
            String dbSelected = (String) parent.getItemAtPosition(pos);
            this.targetDbName = dbSelected;
            this.dynamicTableNameArrayAdapter.clear();
            this.dynamicTableNameArrayAdapter.addAll(SimpleSQLiteDbHelper.getAllTableNames(dbSelected));
            this.dynamicTableNameArrayAdapter.notifyDataSetChanged();
            this.targetTableName = this.dynamicTableNameArrayAdapter.getItem(0);
        }
        if (R.id.delete_table_dropdown_pick_table == parent.getId()) {
            String tableSelected = (String) parent.getItemAtPosition(pos);
            this.targetTableName = tableSelected;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        if(R.id.delete_table_delete_table_finish == view.getId()){
            String dropTableQuery = "DROP TABLE " + targetTableName + ";";
            boolean rs = SimpleSQLiteDbHelper.excuteQuery(targetDbName , dropTableQuery);
            if(rs){
                Toast.makeText(this, "Deleting success!", Toast.LENGTH_LONG).show();
                this.dynamicTableNameArrayAdapter.clear();
                this.dynamicTableNameArrayAdapter.addAll(SimpleSQLiteDbHelper.getAllTableNames(this.targetDbName));
                this.dynamicTableNameArrayAdapter.notifyDataSetChanged();
                this.targetTableName = this.dynamicTableNameArrayAdapter.getItem(0);
            }
            else {
                Toast.makeText(this, "Cannot deleting table by some causes!", Toast.LENGTH_LONG).show();

            }
        }
    }
}
