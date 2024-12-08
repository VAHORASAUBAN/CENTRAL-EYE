package com.example.integration.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.integration.R;

import java.util.List;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {

    private List<RequestModel> requestList;
    private FragmentManager fragmentManager;

    public RequestListAdapter(FragmentManager fragmentManager, List<RequestModel> requestList) {
        this.fragmentManager = fragmentManager;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestModel request = requestList.get(position);
        holder.requestId.setText(request.getRequestId());
        holder.userName.setText(request.getUserName());

        holder.itemView.setOnClickListener(v -> {
            // Navigate to RequestDetailsFragment
            Request_details detailsFragment = Request_details.newInstance(request.getRequestId());
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView requestId, userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            requestId = itemView.findViewById(R.id.requestId);
            userName = itemView.findViewById(R.id.userName);
        }
    }
}
