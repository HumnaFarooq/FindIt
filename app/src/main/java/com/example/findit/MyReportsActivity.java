package com.example.findit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MyReportsActivity extends AppCompatActivity {

    private LinearLayout llMyReportsList;
    private TextView tvEmptyMyReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);

        Button btnBack = findViewById(R.id.btnBackMyReports);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        llMyReportsList = findViewById(R.id.llMyReportsList);
        tvEmptyMyReports = findViewById(R.id.tvEmptyMyReports);
        Button btnAddNewReport = findViewById(R.id.btnAddNewReport);

        if (btnAddNewReport != null) {
            btnAddNewReport.setOnClickListener(v -> {
                Intent intent = new Intent(MyReportsActivity.this, ReportItemActivity.class);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        renderMyReports();
    }

    private void renderMyReports() {
        if (llMyReportsList == null) return;
        llMyReportsList.removeAllViews();

        List<Item> myReports = DataRepository.getInstance().getMyReports();

        if (myReports.isEmpty()) {
            if (tvEmptyMyReports != null) {
                tvEmptyMyReports.setVisibility(View.VISIBLE);
            }
            return;
        }

        if (tvEmptyMyReports != null) {
            tvEmptyMyReports.setVisibility(View.GONE);
        }

        for (Item item : myReports) {
            View card = createReportCard(item);
            llMyReportsList.addView(card);
        }
    }

    private View createReportCard(Item item) {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setBackgroundColor(Color.WHITE);
        int pad = (int) (18 * getResources().getDisplayMetrics().density);
        container.setPadding(pad, pad, pad, pad);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = (int) (12 * getResources().getDisplayMetrics().density);
        container.setLayoutParams(params);

        // Header with title
        TextView tvTitle = new TextView(this);
        tvTitle.setText(item.getName());
        tvTitle.setTextColor(Color.parseColor("#153448"));
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, Typeface.BOLD);
        container.addView(tvTitle);

        // Details
        TextView tvDetails = new TextView(this);
        String details = "Type: " + item.getType() + " | Status: Active\nLocation: " + item.getLocation() + "\nDate: " + item.getDate();
        tvDetails.setText(details);
        tvDetails.setTextColor(Color.parseColor("#4A5568"));
        tvDetails.setTextSize(14);
        tvDetails.setPadding(0, 6, 0, 10);
        container.addView(tvDetails);

        // Actions: View, Edit, Delete
        LinearLayout btnRow = new LinearLayout(this);
        btnRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.topMargin = (int) (8 * getResources().getDisplayMetrics().density);
        btnRow.setLayoutParams(rowParams);

        // View Button
        Button btnView = new Button(this);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        viewParams.rightMargin = (int) (4 * getResources().getDisplayMetrics().density);
        btnView.setLayoutParams(viewParams);
        btnView.setText("View Details");
        btnView.setAllCaps(false);
        btnView.setBackgroundColor(Color.parseColor("#DCEEF2"));
        btnView.setTextColor(Color.parseColor("#153448"));
        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(MyReportsActivity.this, ItemDetailsActivity.class);
            intent.putExtra("item_id", item.getId());
            intent.putExtra("item_name", item.getName());
            intent.putExtra("item_type", item.getType());
            intent.putExtra("item_desc", item.getDescription());
            intent.putExtra("item_location", item.getLocation());
            intent.putExtra("item_date", item.getDate());
            intent.putExtra("item_contact", item.getContact());
            startActivity(intent);
        });
        btnRow.addView(btnView);

        // Edit Button
        Button btnEdit = new Button(this);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        editParams.leftMargin = (int) (4 * getResources().getDisplayMetrics().density);
        editParams.rightMargin = (int) (4 * getResources().getDisplayMetrics().density);
        btnEdit.setLayoutParams(editParams);
        btnEdit.setText("Edit");
        btnEdit.setAllCaps(false);
        btnEdit.setBackgroundColor(Color.parseColor("#176B78"));
        btnEdit.setTextColor(Color.WHITE);
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(MyReportsActivity.this, ReportItemActivity.class);
            intent.putExtra("is_edit_mode", true);
            intent.putExtra("item_id", item.getId());
            intent.putExtra("item_name", item.getName());
            intent.putExtra("item_type", item.getType());
            intent.putExtra("item_desc", item.getDescription());
            intent.putExtra("item_location", item.getLocation());
            intent.putExtra("item_date", item.getDate());
            intent.putExtra("item_contact", item.getContact());
            startActivity(intent);
        });
        btnRow.addView(btnEdit);

        // Delete Button
        Button btnDelete = new Button(this);
        LinearLayout.LayoutParams deleteParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        deleteParams.leftMargin = (int) (4 * getResources().getDisplayMetrics().density);
        btnDelete.setLayoutParams(deleteParams);
        btnDelete.setText("Delete");
        btnDelete.setAllCaps(false);
        btnDelete.setBackgroundColor(Color.parseColor("#E53E3E"));
        btnDelete.setTextColor(Color.WHITE);
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(MyReportsActivity.this)
                    .setTitle("Delete Report")
                    .setMessage("Are you sure you want to delete \"" + item.getName() + "\"?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        DataRepository.getInstance().deleteItem(item.getId());
                        Toast.makeText(MyReportsActivity.this, "Report deleted successfully!", Toast.LENGTH_SHORT).show();
                        renderMyReports();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
        btnRow.addView(btnDelete);

        container.addView(btnRow);

        return container;
    }
}
