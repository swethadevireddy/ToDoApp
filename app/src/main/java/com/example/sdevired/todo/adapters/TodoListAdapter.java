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
       // commenting this as this causing the animation to set the background color for the last element when new item is added
        // scenario to reproduce ,
        // 1.create  2 to-do's.
        // 2.edit one to-do
        // 3.add a new item --> In this case the new TO-DO is highlighted and also the last item is highlighted.Creating view holder every time fixed this issue.
        //if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.todo_item, parent, false);
            viewHolder.task= (TextView) convertView.findViewById(R.id.tvTask);
            viewHolder.date = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.priority  = (TextView) convertView.findViewById(R.id.tvPriority);
         /*    convertView.setTag(viewHolder);
        } else {
             viewHolder = (ViewHolder) convertView.getTag();
        }*/
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

        ObjectAnimator animaor = null;
        //set background color for the updated item
        if( selectedItemId != -1 && selectedItemId == todoItem.getId()){
            animaor = ObjectAnimator.ofObject(convertView, "backgroundColor", new ArgbEvaluator(), Color.parseColor("#FFFFE6"), Color.TRANSPARENT);
            //add fade out color for 2 sec
            animaor.setDuration(2 * 1000);
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
