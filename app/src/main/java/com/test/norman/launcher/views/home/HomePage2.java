 

package com.test.norman.launcher.views.home;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.HomeScreenActivity;
import com.test.norman.launcher.activities.SettingsActivity;
import com.test.norman.launcher.utils.blankToast;
import com.test.norman.launcher.utils.DropDownRecyclerViewAdapter;
import com.test.norman.launcher.utils.S;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.os.Build.VERSION_CODES;

public class HomePage2 extends HomeView {
    public static final String TAG = HomePage2.class.getSimpleName();
    private View view;
    private ImageView iv_internet, iv_maps;
    private TextView tv_internet, tv_maps;
    private View bt_settings, bt_internet, bt_maps, bt_help;
    private PackageManager packageManager;

    public HomePage2(@NonNull HomeScreenActivity homeScreen) {
        super(homeScreen, homeScreen);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_home_page2, container, false);
        packageManager = homeScreen.getPackageManager();
        attachXml();
        genOnLongClickListeners();
        return view;
    }

    private void attachXml() {
        bt_settings = view.findViewById(R.id.bt_settings);
        bt_internet = view.findViewById(R.id.bt_apps);
        bt_maps = view.findViewById(R.id.bt_maps);
        iv_internet = view.findViewById(R.id.iv_internet);
        iv_maps = view.findViewById(R.id.iv_maps);
        tv_internet = view.findViewById(R.id.tv_internet);
        tv_maps = view.findViewById(R.id.tv_maps);
        bt_help = view.findViewById(R.id.bt_help);

    }

    private void genOnLongClickListeners() {
        bt_settings.setOnClickListener(v ->
                homeScreen.startActivity(new Intent(getContext(), SettingsActivity.class)));

        clickListenerForAbstractOpener(Uri.parse("http://www.google.com"), bt_internet, iv_internet, tv_internet);
        clickListenerForAbstractOpener(Uri.parse("geo:0,0"), bt_maps, iv_maps, tv_maps);

        // Use the following URI to open the YouTube app instead of the website
        Uri youtubeUri = Uri.parse("vnd.youtube:DfjIOL5MxLY");

        bt_help.setOnClickListener(v -> {
            Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, youtubeUri);
            List<ResolveInfo> activities = packageManager.queryIntentActivities(youtubeIntent, 0);
            boolean isIntentSafe = activities.size() > 0;

            if (isIntentSafe) {
                homeScreen.startActivity(youtubeIntent);
            } else {
                Uri youtubeWebUri = Uri.parse("http://www.youtube.com/watch?v=DfjIOL5MxLY");
                Intent youtubeWebIntent = new Intent(Intent.ACTION_VIEW, youtubeWebUri);
                homeScreen.startActivity(youtubeWebIntent);
            }
        });

    }

    private void clickListenerForAbstractOpener(@NonNull final Uri uri, @NonNull final View bt, @NonNull final ImageView iv, @NonNull final TextView tv) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        final List<ResolveInfo> resolveInfosWithDups =
                packageManager.queryIntentActivities(
                        intent,
                        VERSION.SDK_INT >= VERSION_CODES.M ?
                                PackageManager.MATCH_ALL : PackageManager.MATCH_DEFAULT_ONLY);

        //Removing dups
        final Set<String> packagesSet = new HashSet<>();
        for (final ResolveInfo resolveInfo : resolveInfosWithDups)
            packagesSet.add(resolveInfo.activityInfo.applicationInfo.packageName);
        final List<ResolveInfo> resolveInfos = new ArrayList<>(packagesSet.size());
        for (final ResolveInfo resolveInfo : resolveInfosWithDups) {
            final String packageName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (packagesSet.contains(packageName)) {
                packagesSet.remove(packageName);
                resolveInfos.add(resolveInfo);
            }
        }

        if (resolveInfos.size() > 1) {
            bt.setOnClickListener(v -> S.showDropDownPopup(
                    homeScreen,
                    getWidth(),
                    new DropDownRecyclerViewAdapter.DropDownListener() {
                        @Override
                        public void onUpdate(DropDownRecyclerViewAdapter.ViewHolder viewHolder, int position, PopupWindow popupWindow) {
                            final ResolveInfo resolveInfo = resolveInfos.get(position);
                            if (S.isValidContextForGlide(viewHolder.pic.getContext()))
                                Glide.with(viewHolder.pic)
                                        .load(resolveInfo.loadIcon(packageManager))
                                        .into(viewHolder.pic);
                            viewHolder.text.setText(resolveInfo.loadLabel(packageManager));
                            viewHolder.itemView.setOnClickListener(v1 -> {
                                homeScreen.startActivity(packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.applicationInfo.packageName));
                                popupWindow.dismiss();
                            });
                        }

                        @Override
                        public int size() {
                            return resolveInfos.size();
                        }

                    }, bt));
        } else if (resolveInfos.size() == 1) {
            final ResolveInfo resolveInfo = resolveInfos.get(0);
            if (S.isValidContextForGlide(iv.getContext()))
                Glide.with(iv)
                        .load(resolveInfo.loadIcon(packageManager))
                        .into(iv);
            tv.setText(resolveInfo.loadLabel(packageManager));
            bt.setOnClickListener(v1 -> homeScreen.startActivity(packageManager.getLaunchIntentForPackage(resolveInfo.activityInfo.applicationInfo.packageName)));
        } else {
            bt.setOnClickListener(this::showErrorMessage);
        }
    }

    private void showErrorMessage(View v) {
        blankToast.from(v.getContext()).setType(blankToast.TYPE_ERROR).setText(R.string.no_app_was_found).show();
    }

}