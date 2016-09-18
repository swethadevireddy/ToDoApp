package com.example.sdevired.todo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sdevired.todo.R;
import com.example.sdevired.todo.db.model.TodoItem;

import java.util.Calendar;

/**
 * Created by sdevired on 9/17/16.
 * Dialog fragment for edit TodoItem
 */
public class EditItemDialogFragment extends DialogFragment implements TextView.OnClickListener {


    Long id;
    Long dueDate;
    String priority;

    public EditItemDialogFragment() {

    }

    public static EditItemDialogFragment newInstance(int position, TodoItem entry) {

        EditItemDialogFragment frag = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putLong("id", entry.getId());
        args.putString("task", entry.getTask());
        args.putLong("date", entry.getDate());
        args.putInt("position", position);
        args.putString("priority", entry.getPriority());
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_item, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        EditText task = (EditText) view.findViewById(R.id.etEditItem);

        // Fetch arguments from bundle and set title
        String editItem = getArguments().getString("task");
        dueDate = getArguments().getLong("date");
        id = getArguments().getLong("id");
        getDialog().setTitle(R.string.edit_activity_label);
        //populate the item field
        task.setText(editItem);
        //set focus to end
        task.setSelection(editItem.length());

        DatePicker dpDueDate = (DatePicker) view.findViewById(R.id.dpDueDate);
        Calendar c = Calendar.getInstance();
        //set the due date
        if (dueDate > 0) {
            c.setTimeInMillis(dueDate);
        }
        //setup date change listener
        dpDueDate.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                dueDate = calendar.getTimeInMillis();

            }
        });
        //submit button and register on click listener
        Button btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        //Spinner is for drop-down
        Spinner spinner = (Spinner) view.findViewById(R.id.priority_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        priority = getArguments().getString("priority");

        //set the priority in edit dialog if present from the main activity
        if (priority != null) {
            int selectionPosition = adapter.getPosition(priority);
            spinner.setSelection(selectionPosition);
        }

        //register item selected listener for the change
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priority = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * This method is called on click of the submit button
     * @param view
     */
    @Override
    public void onClick(View view) {
        // Get field from view
        EditText task = (EditText) this.getView().findViewById(R.id.etEditItem);
        if (!task.getText().toString().isEmpty()) {
            TodoItem entry = new TodoItem();
            entry.setId(id);
            entry.setTask(task.getText().toString());
            entry.setDate(dueDate > 0L ? dueDate : Calendar.getInstance().getTimeInMillis());
            entry.setPriority(priority);
            EditItemDialogListener listener = (EditItemDialogListener) getActivity();
            //call main activity
            listener.onFinishEditDialog((Integer) getArguments().get("position"), entry);
            dismiss();
        } else {
            //show alert if item is empty
            Toast toast = Toast.makeText(this.getContext(),R.string.item_validation_alert, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    // Defines the listener interface with a method passing back data result.
    public interface EditItemDialogListener {
        void onFinishEditDialog(int pos, TodoItem dbEntry);
    }
}

