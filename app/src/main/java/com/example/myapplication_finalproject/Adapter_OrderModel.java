package com.example.myapplication_finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_OrderModel extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<OrderService> services;
    private OnItemClickListener mItemClickListener;


    public Adapter_OrderModel(Context context, ArrayList<OrderService> services) {
        this.context = context;
        this.services = services;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_service, viewGroup, false);
        view.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final OrderService service = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;
            final ArrayList<Integer> list = new ArrayList<>();
            for (int i = 0; i <= service.getMaxPerEvent(); i++) {
                list.add(i);
            }
            ArrayAdapter<Integer> dataAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, list);

            genericViewHolder.spinner.setAdapter(dataAdapter);
            genericViewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    service.setCount(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    return;
                }
            });
            genericViewHolder.note_LBL_title.setText(service.getServiceName());

        }
    }

    @Override
    public int getItemCount() {
        return services != null ? services.size() : 0;
    }

    private OrderService getItem(int position) {
        return services.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Spinner spinner;
        private TextView note_LBL_title;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.spinner = itemView.findViewById(R.id.spinner);
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
        void onItemClick(View view, int position, OrderService services);
    }

    public ArrayList<OrderService> getServices() {
        return services;
    }

    public ArrayList<OrderService> getFilteredServices() {
        ArrayList<OrderService> temp = new ArrayList<>();
        for (OrderService orderService : services
        ) {
            if (orderService.getCount() > 0)
                temp.add(orderService);

        }
        return temp;
    }
}
