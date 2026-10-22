package com.example.findit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class SearchFilterActivity extends AppCompatActivity {

    private EditText etSearchKeyword;
    private RadioGroup rgCategory;
    private RadioButton rbAllItems;
    private RadioButton rbLostItems;
    private RadioButton rbFoundItems;
    private EditText etFilterLocation;
    private Button btnApplyFilter;
    private Button btnClearFilter;
    private TextView tvMatchingItemsHeader;
    private TextView tvNoMatchingItems;
    private LinearLayout llMatchingItemsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter);

        Button btnBackSearch = findViewById(R.id.btnBackSearch);
        if (btnBackSearch != null) {
            btnBackSearch.setOnClickListener(v -> finish());
        }

        etSearchKeyword = findViewById(R.id.etSearchKeyword);
        rgCategory = findViewById(R.id.rgCategory);
        rbAllItems = findViewById(R.id.rbAllItems);
        rbLostItems = findViewById(R.id.rbLostItems);
        rbFoundItems = findViewById(R.id.rbFoundItems);
        etFilterLocation = findViewById(R.id.etFilterLocation);
        btnApplyFilter = findViewById(R.id.btnApplyFilter);
        btnClearFilter = findViewById(R.id.btnClearFilter);
        tvMatchingItemsHeader = findViewById(R.id.tvMatchingItemsHeader);
        tvNoMatchingItems = findViewById(R.id.tvNoMatchingItems);
        llMatchingItemsContainer = findViewById(R.id.llMatchingItemsContainer);

        // Pre-fill keyword if passed from Home Screen
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("search_keyword")) {
            String initialKeyword = intent.getStringExtra("search_keyword");
            if (initialKeyword != null && !initialKeyword.isEmpty()) {
                etSearchKeyword.setText(initialKeyword);
                applySearchFilters();
            }
        }

        btnApplyFilter.setOnClickListener(v -> applySearchFilters());

        btnClearFilter.setOnClickListener(v -> {
            etSearchKeyword.setText("");
            etFilterLocation.setText("");
            rbAllItems.setChecked(true);
            llMatchingItemsContainer.removeAllViews();
            tvMatchingItemsHeader.setVisibility(View.GONE);
            tvNoMatchingItems.setVisibility(View.GONE);
            Toast.makeText(this, "Filters cleared", Toast.LENGTH_SHORT).show();
        });
    }

    private void applySearchFilters() {
        String keyword = etSearchKeyword.getText().toString().trim();
        String location = etFilterLocation.getText().toString().trim();

        String category = "ALL";
        if (rbLostItems.isChecked()) {
            category = "LOST";
        } else if (rbFoundItems.isChecked()) {
            category = "FOUND";
        }

        List<Item> matchingItems = DataRepository.getInstance().searchAndFilter(keyword, category, location);

        llMatchingItemsContainer.removeAllViews();
        tvMatchingItemsHeader.setVisibility(View.VISIBLE);

        if (matchingItems.isEmpty()) {
            tvNoMatchingItems.setVisibility(View.VISIBLE);
        } else {
            tvNoMatchingItems.setVisibility(View.GONE);
            tvMatchingItemsHeader.setText("Matching Items (" + matchingItems.size() + ")");
            for (Item item : matchingItems) {
                View card = createMatchingItemCard(item);
                llMatchingItemsContainer.addView(card);
            }
        }
    }

    private View createMatchingItemCard(Item item) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundColor(Color.WHITE);
        int pad = (int) (18 * getResources().getDisplayMetrics().density);
        card.setPadding(pad, pad, pad, pad);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = (int) (12 * getResources().getDisplayMetrics().density);
        card.setLayoutParams(params);

        TextView tvTitle = new TextView(this);
        tvTitle.setText(item.getName());
        tvTitle.setTextColor(Color.parseColor("#153448"));
        tvTitle.setTextSize(18);
        tvTitle.setTypeface(null, Typeface.BOLD);
        card.addView(tvTitle);

        TextView tvDetails = new TextView(this);
        String details = "Status: " + item.getType() + " | Location: " + item.getLocation() + "\nDate: " + item.getDate();
        tvDetails.setText(details);
        tvDetails.setTextColor(Color.parseColor("#4A5568"));
        tvDetails.setTextSize(14);
        tvDetails.setPadding(0, 8, 0, 0);
        card.addView(tvDetails);

        TextView tvTap = new TextView(this);
        tvTap.setText("View Details →");
        tvTap.setTextColor(Color.parseColor("#176B78"));
        tvTap.setTextSize(13);
        tvTap.setPadding(0, 10, 0, 0);
        card.addView(tvTap);

        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(v -> {
            Intent intent = new Intent(SearchFilterActivity.this, ItemDetailsActivity.class);
            intent.putExtra("item_id", item.getId());
            intent.putExtra("item_name", item.getName());
            intent.putExtra("item_type", item.getType());
            intent.putExtra("item_desc", item.getDescription());
            intent.putExtra("item_location", item.getLocation());
            intent.putExtra("item_date", item.getDate());
            intent.putExtra("item_contact", item.getContact());
            startActivity(intent);
        });

        return card;
    }
}
