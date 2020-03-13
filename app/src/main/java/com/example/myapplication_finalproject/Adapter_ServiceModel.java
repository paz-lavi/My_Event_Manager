package com.example.myapplication_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_ServiceModel extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MyService> services;
    private OnItemClickListener mItemClickListener;


    public Adapter_ServiceModel(ArrayList<MyService> services) {
        this.services = services;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_card, viewGroup, false);
        view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final MyService service = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.note_LBL_title.setText(service.getServiceName());

        }
    }

    @Override
    public int getItemCount() {
        return services != null ? services.size() : 0;
    }

    private MyService getItem(int position) {
        return services.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView note_LBL_title;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.note_LBL_title = itemView.findViewById(R.id.note_LBL_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }
    }

    public void removeAt(int position) {
        services.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, services.size());
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, MyService services);
    }

    public ArrayList<MyService> getServices() {
        return services;
    }
}
