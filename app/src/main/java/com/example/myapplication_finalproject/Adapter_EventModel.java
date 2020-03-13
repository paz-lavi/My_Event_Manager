package com.example.myapplication_finalproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_EventModel extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MyEvent> events;
    private OnItemClickListener mItemClickListener;

    public Adapter_EventModel(ArrayList<MyEvent> events) {
        this.events = events;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final MyEvent event = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.eventcard_LBL_title.setText(event.getCustomerName() + " " + event.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    private MyEvent getItem(int position) {
        return events.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eventcard_LBL_title;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.eventcard_LBL_title = itemView.findViewById(R.id.eventcard_LBL_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }
    }


    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, MyEvent note);
    }
}