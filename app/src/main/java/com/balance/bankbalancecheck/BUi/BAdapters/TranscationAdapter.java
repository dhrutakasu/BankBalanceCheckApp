package com.balance.bankbalancecheck.BUi.BAdapters;

import android.content.Context;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balance.bankbalancecheck.BConstants.BankConstantsData;
import com.balance.bankbalancecheck.BModel.SMSModel;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;
import java.util.Date;

public class TranscationAdapter extends RecyclerView.Adapter<TranscationAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private final TransactionListener listern;
    private ArrayList<SMSModel> strings;
    private ArrayList<SMSModel> stringsNew;
    private String date;

    public TranscationAdapter(Context context, ArrayList<SMSModel> strings, TransactionListener listener) {
        this.context = context;
        this.listern = listener;
        this.strings = strings;
        this.stringsNew = strings;
    }

    @NonNull
    @Override
    public TranscationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_transaction, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TranscationAdapter.MyViewHolder holder, int position) {
        String getMsg = BankConstantsData.FetchMsg(strings.get(position).getBodyMsg());
        String getAmount = BankConstantsData.FetchAmount(" " + strings.get(position).getBodyMsg());
        if (getMsg == null) {
            getMsg = BankConstantsData.FetchSMSData(strings.get(position).getBodyMsg(), getAmount);
        }
        String str2;

        if (!strings.get(position).getBodyMsg().toLowerCase().contains("card") || strings.get(position).getBodyMsg().toLowerCase().contains("debit card of acct") || strings.get(position).getBodyMsg().toLowerCase().contains("debit card of a/c") || strings.get(position).getBodyMsg().toLowerCase().contains("debit card of account")) {
            str2 = getMsg;
            if (!str2.isEmpty()) {
                holder.TxtStartLetter.setText(str2.substring(0, 1).toUpperCase());
            }
        } else {
            if (getMsg.toLowerCase().contains("credit card")) {
                str2 = "(Card Transaction) " + getMsg.substring(0, getMsg.lastIndexOf(" "));
            } else if (getMsg.toLowerCase().contains("debit card")) {
                str2 = "(Card Transaction)";
            } else {
                String[] SplitsSpace = String.valueOf(strings.get(position).getBodyMsg()).split(" ");
                StringBuilder sb = null;
                if (strings.get(position).getBodyMsg() != null) {
                    sb = new StringBuilder();
                    sb.append(SplitsSpace[1]);
                }
                str2 = sb.toString();
            }
            if (!str2.isEmpty()) {
                holder.TxtStartLetter.setText(str2.substring(1, 2).toUpperCase());
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StringBuilder stringBuilder = new StringBuilder();
            if (strings.get(position).getTypes().contains("credit")) {
                stringBuilder.append("+ ");

                stringBuilder.append(strings.get(position).getAmount());
                holder.TxtAmount.setTextColor(context.getColor(R.color.Txt_green_color));
            } else {
                stringBuilder.append("- ");
                stringBuilder.append(strings.get(position).getAmount());
                holder.TxtAmount.setTextColor(context.getColor(R.color.Txt_red_color));
            }
            holder.TxtAmount.setText(stringBuilder);
        }

        holder.TxtRefNo.setText(str2);
        if (!DateFormat.format("dd MMM,yyyy", new Date(strings.get(position).getDate())).toString().equalsIgnoreCase(date)) {
            date = DateFormat.format("dd MMM,yyyy", new Date(strings.get(position).getDate())).toString();
            holder.TxtFormateDate.setVisibility(View.VISIBLE);
        } else {
            holder.TxtFormateDate.setVisibility(View.GONE);
        }
        holder.TxtDate.setText(DateFormat.format("dd MMM,yyyy", new Date(strings.get(position).getDate())).toString());
        holder.TxtFormateDate.setText(DateFormat.format("dd MMM,yyyy", new Date(strings.get(position).getDate())).toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listern.TransactionClick(position, strings);
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public interface TransactionListener {
        void TransactionClick(int pos, ArrayList<SMSModel> strings);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView TxtRefNo, TxtDate, TxtFormateDate, TxtAmount, TxtStartLetter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtRefNo = itemView.findViewById(R.id.TxtRefNo);
            TxtDate = itemView.findViewById(R.id.TxtDate);
            TxtStartLetter = itemView.findViewById(R.id.TxtStartLetter);
            TxtFormateDate = itemView.findViewById(R.id.TxtFormateDate);
            TxtAmount = itemView.findViewById(R.id.TxtAmount);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                strings = (ArrayList<SMSModel>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<SMSModel> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = stringsNew;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }
        };
    }

    protected ArrayList<SMSModel> getFilteredResults(String constraint) {
        ArrayList<SMSModel> results = new ArrayList<>();
        for (SMSModel item : stringsNew) {
//            if (item.toLowerCase().contains(constraint)) {
//                results.add(item);
//            }
        }
        return results;
    }
}
