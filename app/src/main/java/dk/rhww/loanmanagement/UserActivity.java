package dk.rhww.loanmanagement;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class UserActivity extends AppCompatActivity {
    private Spinner spinnerTabletBrand, spinnerCableType;
    private EditText editLoanerName;
    private TextView loanedDateTextView;
    private Button submitButton;
    private Button returnButton;

    private String tabletBrand, cableType, loanerName, loanedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        spinnerTabletBrand = findViewById(R.id.spinnerTabletBrand);
        spinnerCableType = findViewById(R.id.spinnerCableType);
        editLoanerName = findViewById(R.id.editLoanerName);
        loanedDateTextView = findViewById(R.id.loanedDateTextView);
        submitButton = findViewById(R.id.submitButton);
        returnButton = findViewById(R.id.returnButton);

        // region Setting up Spinner Items

        // Set up Spinner for Tablet Brand
        ArrayAdapter<CharSequence> tabletBrandAdapter = ArrayAdapter.createFromResource(this,
                R.array.tablet_brands, android.R.layout.simple_spinner_item);
        tabletBrandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTabletBrand.setAdapter(tabletBrandAdapter);

        spinnerTabletBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                tabletBrand = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                tabletBrand = "";
            }
        });

        // Set up Spinner for Cable Type
        ArrayAdapter<CharSequence> cableTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.cable_types, android.R.layout.simple_spinner_item);
        cableTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCableType.setAdapter(cableTypeAdapter);

        spinnerCableType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                cableType = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                cableType = "";
            }
        });

        // endregion

        returnButton.setOnClickListener(v -> {
            finish();
        });

        // region Set up Date Dialog
        loanedDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(UserActivity.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            // Format the date as dd/MM/yyyy
                            loanedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                            loanedDateTextView.setText(loanedDate);
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        // endregion

        // region Submit Button listener
        submitButton.setOnClickListener(v -> {
            loanerName = editLoanerName.getText().toString();

            if (tabletBrand.isEmpty() || loanerName.isEmpty() || loanedDate == null || cableType.isEmpty()) {
                Toast.makeText(UserActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Process the data
                Tablet tablet = new Tablet(tabletBrand, loanerName, loanedDate, cableType);
                TabletController tabletController = new TabletController(UserActivity.this);
                if (tabletController.addTablet(tablet)) {
                    // Display the data in a Dialog box as a receipt
                    UiComponent.showMessageDialog(UserActivity.this, "Tablet Lånt", "Kvittering: \n\n" +
                            "Brand: " + tabletBrand + "\n" +
                            "Låner: " + loanerName + "\n" +
                            "Dato: " + loanedDate + "\n" +
                            "Kabel: " + cableType,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // When the dialog is dismissed (button clicked), finish the activity
                                    finish();
                            }
                    });
                }
                else {
                    // Makes a toast if the tablet could not be added.
                    Toast.makeText(UserActivity.this, "Error adding tablet. Try Again later", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // endregion
    }
}