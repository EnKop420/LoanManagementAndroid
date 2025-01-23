package dk.rhww.loanmanagement;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AdminActivity extends AppCompatActivity{
    private List<Tablet> tabletList;
    private RecyclerView recyclerView;
    private TabletAdapter tabletAdapter;
    private Spinner filteringSpinner;
    private String filteringOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(v -> {
            finish();
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // region Setting up Spinner Items
        filteringSpinner = findViewById(R.id.filteringSpinner);

        ArrayAdapter<CharSequence> filteringAdapter = ArrayAdapter.createFromResource(this,
                R.array.filtering, android.R.layout.simple_spinner_item); // Gets a reference to the filtering array in strings.xml. Creates an array adapter from the filtering array.
        filteringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Sets the layout for the drop down menu.
        filteringSpinner.setAdapter(filteringAdapter);

        filteringSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                filteringOption = parentView.getItemAtPosition(position).toString();
                filterTabletList(tabletList, filteringOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                filteringOption = "All";
                filterTabletList(tabletList, filteringOption);
            }
        });
        // endregion

    }

    @Override
    protected void onStart() {
        super.onStart();
        TabletDatabaseHelper dbHelper = new TabletDatabaseHelper(this);
        tabletList = dbHelper.getAllTablets();

        if (tabletList == null) {

        }
        else {
            tabletAdapter = new TabletAdapter(tabletList);
            recyclerView.setAdapter(tabletAdapter);
        }
    }

    public void filterTabletList(List<Tablet> list, String filter) {
        List<Tablet> filteredList;

        // Switch statement to filter the list based on the selected filter.
        switch (filter) {
            case "Samsung":
            case "Huawei":
                filteredList = list.stream()
                        .filter(obj -> obj.getTabletBrand().equals(filter))
                        .collect(Collectors.toList());
                break;
            case "USB-C":
            case "Micro-USB":
                filteredList = list.stream()
                        .filter(obj -> obj.getCableType().equals(filter))
                        .collect(Collectors.toList());
                break;
            case "Dato":
                filteredList = list;
                // Define the formatter to match dd/MM/yyyy
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

                // Sort with validation in descending order
                filteredList.sort(Comparator.comparing(o -> {
                    String dateStr = o.getLoanedDate();
                    try {
                        return LocalDate.parse(dateStr, formatter);
                    } catch (DateTimeParseException e) {
                        // Use a default fallback value for invalid dates. LocalDate.MIN is a valid date in Java and is the earliest possible date.
                        return LocalDate.MIN;
                    }
                }, Comparator.reverseOrder())); // Sort in descending order
                break;
            default:
                filteredList = list;
                break;
        }

        // Update the adapter with the filtered list
        tabletAdapter = new TabletAdapter(filteredList);
        recyclerView.setAdapter(tabletAdapter);
    }
}