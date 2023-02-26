 

package com.test.norman.launcher.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.contacts.ContactsActivity;
import com.test.norman.launcher.adapters.ContactRecyclerViewAdapter;
import com.test.norman.launcher.databases.contacts.MiniContact;
import com.test.norman.launcher.databases.home_screen_pins.HomeScreenPinHelper;
import com.test.norman.launcher.views.blankLinearLayoutButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.test.norman.launcher.databases.home_screen_pins.HomeScreenPinHelper.SHARED_PREFS_KEY;

public class SOSActivity extends blankActivity {
    private static final int MAX_PINNED_CONTACTS = 2;
    private blankLinearLayoutButton[] ec;
    private blankLinearLayoutButton ecReal;

    private static void setupEC(blankLinearLayoutButton blankLinearLayoutButton, MiniContact miniContact) {
        if (miniContact.photo != null)
            ((ImageView) blankLinearLayoutButton.getChildAt(0)).setImageURI(Uri.parse(miniContact.photo));
        else
            ((ImageView) blankLinearLayoutButton.getChildAt(0)).setImageResource(R.drawable.face_on_button);

        ((TextView) blankLinearLayoutButton.getChildAt(1)).setText(miniContact.name);
        blankLinearLayoutButton.setOnClickListener(v -> DialerActivity.call(miniContact, v.getContext()));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkPermissions(this, requiredPermissions()))
            return;
        setContentView(R.layout.activity_sos);
        ec = new blankLinearLayoutButton[]{findViewById(R.id.ec1), findViewById(R.id.ec2)};
        ecReal = findViewById(R.id.ec_real);
        setupYoutube(4);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final View.OnClickListener addContactListener = v -> startActivity(new Intent(v.getContext().getApplicationContext(), ContactsActivity.class).putExtra(ContactsActivity.INTENT_EXTRA_CONTACT_ADAPTER_MODE, ContactRecyclerViewAdapter.MODE_SOS));
        final List<MiniContact> miniContacts = PinHelper.getAllPinnedContacts(this);
        final int size = miniContacts == null ? 0 : miniContacts.size();
        for (int i = 0; i < MAX_PINNED_CONTACTS; i++) {
            if (size > i)
                setupEC(ec[i], miniContacts.get(i));
            else
                ec[i].setOnClickListener(addContactListener);
        }

        ecReal.setOnClickListener((v) -> callEmergencyNumber());
    }

    private void callEmergencyNumber() {
        DialerActivity.call("112", this, true);//should work 99.99% of the times
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.nothing, R.anim.slide_out_up);
    }

    public static class PinHelper {
        /**
         * @return true if succeeded
         */
        public static boolean pinContact(Context context, String lookupKey) {
            final SharedPreferences sharedPreferences = context.getSharedPreferences(HomeScreenPinHelper.PinnedContactPreferences.KEY, Context.MODE_PRIVATE);
            final Set<String> befSet = sharedPreferences.getStringSet(HomeScreenPinHelper.PinnedContactPreferences.SOS_KEY, null);
            final Set<String> newSet;
            if (befSet == null)
                newSet = new HashSet<>();
            else {
                if (befSet.size() >= SOSActivity.MAX_PINNED_CONTACTS)
                    return false;
                else
                    newSet = new HashSet<>(befSet);
            }
            newSet.add(lookupKey);
            sharedPreferences.edit().putStringSet(HomeScreenPinHelper.PinnedContactPreferences.SOS_KEY, newSet).apply();
            return true;
        }

        public static boolean isPinned(Context context, String lookupKey) {
            Set<String> set = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE).getStringSet(HomeScreenPinHelper.PinnedContactPreferences.SOS_KEY, null);
            return set != null && set.contains(lookupKey);
        }

        public static void removeContact(Context context, String lookupKey) {
            final SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
            final Set<String> befSet = sharedPreferences.getStringSet(HomeScreenPinHelper.PinnedContactPreferences.SOS_KEY, null);
            final Set<String> newSet;
            if (befSet == null)
                newSet = new HashSet<>();
            else
                newSet = new HashSet<>(befSet);
            newSet.remove(lookupKey);
            sharedPreferences.edit().putStringSet(HomeScreenPinHelper.PinnedContactPreferences.SOS_KEY, newSet).apply();
        }

        /**
         * @param context
         * @return sorted list of lookup keys
         */
        private static List<MiniContact> getAllPinnedContacts(Context context) {
            final Set<String> lookupKeys = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE).getStringSet(HomeScreenPinHelper.PinnedContactPreferences.SOS_KEY, null);
            if (lookupKeys == null)
                return null;
            final List<MiniContact> ret = new ArrayList<>(lookupKeys.size());
            final ContentResolver contentResolver = context.getContentResolver();
            for (String lookupKey :
                    lookupKeys) {
                try (Cursor cursor = contentResolver.query(
                        ContactsContract.Contacts.CONTENT_URI,
                        MiniContact.PROJECTION,
                        ContactsContract.Data.LOOKUP_KEY + " = ?",
                        new String[]{
                                lookupKey
                        }, null)) {
                    if (cursor.moveToFirst()) {
                        ret.add(new MiniContact(
                                lookupKey,
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                                cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)),
                                cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID)),
                                cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.STARRED)) == 1
                        ));
                    } else {
                        removeContact(context, lookupKey);
                    }

                } catch (Exception e) {
                    throw new AssertionError(e);
                }
            }
            Collections.sort(ret, (o1, o2) -> o1.name.compareTo(o2.name));
            return ret;
        }
    }

    @Override
    protected int requiredPermissions() {
        return PERMISSION_CALL_PHONE | PERMISSION_READ_CONTACTS;
    }
}