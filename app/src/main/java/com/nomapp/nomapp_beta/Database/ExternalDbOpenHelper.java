package com.nomapp.nomapp_beta.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by antonid on 10.07.2015.
 */
public class ExternalDbOpenHelper extends SQLiteOpenHelper {

    public String DB_PATH;
    public String DB_NAME;

    public SQLiteDatabase database;
    public final Context context;

    private static final int DB_VERSION = 21;

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public ExternalDbOpenHelper(Context context, String databaseName) {
        super(context, databaseName, null, DB_VERSION);

        this.context = context;

        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
       // DB_PATH = context.getApplicationInfo().dataDir + "/databases/";

        DB_NAME = databaseName;

        initDatabase();
    }

    public void initDatabase() throws SQLException {
        String path = DB_PATH + DB_NAME;

        if (isDatabaseActual()){
            database = SQLiteDatabase.openDatabase(path, null,
                           SQLiteDatabase.OPEN_READWRITE);
        } else {
              this.getReadableDatabase();
              updateDatabase(path);
        }
    }

    private void updateDatabase(String path){
        ArrayList<Integer> states = new ArrayList<>();
        try {
            database = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

            Cursor ingredientStates = database.query(Database.getIngredientsTableName(), new String[]{
                    Database.getIngredientIsChecked()}, null, null, null, null, null);

            ingredientStates.moveToFirst();
            while (!ingredientStates.isAfterLast()) {
                states.add(ingredientStates.getInt(0));
                ingredientStates.moveToNext();
            }
            ingredientStates.close();
        } catch (SQLException e ){
            Log.e("SQLite", "SQLite database does not exist. Cant copy values.");
        }


        try {
            copyDataBase();
        } catch (IOException e) {
            Log.e(this.getClass().toString(), "Copying error");
            throw new Error("Error copying database!");
        }

        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);

        int numberOfIngredients = states.size();
        for (int position = 0; position < numberOfIngredients; position++) {
            database.execSQL("UPDATE " + Database.getIngredientsTableName()
                 + " SET checked=" + states.get(position) + " WHERE _id=" + (position + 1) + ";");
        }

        database.setVersion(DB_VERSION);
    }

    private boolean isDatabaseActual() {
        SQLiteDatabase checkDb = null;
        boolean isVersionCorrect = false;

        try {
            String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READONLY);
            int ver = checkDb.getVersion();

            isVersionCorrect = DB_VERSION == checkDb.getVersion();

        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }

        if (checkDb != null) {
            checkDb.close();
        }


        return checkDb != null && isVersionCorrect;
    }

    private void copyDataBase() throws IOException {
        InputStream externalDbStream = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;

        OutputStream localDbStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }

        localDbStream.close();
        externalDbStream.close();
    }


    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
