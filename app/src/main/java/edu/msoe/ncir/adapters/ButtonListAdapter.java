package edu.msoe.ncir.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.msoe.ncir.R;

import edu.msoe.ncir.models.Button;
import edu.msoe.ncir.models.Signal;
import edu.msoe.ncir.udp.UDPClient;

public class ButtonListAdapter extends RecyclerView.Adapter<ButtonListAdapter.ButtonViewHolder> {

    private int selectedPosition = -1;

    class ButtonViewHolder extends RecyclerView.ViewHolder {
        private final TextView buttonItemView;

        private ButtonViewHolder(View itemView) {
            super(itemView);
            buttonItemView = itemView.findViewById(R.id.textView);

            // Notices when an item is selected
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

                    int index = getSelectedSignalIndex();
                    String name = getSelectedName();
                    if(index > -1 && name.length() > 0) {
                        boolean sent = sendButton(name, index);
                        if(sent) {
                            Log.d("Buttons", "Button was sent and recieved.");
                        }
                        Log.d("Buttons", name +" "+index + " Button Pressed");
                    } else {
                        Log.d("Buttons", name +" "+index + " Button Press Not Registered.");
                    }
                }
            });
        }
    }

    private final LayoutInflater myInflater;
    private List<Button> myButtons; // Cached copy of buttons
    private List<Signal> mySignals; // Cached copy of signals

    public ButtonListAdapter(Context context) { myInflater = LayoutInflater.from(context); }

    /**
     * This function sends a command to send a button signal and
     * determines if it was successful.
     * @param name This is the name of the button
     * @param index This is the hardware index of the button
     * @return This is true if the button was sent
     */
    private boolean sendButton(String name, int index) {
        boolean retVal = false;
        UDPClient.getInstance().send(("send_button," + name + "," + index)); // Send the send command
        String response = UDPClient.getInstance().receive(); // Wait for a response (blocking)
        // Determine if the button was sent
        if(response.equalsIgnoreCase("\r\nbutton_sent," + name + "," + index + "\r\n")) {
            retVal = true;
        }
        return retVal;
    }

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

    public void setButtons(List<Button> buttons) {
        myButtons = buttons;
        notifyDataSetChanged();
    }

    public void setSignals(List<Signal> signals) {
        mySignals = signals;
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

    public String getSelectedName() {
        if(selectedPosition >= 0 && selectedPosition < getItemCount()) {
            Button current = myButtons.get(selectedPosition);
            return current.getName();
        }
        return "";
    }

    public int getSelectedSignalIndex() {
        if(selectedPosition >= 0 && selectedPosition < getItemCount()) {
            Button current = myButtons.get(selectedPosition);
            int signalID = current.getSignalID();
            Signal signal = null;
            for(int i = 0; i < mySignals.size(); i++) {
                if(mySignals.get(i).getId() == signalID) {
                    signal = mySignals.get(i);
                    i = mySignals.size();
                }
            }
            if(null == signal) {
                Log.d("ButtonListAdapter", "Error finding signal.");
            } else {
                return signal.getDeviceIndex();
            }
        }
        return -1;
    }
}
