 

package com.test.norman.launcher.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Simple Toggeler maker, moves the boilerplate code to here
 * each one is different, and uses different technique
 */
public class Toggeler {
    public static void newSimpleTextImageToggeler(View button, ImageView imageView, TextView textView, int pic1, int pic2, int text1, int text2, View.OnClickListener listener1, View.OnClickListener listener2) {
        final boolean[] state = new boolean[]{true};
        button.setOnClickListener((v -> {
            if (state[0]) {
                listener1.onClick(v);
                imageView.setImageResource(pic2);
                textView.setText(text2);
            } else {
                listener2.onClick(v);
                imageView.setImageResource(pic1);
                textView.setText(text1);
            }
            state[0] = !state[0];
        }));
    }

    public static void newTextImageToggeler(View button, ImageView imageView, TextView textView, int[] pics, int[] texts, View.OnClickListener[] listeners, int startingIndex) {
        final int[] which = new int[]{startingIndex};
        imageView.setImageResource(pics[startingIndex]);
        textView.setText(texts[startingIndex]);
        button.setOnClickListener((v -> {
            final int index = (which[0] + 1) % listeners.length;
            which[0] = index;
            listeners[index].onClick(v);
            imageView.setImageResource(pics[index]);
            textView.setText(texts[index]);
        }));
    }

    public static void newImageToggeler(View button, ImageView imageView, int[] pics, View.OnClickListener[] listeners) {
        final int[] which = new int[]{1};
        button.setOnClickListener((v -> {
            final int index = (which[0] + 1) % pics.length;
            which[0] = index;
            listeners[index].onClick(v);
            imageView.setImageResource(pics[index]);
        }));
    }

    public static void newBackgroundToggeler(View button, ImageView imageView, int[] pics, View.OnClickListener[] listeners) {
        final int[] which = new int[]{1};
        button.setOnClickListener((v -> {
            final int index = (which[0] + 1) % pics.length;
            which[0] = index;
            listeners[index].onClick(v);
            imageView.setBackgroundResource(pics[index]);

        }));
    }

    public static void newAdvancedImageToggeler(View button, ImageView imageView, int[] pics, IndexConsumer consumer, int startingIndex, int jumps) {
        final int[] which = new int[]{startingIndex};
        imageView.setImageResource(pics[startingIndex]);
        button.setOnClickListener((v -> {
            int index = (which[0] + jumps) % pics.length;
            if (index < 0)
                index = pics.length - 1;
            which[0] = index;
            consumer.accept(index);
            imageView.setImageResource(pics[index]);

        }));
    }

    public interface IndexConsumer {
        void accept(int index);
    }
}