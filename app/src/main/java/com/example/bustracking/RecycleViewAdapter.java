package com.example.bustracking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter <RecycleViewAdapter.ViewHolder> {
    private ArrayList<Model> modelArrayList;
    private Context context;
    private OnBusStopClickListener onBusStopClickListener;

    // Define the interface
    public interface OnBusStopClickListener {
        void onBusStopClick(Model model);
    }

    public RecycleViewAdapter(ArrayList<Model> modelArrayList, Context context, OnBusStopClickListener listener) {
        this.modelArrayList = modelArrayList;
        this.context = context;
        this.onBusStopClickListener = listener;

    }

    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        Model model = modelArrayList.get(position);

        holder.name.setText(model.getName());
        holder.number.setText(model.getNumber());
        holder.profileImage.setImageResource(R.drawable.ic_bus);

        // Set click listener for navigating to MapActivity
        holder.itemView.setOnClickListener(v -> {
            if (onBusStopClickListener != null) {
                onBusStopClickListener.onBusStopClick(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, number;
        ImageView profileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);

        }
    }
}

