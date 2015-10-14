package com.evdokimov.eugene.mobilecoach.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.Utils.ChartCreator;
import com.evdokimov.eugene.mobilecoach.db.HelperFactory;
import com.evdokimov.eugene.mobilecoach.db.stats.Stats;
import com.github.mikephil.charting.charts.LineChart;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.ViewHolder> {

    private String[] chartNames;
    private short[] periods;
    private String[] curDate;
    private ChartCreator chartCreator;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case

        public LineChart chart;
        public CardView cardView;

        public TextView tvMonth, tvYear, tvCur;

        ImageView ivPrev, ivNext;

        public ViewHolder(View v){
            super(v);

            chart = (LineChart) v.findViewById(R.id.chart);
            cardView = (CardView) v.findViewById(R.id.card_chart);

            tvYear = (TextView) v.findViewById(R.id.chart_tv_pick_year);
            tvMonth = (TextView) v.findViewById(R.id.chart_tv_pick_month);
            tvCur = (TextView) v.findViewById(R.id.chart_tv_cur);

            ivPrev = (ImageView) v.findViewById(R.id.chart_iv_prev);
            ivNext = (ImageView) v.findViewById(R.id.chart_iv_next);

        }

    }

    public StatsAdapter(Context context, String[] chartNames) {
        //super(context, R.layout.row_main_plan_t_other, chartNames);
        this.chartNames = chartNames;

        this.periods = new short[chartNames.length];
        short period = 1;
        setPeriods(period);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.curDate = new String[chartNames.length];
        for (int i=0;i<curDate.length;i++){
            this.curDate[i] = sdf.format(new Date());
        }

        chartCreator = new ChartCreator(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StatsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_stats,parent,false);
        return new ViewHolder(rowView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        ArrayList<Stats> curStats = getStats(chartNames[position], periods[position], curDate[position]);

        holder.chart = chartCreator.initLineChart(holder.chart,curStats,periods[position]);

        int color = Color.parseColor("#ffffff");
        if(curStats.size()>0) {
            switch (curStats.get(0).getType()) {
                case 0: //workout
                    color = Color.parseColor("#3F51B5");
                    break;
                case 1: //dish
                    color = Color.parseColor("#009688");
                    break;
                case 2: //workout plan
                    color = Color.parseColor("#673AB7");
                    break;
                case 3:// nutrition
                    color = Color.parseColor("#4CAF50");
                    break;
            }
        }
        holder.cardView.setCardBackgroundColor(color);
        holder.chart.setBackgroundColor(color);
        holder.chart.setGridBackgroundColor(color);


        View.OnClickListener pickPeriodListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.chart_tv_pick_year:
                        periods[position] = 0;
                        notifyDataSetChanged();
                        holder.chart.animateX(1000);
                        holder.chart.animateY(1100);
                        break;
                    case R.id.chart_tv_pick_month:
                        periods[position] = 1;
                        notifyDataSetChanged();
                        holder.chart.animateX(1000);
                        holder.chart.animateY(1100);
                        break;
                }
            }
        };
        holder.tvYear.setOnClickListener(pickPeriodListener);
        holder.tvMonth.setOnClickListener(pickPeriodListener);

        if (periods[position]==0){
            if (curStats.size()>0)holder.tvCur.setText(curStats.get(0).getDate().substring(0, 4));//get year from stats
            //styling TextView's
            holder.tvYear.setTextColor(Color.WHITE);
            holder.tvMonth.setTextColor(Color.parseColor("#77212121"));
        }else{
            if (curStats.size()>0)holder.tvCur.setText(curStats.get(0).getDate().substring(0, 7));//get year and month from stats
            //styling TextView's
            holder.tvMonth.setTextColor(Color.WHITE);
            holder.tvYear.setTextColor(Color.parseColor("#77212121"));
        }

        View.OnClickListener choseCurDateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder builder = new StringBuilder(curDate[position]);
                switch (v.getId()){
                    case R.id.chart_iv_prev:
                        if (periods[position] == 0) {//year
                            int curYear = Integer.valueOf(builder.substring(0, 4));
                            if (curYear > 0) curYear--;
                            String newYear = String.valueOf(curYear);
                            while (newYear.length() < 3)
                                newYear = "0" + newYear;
                            builder.replace(0, 4, newYear);
                            curDate[position] = builder.toString();
                        } else {//month
                            int month = Integer.valueOf(builder.substring(6, 7));
                            if (month > 0) {
                                month--;
                                String newMonth = String.valueOf(month);
                                if (month < 10)
                                    newMonth = "0" + newMonth;
                                builder.replace(6, 7, newMonth);
                                curDate[position] = builder.toString();
                            } else {
                                month = 11;
                                builder.replace(6, 7, String.valueOf(month));

                                int curYear = Integer.valueOf(builder.substring(0, 4));
                                if (curYear > 0) curYear--;
                                String newYear = String.valueOf(curYear);
                                while (newYear.length() < 3)
                                    newYear = "0" + newYear;
                                builder.replace(0, 4, newYear);
                                curDate[position] = builder.toString();
                            }
                            notifyDataSetChanged();
                        }
                        break;
                    case R.id.chart_iv_next:
                        if (periods[position] == 0) {//year
                            int curYear = Integer.valueOf(builder.substring(0, 4));
                            curYear++;
                            String newYear = String.valueOf(curYear);
                            while (newYear.length() < 3)
                                newYear = "0" + newYear;
                            builder.replace(0, 4, newYear);
                            curDate[position] = builder.toString();
                            notifyDataSetChanged();
                        } else {//month
                            int month = Integer.valueOf(builder.substring(6, 7));
                            if (month < 11) {
                                month++;
                                String newMonth = String.valueOf(month);
                                if (month < 10)
                                    newMonth = "0" + newMonth;
                                builder.replace(6, 7, newMonth);
                                curDate[position] = builder.toString();
                            } else {

                                builder.replace(6, 7, "00");

                                int curYear = Integer.valueOf(builder.substring(0, 4));
                                curYear++;
                                String newYear = String.valueOf(curYear);
                                while (newYear.length() < 3)
                                    newYear = "0" + newYear;
                                builder.replace(0, 4, newYear);
                                curDate[position] = builder.toString();
                            }
                            notifyDataSetChanged();
                        }
                        break;
                }
            }
        };
