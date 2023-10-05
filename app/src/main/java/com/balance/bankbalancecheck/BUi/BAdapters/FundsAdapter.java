package com.balance.bankbalancecheck.BUi.BAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balance.bankbalancecheck.BModel.LoanModel;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

public class FundsAdapter extends RecyclerView.Adapter<FundsAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<LoanModel> strings;
    private final setClickListener clickListener;

    public FundsAdapter(Context context, ArrayList<LoanModel> strings, setClickListener clickListener) {
        this.context = context;
        this.strings = strings;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FundsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_funds_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FundsAdapter.MyViewHolder holder, int position) {
        holder.BtnCalculatorsName.setImageResource(strings.get(position).getIcons());
        if (position == (strings.size() - 1)) {
            holder.ViewBg.setVisibility(View.GONE);
        }else {
            holder.ViewBg.setVisibility(View.VISIBLE);
        }
        holder.BtnCalculatorsName.setOnClickListener(new View.OnClickListener() {
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
        private final ImageView BtnCalculatorsName;
        private final View ViewBg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            BtnCalculatorsName = itemView.findViewById(R.id.BtnCalculatorsName);
            ViewBg = itemView.findViewById(R.id.ViewBg);
        }
    }
}
