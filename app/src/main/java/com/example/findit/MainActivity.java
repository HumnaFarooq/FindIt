package com.example.findit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private long backPressedTime = 0;
    private Toast exitToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (view, insets) -> {
                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());
                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );
                    return insets;
                });

        // Find buttons and search field
        Button btnReportItem = findViewById(R.id.btnReportItem);
        Button btnLostItems = findViewById(R.id.btnLostItems);
        Button btnFoundItems = findViewById(R.id.btnFoundItems);
        Button btnMyReports = findViewById(R.id.btnMyReports);
        Button btnSearch = findViewById(R.id.btnSearch);
        EditText etSearch = findViewById(R.id.etSearch);
        TextView tvRecentItem1 = findViewById(R.id.tvRecentItem1);
        TextView tvRecentItem2 = findViewById(R.id.tvRecentItem2);

        // Flow 4: Home Screen → Report Lost / Found Item
        btnReportItem.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ReportItemActivity.class);
            startActivity(intent);
        });

        // Flow 2: Home Screen → Lost Items
        btnLostItems.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LostItemsActivity.class);
            startActivity(intent);
        });

        // Flow 3: Home Screen → Found Items
        btnFoundItems.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, FoundItemsActivity.class);
            startActivity(intent);
        });

        // Flow 5, 6: Home Screen → My Reports
        btnMyReports.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MyReportsActivity.class);
            startActivity(intent);
        });

        // Flow 7: Home Screen → Search Items → Search & Filter
        View.OnClickListener openSearchListener = view -> {
            Intent intent = new Intent(MainActivity.this, SearchFilterActivity.class);
            String query = etSearch.getText().toString().trim();
            if (!query.isEmpty()) {
                intent.putExtra("search_keyword", query);
            }
            startActivity(intent);
        };

        if (btnSearch != null) {
            btnSearch.setOnClickListener(openSearchListener);
        }

        etSearch.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE) {
                openSearchListener.onClick(textView);
                return true;
            }
            return false;
        });

        // Recent items clicks → Item Details
        if (tvRecentItem1 != null) {
            tvRecentItem1.setOnClickListener(v -> {
                Item item = DataRepository.getInstance().getItemById("item-1");
                if (item != null) {
                    openItemDetails(item);
                }
            });
        }

        if (tvRecentItem2 != null) {
            tvRecentItem2.setOnClickListener(v -> {
                Item item = DataRepository.getInstance().getItemById("item-4");
                if (item != null) {
                    openItemDetails(item);
                }
            });
        }

        // Flow 8: Home Screen → Back / Exit App
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    if (exitToast != null) {
                        exitToast.cancel();
                    }
                    finishAffinity(); // Exit the app
                } else {
                    exitToast = Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT);
                    exitToast.show();
                    backPressedTime = System.currentTimeMillis();
                }
            }
        });
    }

    private void openItemDetails(Item item) {
        Intent intent = new Intent(MainActivity.this, ItemDetailsActivity.class);
        intent.putExtra("item_id", item.getId());
        intent.putExtra("item_name", item.getName());
        intent.putExtra("item_type", item.getType());
        intent.putExtra("item_desc", item.getDescription());
        intent.putExtra("item_location", item.getLocation());
        intent.putExtra("item_date", item.getDate());
        intent.putExtra("item_contact", item.getContact());
        startActivity(intent);
    }
}
