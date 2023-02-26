 

package com.test.norman.launcher.databases.contacts;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.databases.home_screen_pins.HomeScreenPinHelper;
import com.test.norman.launcher.utils.Constants;
import com.test.norman.launcher.utils.S;
import com.test.norman.launcher.views.HomeScreenAppView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Mini contact, contains lookupkey,photo,name and id.
 */
public class MiniContact implements HomeScreenPinHelper.HomeScreenPinnable, Constants.BaseContactsConstants {

    public final String lookupKey, photo;
    @Nullable
    public final String name;
    public final int id;
    public final boolean favorite;

    public MiniContact(String lookupKey, @Nullable String name, String photo, int id, boolean favorite) {
        this.lookupKey = lookupKey;
        this.name = name;
        this.photo = photo;
        this.id = id;
        this.favorite = favorite;
    }

    @Override
    public void applyToHomeScreenAppView(HomeScreenAppView homeScreenAppView) {
        if (S.isValidContextForGlide(homeScreenAppView.iv_icon.getContext()))
            Glide
                    .with(homeScreenAppView.iv_icon)
                    .load(photo)
                    .apply(new RequestOptions()
                            .error(R.drawable.face_on_button))
                    .into(homeScreenAppView.iv_icon);
        homeScreenAppView.setText(name);
        homeScreenAppView.setIntent(lookupKey);
    }
}