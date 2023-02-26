 

package com.test.norman.launcher.views.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.test.norman.launcher.R;
import com.test.norman.launcher.activities.HomeScreenActivity;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.utils.Toggeler;
import com.test.norman.launcher.views.blankPictureTextButton;

import java.lang.ref.WeakReference;

public class NotesView extends HomeView {
    public static final String TAG = NotesView.class.getSimpleName();
    private EditText editText;
    private SharedPreferences sharedPreferences;

    public NotesView(@NonNull HomeScreenActivity activity) {
        super(activity, activity);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        homeScreen.recognizerManager.setNotesFragment(this);
        sharedPreferences = BPrefs.get(getContext());
        final View view = inflater.inflate(R.layout.notes_fragment, container, false);
        editText = view.findViewById(R.id.edit_text);
        editText.setText(sharedPreferences.getString(BPrefs.NOTE_KEY, ""));

        final blankPictureTextButton bt_edit = view.findViewById(R.id.bt_edit);
        Toggeler.newSimpleTextImageToggeler(
                bt_edit,
                bt_edit.getImageView(),
                bt_edit.getTextView(),
                R.drawable.edit_on_button,
                R.drawable.check_on_button,
                R.string.edit,
                R.string.done,
                (v -> {
                    editText.setEnabled(true);
                    if (editText.requestFocus()) {
                        editText.setSelection(editText.getText().length());
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

                    }
                }),
                (v -> editText.setEnabled(false))
        );
        view.findViewById(R.id.bt_speak).setOnClickListener((v) -> homeScreen.recognizerManager.displaySpeechRecognizer());

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                sharedPreferences.edit().putString(BPrefs.NOTE_KEY, s.toString()).apply();
            }
        });
        return view;
    }

    public void onSpeechRecognizerResult(final String spokenText) {
        editText.append("\n");
        editText.append(spokenText);
        sharedPreferences.edit().putString(BPrefs.NOTE_KEY, editText.getText().toString()).apply();
    }

    public static class RecognizerManager {
        private WeakReference<HomeScreenActivity> homeScreen;
        private WeakReference<NotesView> notesFragment;

        public void displaySpeechRecognizer() {
            if (assertOk())
                homeScreen.get().displaySpeechRecognizer();

        }

        public void onSpeechRecognizerResult(String spokenText) {
            if (assertOk())
                notesFragment.get().onSpeechRecognizerResult(spokenText);
        }

        public boolean assertOk() {
            return homeScreen != null && notesFragment != null && homeScreen.get() != null && notesFragment.get() != null;
        }

        public void setHomeScreen(HomeScreenActivity homeScreen) {
            this.homeScreen = new WeakReference<>(homeScreen);
        }

        public void setNotesFragment(NotesView notesFragment) {
            this.notesFragment = new WeakReference<>(notesFragment);
        }
    }
}