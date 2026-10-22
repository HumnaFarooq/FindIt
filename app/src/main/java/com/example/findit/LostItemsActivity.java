package com.example.findit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class LostItemsActivity extends AppCompatActivity {

    private LinearLayout llLostItemsList;
    private TextView tvEmptyLost;
    private EditText etLostSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_items);

        Button btnBack = findViewById(R.id.btnBackLost);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        llLostItemsList = findViewById(R.id.llLostItemsList);
        tvEmptyLost = findViewById(R.id.tvEmptyLost);
        etLostSearch = findViewById(R.id.etLostSearch);

        etLostSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                renderLostItems(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentQuery = etLostSearch != null ? etLostSearch.getText().toString().trim() : "";
        renderLostItems(currentQuery);
    }

    private void renderLostItems(String query) {
        if (llLostItemsList == null) return;
        llLostItemsList.removeAllViews();

        List<Item> lostItems = DataRepository.getInstance().getLostItems();
        int visibleCount = 0;

        for (Item item : lostItems) {
            if (!query.isEmpty()) {
                boolean nameMatch = item.getName() != null && item.getName().toLowerCase().contains(query.toLowerCase());
                boolean locMatch = item.getLocation() != null && item.getLocation().toLowerCase().contains(query.toLowerCase());
                boolean descMatch = item.getDescription() != null && item.getDescription().toLowerCase().contains(query.toLowerCase());
                if (!nameMatch && !locMatch && !descMatch) {
                    continue;
                }
            }

            visibleCount++;
            View itemView = createItemCard(item);
            llLostItemsList.addView(itemView);
        }

        if (tvEmptyLost != null) {
            tvEmptyLost.setVisibility(visibleCount == 0 ? View.VISIBLE : View.GONE);
        }
    }

    private View createItemCard(Item item) {
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
        String details = "Location: " + item.getLocation() + "\nDate: " + item.getDate() + "\nStatus: LOST";
        tvDetails.setText(details);
        tvDetails.setTextColor(Color.parseColor("#4A5568"));
        tvDetails.setTextSize(14);
        tvDetails.setPadding(0, 8, 0, 0);
        card.addView(tvDetails);

        TextView tvTapHint = new TextView(this);
        tvTapHint.setText("Tap to view details →");
        tvTapHint.setTextColor(Color.parseColor("#176B78"));
        tvTapHint.setTextSize(13);
        tvTapHint.setPadding(0, 10, 0, 0);
        card.addView(tvTapHint);

        card.setClickable(true);
        card.setFocusable(true);
        card.setOnClickListener(v -> {
            Intent intent = new Intent(LostItemsActivity.this, ItemDetailsActivity.class);
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
