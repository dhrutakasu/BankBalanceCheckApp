package com.balance.bankbalancecheck.BUi.BAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.balance.bankbalancecheck.BModel.USSDCodeModel;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ALlBankUSSDAdapter extends RecyclerView.Adapter<ALlBankUSSDAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<USSDCodeModel> ussdCodeModels;

    public ALlBankUSSDAdapter(Context context, ArrayList<USSDCodeModel> ussdCodeModels) {
        this.context = context;
        this.ussdCodeModels = ussdCodeModels;
    }

    @NonNull
    @Override
    public ALlBankUSSDAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_all_bank_ussd, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ALlBankUSSDAdapter.MyViewHolder holder, int position) {
        holder.TxtBankName.setText(ussdCodeModels.get(position).getBankName());
        holder.TxtCallNumber.setText(ussdCodeModels.get(position).getCallNumber());
        holder.TxtShortNameFormat.setText(ussdCodeModels.get(position).getShortyName());
        holder.TxtIFSCFormat.setText(ussdCodeModels.get(position).getIFSC());
    }

    @Override
    public int getItemCount() {
        return ussdCodeModels.size();
    }

    public interface BankListener {
        void BankClick(int pos, ArrayList<String> strings);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView TxtCallNumber;
        private final TextView TxtShortNameFormat;
        private final TextView TxtIFSCFormat;
        private final TextView TxtBankName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtBankName = itemView.findViewById(R.id.TxtBankName);
            TxtIFSCFormat = itemView.findViewById(R.id.TxtIFSCFormat);
            TxtShortNameFormat = itemView.findViewById(R.id.TxtShortNameFormat);
            TxtCallNumber = itemView.findViewById(R.id.TxtCallNumber);
        }
    }
}
