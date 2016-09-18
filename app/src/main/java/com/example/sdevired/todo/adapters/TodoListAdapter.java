package com.example.sdevired.todo.adapters;

/**
 * Created by sdevired on 9/17/16.
 */

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sdevired.todo.R;
import com.example.sdevired.todo.db.model.TodoItem;

import java.util.ArrayList;

/**
 * ArrayAdapter for the TodoItem
 */
public class TodoListAdapter extends ArrayAdapter<TodoItem> {

    private long selectedItemId = -1;


    public TodoListAdapter(Context context,ArrayList<TodoItem> list) {
        super(context, 0, list);
    }

    public void setToDoEntryId(long id) {
        this.selectedItemId = id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        TodoItem todoItem = getItem(position);
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.todo_item, parent, false);
            viewHolder.task= (TextView) convertView.findViewById(R.id.tvTask);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.priority  = (TextView) convertView.findViewById(R.id.tvPriority);
             convertView.setTag(viewHolder);
        } else {
             viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.task.setText(todoItem.getTask());
        viewHolder.date.setText(todoItem.getFormatedDate());
        String priority = todoItem.getPriority();
        viewHolder.priority.setText(todoItem.getPriority());

        //set the color based on the priority
        if("Low".equals(priority)){
            viewHolder.priority.setTextColor(getContext().getResources().getColor(R.color.priorityLow));
        }else if("Medium".equalsIgnoreCase(priority)){
            viewHolder.priority.setTextColor(getContext().getResources().getColor(R.color.priorityMedium));
        }else{
           viewHolder.priority.setTextColor(getContext().getResources().getColor(R.color.priorityHigh));
        }

        //set background color for the updated item
        if(selectedItemId == todoItem.getId()){
            final ObjectAnimator animaor = ObjectAnimator.ofObject(convertView, "backgroundColor", new ArgbEvaluator(), Color.parseColor("#FFFFE6"), Color.TRANSPARENT);
            //add fade out color for 4 sec
            animaor.setDuration(4 * 1000);
            animaor.start();
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView task;
        public TextView date;
        public TextView priority;
    }
}
