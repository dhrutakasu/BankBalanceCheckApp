package com.check.allbank.account.balance.banking.passbook.BUi.BAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.check.allbank.account.balance.banking.passbook.BModel.Holiday;
import com.check.allbank.account.balance.banking.passbook.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.MyViewHolder> {
    private final Context context;
    private final ArrayList<Holiday> strings;
    private String dates;

    public HolidayAdapter(Context context, ArrayList<Holiday> strings) {
        this.context = context;
        this.strings = strings;
    }

    @NonNull
    @Override
    public HolidayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_item_holiday, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HolidayAdapter.MyViewHolder holder, int position) {
        holder.TxtHoliday.setText(strings.get(position).getHoliday());
        holder.TxtDay.setText("Day:- " + strings.get(position).getDay());
        holder.TxtDate.setText("Date:- " + strings.get(position).getDate());
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = inputDateFormat.parse(strings.get(position).getDate());

            SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMM yyyy");
            String formattedDate = outputDateFormat.format(date);
            if (!formattedDate.equalsIgnoreCase(dates)) {
                dates = formattedDate;
                holder.TxtFormateDate.setVisibility(View.VISIBLE);
            } else {
                holder.TxtFormateDate.setVisibility(View.GONE);
            }

            holder.TxtFormateDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView TxtHoliday, TxtDate, TxtDay, TxtFormateDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TxtHoliday = itemView.findViewById(R.id.TxtHoliday);
            TxtDate = itemView.findViewById(R.id.TxtDate);
            TxtDay = itemView.findViewById(R.id.TxtDay);
            TxtFormateDate = itemView.findViewById(R.id.TxtFormateDate);
        }
    }
}
