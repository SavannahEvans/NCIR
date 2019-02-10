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
import edu.msoe.ncir.models.Button;

public class ButtonListAdapter extends RecyclerView.Adapter<ButtonListAdapter.ButtonViewHolder> {

    private int selectedPosition = -1;

    class ButtonViewHolder extends RecyclerView.ViewHolder {
        private final TextView buttonItemView;

        private ButtonViewHolder(View itemView) {
            super(itemView);
            buttonItemView = itemView.findViewById(R.id.textView);

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
    private List<Button> myButtons; // Cached copy of buttons

    public ButtonListAdapter(Context context) { myInflater = LayoutInflater.from(context); }

    @Override
    public ButtonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = myInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ButtonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ButtonViewHolder holder, int position) {
        if (myButtons != null) {
            Button current = myButtons.get(position);
            holder.buttonItemView.setText(current.getName());
        } else {
            // Covers the case of data not being ready yet.
            holder.buttonItemView.setText("No Buttons");
        }
    }

    public void setButtons(List<Button> buttons){
        myButtons = buttons;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (myButtons != null)
            return myButtons.size();
        else return 0;
    }

    public int getSelectedID() {
        if(selectedPosition >= 0 && selectedPosition < getItemCount()) {
            Button current = myButtons.get(selectedPosition);
            Log.d("ButtonListAdapter", "id is " + current.getId() +", name is " + current.getName());
            return current.getId();
        }
        return -1;
    }
}
