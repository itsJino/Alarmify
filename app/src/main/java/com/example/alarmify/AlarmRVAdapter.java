package com.example.alarmify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter for RecyclerView to display AlarmModal items.
 * Manages item differences, binds data to views, and handles item clicks.
 */
public class AlarmRVAdapter extends ListAdapter<AlarmModal, AlarmRVAdapter.ViewHolder> {
    private OnItemClickListener listener; // Listener for item clicks

    // Constructor initializes the adapter with the DIFF_CALLBACK
    AlarmRVAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<AlarmModal> DIFF_CALLBACK = new DiffUtil.ItemCallback<AlarmModal>() {
        @Override
        // Checks if items are the same based on ID
        public boolean areItemsTheSame(@NonNull AlarmModal oldItem, @NonNull AlarmModal newItem) {
            return oldItem.getId() == newItem.getId();
        }


        @Override
        // Checks if item content is the same based on alarm name and time
        public boolean areContentsTheSame(@NonNull AlarmModal oldItem, @NonNull AlarmModal newItem) {
            return oldItem.getAlarmName().equals(newItem.getAlarmName()) &&
                    oldItem.getAlarmTime().equals(newItem.getAlarmTime());
        }
    };

    @NonNull
    @Override
    // Inflates the item layout and creates ViewHolder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm_row, parent, false);
        return new ViewHolder(item);
    }

    // Binds data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlarmModal model = getAlarmAt(position);
        holder.alarmNameTV.setText(model.getAlarmName());
        holder.alarmTimeTV.setText(model.getAlarmTime());
    }

    // Retrieves an AlarmModal object at a given position
    public AlarmModal getAlarmAt(int position) {
        return getItem(position);
    }

    // ViewHolder class to hold item views
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView alarmNameTV, alarmTimeTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            alarmNameTV = itemView.findViewById(R.id.textViewName);
            alarmTimeTV = itemView.findViewById(R.id.textViewTime);

            // Handles item clicks and invokes the listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    // Interface for item click events
    public interface OnItemClickListener {
        void onItemClick(AlarmModal model);
    }

    // Setter for the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
