package com.balance.bankbalancecheck.BUi.BAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class CalculatorsAdapter extends RecyclerView.Adapter<CalculatorsAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<String> strings;
    private final setClickListener clickListener;

    public CalculatorsAdapter(Context context, ArrayList<String> strings, setClickListener clickListener) {
        this.context = context;
        this.strings = strings;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CalculatorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_calculator_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CalculatorsAdapter.MyViewHolder holder, int position) {
        holder.BtnCalculatorsName.setText(strings.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.CalculatorsClickListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public interface setClickListener {
        void CalculatorsClickListener(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final Button BtnCalculatorsName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            BtnCalculatorsName = itemView.findViewById(R.id.BtnCalculatorsName);
        }
    }
}
