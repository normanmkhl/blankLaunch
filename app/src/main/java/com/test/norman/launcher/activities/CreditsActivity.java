 

package com.test.norman.launcher.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.R;
import com.test.norman.launcher.views.ModularRecyclerView;

import java.util.Objects;

public class CreditsActivity extends blankActivity {
    private String[] names, tasks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        names = getResources().getStringArray(R.array.names);
        tasks = getResources().getStringArray(R.array.tasks);

        recyclerView.setAdapter(new CreditsAdapter());
        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(Objects.requireNonNull(getDrawable(R.drawable.ll_divider)));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    class CreditsAdapter extends ModularRecyclerView.ModularAdapter<CreditsAdapter.ViewHolder> {
        final LayoutInflater inflater;

        public CreditsAdapter() {
            this.inflater = getLayoutInflater();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(inflater.inflate(R.layout.credit, parent, false));
        }

        @Override
        public int getItemCount() {
            return names.length;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            holder.name.setText(names[position]);
            holder.task.setText(tasks[position]);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView name, task;

            ViewHolder(View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                task = itemView.findViewById(R.id.task);
            }
        }
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_NONE;
    }
}
