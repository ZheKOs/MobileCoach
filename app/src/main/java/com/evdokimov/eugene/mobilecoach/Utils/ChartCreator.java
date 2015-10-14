package com.evdokimov.eugene.mobilecoach.Utils;

import android.content.Context;
import android.graphics.Color;

import com.evdokimov.eugene.mobilecoach.R;
import com.evdokimov.eugene.mobilecoach.db.stats.Stats;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class ChartCreator {
    private Context context;
    public ChartCreator(Context context){
        this.context = context;
    }

    /**
     * @param chart chart that should be prepared
     * @param stats arraylist with stats
     * @param period set what period is needed (0 year, 1 month)
     * @return prepared LineChart
     */
    public LineChart initLineChart(LineChart chart, ArrayList<Stats>  stats, short period){

        ArrayList<LineDataSet> dataSets = new ArrayList<>(); //this array contains all lines
        if (stats.size()>0) {
            ArrayList<Entry> yValsComp1 = statsToEntries(stats, period);

            LineDataSet setComp1 = new LineDataSet(yValsComp1, stats.get(0).getName());
            setComp1.setLineWidth(2);
            setComp1.setCircleSize(3);
            setComp1.setColor(Color.WHITE);
            setComp1.setCircleColor(Color.WHITE);
            setComp1.setCircleColorHole(Color.WHITE);
            setComp1.setDrawValues(false);
            //setComp1.setFillColor(color);

            dataSets.add(setComp1);


            ArrayList<String> xVals = new ArrayList<>();
            if (period == 0) { //period 0 year, 1 month
                xVals.add(context.getString(R.string.january).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.february).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.march).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.april).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.may).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.june).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.july).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.august).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.september).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.october).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.november).substring(0,3).toUpperCase());
                xVals.add(context.getString(R.string.december).substring(0,3).toUpperCase());
            } else {
                Calendar cal = new GregorianCalendar(
                        Integer.valueOf(stats.get(0).getDate().substring(0, 4)), //year
                        Integer.valueOf(stats.get(0).getDate().substring(6, 7)) - 1, //month; -1 because count starts form 0
                        Integer.valueOf(stats.get(0).getDate().substring(9, 10)) //day
                );
                int maxDays = cal.getMaximum(Calendar.DAY_OF_MONTH);
                for (int i = 1; i <= maxDays; i++) {
                    xVals.add(String.valueOf(i));
                }
            }

            LineData data = new LineData(xVals, dataSets);

            chart.setData(data);
        }

        chart.setDrawGridBackground(false);

        chart.getXAxis().setGridColor(Color.parseColor("#77212121"));
        chart.getXAxis().setAxisLineColor(Color.parseColor("#77212121"));
        chart.getXAxis().setTextColor(Color.WHITE);

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisLeft().setGridColor(Color.parseColor("#77212121"));
        chart.getAxisLeft().setAxisLineColor(Color.parseColor("#77212121"));

        chart.getAxisRight().setEnabled(false);


        chart.setDescription("");
        chart.setNoDataTextDescription("Нужно добавить данные для графика.");

        chart.getLegend().setTextColor(Color.WHITE);

        return chart;
    }

    private ArrayList<Entry> statsToEntries(ArrayList<Stats> stats, short period){

        ArrayList<Entry> entries = new ArrayList<>();

        if (period==0) {

        }else {
            for (Stats s: stats)
                entries.add(new Entry(s.getValue(),Integer.valueOf(s.getDate().substring(8, 10))));
//            Set<String> uniqueDates = new HashSet<>();
//            for (Stats s : stats) {
//                if (!uniqueDates.contains(s.getDate())) {
//                    uniqueDates.add(s.getDate());
//                }
//            }
//            int i = 0;
//            for (String str : uniqueDates){
//                float tmpVal = 0;
//                for (Stats s : stats){
//                    if (str.equals(s.getDate())){
//                        tmpVal = tmpVal+s.getValue();
//                        entries.add(new Entry(tmpVal,i));
//                    }
//                }
//                i++;
//            }
        }

        return entries;

    }

    private boolean checkSameDate(short period,String firstDate, String secondDate){
        //formate of date - yyyy-MM-DD
        if (period == 0) //should check year and month
            return firstDate.substring(0, 7).equals(secondDate.substring(0, 7));
        else //should check all date
            return firstDate.equals(secondDate);
    }

}
