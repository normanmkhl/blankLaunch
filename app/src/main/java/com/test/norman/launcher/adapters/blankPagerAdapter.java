 

package com.test.norman.launcher.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.util.Pools;

import com.test.norman.launcher.activities.HomeScreenActivity;
import com.test.norman.launcher.databases.home_screen_pins.HomeScreenPinHelper;
import com.test.norman.launcher.utils.BPrefs;
import com.test.norman.launcher.views.home.HomePage1;
import com.test.norman.launcher.views.home.HomePage2;
import com.test.norman.launcher.views.home.HomeViewFactory;
import com.test.norman.launcher.views.home.NotesView;

import java.util.Collections;
import java.util.List;

/**
 * This is the pager adapter used in the {@link HomeScreenActivity}
 * It contains:
 * {@link HomePage1}
 * {@link HomePage2}
 * {@link NotesView}
 * {@link HomeViewFactory} (For accessing Apps)
 * Notice - it uses Views and not Fragments
 */
public class blankPagerAdapter extends blankViewAdapter {
    private static final String TAG = blankPagerAdapter.class.getSimpleName();
    private final Pools.SimplePool<HomeViewFactory> factoryPool = new Pools.SimplePool<>(10);
    /**
     * This field holds the index of the {@link HomePage1}.
     * It depends on the value of {@link BPrefs#NOTE_VISIBLE_KEY} in {@link BPrefs}
     */
    public int startingPage;
    @SuppressWarnings("unchecked")
    public List<HomeScreenPinHelper.HomeScreenPinnable> pinnedList = Collections.EMPTY_LIST;
    private int numItemsBefore, numItems;
    private HomeScreenActivity homeScreen;

    public blankPagerAdapter(HomeScreenActivity homeScreen) {
        this.homeScreen = homeScreen;
        startingPage = (BPrefs.get(this.homeScreen).getBoolean(BPrefs.NOTE_VISIBLE_KEY, BPrefs.NOTE_VISIBLE_DEFAULT_VALUE) ? 2 : 1);
        numItems = numItemsBefore = startingPage + 1;
    }

    public void obtainAppList() {
        pinnedList = HomeScreenPinHelper.getAll(homeScreen);
        numItems =
                numItemsBefore + (pinnedList.size() / HomeViewFactory.AMOUNT_PER_PAGE + (pinnedList.size() % HomeViewFactory.AMOUNT_PER_PAGE == 0 ? 0 : 1));
        notifyDataSetChanged();
    }

    public View getItem(int position) {
        final View view;
        switch (position) {
            case -1:
                view = new NotesView(homeScreen);
                view.setTag(NotesView.TAG);
                break;
            case 0:
                view = new HomePage2(homeScreen);
                view.setTag(HomePage2.TAG);
                break;
            case 1:
                view = new HomePage1(homeScreen);
                view.setTag(HomePage1.TAG);
                break;
            default:
                HomeViewFactory homeFragmentFactory = factoryPool.acquire();
                if (homeFragmentFactory == null)
                    homeFragmentFactory = new HomeViewFactory(homeScreen);
                view = homeFragmentFactory;
                ((HomeViewFactory) view).populate(position - 2);
                view.setTag(HomeViewFactory.TAG + (position - 2));
                break;
        }
        return view;
    }

    @Override
    public int getCount() {
        return numItems;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final int virtualPosition = position - (startingPage - 1);
        return super.instantiateItem(container, virtualPosition);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        final int virtualPosition = position - (startingPage - 1);
        super.destroyItem(container, virtualPosition, object);
        if (object instanceof HomeViewFactory) {
            final HomeViewFactory homeFragmentFactory = (HomeViewFactory) object;
            homeFragmentFactory.recycle();
            factoryPool.release(homeFragmentFactory);
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return object instanceof HomeViewFactory ? POSITION_NONE : super.getItemPosition(object);
    }
}