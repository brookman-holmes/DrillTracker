package com.brookmanholmes.drilltracker.presentation.purchasedrills;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brookmanholmes.drilltracker.MyApp;
import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.base.BaseFragment;
import com.brookmanholmes.drilltracker.presentation.drillpackdetail.DrillPackDetailDialog;
import com.brookmanholmes.drilltracker.presentation.drills.ActivityCallback;
import com.brookmanholmes.drilltracker.presentation.drills.FragmentCallback;
import com.brookmanholmes.drilltracker.presentation.model.DrillPackModel;
import com.brookmanholmes.drilltracker.presentation.model.Type;
import com.google.firebase.auth.FirebaseAuth;

import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.IntentStarter;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;
import org.solovyev.android.checkout.UiCheckout;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Brookman Holmes on 8/9/2017.
 */

public class PurchaseDrillsFragment extends BaseFragment<PurchaseDrillsContract> implements
        PurchaseDrillsView,
        ActivityCallback,
        PurchaseDrillsAdapter.OnItemClickListener<DrillPackModel>,
        IntentStarter {

    private static final String TAG = PurchaseDrillsFragment.class.getName();

    @BindView(R.id.scrollView)
    RecyclerView recyclerView;

    private PurchaseDrillsAdapter adapter;
    private UiCheckout checkout;
    private InventoryCallback inventoryCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkout = Checkout.forUi(this, TAG, MyApp.get().getBilling());
        checkout.start();

        adapter = new PurchaseDrillsAdapter(getContext());
        inventoryCallback = new InventoryCallback(adapter);
        presenter = new PurchaseDrillsPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drills_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        presenter.setView(this);
        adapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemViewCacheSize(5);
        recyclerView.setHasFixedSize(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setAdapter(adapter);

        getCallback().addListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.presenter.loadDrillsList();
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        getCallback().removeListener(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        checkout.stop();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkout.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startForResult(@Nonnull IntentSender intentSender, int requestCode, @Nonnull Intent intent) throws IntentSender.SendIntentException {
        this.startIntentSenderForResult(intentSender, requestCode, intent, 0, 0, 0, null);
    }

    private FragmentCallback getCallback() {
        if (getActivity() instanceof FragmentCallback) {
            return ((FragmentCallback) getActivity());
        } else {
            throw new IllegalStateException("Parent activity must implement FragmentCallback");
        }
    }

    @Override
    public void loadInventory(List<String> skus) {
        final Inventory.Request request = Inventory.Request.create();
        request.loadAllPurchases();
        request.loadSkus(ProductTypes.IN_APP, skus);
        checkout.loadInventory(request, inventoryCallback);
    }

    @Override
    public void onItemClicked(DrillPackModel pack, @IdRes int id) {
        if (id == R.id.price) {
            if (!pack.getPurchased()) {
                //presenter.purchaseDrillPack(pack.sku);
                checkout.startPurchaseFlow(ProductTypes.IN_APP, pack.getSku(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), new PurchaseListener(presenter));
            }
        } else if (id == R.id.cv_drill_pack) {
            viewDrillPack(pack);
        }
    }

    @Override
    public void onItemLongClicked(final DrillPackModel item) {

    }

    /*
        PurchaseDrillsAdapter.OnItemClickListener
     */

    @Override
    public void renderDrillPacks(List<DrillPackModel> drillPacks) {
        adapter.setData(drillPacks);
    }

    @Override
    public void viewDrillPack(DrillPackModel drillModel) {
        DialogFragment dialog = DrillPackDetailDialog.newInstance(getString(R.string.drills_included_in, drillModel.getName()), drillModel.getSku());
        dialog.show(requireFragmentManager(), drillModel.getName());
    }

    /*
        PurchaseDrillsView methods
     */

    @Override
    public void showLoading() {
        getCallback().showLoading();
    }

    @Override
    public void hideLoading() {
        getCallback().hideLoading();
    }

    @Override
    public void showRetry() {
        getCallback().showRetry();
    }

    @Override
    public void hideRetry() {
        getCallback().hideRetry();
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context context() {
        return getContext();
    }

    @Override
    public void setFilterSelection(Type type) {

    }

    private static class PurchaseListener implements RequestListener<Purchase> {
        final PurchaseDrillsContract presenter;

        PurchaseListener(PurchaseDrillsContract presenter) {
            this.presenter = presenter;
        }

        @Override
        public void onSuccess(@Nonnull Purchase result) {
            presenter.purchaseDrillPack(result.sku);
        }

        @Override
        public void onError(int response, @Nonnull Exception e) {
        }
    }

    /*
        ActivityCallback methods
     */

    private static class InventoryCallback implements Inventory.Callback {
        private final PurchaseDrillsAdapter adapter;

        InventoryCallback(PurchaseDrillsAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public void onLoaded(@Nonnull Inventory.Products products) {
            final Inventory.Product product = products.get(ProductTypes.IN_APP);
            if (!product.supported)
                return;

            adapter.updatePurchases(product);
        }
    }
}
