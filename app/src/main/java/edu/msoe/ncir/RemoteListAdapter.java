package edu.msoe.ncir;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Does something
 */
public class RemoteListAdapter extends RecyclerView.Adapter<RemoteListAdapter.RemoteViewHolder> {

    class RemoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView remoteItemView;

        private RemoteViewHolder(View itemView) {
            super(itemView);
            remoteItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater myInflater;
    private List<Remote> myRemotes; // Cached copy of remotes

    RemoteListAdapter(Context context) { myInflater = LayoutInflater.from(context); }

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
        } else {
            // Covers the case of data not being ready yet.
            holder.remoteItemView.setText("No Remotes");
        }
    }

    void setRemotes(List<Remote> remotes){
        myRemotes = remotes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (myRemotes != null)
            return myRemotes.size();
        else return 0;
    }
}
