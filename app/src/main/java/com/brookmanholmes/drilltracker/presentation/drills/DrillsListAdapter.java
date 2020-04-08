package com.brookmanholmes.drilltracker.presentation.drills;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseRecyclerViewAdapter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 7/7/2017.
 */
class DrillsListAdapter extends BaseRecyclerViewAdapter<DrillModel> {
    private static final String TAG = DrillsListAdapter.class.getName();

    DrillsListAdapter(Context context) {
        super(context);
    }

    @Override
    protected ViewHolder<DrillModel> getDefaultViewHolder(ViewGroup parent) {
        final View view = inflater.inflate(R.layout.row_drill, parent, false);
        return new DrillModelViewHolder(view);
    }

    static class DrillModelViewHolder extends BaseRecyclerViewAdapter.ViewHolder<DrillModel>
            implements View.OnClickListener, View.OnLongClickListener {
        private static final String TAG = DrillModelViewHolder.class.getName();

        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.image)
        ImageView image;

        DrillModelViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(DrillModel model, OnItemClickListener<DrillModel> onItemClickListener) {
            super.bind(model, onItemClickListener);
            name.setText(model.name);
            ImageHandler.loadImage(image, model.imageUrl);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(view.getId());
        }

        @Override
        public boolean onLongClick(View view) {
            return onItemLongClick();
        }
    }
}
