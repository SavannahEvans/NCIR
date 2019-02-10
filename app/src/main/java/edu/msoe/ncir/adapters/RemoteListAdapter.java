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
import edu.msoe.ncir.models.Remote;

public class RemoteListAdapter extends RecyclerView.Adapter<RemoteListAdapter.RemoteViewHolder> {

    private int selectedPosition = -1;

    class RemoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView remoteItemView;

        private RemoteViewHolder(View itemView) {
            super(itemView);
            remoteItemView = itemView.findViewById(R.id.textView);

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
    private List<Remote> myRemotes; // Cached copy of remotes

    public RemoteListAdapter(Context context) { myInflater = LayoutInflater.from(context); }

    @Override
    public RemoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = myInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new RemoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RemoteViewHolder holder, int position) {
        if (myRemotes != null) {
            Remote current = myRemotes.get(position);
            holder.remoteItemView.setText(current.getName());
            if(selectedPosition == position) {
                // This is the selected item
                holder.remoteItemView.setBackgroundColor(ContextCompat
                        .getColor(holder.remoteItemView.getContext(), R.color.colorSecondaryDark));
            } else {
                // This is an unselected item
                holder.remoteItemView.setBackgroundColor(ContextCompat
                        .getColor(holder.remoteItemView.getContext(), R.color.colorSecondary));
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.remoteItemView.setText("No Remotes");
        }
    }

    public void setRemotes(List<Remote> remotes){
        myRemotes = remotes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (myRemotes != null)
            return myRemotes.size();
        else return 0;
    }

    public int getSelectedID() {
        if(selectedPosition >= 0 && selectedPosition < getItemCount()) {
            Remote current = myRemotes.get(selectedPosition);
            Log.d("RemoteListAdapter", "id is " + current.getId() +", name is " + current.getName());
            return current.getId();
        }
        return -1;
    }
}
