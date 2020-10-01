package com.example.gethelp;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class AdminHome extends Fragment {

    public static final int[] SLICE_COLOURS = {
            Color.parseColor("#ab4ded"), Color.parseColor("#7806e3"), Color.parseColor("#844dea")
    };
    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.admin_home,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPieChart();
        setupLineChart();
    }

    public void setupPieChart() {
        PieChart chart = (PieChart)v.findViewById(R.id.service_chart);
        int values[] = {6,1,3};
        String titles[] = {"Technician","Carpenter","Plumber"};
        //Populating a list of pie entries
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        for(int i=0; i< values.length; i++){
            pieEntries.add(new PieEntry(values[i],titles[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(SLICE_COLOURS);
        PieData data = new PieData(dataSet);
        chart.setDrawSliceText(false);
        chart.setData(data);
        chart.animateY(1000);
        chart.getDescription().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setTextSize(15);
        chart.invalidate();
    }

    public void setupLineChart(){
        final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        LineChart mChart = (LineChart) v.findViewById(R.id.help_chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.getLegend().setEnabled(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.animateXY(1000,1000);

        ArrayList<Entry> values = new ArrayList<>();
        values.add(new Entry(0, 0));
        values.add(new Entry(1, 1));
        values.add(new Entry(2, 3));
        values.add(new Entry(3, 2));
        values.add(new Entry(4, 4));
        values.add(new Entry(5, 1));
        values.add(new Entry(6, 2));
        values.add(new Entry(7, 3));
        values.add(new Entry(8, 2));
        values.add(new Entry(9, 1));
        values.add(new Entry(10, 3));
        values.add(new Entry(11, 4));

        //Building Chart
        LineDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
            set1.setDrawIcons(false);
            set1.enableDashedLine(10f, 0f, 0f);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(0f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);
            set1.setFillColor(Color.WHITE);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            XAxis xAxis = mChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
            xAxis.setLabelCount(12,true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(Color.WHITE);
            mChart.setData(data);
        }
    }

}
