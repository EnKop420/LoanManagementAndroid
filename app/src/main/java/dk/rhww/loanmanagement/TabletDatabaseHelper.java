package dk.rhww.loanmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TabletDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    // region Table and column names
    private static final String TABLE_NAME = "tablets";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BRAND = "brand";
    private static final String COLUMN_LOANER_NAME = "loaner_name";
    private static final String COLUMN_LOANED_DATE = "loaned_date";
    private static final String COLUMN_CABLE_TYPE = "cable_type";

    // endregion
    public TabletDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // region OnCreate and OnUpgrade Methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BRAND + " TEXT, " +
                COLUMN_LOANER_NAME + " TEXT, " +
                COLUMN_LOANED_DATE + " TEXT, " +
                COLUMN_CABLE_TYPE + " TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    // endregion

    // Function to Add a new tablet to the database
    public boolean addTablet(String brand, String loanerName, String loanedDate, String cableType) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            ContentValues values = new ContentValues();

            values.put(COLUMN_BRAND, brand);
            values.put(COLUMN_LOANER_NAME, loanerName);
            values.put(COLUMN_LOANED_DATE, loanedDate);
            values.put(COLUMN_CABLE_TYPE, cableType);

            // Insert the row and return the ID of the new row
            long id = db.insert(TABLE_NAME, null, values);
            db.close();
            return true;
        } catch (Exception e){
            Log.e("TabletDatabaseHelper", "Error adding tablet: " + e.getMessage());
            return false;
        }
    }

    // Function to Delete a tablet by ID
    public int deleteTablet(int id) {
        try (SQLiteDatabase db = this.getWritableDatabase()) {
            // Delete row by ID and return the number of rows deleted
            int rowsDeleted = db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
            return rowsDeleted;
        } catch (Exception e){
            Log.e("TabletDatabaseHelper", "Error adding tablet: " + e.getMessage());
            return 0;
        }

    }

    // Function to Get all tablets
    public List<Tablet> getAllTablets() {
        try (SQLiteDatabase db = this.getReadableDatabase()) {

            List<Tablet> tabletsList = new ArrayList<>();
            String query = "SELECT * FROM " + TABLE_NAME;
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    // Retrieve data for each row
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                    String brand = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BRAND));
                    String loanerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOANER_NAME));
                    String loanedDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOANED_DATE));
                    String cableType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CABLE_TYPE));

                    // Add it to the list as a formatted string
                    Tablet tablet = new Tablet(brand, loanerName, loanedDate, cableType);
                    tablet.setId(id);
                    tabletsList.add(tablet);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
            return tabletsList;
        } catch (Exception e){
            Log.e("TabletDatabaseHelper", "Error getting all tablets: " + e.getMessage());
            return null;
        }
    }
}
