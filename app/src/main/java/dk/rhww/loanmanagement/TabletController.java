package dk.rhww.loanmanagement;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

// This is the CONTROLLER Class
public class TabletController {
    private Context context;

    public TabletController(Context context) {
        this.context = context;
    }

    public boolean addTablet(Tablet tablet) {
        TabletDatabaseHelper dbHelper = new TabletDatabaseHelper(context);
        String brand = tablet.getTabletBrand();
        String loanerName = tablet.getLoanerName();
        String loanedDate = tablet.getLoanedDate();
        String cableType = tablet.getCableType();

        return dbHelper.addTablet(brand, loanerName, loanedDate, cableType);
    }

    public boolean removeTablet(int tablet_id) {
        TabletDatabaseHelper dbHelper = new TabletDatabaseHelper(context);
        return dbHelper.deleteTablet(tablet_id) > 0;
    }

    public List<Tablet> getTablets() {
        TabletDatabaseHelper dbHelper = new TabletDatabaseHelper(context);
        return dbHelper.getAllTablets();
    }
}
