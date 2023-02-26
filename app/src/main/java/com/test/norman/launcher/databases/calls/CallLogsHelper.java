 

package com.test.norman.launcher.databases.calls;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;

import com.test.norman.launcher.BuildConfig;
import com.test.norman.launcher.adapters.CallsRecyclerViewAdapter;
import com.test.norman.launcher.databases.contacts.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.provider.CallLog.Calls.IS_READ;
import static android.provider.CallLog.Calls.NEW;
import static android.provider.CallLog.Calls.TYPE;

/**
 * Simple Helper to get the call log.
 */
public class CallLogsHelper {
    public static List<Call> getAllCalls(ContentResolver contentResolver) {
        try (Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, Call.PROJECTION, null, null, CallLog.Calls.DATE + " DESC")) {
            final List<Call> calls = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                calls.add(new Call(cursor));
            }
            return calls;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Call> getForSpecificContact(ContentResolver contentResolver, Contact contact) {
        if (BuildConfig.FLAVOR.equals("gPlay"))
            return new ArrayList<>();
        final Uri contactUri = ContactsContract.Contacts.getLookupUri(contact.getId(), contact.getLookupKey());
        try (Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, Call.PROJECTION, CallLog.Calls.CACHED_LOOKUP_URI + "=?", new String[]{contactUri.toString()}, CallLog.Calls.DATE + " DESC")) {
            final List<Call> calls = new ArrayList<>(cursor.getCount());
            while (cursor.moveToNext()) {
                calls.add(new Call(cursor));
            }
            return calls;
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        }
    }

    public static void markAllAsRead(ContentResolver contentResolver) {
        final ContentValues values = new ContentValues();
        values.put(IS_READ, true);
        values.put(CallLog.Calls.NEW, false);
        try {
            contentResolver.update(CallLog.Calls.CONTENT_URI, values, null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAllReadSafe(ContentResolver contentResolver) {
        try (final Cursor cursor = contentResolver.query(CallLog.Calls.CONTENT_URI, new String[]{TYPE, IS_READ, NEW}, String.format(Locale.US, "%s=0 AND %s=1 AND %s=%d", IS_READ, NEW, TYPE, CallsRecyclerViewAdapter.MISSED_TYPE), null, null)) {
            return cursor.getCount() == 0;
        } catch (SecurityException ignore) {
            return true;
        }
    }
}
