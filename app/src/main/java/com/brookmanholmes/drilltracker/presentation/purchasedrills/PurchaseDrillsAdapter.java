package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseRecyclerViewAdapter;
import com.brookmanholmes.drilltracker.presentation.model.DrillPackModel;
import com.brookmanholmes.drilltracker.presentation.view.util.ImageHandler;
import com.google.firebase.database.FirebaseDatabase;

import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.Sku;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Brookman Holmes on 8/9/2017.
 */

class PurchaseDrillsAdapter extends BaseRecyclerViewAdapter<DrillPackModel> {
    private static final String TAG = PurchaseDrillsAdapter.class.getName();

    PurchaseDrillsAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewAdapter.ViewHolder<DrillPackModel> getDefaultViewHolder(ViewGroup parent, int viewType) {
        final View view = inflater.inflate(R.layout.row_drill_pack, parent, false);
        return new DrillPackViewHolder(view);
    }

    void updatePurchases(Inventory.Product product) {
        for (DrillPackModel model : data) {
            Sku sku = product.getSku(model.sku);
            if (sku != null) {
                model.purchased = product.isPurchased(sku);
                model.price = sku.price;
            } else {
                FirebaseDatabase.getInstance().getReference().child("log").child(model.sku).setValue("sku for " + model.sku + " is null");
            }
        }

        notifyItemRangeChanged(0, data.size());
    }

    static class DrillPackViewHolder extends BaseRecyclerViewAdapter.ViewHolder<DrillPackModel> {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.price)
        Button price;
        @BindView(R.id.image)
        ImageView image;

        DrillPackViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(DrillPackModel model, OnItemClickListener<DrillPackModel> onItemClickListener) {
            super.bind(model, onItemClickListener);
            this.onItemClickListener = onItemClickListener;
            name.setText(model.name);
            description.setText(model.description);
            if (model.purchased) {
                price.setText(R.string.purchased);
                price.setEnabled(false);
            } else {
                price.setText(String.format("Buy for %s", model.price));
                price.setEnabled(true);
            }
            ImageHandler.loadImage(image, model.url);
        }

        @OnClick(R.id.price)
        void onPriceClicked(View view) {
            onItemClick(view.getId());
        }

        @OnClick(R.id.cv_drill_pack)
        void onDrillPackClicked(View view) {
            onItemClick(view.getId());
        }
    }
}
