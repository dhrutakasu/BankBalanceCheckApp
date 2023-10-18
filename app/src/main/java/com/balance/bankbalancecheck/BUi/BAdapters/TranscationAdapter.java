package com.balance.bankbalancecheck.BUi.BAdapters;

import android.content.Context;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.balance.bankbalancecheck.BModel.SMSModel;
import com.balance.bankbalancecheck.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        System.out.println("--- - - - - - body ::: " + strings.get(position).getBodyMsg());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StringBuilder stringBuilder = new StringBuilder();
            double number = Double.parseDouble(strings.get(position).getAmount());
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
            if (strings.get(position).getTypes().contains("credit")) {
                stringBuilder.append("+ ");

                stringBuilder.append(numberFormat.format(number));
                holder.TxtAmount.setTextColor(context.getColor(R.color.Txt_green_color));
            } else {
                stringBuilder.append("- ");
                stringBuilder.append(numberFormat.format(number));
                holder.TxtAmount.setTextColor(context.getColor(R.color.Txt_red_color));
            }
            holder.TxtAmount.setText(stringBuilder);
        }
        if (strings.get(position).getBodyMsg().contains("UPI/")) {
            String ANumber;
            if (strings.get(position).getBodyMsg().contains("to:")) {
                List<String> string = Arrays.asList(strings.get(position).getBodyMsg().split("to:", 16));
                ANumber = string.get(1).substring(0, string.get(1).indexOf("."));
                holder.TxtRefNo.setText(ANumber);
            } else {
                List<String> string = Arrays.asList(strings.get(position).getBodyMsg().split("thru ", 16));
                ANumber = string.get(1).substring(0, string.get(1).indexOf(" "));
                holder.TxtRefNo.setText(ANumber.replace("UPI/", "Ref no. "));
            }
            holder.TxtStartLetter.setText(holder.TxtRefNo.getText().toString().substring(0,1).toUpperCase());
        }
        if (!DateFormat.format("dd MMM,yyyy", new Date(strings.get(position).getDate())).toString().equalsIgnoreCase(date)) {
            holder.TxtFormateDate.setVisibility(View.VISIBLE);
            date = DateFormat.format("dd MMM,yyyy", new Date(strings.get(position).getDate())).toString();
        } else {
            holder.TxtFormateDate.setVisibility(View.GONE);
        }
        holder.TxtDate.setText(DateFormat.format("dd MMM,yyyy", new Date(strings.get(position).getDate())).toString());
        holder.TxtFormateDate.setText(DateFormat.format("dd MMM,yyyy", new Date(strings.get(position).getDate())).toString());

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
