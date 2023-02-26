 

package com.test.norman.launcher.screenshots;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

class TestUtils {

    /**
     * NEVER move this method to Utils or S
     * It should never be accessible from production, Only in Tests
     */
    public static void deleteAllContactsInEmulator(final Context context) {
        assert false;
        final ContentResolver cr = context.getContentResolver();
        final Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        while (cur.moveToNext()) {
            try {
                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                cr.delete(uri, null, null);
            } catch (Exception e) {
            }
        }
    }
}
