 

package com.test.norman.launcher.fragments_and_dialogs.tutorial_fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class TutorialFragment extends Fragment {
    protected View root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(layoutRes(), container, false);
        attachXml();
        actualSetup();
        return root;
    }

    protected abstract void attachXml();

    protected abstract void actualSetup();

    @LayoutRes
    protected abstract int layoutRes();
}
