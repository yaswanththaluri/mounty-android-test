package com.maya.mounty.mountyinternship;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class TouchListener extends ItemTouchHelper.Callback
{
    private SwipeListener mSwipeListener;

    TouchListener(SwipeListener mSwipeListener)
    {
        this.mSwipeListener = mSwipeListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;//since we dont want to drag
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mSwipeListener.onSwipe(viewHolder.getPosition());
    }


}