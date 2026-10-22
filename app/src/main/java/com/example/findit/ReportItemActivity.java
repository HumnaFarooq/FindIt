package com.example.findit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;

public class ReportItemActivity extends AppCompatActivity {

    private TextView tvReportTitle;
    private RadioGroup rgReportType;
    private RadioButton rbLost;
    private RadioButton rbFound;
    private EditText etItemName;
    private EditText etDescription;
    private EditText etLocation;
    private EditText etDate;
    private EditText etContact;
    private Button btnUploadImage;
    private ImageView ivSelectedImagePreview;
    private Button btnSubmitReport;

    private boolean isEditMode = false;
    private String editingItemId = null;
    private String selectedImageUriString = null;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUriString = uri.toString();
                    if (ivSelectedImagePreview != null) {
                        ivSelectedImagePreview.setVisibility(View.VISIBLE);
                        ivSelectedImagePreview.setImageURI(uri);
                    }
                    if (btnUploadImage != null) {
                        btnUploadImage.setText("Image Selected ✓ (Tap to Change)");
                    }
                    Toast.makeText(this, "Item image chosen", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_item);

        tvReportTitle = findViewById(R.id.tvReportTitle);
        rgReportType = findViewById(R.id.rgReportType);
        rbLost = findViewById(R.id.rbLost);
        rbFound = findViewById(R.id.rbFound);
        etItemName = findViewById(R.id.etItemName);
        etDescription = findViewById(R.id.etDescription);
        etLocation = findViewById(R.id.etLocation);
        etDate = findViewById(R.id.etDate);
        etContact = findViewById(R.id.etContact);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        ivSelectedImagePreview = findViewById(R.id.ivSelectedImagePreview);
        btnSubmitReport = findViewById(R.id.btnSubmitReport);
        Button btnBackReport = findViewById(R.id.btnBackReport);

        if (btnBackReport != null) {
            btnBackReport.setOnClickListener(v -> finish());
        }

        // Image Picker
        btnUploadImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        // Check for edit mode
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("is_edit_mode", false)) {
            isEditMode = true;
            editingItemId = intent.getStringExtra("item_id");
            setupEditMode(intent);
        }

        // Submit / Update action
        btnSubmitReport.setOnClickListener(v -> handleSubmitReport());
    }

    private void setupEditMode(Intent intent) {
        tvReportTitle.setText("Edit Report");
        btnSubmitReport.setText("Update Report");

        String name = intent.getStringExtra("item_name");
        String type = intent.getStringExtra("item_type");
        String desc = intent.getStringExtra("item_desc");
        String loc = intent.getStringExtra("item_location");
        String date = intent.getStringExtra("item_date");
        String contact = intent.getStringExtra("item_contact");

        if (name != null) etItemName.setText(name);
        if (desc != null) etDescription.setText(desc);
        if (loc != null) etLocation.setText(loc);
        if (date != null) etDate.setText(date);
        if (contact != null) etContact.setText(contact);

        if ("FOUND".equalsIgnoreCase(type)) {
            rbFound.setChecked(true);
        } else {
            rbLost.setChecked(true);
        }
    }

    private void handleSubmitReport() {
        String name = etItemName.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String type = rbFound.isChecked() ? "FOUND" : "LOST";

        if (name.isEmpty()) {
            etItemName.setError("Item name is required");
            etItemName.requestFocus();
            return;
        }

        if (location.isEmpty()) {
            etLocation.setError("Location is required");
            etLocation.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            date = "Recently";
        }

        if (contact.isEmpty()) {
            contact = "Campus Student";
        }

        if (isEditMode && editingItemId != null) {
            Item existingItem = DataRepository.getInstance().getItemById(editingItemId);
            if (existingItem != null) {
                existingItem.setName(name);
                existingItem.setType(type);
                existingItem.setDescription(description);
                existingItem.setLocation(location);
                existingItem.setDate(date);
                existingItem.setContact(contact);
                if (selectedImageUriString != null) {
                    existingItem.setImageUri(selectedImageUriString);
                }
                DataRepository.getInstance().updateItem(existingItem);
            }
            Toast.makeText(this, "Report updated successfully!", Toast.LENGTH_SHORT).show();
            // Finish and return to My Reports
            finish();
        } else {
            String newId = "report-" + UUID.randomUUID().toString().substring(0, 8);
            Item newItem = new Item(
                    newId,
                    name,
                    type,
                    "General",
                    description,
                    location,
                    date,
                    contact,
                    selectedImageUriString,
                    true // user report
            );
            DataRepository.getInstance().addItem(newItem);
            Toast.makeText(this, "Report submitted successfully!", Toast.LENGTH_SHORT).show();

            // Flow: Submit Report → My Reports
            Intent myReportsIntent = new Intent(ReportItemActivity.this, MyReportsActivity.class);
            myReportsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myReportsIntent);
            finish();
        }
    }
}
