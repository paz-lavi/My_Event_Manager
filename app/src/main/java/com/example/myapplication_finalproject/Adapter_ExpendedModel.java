package com.example.myapplication_finalproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter_ExpendedModel extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Expenses> expenses;
    private OnItemClickListener mItemClickListener;

    public Adapter_ExpendedModel(Context context, ArrayList<Expenses> expenses) {
        this.context = context;
        this.expenses = expenses;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expenses_card, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            final Expenses exp = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            genericViewHolder.expnote_LBL_purposecontext.setText(exp.getPurpose());
            genericViewHolder.expnote_LBL_amountcontext.setText(String.valueOf(exp.getAmount()));
            genericViewHolder.expnote_LBL_datecontext.setText(exp.getDate());
            GlideApp.with(context).load(exp.getImage_url()).into(genericViewHolder.expnote_IMG_invoice);

            genericViewHolder.expnote_CARD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (genericViewHolder.expnote_IMG_invoice.getVisibility() == View.VISIBLE) {
                        genericViewHolder.expnote_IMG_invoice.setVisibility(View.GONE);
                        genericViewHolder.expnote_LBL_showmsg.setText("לחץ להצגת הקבלה / חשבונית");

                    } else {
                        genericViewHolder.expnote_IMG_invoice.setVisibility(View.VISIBLE);
                        genericViewHolder.expnote_LBL_showmsg.setText("לחץ לסגירת הקבלה / חשבונית");
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return expenses != null ? expenses.size() : 0;
    }

    private Expenses getItem(int position) {
        return expenses.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView expnote_LBL_purposecontext;
        private TextView expnote_LBL_amountcontext;
        private TextView expnote_LBL_datecontext;
        private ImageView expnote_IMG_invoice;
        private TextView expnote_LBL_showmsg;
        private CardView expnote_CARD;

        public ViewHolder(final View itemView) {
            super(itemView);
            this.expnote_LBL_purposecontext = itemView.findViewById(R.id.expnote_LBL_purposecontext);
            this.expnote_LBL_amountcontext = itemView.findViewById(R.id.expnote_LBL_amountcontext);
            this.expnote_LBL_datecontext = itemView.findViewById(R.id.expnote_LBL_datecontext);
            this.expnote_IMG_invoice = itemView.findViewById(R.id.expnote_IMG_invoice);
            this.expnote_LBL_showmsg = itemView.findViewById(R.id.expnote_LBL_showmsg);
            this.expnote_CARD = itemView.findViewById(R.id.expnote_CARD);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), getItem(getAdapterPosition()));
                }
            });
        }
    }

    public void removeAt(int position) {
        expenses.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, expenses.size());
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Expenses note);
    }
}