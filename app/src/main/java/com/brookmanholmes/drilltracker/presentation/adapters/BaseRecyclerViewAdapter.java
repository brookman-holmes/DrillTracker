package com.brookmanholmes.drilltracker.presentation.adapters;


import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerViewAdapter<S, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {
    private final List<S> data = new ArrayList<>();
    private final Equals<S> equals = new Equals<S>() {
        @Override
        public boolean areItemsTheSame(S oldItem, S newItem) {
            return BaseRecyclerViewAdapter.this.areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(S oldItem, S newItem) {
            return BaseRecyclerViewAdapter.this.areContentTheSame(oldItem, newItem);
        }
    };

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<S> data) {
        final DataDiffCallback<S> diffCallback = new DataDiffCallback<>(this.data, data, equals);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.data.clear();
        this.data.addAll(data);
        diffResult.dispatchUpdatesTo(this);
    }

    protected abstract boolean areItemsTheSame(S oldItem, S newItem);

    protected abstract boolean areContentTheSame(S oldItem, S newItem);

    private interface Equals<S> {
        boolean areItemsTheSame(S oldItem, S newItem);

        boolean areContentsTheSame(S oldItem, S newItem);
    }

    private static class DataDiffCallback<S> extends DiffUtil.Callback {
        private final List<S> oldList;
        private final List<S> newList;
        private final Equals<S> equals;

        DataDiffCallback(List<S> oldList, List<S> newList, Equals<S> equals) {
            this.equals = equals;
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return equals.areItemsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return equals.areContentsTheSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
        }
    }
}
