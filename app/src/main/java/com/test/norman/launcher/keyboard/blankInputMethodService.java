 

package com.test.norman.launcher.keyboard;

import android.graphics.Point;
import android.inputmethodservice.InputMethodService;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.test.norman.launcher.utils.blankPrefsUtils;

import java.util.List;

/**
 * This class is
 */
public class blankInputMethodService extends InputMethodService implements View.OnClickListener {//} implements KeyboardView.OnKeyboardActionListener {
    private static final String TAG = blankInputMethodService.class.getSimpleName();
    public static final String VOICE_RECOGNITION_IMS = "com.google.android.googlequicksearchbox/com.google.android.voicesearch.ime.VoiceInputMethodService";
    private static int lastLanguage = KeyboardPicker.LANGUAGE_ID;
    private boolean onNumbers = false;
    private blankPrefsUtils blankPrefsUtils;
    private FrameLayout keyboardFrame;
    private blankKeyboard keyboard;

    private static boolean voiceExists(InputMethodManager imeManager) {
        final List<InputMethodInfo> list = imeManager.getInputMethodList();
        for (final InputMethodInfo el : list) {
            if (el.getId().equals(VOICE_RECOGNITION_IMS))
                return true;
        }
        return false;
    }

    public static boolean defaultEditorActionExists(final int imeOptions) {
        return ((imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) == 0) &&
                (imeOptions & EditorInfo.IME_MASK_ACTION) != EditorInfo.IME_ACTION_NONE;

    }

    @Override
    public View onCreateInputView() {
        keyboardFrame = new FrameLayout(this);
        changeLanguage(lastLanguage);
        return keyboardFrame;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        if (blankPrefsUtils == null) {
            blankPrefsUtils = blankPrefsUtils.newInstance(this);
        } else {
            final blankPrefsUtils tmp = blankPrefsUtils.newInstance(this);
            if (!tmp.equals(blankPrefsUtils)) {
                blankPrefsUtils = tmp;
                changeLanguage(lastLanguage);
            }
        }
    }

    public void changeLanguage(int newLanguageKeyboard) {
        keyboardFrame.removeAllViews();
        if (newLanguageKeyboard != NumberKeyboard.LANGUAGE_ID)
            lastLanguage = newLanguageKeyboard;
        final Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        final Point point = new Point();
        display.getSize(point);

        final View view =
                newLanguageKeyboard != KeyboardPicker.LANGUAGE_ID ?
                        keyboard = blankKeyboard.newInstance(newLanguageKeyboard, this, this, this::backspace, getCurrentInputEditorInfo().imeOptions)
                        :
                        new KeyboardPicker(this);
        keyboardFrame.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, point.x > point.y ? (int) (point.y * 0.8) : ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);
        if (keyboard != null)
            keyboard.imeOptionsChanged(getCurrentInputEditorInfo().imeOptions);
    }

    public void startVoiceListening() {
        final InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        if (voiceExists(imeManager)) {
            final IBinder token = getWindow().getWindow().getAttributes().token;
            imeManager.setInputMethod(token, VOICE_RECOGNITION_IMS);
        }
    }

    public void backspace() {
        getCurrentInputConnection().deleteSurroundingText(1, 0);
    }

    @Override
    public void onClick(View v) {
        final char actualCode = (char) v.getTag();
        final InputConnection ic = getCurrentInputConnection();
        final EditorInfo editorInfo = getCurrentInputEditorInfo();
        switch (actualCode) {
            case blankKeyboard.BACKSPACE:
                backspace();
                break;
            case blankKeyboard.SHIFT:
                try {
                    ((blankKeyboard.Capitalised) keyboard).setCaps();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
                break;
            case blankKeyboard.ENTER:
                if (defaultEditorActionExists(editorInfo.imeOptions)) {
                    ic.performEditorAction(editorInfo.imeOptions & EditorInfo.IME_MASK_ACTION);
                } else {
                    sendDownUpKeyEvents(KeyEvent.KEYCODE_ENTER);
                }
                break;
            case blankKeyboard.HIDE:
                hideWindow();
                break;
            case blankKeyboard.LANGUAGE:
                changeLanguage(keyboard.nextLanguage());
                break;
            case blankKeyboard.NUMBERS:
                changeLanguage(onNumbers ? lastLanguage : NumberKeyboard.LANGUAGE_ID);
                onNumbers = !onNumbers;
                break;
            case blankKeyboard.SPEECH_TO_TEXT:
                startVoiceListening();
                break;
            default:
                final String str = String.valueOf(actualCode);
                ic.commitText(str, 1);
        }
    }
}
