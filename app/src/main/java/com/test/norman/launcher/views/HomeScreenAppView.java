 

package com.test.norman.launcher.views;

import android.content.ComponentName;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.StringRes;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.contacts.SingleContactActivity;
import com.test.norman.launcher.utils.S;

public class HomeScreenAppView {
    public final ImageView iv_icon;
    private final blankLinearLayoutButton child;
    private final TextView tv_name;

    public HomeScreenAppView(blankLinearLayoutButton child) {
        this.child = child;
        tv_name = child.findViewById(R.id.et_name);
        iv_icon = child.findViewById(R.id.iv_icon);
    }

    public void setText(@StringRes int resId) {
        tv_name.setText(resId);
    }

    public void setText(CharSequence charSequence) {
        tv_name.setText(charSequence);
    }

    public void setIntent(final ComponentName componentName) {
        child.setOnClickListener(v -> S.startComponentName(v.getContext(), componentName));
    }

    public void setIntent(final String contactLookupKey) {
        child.setOnClickListener(v -> v.getContext().startActivity(new Intent(v.getContext(), SingleContactActivity.class).putExtra(SingleContactActivity.CONTACT_LOOKUP_KEY, contactLookupKey)));
    }

    public void setVisibility(int visibility) {
        child.setVisibility(visibility);
    }
}
