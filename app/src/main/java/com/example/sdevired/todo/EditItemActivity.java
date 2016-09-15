package com.example.sdevired.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private  int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String item = getIntent().getStringExtra("item");
        position = getIntent().getIntExtra("position", 0);
        EditText editText = (EditText) findViewById(R.id.etEditItem);
        editText.append(item);
    }

    //
    public void onEditItem(View view) {
       EditText editText = (EditText) findViewById(R.id.etEditItem);
        Intent i = new Intent();
        i.putExtra("modifiedValue", editText.getText().toString());
        i.putExtra("position", position);
        setResult(RESULT_OK, i);
        finish();
    }
}
