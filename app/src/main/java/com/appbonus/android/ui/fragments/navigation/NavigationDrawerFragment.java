package com.appbonus.android.ui.fragments.navigation;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.appbonus.android.R;
import com.dolphin.ui.fragment.NavigationDrawer;
import com.dolphin.ui.fragment.SimpleFragment;

import java.util.ArrayList;
import java.util.List;


public class NavigationDrawerFragment extends SimpleFragment implements NavigationDrawer {

    private static final String KEY_SELECTED_POSITION = "selected_position_key";

    public static final int NAV_OFFERS = 0;
    public static final int NAV_BALANCE = 1;
    public static final int NAV_FRIENDS = 2;
    public static final int NAV_PROFILE = 3;

    public static final int ITEMS_COUNT = 4;

    private NavigationDrawerCallbacks mListener;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;

    private View mFragmentContainerView;

    private int mSelectedPosition = -1;
    private boolean isInit;

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        try {
            mListener = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            mSelectedPosition = savedInstanceState.getInt(KEY_SELECTED_POSITION);

        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.navigation_drawer, container, false);
        assert root != null;

        mListView = (ListView) root.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        isInit = savedInstanceState != null;
        repaint();
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    public void toggle() {
        if (mDrawerLayout != null) {
            if (isDrawerOpen()) {
                mDrawerLayout.closeDrawer(mFragmentContainerView);
            } else {
                mDrawerLayout.openDrawer(mFragmentContainerView);
            }
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {

        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                mDrawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {

        if (position >= 0) {
            mSelectedPosition = position;
            if (mListView != null)
                mListView.setItemChecked(position, true);
        }

        if (mDrawerLayout != null)
            mDrawerLayout.closeDrawer(mFragmentContainerView);

        if (mListener != null)
            mListener.onNavigationItemSelected(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_POSITION, mSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {

        super.onDetach();
        mListener = null;
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app 'context', rather than
     * just what's in the current screen.
     */
    private void showGlobalContextActionBar() {

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    private class PopulateListAsyncTask extends AsyncTask<Void, Void, List<Integer>> {

        @Override
        protected List<Integer> doInBackground(Void... params) {
            List<Integer> list = new ArrayList<Integer>(ITEMS_COUNT);
            for (int i = 0; i < ITEMS_COUNT; i++) {
                list.add(i);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Integer> objects) {
            if (getActivity() != null && mListView != null) {
                mListView.setAdapter(new NavigationDrawerAdapter(getActivity(), objects));

                if (!isInit) {
                    selectItem(mSelectedPosition);
                }
            }
        }
    }

    public void repaint() {
        isInit = true;
        new PopulateListAsyncTask().execute();
    }

    public void setDrawerIndicatorEnabled(boolean enable) {
        mDrawerToggle.setDrawerIndicatorEnabled(enable);
    }

    public boolean isDrawerIndicatorEnabled() {
        return mDrawerToggle.isDrawerIndicatorEnabled();
    }

    public void lockNavigationDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockNavigationDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }
}
