package dk.rhww.loanmanagement;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ensure SQLite Database has been created.
        TabletDatabaseHelper dbHelper = new TabletDatabaseHelper(MainActivity.this);
        dbHelper.getWritableDatabase();

        // Initialize buttons
        Button btnBruger = findViewById(R.id.btn_bruger);
        btnBruger.setOnClickListener(v -> {
            // Start UserActivity by making an intent
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            startActivity(intent);
        });

        Button btnAdministrator = findViewById(R.id.btn_administrator);
        btnAdministrator.setOnClickListener(v -> {
            // Start AdminActivity
            showPasswordDialog();
        });
    }

    // Method to show the password alert dialog.
    private void showPasswordDialog() {
        // Create an EditText for password input
        final EditText passwordInput = new EditText(this);
        passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        passwordInput.setHint("Indtast Password");

        // Build the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Administrator Login");
        builder.setMessage("Indtast Administrator Password:");
        builder.setView(passwordInput);

        // Set up the "Login" button
        builder.setPositiveButton("Login", (dialog, which) -> {
            String password = passwordInput.getText().toString();
            if (password.equals("admin123")) {
                // Correct password, start AdminActivity
                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                // Display message for incorrect password. Toast is a small temporary message.
                Toast.makeText(MainActivity.this, "Forkert Password", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the "Cancel" button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Show the dialog
        builder.create().show();
    }
}