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

import edu.msoe.ncir.R;
import edu.msoe.ncir.models.Signal;

public class SignalListAdapter extends RecyclerView.Adapter<SignalListAdapter.SignalViewHolder> {

    private int selectedPosition = -1;

    class SignalViewHolder extends RecyclerView.ViewHolder {
        private final TextView signalItemView;

        private SignalViewHolder(View itemView) {
            super(itemView);
            signalItemView = itemView.findViewById(R.id.textView);

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
    private List<Signal> mySignals; // Cached copy of Signals

    public SignalListAdapter(Context context) { myInflater = LayoutInflater.from(context); }

    @Override
    public SignalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = myInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new SignalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SignalViewHolder holder, int position) {
        if (mySignals != null) {
            Signal current = mySignals.get(position);
            holder.signalItemView.setText(current.getName());
            if(selectedPosition == position) {
                // This is the selected item
                holder.signalItemView.setBackgroundColor(ContextCompat
                        .getColor(holder.signalItemView.getContext(), R.color.colorSecondaryDark));
            } else {
                // This is an unselected item
                holder.signalItemView.setBackgroundColor(ContextCompat
                        .getColor(holder.signalItemView.getContext(), R.color.colorSecondary));
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.signalItemView.setText("No Signals");
        }
    }

    public void setSignals(List<Signal> Signals){
        mySignals = Signals;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mySignals != null)
            return mySignals.size();
        else return 0;
    }

    public int getSelectedID() {
        if(selectedPosition >= 0 && selectedPosition < getItemCount()) {
            Signal current = mySignals.get(selectedPosition);
            Log.d("SignalListAdapter", "id is " + current.getId() +", name is " + current.getName());
            return current.getId();
        }
        return -1;
    }

    public String getSelectedName() {
        if(selectedPosition >= 0 && selectedPosition < getItemCount()) {
            Signal current = mySignals.get(selectedPosition);
            Log.d("SignalListAdapter", "id is " + current.getId() +", name is " + current.getName());
            return current.getName();
        }
        return "";
    }
}
