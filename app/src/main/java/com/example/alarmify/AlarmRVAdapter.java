package com.example.alarmify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class AlarmRVAdapter extends ListAdapter<AlarmModal, AlarmRVAdapter.ViewHolder> {
    private OnItemClickListener listener;

    AlarmRVAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<AlarmModal> DIFF_CALLBACK = new DiffUtil.ItemCallback<AlarmModal>() {
        @Override
        public boolean areItemsTheSame(@NonNull AlarmModal oldItem, @NonNull AlarmModal newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull AlarmModal oldItem, @NonNull AlarmModal newItem) {
            return oldItem.getAlarmName().equals(newItem.getAlarmName()) &&
                    oldItem.getAlarmTime().equals(newItem.getAlarmTime());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm_row, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlarmModal model = getAlarmAt(position);
        holder.alarmNameTV.setText(model.getAlarmName());
        holder.alarmTimeTV.setText(model.getAlarmTime());
    }

    public AlarmModal getAlarmAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView alarmNameTV, alarmTimeTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            alarmNameTV = itemView.findViewById(R.id.textViewName);
            alarmTimeTV = itemView.findViewById(R.id.textViewTime);

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

    public interface OnItemClickListener {
        void onItemClick(AlarmModal model);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
