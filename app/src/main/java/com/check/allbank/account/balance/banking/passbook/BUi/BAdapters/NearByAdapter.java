package com.check.allbank.account.balance.banking.passbook.BUi.BAdapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.check.allbank.account.balance.banking.passbook.R;

import java.util.ArrayList;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.MyViewHolder> implements Filterable {
    private final Context context;
    private ArrayList<String> strings;
    private final ArrayList<String> stringsNew;

    public NearByAdapter(Context context, ArrayList<String> strings) {
        this.context = context;
        this.strings = strings;
        this.stringsNew = strings;
    }

    @NonNull
    @Override
    public NearByAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_nearby_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NearByAdapter.MyViewHolder holder, int position) {
        holder.TxtNearBy.setText(strings.get(position));
        holder.TxtNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0,0?q=" + strings.get(position).toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri.toString()));
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView TxtNearBy;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtNearBy = itemView.findViewById(R.id.TxtNearBy);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                strings = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<String> filteredResults = null;
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

    protected ArrayList<String> getFilteredResults(String constraint) {
        ArrayList<String> results = new ArrayList<>();
        for (String item : stringsNew) {
            if (item.toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }
}
