package com.brookmanholmes.drilltracker.presentation.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.brookmanholmes.drilltracker.R;
import com.brookmanholmes.drilltracker.presentation.model.Model;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brookman Holmes on 8/14/2017.
 */

public abstract class BaseRecyclerViewAdapter<T extends Model> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder<T>> {
    private static final int VIEW_FOOTER = -1;
    protected static final int VIEW_DEFAULT = 1;
    protected final LayoutInflater inflater;
    protected List<T> data;
    private OnItemClickListener<T> onItemClickListener;

    protected BaseRecyclerViewAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.data = Collections.emptyList();
    }

    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_FOOTER) {
            return new Footer<>(inflater.inflate(R.layout.row_footer, parent, false));
        } else {
            return getDefaultViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder<T> holder, int position) {
        if (getItemViewType(position) != VIEW_FOOTER) {
            holder.bind(data.get(position), onItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == data.size()) {
            return VIEW_FOOTER;
        } else {
            return VIEW_DEFAULT;
        }
    }

    public void setData(List<T> data) {
        validateData(data);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtilCallback<>(this.data, data));
        this.data = data;
        result.dispatchUpdatesTo(this);
    }

    private void validateData(List<T> data) {
        if (data == null)
            throw new IllegalArgumentException("List data cannot be null");
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    protected abstract ViewHolder<T> getDefaultViewHolder(ViewGroup parent);

    public interface OnItemClickListener<T> {
        void onItemClicked(T item, @IdRes int res);
        void onItemLongClicked(T item);
    }

    protected static class ViewHolder<T> extends RecyclerView.ViewHolder {
        protected OnItemClickListener<T> onItemClickListener;
        T model;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @CallSuper
        public void bind(T model, OnItemClickListener<T> onItemClickListener) {
            this.model = model;
            this.onItemClickListener = onItemClickListener;
        }

        protected void onItemClick(@IdRes int res) {
            if (onItemClickListener != null)
                onItemClickListener.onItemClicked(model, res);
        }

        protected boolean onItemLongClick() {
            if (onItemClickListener != null) {
                onItemClickListener.onItemLongClicked(model);
                return true;
            } else {
                return false;
            }
        }
    }

    private static class Footer<T> extends BaseRecyclerViewAdapter.ViewHolder<T> {
        Footer(View itemView) {
            super(itemView);
        }
    }

    private static class DiffUtilCallback<T extends Model> extends DiffUtil.Callback {
        final List<T> oldData;
        final List<T> newData;

        private DiffUtilCallback(List<T> oldData, List<T> newData) {
            this.oldData = oldData;
            this.newData = newData;
        }

        @Override
        public int getOldListSize() {
            return oldData.size();
        }

        @Override
        public int getNewListSize() {
            return newData.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).getModelId().equals(newData.get(newItemPosition).getModelId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldData.get(oldItemPosition).equals(newData.get(newItemPosition));
        }
    }
}
