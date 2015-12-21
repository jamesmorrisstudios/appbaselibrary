package com.jamesmorrisstudios.appbaselibrary.touchHelper;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * An implementation of {@link ItemTouchHelper.Callback} that enables basic drag & drop and
 * swipe-to-dismiss. Drag events are automatically started by an item long-press.<br/>
 * </br/>
 * Expects the <code>RecyclerView.Adapter</code> to react to {@link
 * ItemTouchHelperAdapter} callbacks and the <code>RecyclerView.ViewHolder</code> to implement
 * {@link ItemTouchHelperViewHolder}.
 *
 * @author Paul Burke (ipaulpro)
 */
public final class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(@NonNull final ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public final boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public final boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public final int getMovementFlags(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
        if (mAdapter.allowMove()) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            return makeMovementFlags(0, 0);
        }
    }

    @Override
    public final boolean onMove(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder source, @NonNull final RecyclerView.ViewHolder target) {
        if (mAdapter.allowMove()) {
            mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public final void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {
        if (mAdapter.allowMove()) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public final void onSelectedChanged(@NonNull final RecyclerView.ViewHolder viewHolder, final int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemSelected();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public final void clearView(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
        itemViewHolder.onItemClear();
    }

}
