package com.brookmanholmes.drilltracker.presentation.drills;

import android.content.Context;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.view.util.ChartUtil;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */

class DrillsListAdapter extends RecyclerView.Adapter<DrillsListAdapter.DrillViewHolder> {
    private static final String TAG = DrillsListAdapter.class.getName();
    private final LayoutInflater inflater;
    private List<DrillModel> drillsCollection;
    private OnItemClickListener onItemClickListener;
    public DrillsListAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.drillsCollection = Collections.emptyList();
    }

    @Override
    public DrillViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.row_drill, parent, false);
        return new DrillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DrillViewHolder holder, int position) {
        final DrillModel drillModel = drillsCollection.get(position);
        holder.bind(drillModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DrillsListAdapter.this.onItemClickListener != null) {
                    DrillsListAdapter.this.onItemClickListener.onDrillItemClicked(drillModel);
                }
            }
        });
        // forward the click on the chart to the whole holder because for some reason it intercepts it otherwise
        holder.chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemView.performClick();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (DrillsListAdapter.this.onItemClickListener != null) {
                    DrillsListAdapter.this.onItemClickListener.onDrillItemLongClicked(drillModel);
                    return true;
                } else {
                    return false;
                }
            };
        });
    }

    @Override
    public int getItemCount() {
        return (this.drillsCollection != null) ? this.drillsCollection.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDrillsCollection(List<DrillModel> drillsCollection) {
        this.validateDrillsCollection(drillsCollection);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtilCallback(this.drillsCollection, drillsCollection));
        this.drillsCollection = drillsCollection;
        result.dispatchUpdatesTo(this);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateDrillsCollection(List<DrillModel> drillModelCollection) {
        if (drillModelCollection == null)
            throw new IllegalArgumentException("The list cannot be null");
    }

    public interface OnItemClickListener {
        void onDrillItemClicked(DrillModel drillModel);

        void onDrillItemLongClicked(DrillModel drillModel);
    }

    static class DrillViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = DrillViewHolder.class.getName();

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.chart)
        LineChartView chart;

        DrillViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            chart.setClickable(false);
            chart.setFocusable(false);
        }

        void bind(DrillModel model) {
            name.setText(model.name);
            ImageHandler.loadImage(image, model.imageUrl);
            ChartUtil.setupLifetimeChart(chart, model, false);
        }
    }

    private static class DiffUtilCallback extends DiffUtil.Callback {
        List<DrillModel> oldCollection, newCollection;

        public DiffUtilCallback(List<DrillModel> oldCollection, List<DrillModel> newCollection) {
            this.oldCollection = oldCollection;
            this.newCollection = newCollection;
        }

        @Override
        public int getOldListSize() {
            return oldCollection.size();
        }

        @Override
        public int getNewListSize() {
            return newCollection.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldCollection.get(oldItemPosition).id.equals(newCollection.get(newItemPosition).id);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldCollection.get(oldItemPosition).equals(newCollection.get(newItemPosition));
        }
    }
}
