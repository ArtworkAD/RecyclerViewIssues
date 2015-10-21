package de.azzoft.recyclerviewissues;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewFragment extends Fragment {

    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.recycler_view_fragment, container, false);

        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new RecyclerViewAdapter());

        for (int i = 0; i < 10; i++) {
            ((RecyclerViewAdapter) recyclerView.getAdapter()).items.add("Item " + i);
        }

        recyclerView.getAdapter().notifyDataSetChanged();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = ItemTouchHelper.START;
                return makeMovementFlags(0, swipeFlags);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ((RecyclerViewAdapter) recyclerView.getAdapter()).items.remove(viewHolder.getAdapterPosition());
                recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                getDefaultUIUtil().clearView(((RecyclerViewAdapter.BaseViewHolder) viewHolder).getRemovable());
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (viewHolder != null) {
                    getDefaultUIUtil().onSelected(((RecyclerViewAdapter.BaseViewHolder) viewHolder).getRemovable());
                }
            }

            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDraw(c, recyclerView, ((RecyclerViewAdapter.BaseViewHolder) viewHolder).getRemovable(), dX, dY, actionState, isCurrentlyActive);
            }

            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                getDefaultUIUtil().onDrawOver(c, recyclerView, ((RecyclerViewAdapter.BaseViewHolder) viewHolder).getRemovable(), dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return root;
    }

    /**
     * Adapter.
     */
    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder> {

        public ArrayList<String> items = new ArrayList<>();

        @Override
        public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
            ViewGroup itemView = (ViewGroup) mInflater.inflate(R.layout.item_view, parent, false);
            return new BaseViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(BaseViewHolder holder, int position) {
            holder.titleView.setText(items.get(position));
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public static class BaseViewHolder extends RecyclerView.ViewHolder {

            TextView titleView;

            View removable;

            public BaseViewHolder(View itemView) {
                super(itemView);

                titleView = (TextView) itemView.findViewById(R.id.titleView);
                removable = itemView.findViewById(R.id.removable);
            }

            public View getRemovable() {
                return removable;
            }
        }
    }
}
