 

package com.test.norman.launcher.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.YoutubeActivity;
import com.test.norman.launcher.views.ModularRecyclerView;

public class HelpRecyclerViewAdapter extends ModularRecyclerView.ModularAdapter<HelpRecyclerViewAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    @StringRes
    private final int[] texts;
    @DrawableRes
    private final int[] pics;

    public HelpRecyclerViewAdapter(@NonNull final Context context, @StringRes final int[] texts, @DrawableRes final int[] pics) {
        inflater = LayoutInflater.from(context);
        this.pics = pics;
        this.texts = texts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.help_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.update(texts[position], pics[position], position);//may change position in future
    }

    @Override
    public int getItemCount() {
        return texts.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView pic;
        final TextView text;
        int index;

        public ViewHolder(final View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(v -> {
                final Context context = v.getContext();
                context.startActivity(new Intent(context, YoutubeActivity.class).putExtra(YoutubeActivity.EXTRA_ID, index));
            });
        }

        public void update(@StringRes final int textRes, @DrawableRes final int picRes, int index) {
            pic.setImageResource(picRes);
            text.setText(textRes);
            this.index = index;
        }
    }
}

