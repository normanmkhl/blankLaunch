 

package com.test.norman.launcher.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.norman.launcher.R;
import com.test.norman.launcher.adapters.AppsRecyclerViewAdapter;
import com.test.norman.launcher.databases.apps.App;
import com.test.norman.launcher.databases.apps.AppsDatabase;
import com.test.norman.launcher.databases.apps.AppsDatabaseHelper;
import com.test.norman.launcher.utils.BDB;
import com.test.norman.launcher.utils.BDialog;
import com.test.norman.launcher.utils.DropDownRecyclerViewAdapter;
import com.test.norman.launcher.utils.S;

import java.util.List;
import java.util.Objects;

import static com.test.norman.launcher.adapters.AppsRecyclerViewAdapter.TYPE_HEADER;
import static com.test.norman.launcher.databases.apps.AppsDatabaseHelper.blankComponentNameBeginning;

public class AppsActivity extends com.test.norman.launcher.activities.blankActivity {
    private static final String TAG = AppsActivity.class.getSimpleName();
    public static final int REQUEST_SELECT_CUSTOM_APP = 88;
    public static final String CHOOSE_MODE = "CHOOSE_MODE";
    public static final int UNINSTALL_REQUEST_CODE = 52;
    private static final String SELECTED_APP_INDEX = "SELECTED_APP_INDEX";

    private AppsDatabase appsDatabase;
    private int numberOfAppsInARow;

    private RecyclerView recyclerView;

    private AppsRecyclerViewAdapter appsRecyclerViewAdapter;
    private String chooseKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);

        AppsDatabaseHelper.updateDB(this);

        appsDatabase = AppsDatabase.getInstance(AppsActivity.this);
        final List<App> appList = appsDatabase.appsDatabaseDao().getAllOrderedByABC();
        recyclerView = findViewById(R.id.rc_apps);
        chooseKey = getIntent().getStringExtra(CHOOSE_MODE);
        appsRecyclerViewAdapter = new AppsRecyclerViewAdapter(appList, this, chooseKey != null ? this::appChosen : this::showDropDown, recyclerView);

        final WindowManager windowManager = getWindowManager();
        final Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        final boolean screenOrientation = (point.x / point.y) != 0;
        numberOfAppsInARow = screenOrientation ? 6 : 3;
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfAppsInARow);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (appsRecyclerViewAdapter.getItemViewType(position)) {
                    case TYPE_HEADER:
                        return numberOfAppsInARow;
                    case AppsRecyclerViewAdapter.TYPE_ITEM:
                        return 1;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(appsRecyclerViewAdapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_APP_INDEX, ((AppsRecyclerViewAdapter) recyclerView.getAdapter()).index);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int index = savedInstanceState.getInt(SELECTED_APP_INDEX);
        final AppsRecyclerViewAdapter adapter = ((AppsRecyclerViewAdapter) recyclerView.getAdapter());
        if (index < adapter.dataList.size() && index > 0 && adapter.dataList.get(index).type() != TYPE_HEADER) {
            adapter.index = index;
            recyclerView.getLayoutManager().scrollToPosition(index);
            recyclerView.post(() -> showDropDown(index));
        }
    }

    private void uninstallApp(App app) {
        final String app_pkg_name = ComponentName.unflattenFromString(app.getFlattenComponentName()).getPackageName();
        startActivityForResult(new Intent(Intent.ACTION_UNINSTALL_PACKAGE)
                .setData(Uri.parse("package:" + app_pkg_name))
                .putExtra(Intent.EXTRA_RETURN_RESULT, true), UNINSTALL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                recreate();
            }
        }
    }

    private void showDropDown(final int index) {
        appsRecyclerViewAdapter.index = index;
        final App app = (App) appsRecyclerViewAdapter.dataList.get(index);
        final View view = Objects.requireNonNull(recyclerView.getLayoutManager()).findViewByPosition(index);
        if (view == null)
            return;
        S.showDropDownPopup(this, recyclerView.getWidth(), new DropDownRecyclerViewAdapter.DropDownListener() {
            @Override
            public void onUpdate(DropDownRecyclerViewAdapter.ViewHolder viewHolder, int position, PopupWindow popupWindow) {
                switch (position) {
                    case 0:
                        if (S.isValidContextForGlide(viewHolder.pic.getContext()))
                            AppsDatabaseHelper.loadPic(app, viewHolder.pic);
                        viewHolder.text.setText(R.string.open);
                        viewHolder.itemView.setOnClickListener(v1 -> {
                            final ComponentName componentName = ComponentName.unflattenFromString(app.getFlattenComponentName());
                            S.startComponentName(AppsActivity.this, componentName);
                            popupWindow.dismiss();
                        });
                        break;
                    case 1:
                        viewHolder.pic.setImageResource(app.isPinned() ? R.drawable.remove_on_button : R.drawable.add_on_button);
                        viewHolder.text.setText(app.isPinned() ? R.string.remove_shortcut : R.string.add_shortcut);
                        viewHolder.itemView.setOnClickListener(v1 -> {
                            if (app.isPinned()) {
                                appsDatabase.appsDatabaseDao().update(app.getId(), false);
                                app.setPinned(false);
                                appsRecyclerViewAdapter.notifyItemChanged(appsRecyclerViewAdapter.index);
                            } else {
                                appsDatabase.appsDatabaseDao().update(app.getId(), true);
                                app.setPinned(true);
                                appsRecyclerViewAdapter.notifyItemChanged(appsRecyclerViewAdapter.index);
                            }
                            popupWindow.dismiss();
                            showDropDown(index);
                        });
                        break;
                    case 2:
                        viewHolder.pic.setImageResource(R.drawable.delete_on_button);
                        viewHolder.text.setText(R.string.uninstall);
                        viewHolder.itemView.setOnClickListener(v1 -> {
                            BDB.from(AppsActivity.this)
                                    .setTitle(String.format("%s %s", getText(R.string.uninstall), app.getLabel()))
                                    .setSubText(String.format(getString(R.string.uninstall_subtext), app.getLabel(), app.getLabel()))
                                    .addFlag(BDialog.FLAG_YES | BDialog.FLAG_CANCEL)
                                    .setPositiveButtonListener(params -> {
                                        uninstallApp(app);
                                        return true;
                                    })
                                    .show();
                            popupWindow.dismiss();
                        });
                        break;
                }
            }

            @Override
            public int size() {
                return app.getFlattenComponentName().startsWith(blankComponentNameBeginning) ? 2 : 3;
            }

            @Override
            public void onDismiss() {
                if (appsRecyclerViewAdapter.lastView != null)
                    appsRecyclerViewAdapter.lastView.setClicked(false);
                appsRecyclerViewAdapter.lastView = null;
                appsRecyclerViewAdapter.index = -1;

            }
        }, view);

        if (index + numberOfAppsInARow >= appsRecyclerViewAdapter.dataList.size())
            recyclerView.scrollToPosition(index);

    }

    private void appChosen(int index) {
        if (index != -1) {
            final App app = (App) appsRecyclerViewAdapter.dataList.get(index);
            setResult(RESULT_OK, new Intent().setComponent(ComponentName.unflattenFromString(app.getFlattenComponentName())).putExtra(CHOOSE_MODE, chooseKey));
            finish();
        }
    }

    public interface ChangeAppListener {
        void changeApp(int index);
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_NONE;
    }
}