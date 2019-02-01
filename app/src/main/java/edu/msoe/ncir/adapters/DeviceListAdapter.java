package edu.msoe.ncir.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.msoe.ncir.models.Device;
import edu.msoe.ncir.R;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DeviceViewHolder> {

    private int selectedPosition = -1;

    class DeviceViewHolder extends RecyclerView.ViewHolder {
        private final TextView deviceItemView;

        private DeviceViewHolder(View itemView) {
            super(itemView);
            deviceItemView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (selectedPosition == pos) {
                        selectedPosition = -1;
                    } else {
                        selectedPosition = pos;
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    private final LayoutInflater myInflater;
    private List<Device> myDevices; // Cached copy of devices

    public DeviceListAdapter(Context context) { myInflater = LayoutInflater.from(context); }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = myInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new DeviceViewHolder(itemView);
    }

    /**
     * Since the RecyclerView just reuses the deviceViewHolders (rather than rendering new ones for
     * each item) this method updates the contents when notified of a change.
     * @param position the position of the item in the data set
     */
    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, int position) {
        if (myDevices != null) {
            Device current = myDevices.get(position);
            holder.deviceItemView.setText(current.getName());
            if(selectedPosition == position) {
                // This is the selected item
               holder.deviceItemView.setBackgroundColor(ContextCompat
                        .getColor(holder.deviceItemView.getContext(), R.color.colorSecondaryDark));
            } else {
                // This is an unselected item
                holder.deviceItemView.setBackgroundColor(ContextCompat
                        .getColor(holder.deviceItemView.getContext(), R.color.colorSecondary));
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.deviceItemView.setText("No Devices");
        }
    }

    public void setDevices(List<Device> devices){
        myDevices = devices;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (myDevices != null)
            return myDevices.size();
        else return 0;
    }

    public int getSelectedID() {
        if(selectedPosition >= 0 && selectedPosition < getItemCount()) {
            Device current = myDevices.get(selectedPosition);
            Log.d("DeviceListAdapter", "id is " + current.getId() +", name is " + current.getName());
            return current.getId();
        }
        return -1;
    }
}
