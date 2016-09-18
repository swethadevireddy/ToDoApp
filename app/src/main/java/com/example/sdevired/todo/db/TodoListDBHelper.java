package com.example.sdevired.todo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sdevired.todo.db.model.TodoItem;

import java.util.ArrayList;

/**
 * Created by sdevired on 9/15/16.
 * DbHelper to retrieve and manipulate data for table todo_list
 */
public class TodoListDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "assignment";
    public static final String TABLE_NAME = "todo_list";


    public static final String COL_ID = "id";
    public static final String COL_TASK = "task";
    public static final String COL_DATE = "due_date";
    public static final String COL_PRIORITY = "priority";
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY," +
                    COL_TASK + TEXT_TYPE + "," + COL_DATE + TEXT_TYPE + " , " + COL_PRIORITY + TEXT_TYPE + ")";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public TodoListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /*
     * Retrieves all the ToItems from todo_list table
     */
    public ArrayList<TodoItem> getTodoList() {
        ArrayList<TodoItem> list = new ArrayList<TodoItem>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("select * from " + TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                TodoItem entry = new TodoItem();
                entry.setId(c.getLong(c.getColumnIndex(COL_ID)));
                entry.setTask(c.getString(c.getColumnIndex(COL_TASK)));
                entry.setDate(c.getLong(c.getColumnIndex(COL_DATE)));
                entry.setPriority(c.getString(c.getColumnIndex(COL_PRIORITY)));
                list.add(entry);
            } while (c.moveToNext());
        }
        return list;

    }

    /*
     * Insert TodoItem to db.
     */
    public long addTodo(TodoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK, todo.getTask());
        long id = db.insert(TABLE_NAME,
                null,
                values);
        db.close();
        return id;
    }

    /*
     * Delete ToDoItem based on ToDo.ID
     */
    public void deleteTodo(TodoItem todoDBEntry) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ? ", new String[]{todoDBEntry.getId().toString()});
        db.close();
    }

    /**
     * Update ToDoItem based on ToDo.ID
     * @param todo
     */
    public void updateToDo(TodoItem todo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TASK, todo.getTask());
        values.put(COL_DATE, todo.getDate());
        values.put(COL_PRIORITY, todo.getPriority());
        db.update(TABLE_NAME, values, COL_ID + " = ? ", new String[]{todo.getId().toString()});
        db.close();
    }

}
