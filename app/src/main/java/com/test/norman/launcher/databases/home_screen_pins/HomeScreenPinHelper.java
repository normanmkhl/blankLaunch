 

package com.test.norman.launcher.databases.home_screen_pins;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.test.norman.launcher.databases.apps.AppsDatabase;
import com.test.norman.launcher.databases.contacts.MiniContact;
import com.test.norman.launcher.views.HomeScreenAppView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeScreenPinHelper {
    public static final String SHARED_PREFS_KEY = PinnedContactPreferences.KEY;
    public static final String SET_KEY = PinnedContactPreferences.SET_KEY;

    /**
     * @param context
     * @return sorted list of lookup keys
     */
    private static List<MiniContact> getAllPinnedContacts(Context context) {
        final Set<String> lookupKeys =
                context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
                        .getStringSet(SET_KEY, null);
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

    public static void pinContact(Context context, String lookupKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        final Set<String> befSet = sharedPreferences.getStringSet(SET_KEY, null);
        final Set<String> newSet;
        if (befSet == null)
            newSet = new HashSet<>();
        else
            newSet = new HashSet<>(befSet);
        newSet.add(lookupKey);
        sharedPreferences.edit().putStringSet(SET_KEY, newSet).apply();
    }

    public static boolean isPinned(Context context, String lookupKey) {
        final Set<String> set = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE).getStringSet(SET_KEY, null);
        return set != null && set.contains(lookupKey);
    }

    public static void removeContact(Context context, String lookupKey) {
        final SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        final Set<String> befSet = sharedPreferences.getStringSet(SET_KEY, null);
        final Set<String> newSet;
        if (befSet == null)
            newSet = new HashSet<>();
        else
            newSet = new HashSet<>(befSet);
        newSet.remove(lookupKey);
        sharedPreferences.edit().putStringSet(SET_KEY, newSet).apply();
    }

    public static List<HomeScreenPinnable> getAll(Context context) {
        final List<HomeScreenPinnable> ret =
                new ArrayList<>(
                        AppsDatabase.getInstance(context)
                                .appsDatabaseDao().getAllPinned()
                );
        final List<MiniContact> contactList = getAllPinnedContacts(context);
        if (contactList != null)
            ret.addAll(contactList);
        return ret;

    }

    public interface HomeScreenPinnable {
        void applyToHomeScreenAppView(HomeScreenAppView homeScreenAppView);
    }

    public static final class PinnedContactPreferences {
        public static final String KEY = "PINNED_CONTACTS_KEY";
        public static final String SET_KEY = "PINNED_CONTACTS_SET_KEY";
        public static final String SOS_KEY = "PINNED_CONTACTS_SET_KEY_SOS";
    }
}