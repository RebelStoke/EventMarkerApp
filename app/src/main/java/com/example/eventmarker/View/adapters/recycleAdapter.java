package com.example.eventmarker.View.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmarker.Entities.MarkerPoint;
import com.example.eventmarker.Model.FirebaseViewModel;
import com.example.eventmarker.R;

import java.util.List;

public class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.RecyclerViewHolder> {
    private List<MarkerPoint> mRecycleList;
    private FirebaseViewModel viewModel;
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView1;
        private TextView mTextView2;
        private Button deleteButton;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.txt_desc);
            mTextView2 = itemView.findViewById(R.id.txt_creator);
            deleteButton = itemView.findViewById(R.id.btn_deleteMarker);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public recycleAdapter(Context context, List<MarkerPoint> markerList) {
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(FirebaseViewModel.class);
        mRecycleList = markerList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.marker_new, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final MarkerPoint currentItem = mRecycleList.get(position);
        holder.mTextView1.setText(currentItem.getDesc());
        holder.mTextView2.setText(currentItem.getNameOfMarker());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewModel.deleteMarker(currentItem);
                mRecycleList.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(holder.getAdapterPosition(), mRecycleList.size());
            }
        });
    }
    @Override
    public int getItemCount() {
        return mRecycleList.size();
    }
}
