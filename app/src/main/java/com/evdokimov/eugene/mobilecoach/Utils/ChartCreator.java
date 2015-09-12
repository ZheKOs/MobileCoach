package com.evdokimov.eugene.mobilecoach.Utils;

import android.graphics.Color;

import com.evdokimov.eugene.mobilecoach.db.stats.Stats;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ChartCreator {
    public ChartCreator(){}

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
                xVals.add("ЯНВ");
                xVals.add("ФЕВ");
                xVals.add("МАР");
                xVals.add("АПР");
                xVals.add("МАЙ");
                xVals.add("ИЮН");
                xVals.add("ИЮЛ");
                xVals.add("АВГ");
                xVals.add("СЕН");
                xVals.add("ОКТ");
                xVals.add("НОЯ");
                xVals.add("ДЕК");
            } else {
                Calendar cal = new GregorianCalendar(
                        Integer.valueOf(stats.get(0).getDate().substring(0, 4)), //year
                        Integer.valueOf(stats.get(0).getDate().substring(6, 7)) - 1, //month
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
        chart.setNoDataTextDescription("You need to provide data for the chart.");

        chart.getLegend().setTextColor(Color.WHITE);

        return chart;
    }

    private ArrayList<Entry> statsToEntries(ArrayList<Stats> stats, short period){

        ArrayList<Entry> entries = new ArrayList<>();
        int i=0;
        int j=0;
        for (; j < stats.size()-1; j++){
            if (checkSameDate(period, stats.get(j).getDate(), stats.get(j + 1).getDate())) {
                float tmp;
                if (entries.size()<i) tmp = entries.get(i).getVal();
                else tmp=0;
                //formate of date - yyyy-MM-DD
                if (period==0) {
                    entries.add(i,
                            new Entry(tmp + stats.get(j).getValue(),
                                    Integer.valueOf(stats.get(j).getDate().substring(6, 7))-1)//get month
                    );
                }else{
                    entries.add(i,
                            new Entry(tmp + stats.get(j).getValue(),
                                    Integer.valueOf(stats.get(j).getDate().substring(9, 10))-1) //get day
                    );
                }

            }else {
                if (period==0) {
                    entries.add(i++,
                            new Entry(stats.get(j).getValue(),
                                    Integer.valueOf(stats.get(j).getDate().substring(6, 7))-1)//get month
                    );
                }else{
                    entries.add(i++,
                            new Entry(stats.get(j).getValue(),
                                    Integer.valueOf(stats.get(j).getDate().substring(9, 10))-1)//get day
                    );
                }
            }
        }
        if (period==0) {
            entries.add(i,
                    new Entry(stats.get(j).getValue(),
                            Integer.valueOf(stats.get(j).getDate().substring(6, 7))-1)//get month
            );
        }else{
            entries.add(i,
                    new Entry(stats.get(j).getValue(),
                            Integer.valueOf(stats.get(j).getDate().substring(9, 10))-1)//get day
            );
        }
        return entries;
    }

    private boolean checkSameDate(short period,String firstDate, String secondDate){
        //formate of date - yyyy-MM-DD
        if (period == 0) //should check year
            return firstDate.substring(5, 6).equals(secondDate.substring(0, 4));
        else //should check month
            return firstDate.substring(0, 3).equals(secondDate.substring(6, 7));
    }

}
