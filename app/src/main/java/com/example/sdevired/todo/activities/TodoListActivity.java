package com.example.sdevired.todo.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sdevired.todo.R;
import com.example.sdevired.todo.adapters.TodoListAdapter;
import com.example.sdevired.todo.db.model.TodoItem;
import com.example.sdevired.todo.db.TodoListDBHelper;
import com.example.sdevired.todo.fragments.EditItemDialogFragment;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Main Activity class for ToDoApp.
 */

public class TodoListActivity extends AppCompatActivity implements EditItemDialogFragment.EditItemDialogListener{
    private TodoListAdapter itemsAdapter;
    private ArrayList<TodoItem> items;
    ListView lvItems;
     TodoListDBHelper mDbHelper;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);

        //create dbhelper
        mDbHelper = new TodoListDBHelper(this);
        //get the list view
        lvItems = (ListView) findViewById(R.id.lvItems);
        //retrieve db items
        populateListFromDB();
        //Delete Listener
        setupDeleteItemListener();
        //register edit view listener
        setupEditViewListener();
    }

    /*
     on click of AddItem button, retrieve text from editText
      add to the database and update the view
    */
    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newTask = etNewItem.getText().toString();
        //check if task is empty
        if(newTask == null || newTask.isEmpty()){
            Toast.makeText(this, R.string.item_validation_alert, Toast.LENGTH_LONG).show();
        }else {
            TodoItem entry = new TodoItem();
            entry.setTask(newTask);
            mDbHelper.addTodo(entry);
            items.add(entry);
            //sort by date
            Collections.sort(items);
            itemsAdapter.notifyDataSetChanged();
            //clear the text
            etNewItem.setText("");
            //show success message
            Toast.makeText(this, R.string.item_validation_alert, Toast.LENGTH_SHORT).show();
        }
    }

    /*
      Listener setup to remove an item on LongClick of the item
     */
    private void setupDeleteItemListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id){
                mDbHelper.deleteTodo(items.get(pos));
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                //show success message
                Toast.makeText(adapterView.getContext(), R.string.item_delete_success, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    /*
     setup listener to open editItem activity
    */
    private void setupEditViewListener(){
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditDialog(position);
            }
        });
    }

    /*
      show edit activity
     */
    private void showEditDialog(int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditItemDialogFragment editNameDialogFragment = EditItemDialogFragment.newInstance(position, items.get(position));
        editNameDialogFragment.show(fm, "fragment_edit_name");

    }

     //Read items from DB
     private void populateListFromDB(){
         items = mDbHelper.getTodoList();
         Collections.sort(items);
         itemsAdapter = new TodoListAdapter(this, items);
         lvItems.setAdapter(itemsAdapter);
     }

    /*
     *  Handler from edit dialog fragment
     */
    @Override
    public void onFinishEditDialog(int pos, TodoItem dbEntry) {
        mDbHelper.updateToDo(dbEntry);
        items.set(pos, dbEntry);
        Collections.sort(items);
        itemsAdapter.setToDoEntryId(dbEntry.getId());
        itemsAdapter.notifyDataSetChanged();
    }
}
