package com.example.findit;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        // Header and back buttons
        Button btnBackHeader = findViewById(R.id.btnBackHeader);
        Button btnBackToList = findViewById(R.id.btnBackToList);
        Button btnBackHome = findViewById(R.id.btnBackHome);
        Button btnContactOwner = findViewById(R.id.btnContactOwner);

        TextView tvItemName = findViewById(R.id.tvItemName);
        TextView tvItemStatus = findViewById(R.id.tvItemStatus);
        TextView tvItemDescription = findViewById(R.id.tvItemDescription);
        TextView tvItemLocation = findViewById(R.id.tvItemLocation);
        TextView tvItemDate = findViewById(R.id.tvItemDate);
        TextView tvItemContact = findViewById(R.id.tvItemContact);
        ImageView ivItemImage = findViewById(R.id.ivItemImage);

        // Read extras
        Intent intent = getIntent();
        String name = intent.getStringExtra("item_name");
        String type = intent.getStringExtra("item_type");
        String desc = intent.getStringExtra("item_desc");
        String location = intent.getStringExtra("item_location");
        String date = intent.getStringExtra("item_date");
        String contact = intent.getStringExtra("item_contact");

        if (name != null) tvItemName.setText(name);
        if (desc != null) tvItemDescription.setText(desc);
        if (location != null) tvItemLocation.setText("Location: " + location);
        if (date != null) tvItemDate.setText("Date: " + date);
        if (contact != null) tvItemContact.setText("Contact: " + contact);

        if (type != null) {
            tvItemStatus.setText(type.toUpperCase());
            if ("FOUND".equalsIgnoreCase(type)) {
                tvItemStatus.setBackgroundColor(Color.parseColor("#DDF4E4"));
                tvItemStatus.setTextColor(Color.parseColor("#1E7E34"));
            } else {
                tvItemStatus.setBackgroundColor(Color.parseColor("#FCE8D5"));
                tvItemStatus.setTextColor(Color.parseColor("#9A4D0B"));
            }
        }

        // Contact Reporter Action
        btnContactOwner.setOnClickListener(v -> {
            String contactInfo = contact != null ? contact : "Reporter";
            new AlertDialog.Builder(this)
                    .setTitle("Contact Reporter")
                    .setMessage("Choose how you would like to reach out:\n\n" + contactInfo)
                    .setPositiveButton("Call Reporter", (dialog, which) -> {
                        try {
                            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                            dialIntent.setData(Uri.parse("tel:" + contactInfo.replaceAll("[^0-9+]", "")));
                            startActivity(dialIntent);
                        } catch (Exception e) {
                            Toast.makeText(this, "Calling: " + contactInfo, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Send Message", (dialog, which) -> {
                        try {
                            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                            smsIntent.setData(Uri.parse("sms:" + contactInfo.replaceAll("[^0-9+]", "")));
                            smsIntent.putExtra("sms_body", "Hello, I am contacting you regarding the " + (name != null ? name : "reported item") + " on FindIt.");
                            startActivity(smsIntent);
                        } catch (Exception e) {
                            Toast.makeText(this, "Message: " + contactInfo, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        });

        // Back to previous screen (Lost Items / Found Items / Search Results)
        if (btnBackHeader != null) {
            btnBackHeader.setOnClickListener(v -> finish());
        }
        if (btnBackToList != null) {
            btnBackToList.setOnClickListener(v -> finish());
        }

        // Back to Home Screen directly
        if (btnBackHome != null) {
            btnBackHome.setOnClickListener(v -> {
                Intent homeIntent = new Intent(ItemDetailsActivity.this, MainActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
                finish();
            });
        }
    }
}
