package com.brookmanholmes.drilltracker.presentation.drillpackdetail;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseRecyclerViewAdapter;
import com.brookmanholmes.drilltracker.presentation.model.DrillModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 8/16/2017.
 */

class DrillsListAdapter extends BaseRecyclerViewAdapter<DrillModel> {
    DrillsListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_DEFAULT;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    protected ViewHolder<DrillModel> getDefaultViewHolder(ViewGroup parent) {
        final View view = inflater.inflate(R.layout.row_drill_pack_drills, parent, false);
        return new DrillModelViewHolder(view);
    }

    static class DrillModelViewHolder extends BaseRecyclerViewAdapter.ViewHolder<DrillModel> {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.description)
        TextView description;
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
            description.setText(model.description);
            Picasso.with(image.getContext())
                    .load(model.imageUrl)
                    .fit()
                    .placeholder(R.drawable.pool_table)
                    .error(R.drawable.pool_table_error)
                    .into(image);
        }
    }
}
