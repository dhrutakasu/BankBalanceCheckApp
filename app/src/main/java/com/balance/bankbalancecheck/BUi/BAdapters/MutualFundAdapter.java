package com.balance.bankbalancecheck.BUi.BAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balance.bankbalancecheck.BModel.LoanModel;
import com.balance.bankbalancecheck.BUi.BActivities.Calculators.ReturnCalculatorActivity;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class MutualFundAdapter extends RecyclerView.Adapter<MutualFundAdapter.MyViewHolder> {
    private final Context context;
    private final String[] strings;
    private final setClickListener clickListener;

    public MutualFundAdapter(Context context, String[] strings, setClickListener clickListener) {
        this.context = context;
        this.strings = strings;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MutualFundAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_mutual_funds_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MutualFundAdapter.MyViewHolder holder, int position) {
        holder.TxtMutualFund.setText(strings[position].toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((strings.length - 1) == position) {
                    Intent intent = new Intent(context, ReturnCalculatorActivity.class);
                    context.startActivity(intent);
                } else
                    clickListener.CalculatorsClickListener(strings, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.length;
    }

    public interface setClickListener {
        void CalculatorsClickListener(String[] strings, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView TxtMutualFund;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtMutualFund = itemView.findViewById(R.id.TxtMutualFund);
        }
    }
}
