 

package com.test.norman.launcher.databases.apps;

import android.content.ComponentName;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.test.norman.launcher.adapters.AppsRecyclerViewAdapter;
import com.test.norman.launcher.databases.home_screen_pins.HomeScreenPinHelper;
import com.test.norman.launcher.utils.S;
import com.test.norman.launcher.views.HomeScreenAppView;

import java.util.Arrays;
import java.util.Objects;

/**
 * even though its not a representation of App and it represents an activity
 * this name fits the best
 * <p>
 * using this old java getters and setters because Room requires that.
 * see {@link Entity}
 */
@Entity
public class App implements AppsRecyclerViewAdapter.InAppsRecyclerView, HomeScreenPinHelper.HomeScreenPinnable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "flatten_component_name")
    private String flattenComponentName;

    @ColumnInfo(name = "icon")
    private byte[] icon;

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "pinned")
    private boolean pinned;

    @Ignore
    @Override
    public int type() {
        return AppsRecyclerViewAdapter.TYPE_ITEM;
    }

    public String getFlattenComponentName() {
        return flattenComponentName;
    }

    public void setFlattenComponentName(String flattenComponentName) {
        this.flattenComponentName = flattenComponentName;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final App app = (App) o;
        return id == app.id &&
                pinned == app.pinned &&
                Objects.equals(flattenComponentName, app.flattenComponentName) &&
                Arrays.equals(icon, app.icon) &&
                Objects.equals(label, app.label);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, flattenComponentName, label, pinned);
        result = 31 * result + Arrays.hashCode(icon);
        return result;
    }

    @Ignore
    @Override
    public void applyToHomeScreenAppView(HomeScreenAppView homeScreenAppView) {
        homeScreenAppView.setText(getLabel());
        if (S.isValidContextForGlide(homeScreenAppView.iv_icon.getContext()))
            AppsDatabaseHelper.loadPic(this, homeScreenAppView.iv_icon);
        homeScreenAppView.setIntent(ComponentName.unflattenFromString(getFlattenComponentName()));
    }
}