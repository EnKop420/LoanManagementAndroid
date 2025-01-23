package dk.rhww.loanmanagement;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Adapter Class. Adapters connects data to ui components. It creates views from data and can create and bind data to those views.
// Recycling views is reusing old views with new data. The adapter also helps with recycling views.
// When creating an adapter it is required to override 3 functions. onCreateViewHolder, onBindViewHolder and getItemCount.
// onCreateViewHolder is called when the adapter needs a new view.
// onBindViewHolder is called when the adapter needs to bind data to a view.
// getItemCount is called when the adapter needs to know how many items are in the list.
public class TabletAdapter extends RecyclerView.Adapter<TabletAdapter.TabletViewHolder> {
    // Creates a private list of tablets. This is where all the data items will be.
    private List<Tablet> tabletList;

    public TabletAdapter(List<Tablet> tabletList) {
        this.tabletList = tabletList;
    }

    @Override
    public TabletViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tablet_item, parent, false);
        return new TabletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TabletViewHolder holder, int position) {
        // Get the tablet at the current position
        Tablet tablet = tabletList.get(position);

        // Bind the data to the views
        holder.tabletBrandTextView.setText(tablet.getTabletBrand());
        holder.loanerNameTextView.setText(tablet.getLoanerName());
        holder.loanedDateTextView.setText(tablet.getLoanedDate());
        holder.cableTypeTextView.setText(tablet.getCableType());

        // Adds a click listener to the delete button aswell
        holder.deleteButton.setOnClickListener(v -> {
            TabletDatabaseHelper dbhelper = new TabletDatabaseHelper(v.getContext());
            dbhelper.deleteTablet(tablet.id);
            // Show a dialog box when the delete button is clicked. Adds a on click listener to the dialog box that closes it.
            UiComponent.showMessageDialog(v.getContext(), "Lånt Tablet Returneret", tablet.getLoanerName() + " har returneret tabletten",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    });
            tabletList.remove(tablet);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        // Return the size of the list
        return tabletList.size();
    }

    // ViewHolder class to hold / cache the views for each item. This is to avoid it inflating and finding views repeatedly for each item.
    // It is used in onCreateViewHolder.
    public static class TabletViewHolder extends RecyclerView.ViewHolder {
        TextView tabletBrandTextView;
        TextView loanerNameTextView;
        TextView loanedDateTextView;
        TextView cableTypeTextView;
        Button deleteButton;

        // The constructor a view and used findViewById to link the UI elements in the item layout.
        public TabletViewHolder(View itemView) {
            super(itemView);
            tabletBrandTextView = itemView.findViewById(R.id.tabletBrandTextView);
            loanerNameTextView = itemView.findViewById(R.id.loanerNameTextView);
            loanedDateTextView = itemView.findViewById(R.id.loanedDateTextView);
            cableTypeTextView = itemView.findViewById(R.id.cableTypeTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