//        holder.ivPrev.setOnClickListener(choseCurDateListener);
//        holder.ivNext.setOnClickListener(choseCurDateListener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return chartNames.length;
    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        final LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View rowView;
//        rowView = inflater.inflate(R.layout.row_stats, parent, false);
//
//        final ArrayList<Stats> curStats = getStats(chartNames[position], periods[position], curDate[position]);
//        chart = (LineChart) rowView.findViewById(R.id.chart);
//
//        chart = chartCreator.initLineChart(chart,curStats,periods[position]);
//
//        CardView cardView = (CardView) rowView.findViewById(R.id.card_chart);
//        int color = 0x000;
//        switch (curStats.get(0).getType()){
//            case 0: //workout
//                color = Color.parseColor("#3F51B5");
//                break;
//            case 1: //dish
//                color = Color.parseColor("#009688");
//                break;
//            case 2: //workout plan
//                color = Color.parseColor("#673AB7");
//                break;
//            case 3:// nutrition
//                color = Color.parseColor("#4CAF50");
//                break;
//        }
//        cardView.setCardBackgroundColor(color);
//        chart.setBackgroundColor(color);
//        chart.setGridBackgroundColor(color);
//
//        TextView tvMonth, tvYear;
//        tvYear = (TextView) rowView.findViewById(R.id.chart_tv_pick_year);
//        tvMonth = (TextView) rowView.findViewById(R.id.chart_tv_pick_month);
//        View.OnClickListener pickPeriodListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()){
//                    case R.id.chart_tv_pick_year:
//                        periods[position] = 0;
//                        notifyDataSetChanged();
//                        chart.animateX(1000);
//                        chart.animateY(1100);
//                        break;
//                    case R.id.chart_tv_pick_month:
//                        periods[position] = 1;
//                        notifyDataSetChanged();
//                        chart.animateX(1000);
//                        chart.animateY(1100);
//                        break;
//                }
//            }
//        };
//        tvYear.setOnClickListener(pickPeriodListener);
//        tvMonth.setOnClickListener(pickPeriodListener);
//
//        TextView tvCur = (TextView) rowView.findViewById(R.id.chart_tv_cur);
//        if (periods[position]==0){
//            tvCur.setText(curStats.get(0).getDate().substring(0, 4));//get year from stats
//            //styling TextView's
//            tvYear.setTextColor(Color.WHITE);
//            tvMonth.setTextColor(Color.parseColor("#77212121"));
//        }else{
//            tvCur.setText(curStats.get(0).getDate().substring(0, 7));//get year and month from stats
//            //styling TextView's
//            tvMonth.setTextColor(Color.WHITE);
//            tvYear.setTextColor(Color.parseColor("#77212121"));
//        }
//
//        View.OnClickListener choseCurDateListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                StringBuilder builder = new StringBuilder(curDate[position]);
//                switch (v.getId()){
//                    case R.id.chart_iv_prev:
//
//                        if (periods[position]==0){//year
//                            int curYear = Integer.valueOf(builder.substring(0, 4));
//                            if (curYear>0) curYear--;
//                            String newYear = String.valueOf(curYear);
//                            while (newYear.length()<3)
//                                newYear = "0" + newYear;
//                            builder.replace(0,4,newYear);
//                            curDate[position] = builder.toString();
//                        }else {//month
//                            int month = Integer.valueOf(builder.substring(6, 7));
//                            if (month>0){
//                                month--;
//                                String newMonth = String.valueOf(month);
//                                if (month<10)
//                                    newMonth = "0" + newMonth;
//                                builder.replace(6,7,newMonth);
//                                curDate[position] = builder.toString();
//                            }else{
//                                month = 11;
//                                builder.replace(6,7,String.valueOf(month));
//
//                                int curYear = Integer.valueOf(builder.substring(0, 4));
//                                if (curYear>0) curYear--;
//                                String newYear = String.valueOf(curYear);
//                                while (newYear.length()<3)
//                                    newYear = "0" + newYear;
//                                builder.replace(0,4,newYear);
//                                curDate[position] = builder.toString();
//                            }
//
//                            notifyDataSetChanged();
//                        }
//                        break;
//                    case R.id.chart_iv_next:
//
//                        if (periods[position]==0){//year
//                            int curYear = Integer.valueOf(builder.substring(0, 4));
//                            curYear++;
//                            String newYear = String.valueOf(curYear);
//                            while (newYear.length()<3)
//                                newYear = "0" + newYear;
//                            builder.replace(0,4,newYear);
//                            curDate[position] = builder.toString();
//                            notifyDataSetChanged();
//                        }else {//month
//                            int month = Integer.valueOf(builder.substring(6, 7));
//                            if (month<11){
//                                month++;
//                                String newMonth = String.valueOf(month);
//                                if (month<10)
//                                    newMonth = "0" + newMonth;
//                                builder.replace(6,7,newMonth);
//                                curDate[position] = builder.toString();
//                            }else{
//
//                                builder.replace(6,7,"00");
//
//                                int curYear = Integer.valueOf(builder.substring(0, 4));
//                                curYear++;
//                                String newYear = String.valueOf(curYear);
//                                while (newYear.length()<3)
//                                    newYear = "0" + newYear;
//                                builder.replace(0,4,newYear);
//                                curDate[position] = builder.toString();
//                            }
//
//                            notifyDataSetChanged();
//                        }
//                        break;
//                }
//            }
//        };
//        ImageView ivPrev, ivNext;
//        ivPrev = (ImageView) rowView.findViewById(R.id.chart_iv_prev);
//        ivNext = (ImageView) rowView.findViewById(R.id.chart_iv_next);
//        ivPrev.setOnClickListener(choseCurDateListener);
//        ivNext.setOnClickListener(choseCurDateListener);
//
//
//        return rowView;
//    }

    private ArrayList<Stats> getStats(String name, short period, String setDate){

        ArrayList<Stats> stats;
        String date;
        date = setDate;
        try {
            stats = new ArrayList<>(
                    HelperFactory.getDbHelper().getStatsDAO().getStatsFromPeriod(name,date,period)
            );
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return stats;
    }

    private void setPeriods(short value){
        for (int i = 0; i < periods.length;i++){
            periods[i] = value;
        }

    }

}
