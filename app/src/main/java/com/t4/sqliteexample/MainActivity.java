package com.t4.sqliteexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.t4.sqliteexample.Callback.OnCallBackRecevicerDialog;
import com.t4.sqliteexample.SubFeatureOfMainActivity.CreateTable;
import com.t4.sqliteexample.SubFeatureOfMainActivity.DeleteRowFromTable;
import com.t4.sqliteexample.SubFeatureOfMainActivity.DeleteTable;
import com.t4.sqliteexample.SubFeatureOfMainActivity.InsertRowToTableClazz;
import com.t4.sqliteexample.SubFeatureOfMainActivity.InsertRowToTableStudent;
import com.t4.sqliteexample.SubFeatureOfMainActivity.QueryingBySimpleFilter;
import com.t4.sqliteexample.SubFeatureOfMainActivity.UpdateRowFromTable;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, OnCallBackRecevicerDialog {
    private static final String TAG = MainActivity.class.getName().toUpperCase();
    private String inputSchemaName = "";
    private String inputTableName = "";

    private SimpleSQLiteDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestWriteDB();
        initClicks();
        this.dbHelper = SimpleSQLiteDbHelper.getHelper(this);
    }

    private void initClicks() {
        View v = findViewById(R.id.createSchema);
        v.setOnClickListener(this);
        v = findViewById(R.id.deleteSchema);
        v.setOnClickListener(this);
        v = findViewById(R.id.createTable);
        v.setOnClickListener(this);
        v = findViewById(R.id.deleteTable);
        v.setOnClickListener(this);
        v = findViewById(R.id.insertRowToTable);
        v.setOnClickListener(this);
        v = findViewById(R.id.deleteRowFromTable);
        v.setOnClickListener(this);
        v = findViewById(R.id.updateRowFromTable);
        v.setOnClickListener(this);
        v = findViewById(R.id.selectAllFromTable);
        v.setOnClickListener(this);
        v = findViewById(R.id.insertStudent);
        v.setOnClickListener(this);
        v = findViewById(R.id.selectStudentBy);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int result = -1;
        Intent i = null;
        switch (view.getId()) {
            case R.id.createSchema:
                // a simple alert dialog to get schema's name
                CustomAlertDialog.askForASimpleTextInput(this,
                        "Creating database's name", this, R.id.createSchema);
                break;
            case R.id.deleteSchema:
                // a simple alert dialog to get schema's name
                CustomAlertDialog.askForASimpleTextInput(this,
                        "Deleting database's name", this, R.id.deleteSchema);
                break;
            case R.id.createTable:
                // navigate to another activity to create with a simple table's schema -> another activity
                i = new Intent(this, CreateTable.class);
                startActivity(i);
                break;
            case R.id.deleteTable:
                // a simple alert dialog to get table name
                i = new Intent(this, DeleteTable.class);
                startActivity(i);
//                CustomAlertDialog.askForASimpleTextInput(this,
//                        "Deleting table's name", this, R.id.deleteTable);
                break;
            case R.id.insertRowToTable:
                // navigate to another activity and display required editable text - columns required -> another activity
                i = new Intent(this, InsertRowToTableClazz.class);
                startActivity(i);
                break;
            case R.id.deleteRowFromTable:
                // a simple list view for receive user's interaction -> another activity
                i = new Intent(this, DeleteRowFromTable.class);
                startActivity(i);
                break;
            case R.id.updateRowFromTable:
                // a simple list view for receive user's interaction - on click -> another activity
                i = new Intent(this, UpdateRowFromTable.class);
                startActivity(i);
                break;
            case R.id.selectAllFromTable:
                // simple querying by columns -> another activity
                i = new Intent(this, QueryingBySimpleFilter.class);
                startActivity(i);
                break;
            case R.id.insertStudent:
                // a simple list editable text same as table columns for user fill in -> another activity
                i = new Intent(this, InsertRowToTableStudent.class);
                startActivity(i);
                break;
            case R.id.selectStudentBy:
                // querying by class id :))) -> another activity
                // i = new Intent(this, InsertRowToTableClazz.class);
//                startActivity(i);
                break;
            default:
                Log.v(this.TAG, "Hello!");

        }
    }

    @Override
    public void emitEvent(int eventId, String dataAsText) {
        int result = -1;
        switch (eventId) {
            case R.id.createSchema:
                this.inputSchemaName = dataAsText;
                result = dbHelper.createSchemaIfNotExist(this.inputSchemaName);
                if (result > 0) {
                    Toast.makeText(this, "Create a schema is success!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Schema existed or error!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.deleteSchema:
                // a simple alert dialog to get schema's name
                this.inputSchemaName = dataAsText;
                result = dbHelper.deleteSchema(this.inputSchemaName);
                if (result > 0) {
                    Toast.makeText(this, "Deleting a schema is success!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.deleteTable:
                // a simple alert dialog to get table name
                this.inputTableName = dataAsText;
                result = dbHelper.deleteTable(this.inputTableName);
                if (result > 0) {
                    Toast.makeText(this, "Deleting a table is success!", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Log.v(this.TAG, "Happy new year! " + dataAsText);
        }
    }

    private void requestWriteDB() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (1 == requestCode) {
            if (grantResults.length > 0 && PackageManager.PERMISSION_GRANTED == grantResults[0])
                Toast.makeText(this, "WRITE IS GRANTED!", Toast.LENGTH_LONG).show();
        }
    }
}
