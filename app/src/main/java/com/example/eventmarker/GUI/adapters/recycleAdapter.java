package com.example.eventmarker.GUI.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventmarker.BE.MarkerPoint;
import com.example.eventmarker.BLL.BLLManager;
import com.example.eventmarker.R;

import java.util.List;

public class recycleAdapter extends RecyclerView.Adapter<recycleAdapter.RecyclerViewHolder> {
    private List<MarkerPoint> mRecycleList;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView1;
        public TextView mTextView2;
        public Button deleteButton;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.txt_desc);
            mTextView2 = itemView.findViewById(R.id.txt_creator);
            deleteButton = itemView.findViewById(R.id.btn_deleteMarker);
        }
    }

    public recycleAdapter(List<MarkerPoint> markerList) {
        mRecycleList = markerList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.marker_new, parent, false);
        RecyclerViewHolder evh = new RecyclerViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        final MarkerPoint currentItem = mRecycleList.get(position);
        holder.mTextView1.setText(currentItem.desc);
        holder.mTextView2.setText(currentItem.markerID);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BLLManager.getInstance().deleteMarker(currentItem);
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
