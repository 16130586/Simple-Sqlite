package com.t4.sqliteexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


import com.t4.sqliteexample.Model.Clazz;
import com.t4.sqliteexample.Model.Student;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SimpleSQLiteDbHelper extends SQLiteOpenHelper {
    private Context callingContext = null;
    private int initializeVersion;
    private String databaseName;
    private SQLiteDatabase currentDb;
    private static String basePath = "";
    private static String createTableCLAZZSQL = "CREATE TABLE IF NOT EXISTS CLAZZ(ID TEXT, NAME TEXT)";
    private static String createTableStudent = "CREATE TABLE IF NOT EXISTS STUDENT(ID TEXT, NAME TEXT, CLAZZ_ID TEXT)";
    private static SimpleSQLiteDbHelper helper;
    private SimpleSQLiteDbHelper(Context context, String databaseName, int initVersionSqlFile) {
        super(context, databaseName, null, initVersionSqlFile);
        this.databaseName = databaseName;
        this.callingContext = context;
        this.basePath = this.callingContext.getFilesDir().getPath();
        this.initializeVersion = initVersionSqlFile;
        if(null == this.currentDb){
            File f = new File(basePath + "/" +
                    this.databaseName + ".db");
            this.currentDb = SQLiteDatabase.
                    openOrCreateDatabase( f, null);
            currentDb.execSQL(SimpleSQLiteDbHelper.createTableCLAZZSQL);
            currentDb.execSQL(SimpleSQLiteDbHelper.createTableStudent);
            sampleData();
        }
    }

    private void sampleData() {
        currentDb.execSQL("INSERT INTO CLAZZ(ID,NAME) VALUES('DH16DTA' , 'Cong nghe thong tin A khoa 16')");
        currentDb.execSQL("INSERT INTO CLAZZ(ID,NAME) VALUES('DH16DTB' , 'Cong nghe thong tin B khoa 16')");
        currentDb.execSQL("INSERT INTO CLAZZ(ID,NAME) VALUES('DH16DTC' , 'Cong nghe thong tin C khoa 16')");

        currentDb.execSQL("INSERT INTO STUDENT(ID,NAME,CLAZZ_ID) VALUES('16130581' , 'Nguyen Van A' , 'DH16DTA')");
        currentDb.execSQL("INSERT INTO STUDENT(ID,NAME,CLAZZ_ID) VALUES('16130586' , 'Nguyen Ngoc That' , 'DH16DTB')");
        currentDb.execSQL("INSERT INTO STUDENT(ID,NAME,CLAZZ_ID) VALUES('16130582' , 'Nguyen Thi C' , 'DH16DTC')");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // no handle on simple example
    }

    public int createSchemaIfNotExist(String schemaName) {
        schemaName += ".db";
        try {
            File db = new File(basePath + "/" + schemaName);
            if (!db.exists()) {
                SQLiteDatabase newDb = SQLiteDatabase.openOrCreateDatabase(db, null);
                newDb.close();
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public int deleteSchema(String schemaName) {
        schemaName += ".db";
        try {
            File db = new File(basePath  + "/"  + schemaName);
            if (db.exists()) {
                boolean rs = SQLiteDatabase.deleteDatabase(db);
                return rs ? 1 : -1;
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public int deleteTable(String tableName) {
        try{
            String fullyDropSQL = "DROP TABLE IF EXISTS `"
                    + this.databaseName + "`." + tableName + ";";
            this.currentDb.execSQL(fullyDropSQL);
            return 1;
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }
    public String[] getAllDBs(){
        String[] currentExistedDbs = new String[1];
        return currentExistedDbs;
    }
    public static boolean excuteQuery(String dbName , String query){
        try{
            dbName+=".db";
            File db = new File(basePath + "/" + dbName);
            SQLiteDatabase newDb = SQLiteDatabase.openOrCreateDatabase(db, null);
            newDb.execSQL(query);
            newDb.close();
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static String[] getExistedDbs(){
        String[] rs = null;
        File f = new File(SimpleSQLiteDbHelper.basePath + "/");
        if(f.exists() &&  f.isDirectory()){
            File[] files = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File fzz) {
                    return fzz.isFile() && fzz.getName().endsWith(".db");
                }
            });
            rs = new String[files.length];
            int i = 0;
            for(File dbFile : files){
                if(dbFile.getName().length() > ".db".length())
                    rs[i++] = dbFile.getName().substring(0 , dbFile.getName().indexOf(".db"));
            }
        }
        return rs;
    }
    public static List<String> getAllTableNames(String dbName){
        List<String> rs = new ArrayList<>();
        try{
            dbName+=".db";
            File db = new File(basePath + "/" + dbName);
            SQLiteDatabase newDb = SQLiteDatabase.openOrCreateDatabase(db, null);
            Cursor c = newDb.rawQuery("SELECT name FROM sqlite_master WHERE type=?",new String[]{"table"});


            if(c.getCount() > 0)
                c.moveToFirst();
            while(c.moveToNext()){
                String tableName = c.getString(c.getColumnIndex("name"));
                if(tableName.contains("metadata"))
                    continue;
               rs.add(tableName);
            }
            c.close();
            newDb.close();
            if(rs.isEmpty()){
                rs.add("No Table Found!");
            }
            return rs;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Collections.emptyList();
        }
    }
    public synchronized  static SimpleSQLiteDbHelper getHelper(Context context){
        if(SimpleSQLiteDbHelper.helper == null){
            SimpleSQLiteDbHelper.helper = new SimpleSQLiteDbHelper(context , "exampleSQLite" , 1);
        }
        return SimpleSQLiteDbHelper.helper;
    }
    public List<Clazz> getAll(){
        Cursor c = currentDb.rawQuery("SELECT * FROM CLAZZ", null);
        List<Clazz> classes = new ArrayList<>();
        if(c.getCount() > 0)
            c.moveToFirst();
        while(c.moveToNext()){
            classes.add(new Clazz(c.getString(c.getColumnIndex("ID")) ,c.getString(c.getColumnIndex("NAME"))));
        }
        c.close();
        return classes;
    }
    public boolean insertNewClazz(Clazz clazz){
        ContentValues values = new ContentValues();
        values.put("ID" , clazz.id);
        values.put("NAME" , clazz.name);
        try{
            this.currentDb.insertOrThrow("CLAZZ" , "" , values);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    public boolean insertNewStudent(Student student) {
        ContentValues values = new ContentValues();
        values.put("ID" , student.id);
        values.put("NAME" , student.name);
        values.put("CLAZZ_ID" , student.classId);
        try{
            this.currentDb.insertOrThrow("STUDENT" , "" , values);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public List<Student> getAllStudent() {
        Cursor c = currentDb.rawQuery("SELECT * FROM STUDENT", null);
        List<Student> classes = new ArrayList<>();
        if(c.getCount() > 0)
            c.moveToFirst();
        while(c.moveToNext()){
            classes.add(new Student(c.getString(c.getColumnIndex("ID"))
                    ,c.getString(c.getColumnIndex("NAME"))
                    , c.getString(c.getColumnIndex("CLAZZ_ID"))));
        }
        c.close();
        return classes;
    }
}
