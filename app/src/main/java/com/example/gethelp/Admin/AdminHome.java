package com.example.gethelp.Admin;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gethelp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class AdminHome extends Fragment {

    public static final int[] SLICE_COLOURS = {
            Color.parseColor("#d1c4e9"),
            Color.parseColor("#ce93d8"),
            Color.parseColor("#ba68c8"),
            Color.parseColor("#ab47bc"),
            Color.parseColor("#9c27b0"),
            Color.parseColor("#8e24aa"),
            Color.parseColor("#7b1fa2"),
            Color.parseColor("#6a1b9a"),
            Color.parseColor("#4a148c"),
            Color.parseColor("#ea80fc"),
            Color.parseColor("#e040fb"),
            Color.parseColor("#d500f9"),
            Color.parseColor("#aa00ff"),
            Color.parseColor("#844dea"),

    };
    View view;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference serviceRef = db.collection("completeServices");
    private CollectionReference serviceTypeRef = db.collection("services");
    private CollectionReference collectionRef = db.collection("professionalPending");
    private UserApprovalAdapter adapter;
    int[] monthlyPerformance = {0,0,0,0,0,0,0,0,0,0,0,0};
    ArrayList<String> services = new ArrayList<>();
    ArrayList<Integer> servicePerformance = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_home,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPieChartData();
        getLineChartData();
        setupRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void setupPieChart(ArrayList<PieEntry> pieEntries) {
        PieChart chart = (PieChart) view.findViewById(R.id.service_chart);

        PieDataSet dataSet = new PieDataSet(pieEntries,"");
        dataSet.setColors(SLICE_COLOURS);
        PieData data = new PieData(dataSet);
        chart.setDrawSliceText(false);
        chart.setData(data);
        chart.animateY(1000);
        chart.getDescription().setEnabled(false);

//        Legend legend = chart.getLegend();
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDrawInside(false);
//        legend.setTextSize(15);
//        chart.invalidate();
    }

    public void setupLineChart(ArrayList<Entry> values){
        final String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        LineChart mChart = (LineChart) view.findViewById(R.id.help_chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.getLegend().setEnabled(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getDescription().setEnabled(false);
        mChart.animateXY(1000,1000);

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

    private void setupRecyclerView() {
        Query query = collectionRef.limit(500);

        FirestoreRecyclerOptions<UserApprovalItem> options = new FirestoreRecyclerOptions.Builder<UserApprovalItem>()
                .setQuery(query,UserApprovalItem.class)
                .build();

        adapter = new UserApprovalAdapter(options);

        RecyclerView recyclerView = view.findViewById(R.id.approval_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void getLineChartData(){
        serviceRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Timestamp time = (Timestamp) document.get("date");
                                Date date = time.toDate();
                                monthlyPerformance[date.getMonth()] ++;
                            }
                            ArrayList<Entry> sortedValues = new ArrayList<>();
                            sortedValues.add(new Entry(0, monthlyPerformance[0]));
                            sortedValues.add(new Entry(1, monthlyPerformance[1]));
                            sortedValues.add(new Entry(2, monthlyPerformance[2]));
                            sortedValues.add(new Entry(3, monthlyPerformance[3]));
                            sortedValues.add(new Entry(4, monthlyPerformance[4]));
                            sortedValues.add(new Entry(5, monthlyPerformance[5]));
                            sortedValues.add(new Entry(6, monthlyPerformance[6]));
                            sortedValues.add(new Entry(7, monthlyPerformance[7]));
                            sortedValues.add(new Entry(8, monthlyPerformance[8]));
                            sortedValues.add(new Entry(9, monthlyPerformance[9]));
                            sortedValues.add(new Entry(10, monthlyPerformance[10]));
                            sortedValues.add(new Entry(11, monthlyPerformance[11]));
                            setupLineChart(sortedValues);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getPieChartData(){
        serviceTypeRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.get("title").toString();
                                services.add(title);
                            }
                            getServicePerformance();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    private void getServicePerformance(){
        serviceRef
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String type = document.get("type").toString();
                                for (int counter = 0; counter < services.size(); counter++) {
                                    servicePerformance.add(0);
                                    if(services.get(counter).equals(type)){
                                        servicePerformance.set(counter, servicePerformance.get(counter) + 1);
                                    }
                                }
                            }
                            ArrayList<PieEntry> pieEntries = new ArrayList<>();
                            for(int i=0; i< services.size(); i++){
                                pieEntries.add(new PieEntry(servicePerformance.get(i),services.get(i)));
                            }
                            setupPieChart(pieEntries);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


}
