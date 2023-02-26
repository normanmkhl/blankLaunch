 

package com.test.norman.launcher.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.R;

public class DropDownRecyclerViewAdapter extends RecyclerView.Adapter<DropDownRecyclerViewAdapter.ViewHolder> {
    private final LayoutInflater layoutInflater;
    private final PopupWindow popupWindow;
    private final DropDownListener dropDownListener;

    public DropDownRecyclerViewAdapter(final Context context, PopupWindow popupWindow, DropDownListener dropDownListener) {
        this.layoutInflater = LayoutInflater.from(context);
        this.popupWindow = popupWindow;
        this.dropDownListener = dropDownListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.settings_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        dropDownListener.onUpdate(holder, position, popupWindow);
    }

    @Override
    public int getItemCount() {
        return dropDownListener.size();
    }

    public interface DropDownListener {
        void onUpdate(DropDownRecyclerViewAdapter.ViewHolder viewHolder, final int position, PopupWindow popupWindow);

        int size();

        default void onDismiss() {
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView pic;
        public final TextView text;

        public ViewHolder(final View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.setting_icon);
            text = itemView.findViewById(R.id.tv_setting_name);
        }
    }
}