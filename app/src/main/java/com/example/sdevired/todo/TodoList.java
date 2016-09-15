package com.example.sdevired.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TodoList extends AppCompatActivity {
    private ArrayAdapter<String> itemsAdapter;
    private ArrayList<String> items;
    ListView lvItems;
    private static final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
        setupEditViewListener();
    }

    /*
     on click of AddItem button, retrieve text from editText and to the list
    */
    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String newTask = etNewItem.getText().toString();
        itemsAdapter.add(newTask);
        setupListViewListener();
        //clear the text
        etNewItem.setText("");
        writeItems();
    }


    /*
      Listener setup to remove an item on LongClick of the item
     */
    private void setupListViewListener(){
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View item, int pos, long id){
                items.remove(pos);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
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
                Intent i = new Intent(parent.getContext(), EditItemActivity.class);
                i.putExtra("item", items.get(position));
                i.putExtra("position", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
    }



    /*
      Retrieve the items from file and to listview
     */
    private void readItems(){
       File filesDir = getFilesDir();
       File toDoFile =  new File(filesDir, "todo.txt");
       try{
           items = new ArrayList<String>(FileUtils.readLines(toDoFile));
       }catch (IOException e){
           items = new ArrayList<String>();
       }

    }

    /*
      Add new item to the list and save to the file
    */
    private void writeItems(){
        File filesDir = getFilesDir();
        File toDoFile =  new File(filesDir, "todo.txt");
        try{
            FileUtils.writeLines(toDoFile, items);
        }catch (IOException e){

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String modifiedValue = data.getExtras().getString("modifiedValue");
            int position = data.getExtras().getInt("position");
            items.remove(position);
            items.add(position, modifiedValue);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }

}
