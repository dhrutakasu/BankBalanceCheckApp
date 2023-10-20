package com.balance.bankbalancecheck.BUi.BAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BModel.SMSModel;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BankBalanceAdapter extends RecyclerView.Adapter<BankBalanceAdapter.MyViewHolder> {
    private final Context context;
    private final BankListener listern;
    private ArrayList<SMSModel> strings;

    public BankBalanceAdapter(Context context, ArrayList<SMSModel> strings, BankListener listener) {
        this.context = context;
        this.listern = listener;
        this.strings = strings;
    }

    @NonNull
    @Override
    public BankBalanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_bank_balance, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BankBalanceAdapter.MyViewHolder holder, int position) {
        holder.TxtBankName.setText(strings.get(position).getBankName());
        String a2 = BankConstantsData.a(strings.get(position).getBodyMsg());
        if (a2.length() > 4) {
            a2 = a2.substring(a2.length() - 4);
        }
        holder.TxtBankAccNumber.setText("A/c No:- " + a2);
        String Currency = strings.get(position).getBodyMsg().substring(0, strings.get(position).getBodyMsg().indexOf(".") + 1);
        holder.TxtBankAmount.setText(Currency + " " + strings.get(position).getBalance());
        holder.TxtBankAmount.setSelected(true);
        holder.TxtBankAccNumber.setSelected(true);
        holder.TxtBankName.setSelected(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listern.BankClick(position, strings);
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public interface BankListener {
        void BankClick(int pos, ArrayList<String> strings);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView TxtBankName, TxtBankAccNumber, TxtBankAmount, TxtBankTranscation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtBankName = itemView.findViewById(R.id.TxtBankName);
            TxtBankAccNumber = itemView.findViewById(R.id.TxtBankAccNumber);
            TxtBankTranscation = itemView.findViewById(R.id.TxtBankTranscation);
            TxtBankAmount = itemView.findViewById(R.id.TxtBankAmount);
        }
    }
}
