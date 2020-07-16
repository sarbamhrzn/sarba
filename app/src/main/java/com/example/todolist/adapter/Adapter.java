package com.example.todolist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todolist.R;
import com.example.todolist.database.model.DatabaseModel;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.TodoViewHolder> {

    private static final String DATE_FORMAT = "dd/MM/yyy";

    final private ItemClickListener mItemClickListener;

    private List<DatabaseModel> mTodoList;
    private Context mContext;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    public Adapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    @NonNull
    @Override
    public Adapter.TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.todo_layout, parent, false);

        return new TodoViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull Adapter.TodoViewHolder holder, int position) {
        DatabaseModel taskEntry = mTodoList.get(position);
        String description = taskEntry.getDescription();
        int priority = taskEntry.getPriority();
        String updatedAt = dateFormat.format(taskEntry.getUpdatedAt());
        if (priority == 1){
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.red_ic_error_black_24dp,null ));
        }else if (priority == 2){
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.yellow_ic_error_black_24dp,null ));
        }else{
            holder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.black_ic_error_black_24dp,null ));
        }
        holder.taskDescriptionView.setText(description);
        holder.updatedAtView.setText(updatedAt);
    }


    @Override
    public int getItemCount() {
        if (mTodoList == null) {
            return 0;
        }
        return mTodoList.size();
    }

    public void setTodoList(List<DatabaseModel> todos) {
        mTodoList = todos;
        notifyDataSetChanged();
    }

    public List<DatabaseModel> getTodos(){
        return mTodoList;
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }


    class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView taskDescriptionView;
        TextView updatedAtView;
        ImageView imageView;
        public TodoViewHolder(View itemView) {
            super(itemView);

            taskDescriptionView = itemView.findViewById(R.id.taskDescription);
            updatedAtView = itemView.findViewById(R.id.taskUpdatedAt);
            imageView = itemView.findViewById(R.id.priorityImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mTodoList.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
