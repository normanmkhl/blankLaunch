 

package com.test.norman.launcher.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.test.norman.launcher.R;
import com.test.norman.launcher.utils.S;
import com.test.norman.launcher.views.ModularRecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class IntentAdapter extends ModularRecyclerView.ModularAdapter<IntentAdapter.ViewHolder> {
    private final LayoutInflater layoutInflater;
    private final List<ResolveInfo> resolveInfoList;
    private final PackageManager packageManager;
    private final ResolveInfoConsumer resolveInfoConsumer;
    private final Context context;

    public IntentAdapter(final Context context, final List<ResolveInfo> resolveInfoList, final ResolveInfoConsumer resolveInfoConsumer) {
        this.layoutInflater = LayoutInflater.from(context);
        this.packageManager = context.getPackageManager();
        this.resolveInfoList = resolveInfoList;
        this.resolveInfoConsumer = resolveInfoConsumer;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.settings_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return resolveInfoList.size();
    }

    public interface ResolveInfoConsumer {
        void consume(final ResolveInfo resolveInfo, final Context context);
    }

    class ViewHolder extends ModularRecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView settings_icon;
        final TextView tv_settings_name;

        public ViewHolder(final View itemView) {
            super(itemView);
            settings_icon = itemView.findViewById(R.id.setting_icon);
            tv_settings_name = itemView.findViewById(R.id.tv_setting_name);
            itemView.setOnClickListener(this);
        }

        public void update(final int position) {
            final ResolveInfo resolveInfo = resolveInfoList.get(position);
            if (S.isValidContextForGlide(settings_icon.getContext()))
                Glide.with(settings_icon)
                        .load(resolveInfo.loadIcon(packageManager))
                        .into(settings_icon);
            tv_settings_name.setText(resolveInfo.loadLabel(packageManager));
        }

        @Override
        public void onClick(final View v) {
            resolveInfoConsumer.consume(resolveInfoList.get(getAdapterPosition()), context);
        }
    }
}
