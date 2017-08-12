package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.DrillPackModel;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Brookman Holmes on 8/9/2017.
 */

class PurchaseDrillsAdapter extends RecyclerView.Adapter<PurchaseDrillsAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private List<DrillPackModel> models;

    PurchaseDrillsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        models = Collections.emptyList();
    }

    @Override
    public PurchaseDrillsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.row_drill_pack, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PurchaseDrillsAdapter.ViewHolder holder, final int position) {
        holder.bind(models.get(position));
        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onDrillPackSelected(models.get(holder.getAdapterPosition()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void setData(List<DrillPackModel> data) {
        models = data;
        notifyDataSetChanged();
    }

    void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    interface OnItemClickListener {
        void onDrillPackSelected(DrillPackModel pack);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.price)
        Button price;
        @BindView(R.id.image)
        ImageView image;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(DrillPackModel model) {
            name.setText(model.name);
            description.setText(model.description);
            price.setText(String.format("Buy for $1%s", model.price));
            ImageHandler.loadImage(image, model.url);

        }
    }
}
