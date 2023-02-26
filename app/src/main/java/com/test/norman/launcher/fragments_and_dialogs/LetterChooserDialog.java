 

package com.test.norman.launcher.fragments_and_dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.R;
import com.test.norman.launcher.views.blankTitleBar;
import com.test.norman.launcher.views.ModularRecyclerView;

public class LetterChooserDialog extends Dialog {
    private static final String TAG = LetterChooserDialog.class.getSimpleName();
    private static final int AMOUNT_PER_ROW = 4;
    private final OnChooseLetterListener onChooseLetterListener;
    private final SparseIntArray lettersToValues;

    public LetterChooserDialog(final Context context, SparseIntArray lettersToValues, OnChooseLetterListener onChooseLetterListener) {
        super(context);
        this.lettersToValues = lettersToValues;
        this.onChooseLetterListener = onChooseLetterListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.full_screen_recycler_view_dialog);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ((blankTitleBar) findViewById(R.id.blank_title_bar)).getBt_back().setOnClickListener(v -> {
            dismiss();
        });
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        int numberOfAppsInARow;
        try {
            WindowManager windowManager = getWindow().getWindowManager();
            final Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            numberOfAppsInARow = ((point.x / point.y) != 0) ? AMOUNT_PER_ROW * 2 : AMOUNT_PER_ROW;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            numberOfAppsInARow = 3;
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfAppsInARow));
        recyclerView.setAdapter(new LetterChooserAdapter());

    }

    @FunctionalInterface
    public interface OnChooseLetterListener {
        void onChooseLetter(int position);
    }

    public class LetterChooserAdapter extends ModularRecyclerView.ModularAdapter<LetterChooserAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.letter_chooser_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            final char c = (char) lettersToValues.keyAt(position);
            holder.textView.setText(String.valueOf(c));
            holder.textView.setOnClickListener((v) -> {
                onChooseLetterListener.onChooseLetter(lettersToValues.valueAt(position));
                cancel();
            });
        }

        @Override
        public int getItemCount() {
            return lettersToValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.text);
            }
        }
    }
}