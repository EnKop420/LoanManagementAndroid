package dk.rhww.loanmanagement;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class UiComponent {
    // Static method to create and show an AlertDialog
    public static void showMessageDialog(Context context, String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setCancelable(false); // Prevent dismissal by touching outside
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
