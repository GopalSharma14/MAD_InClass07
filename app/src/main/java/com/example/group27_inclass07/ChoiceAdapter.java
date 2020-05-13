package com.example.group27_inclass07;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChoiceAdapter extends RecyclerView.Adapter<ChoiceAdapter.ViewHolder> {

    private ArrayList<String> mData = new ArrayList<>();
    private iChoiceListener choiceListener;


    public ChoiceAdapter(ArrayList<String> mData, iChoiceListener choiceListener) {
        this.mData = mData;
        this.choiceListener = choiceListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choice_item, parent, false);
        return new ViewHolder(view, choiceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String choice = mData.get(position);

        holder.choice_tv.setText(choice);
        holder.choice = choice;
    }


    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView choice_tv;
        String choice;
        iChoiceListener choiceListener;

        public ViewHolder(@NonNull View itemView, iChoiceListener choiceListener) {
            super(itemView);
            choice_tv = itemView.findViewById(R.id.choice_tv);
            this.choiceListener = choiceListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            choiceListener.onChoiceClick(getAdapterPosition());
        }
    }
}
