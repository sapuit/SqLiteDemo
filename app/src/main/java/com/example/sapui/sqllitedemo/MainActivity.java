package com.example.sapui.sqllitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.app.AlertDialog.Builder;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {

    EditText editRollno, editName, editMarks;
    Button   btnAdd, btnDelete, btnModify, btnView,
             btnViewAll, btnShowInfo;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createComponent();
        crrateDB("StudentDB");
        createTable("student");

    }

    // Initializing controls
    private void createComponent() {
        editRollno  = (EditText) findViewById(R.id.editRollno);
        editName    = (EditText) findViewById(R.id.editName);
        editMarks   = (EditText) findViewById(R.id.editMarks);
        btnAdd      = (Button)   findViewById(R.id.btnAdd);
        btnDelete   = (Button)   findViewById(R.id.btnDelete);
        btnModify   = (Button)   findViewById(R.id.btnModify);
        btnView     = (Button)   findViewById(R.id.btnView);
        btnViewAll  = (Button)   findViewById(R.id.btnViewAll);
        btnShowInfo = (Button)   findViewById(R.id.btnShowInfo);
    }

    // Creating database
    private void crrateDB(String dbName){
        db = openOrCreateDatabase(dbName,
                Context.MODE_PRIVATE, null);
    }

    // deleting database
    private void deleteDB(String dbName) {
        String message = "";
        if (deleteDatabase(dbName) == true) {
            message = "succcesful";
        } else {
            message = "failed";
        }
        showMessage("Delete database", message);
    }

    // Creating table
    private void createTable(String tableName){
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName +
                "(rollno VARCHAR,name VARCHAR,marks VARCHAR);");
    }

    private void deleteTable(){

    }

    // handle click event
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                addRecord();
                break;
            case R.id.btnDelete:
                deleteRecord();
                break;
            case R.id.btnModify:
                modifyRecord();
                break;
            case R.id.btnView:
                viewRecord();
                break;
            case R.id.btnViewAll:
                viewAllRecord();
                break;
            case R.id.btnShowInfo:
                showInfoRecord();
                break;
        }
    }

    // show info
    private void showInfoRecord() {
        showMessage("Student Management Application", "Developed By Azim");
    }

    // Show all records
    private void viewAllRecord() {
        Cursor c = db.rawQuery("SELECT * FROM student", null);
        if (c.getCount() == 0) {
            showMessage("Error", "No records found");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("Rollno: " + c.getString(0) + "\n");
            buffer.append("Name: " + c.getString(1) + "\n");
            buffer.append("Marks: " + c.getString(2) + "\n\n");
        }
        showMessage("Student Details", buffer.toString());
    }

    // show a record
    private void viewRecord() {
        if (editRollno.getText().toString().trim().length() == 0) {
            showMessage("Error", "Please enter Rollno");
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" +
                editRollno.getText() + "'", null);
        if (c.moveToFirst()) {
            editName.setText(c.getString(1));
            editMarks.setText(c.getString(2));
        } else {
            showMessage("Error", "Invalid Rollno");
            clearText();
        }
    }

    // modify a record
    private void modifyRecord() {
        if (editRollno.getText().toString().trim().length() == 0) {
            showMessage("Error", "Please enter Rollno");
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" +
                editRollno.getText() + "'", null);
        if (c.moveToFirst()) {
            db.execSQL("UPDATE student SET name='" + editName.getText() +
                    "',marks='" + editMarks.getText() +
                    "' WHERE rollno='" + editRollno.getText() + "'");
            showMessage("Success", "Record Modified");
        } else {
            showMessage("Error", "Invalid Rollno");
        }
        clearText();
    }

    // add a record into student table
    private void addRecord() {
        if (editRollno.getText().toString().trim().length() == 0 ||
                editName.getText().toString().trim().length() == 0 ||
                editMarks.getText().toString().trim().length() == 0) {
            showMessage("Error", "Please enter all values");
            return;
        }
        // use command
//        db.execSQL("INSERT INTO student VALUES('" + editRollno.getText() +
//                "','" + editName.getText() +
//                "','" + editMarks.getText() + "');");

        ContentValues values = new ContentValues();
        values.put("rollno", editRollno.getText().toString());
        values.put("name",  editName.getText().toString());
        values.put("marks", editMarks.getText().toString());

        if (db.insert("student", null, values) == -1) {
            showMessage("Error", "Please enter all values");
        } else {
            showMessage("Success", "Record added");
            clearText();
        }
    }

    private void deleteRecord() {
        if (editRollno.getText().toString().trim().length() == 0) {
            showMessage("Error", "Please enter Rollno");
            return;
        }
        Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='" +
                editRollno.getText() + "'", null);
        if (c.moveToFirst()) {
            db.execSQL("DELETE FROM student WHERE rollno='" +
                    editRollno.getText() + "'");
            showMessage("Success", "Record Deleted");
        } else {
            showMessage("Error", "Invalid Rollno");
        }

        // Delete a record
//        db.delete("student","rollno=?",new String[]
//                {editRollno.getText().toString()});
        // delete all records
//        db.delete("student",null,null);
        clearText();
    }

    // show notify
    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void clearText() {
        editRollno.setText("");
        editName.setText("");
        editMarks.setText("");
        editRollno.requestFocus();
    }
}
