package com.dolphin.activity.fragment;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dolphin.utils.Log;

import java.util.Map;


/**
 * Created at 14.02.14 14:58
 *
 * @author Altero
 */
public class BaseListFragment extends BaseFragment {

    private static final String KEY_SCROLL_POSITION = "scroll_position_key";

    static final int INTERNAL_EMPTY_ID = 0x00ff0001;
    static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0002;
    static final int INTERNAL_LIST_CONTAINER_ID = 0x00ff0003;

    private ExtPoint mScrollPosition;

    final private Handler mHandler = new Handler();

    final private Runnable mRequestFocus = new Runnable() {

        public void run() {

            mList.focusableViewAvailable(mList);
        }
    };

    final private AdapterView.OnItemClickListener mOnClickListener
            = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

            onListItemClick((ListView) parent, v, position, id);
        }
    };

    ListAdapter mAdapter;
    protected ListView mList;
    View mEmptyView;
    TextView mStandardEmptyView;
    View mProgressContainer;
    View mListContainer;
    CharSequence mEmptyText;
    boolean mListShown;

    public BaseListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            mScrollPosition = savedInstanceState.getParcelable(KEY_SCROLL_POSITION);
    }

    /**
     * Provide default implementation to return a simple list view.  Subclasses can override to replace with their own
     * layout.  If doing so, the returned view hierarchy <em>must</em> have a ListView whose id is {@link
     * android.R.id#list android.R.id.list} and can optionally have a sibling view id {@link android.R.id#empty
     * android.R.id.empty} that is to be shown when the list is empty.
     * <p/>
     * <p>If you are overriding this method with your own custom content, consider including the standard layout {@link
     * android.R.layout#list_content} in your layout file, so that you continue to retain all of the standard behavior
     * of ListFragment.  In particular, this is currently the only way to have the built-in indeterminant progress state
     * be shown.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Context context = getActivity();

        FrameLayout root = new FrameLayout(context);

        // ------------------------------------------------------------------

        LinearLayout pframe = new LinearLayout(context);
        pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
        pframe.setOrientation(LinearLayout.VERTICAL);
        pframe.setVisibility(View.GONE);
        pframe.setGravity(Gravity.CENTER);

        ProgressBar progress = new ProgressBar(context, null,
                android.R.attr.progressBarStyleLarge);
        pframe.addView(progress, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        root.addView(pframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        // ------------------------------------------------------------------

        FrameLayout lframe = new FrameLayout(context);
        lframe.setId(INTERNAL_LIST_CONTAINER_ID);

        TextView tv = new TextView(getActivity());
        tv.setId(INTERNAL_EMPTY_ID);
        tv.setGravity(Gravity.CENTER);
        lframe.addView(tv, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        ListView lv = new ListView(getActivity());
        lv.setId(android.R.id.list);
        lv.setDrawSelectorOnTop(false);
        lframe.addView(lv, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        root.addView(lframe, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        // ------------------------------------------------------------------

        root.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));

        return root;
    }

    /**
     * Attach to list view once the view hierarchy has been created.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        ensureList();
    }

    /**
     * Detach from list view.
     */
    @Override
    public void onDestroyView() {

        mHandler.removeCallbacks(mRequestFocus);
        mList = null;
        mListShown = false;
        mEmptyView = mProgressContainer = mListContainer = null;
        mStandardEmptyView = null;
        super.onDestroyView();
    }

    /**
     * This method will be called when an item in the list is selected. Subclasses should override. Subclasses can call
     * getListView().getItemAtPosition(position) if they need to access the data associated with the selected item.
     *
     * @param l        The ListView where the click happened
     * @param v        The view that was clicked within the ListView
     * @param position The position of the view in the list
     * @param id       The row id of the item that was clicked
     */
    public void onListItemClick(ListView l, View v, int position, long id) {

    }

    /**
     * Provide the cursor_edit_text_blue for the list view.
     */
    public void setListAdapter(ListAdapter adapter) {

        boolean hadAdapter = mAdapter != null;
        mAdapter = adapter;
        if (mList != null) {
            mList.setAdapter(adapter);
            //todo является ли это причиной скрытия листа?
            /*if (mScrollPosition != null)
                mList.setSelectionFromTop(mScrollPosition.x, mScrollPosition.y);
            if (!mListShown && !hadAdapter) {
                // The list was hidden, and previously didn't have an
                // adapter.  It is now time to show it.
                setListShown(true, getView().getWindowToken() != null);
            }*/
        }
    }

    /**
     * Set the currently selected list item to the specified position with the adapter's data
     *
     * @param position
     */
    public void setSelection(Integer position) {

        ensureList();
        if (mList != null && position != null) {
            mList.setSelection(position);
        }
    }

    public Integer getCount() {
        if (mList != null) {
            return mList.getCount();
        }
        return -1;
    }

    /**
     * Get the position of the currently selected list item.
     */
    public int getSelectedItemPosition() {

        ensureList();
        if (mList != null) {
            return mList.getSelectedItemPosition();
        }
        return -1;
    }

    /**
     * Get the cursor row ID of the currently selected list item.
     */
    public long getSelectedItemId() {

        ensureList();
        if (mList != null) {
            return mList.getSelectedItemId();
        }
        return -1;
    }

    /**
     * Get the activity's list view widget.
     */
    @Deprecated
    public ListView getListView() {

        ensureList();
        return mList;
    }

    public Cursor getCursorAtPosition(int position) {
        if (mAdapter!= null && "SimpleCursorAdapter".equals(mAdapter.getClass().getSuperclass().getSimpleName())) {
            Cursor cursor = ((SimpleCursorAdapter) mAdapter).getCursor();
            if (cursor != null) {
                cursor.moveToPosition(position);
            }
            return cursor;
        }
        return null;
    }

    public Map<String, Object> getItem(int position) {
        if (mAdapter instanceof SimpleAdapter) {
            return (Map<String, Object>) mAdapter.getItem(position);
        }
        return null;
    }

    /**
     * The default content for a ListFragment has a TextView that can be shown when the list is empty.  If you would
     * like to have it shown, call this method to supply the text it should use.
     */
    public void setEmptyText(CharSequence text) {

        ensureList();
        if (mStandardEmptyView == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        mStandardEmptyView.setText(text);
        if (mEmptyText == null && mList != null) {
            mList.setEmptyView(mStandardEmptyView);
        }
        mEmptyText = text;
    }

    /**
     * Control whether the list is being displayed.  You can make it not displayed if you are waiting for the initial
     * data to show in it.  During this time an indeterminant progress indicator will be shown instead.
     * <p/>
     * <p>Applications do not normally need to use this themselves.  The default behavior of ListFragment is to start
     * with the list not being shown, only showing it once an adapter is given with {@link
     * #setListAdapter(android.widget.ListAdapter)}. If the list at that point had not been shown, when it does get
     * shown it will be do without the user ever seeing the hidden state.
     *
     * @param shown If true, the list view is shown; if false, the progress indicator.  The initial value is true.
     */
    public void setListShown(boolean shown) {

        setListShown(shown, true);
    }

    /**
     * Like {@link #setListShown(boolean)}, but no animation is used when transitioning from the previous state.
     */
    public void setListShownNoAnimation(boolean shown) {

        setListShown(shown, false);
    }

    /**
     * Control whether the list is being displayed.  You can make it not displayed if you are waiting for the initial
     * data to show in it.  During this time an indeterminant progress indicator will be shown instead.
     *
     * @param shown   If true, the list view is shown; if false, the progress indicator.  The initial value is true.
     * @param animate If true, an animation will be used to transition to the new state.
     */
    private void setListShown(boolean shown, boolean animate) {

        ensureList();
        if (mProgressContainer == null) {
            throw new IllegalStateException("Can't be used with a custom content view");
        }
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        getActivity(), android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.GONE);
        }
    }

    /**
     * Get the ListAdapter associated with this activity's ListView.
     */
    @Deprecated
    public ListAdapter getListAdapter() {
        return mAdapter;
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            if (mAdapter instanceof SimpleAdapter) {
                ((SimpleAdapter) mAdapter).notifyDataSetChanged();
            }
        }
    }

    protected void ensureList() {

        if (mList != null) {
            return;
        }
        View root = getView();
        if (root == null) {
            return;
//            throw new IllegalStateException("Content view not yet created");
        }
        if (root instanceof ListView) {
            mList = (ListView) root;
        } else {
            mStandardEmptyView = (TextView) root.findViewById(INTERNAL_EMPTY_ID);
            if (mStandardEmptyView == null) {
                mEmptyView = root.findViewById(android.R.id.empty);
            } else {
                mStandardEmptyView.setVisibility(View.GONE);
            }
            mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
            mListContainer = root.findViewById(INTERNAL_LIST_CONTAINER_ID);
            View rawListView = root.findViewById(android.R.id.list);
            if (!(rawListView instanceof ListView)) {
                if (rawListView == null) {
                    throw new RuntimeException(
                            "Your content must have a ListView whose id attribute is " +
                                    "'android.R.id.list'"
                    );
                }
                throw new RuntimeException(
                        "Content has view with id attribute 'android.R.id.list' "
                                + "that is not a ListView class"
                );
            }
            mList = (ListView) rawListView;
            if (mEmptyView != null) {
                mList.setEmptyView(mEmptyView);
            } else if (mEmptyText != null) {
                mStandardEmptyView.setText(mEmptyText);
                mList.setEmptyView(mStandardEmptyView);
            }
        }
        mListShown = true;
        mList.setOnItemClickListener(mOnClickListener);
        if (mAdapter != null) {
            ListAdapter adapter = mAdapter;
            mAdapter = null;
            setListAdapter(adapter);
        } else {
            // We are starting without an adapter, so assume we won't
            // have our data right away and start with the progress indicator.
            if (mProgressContainer != null) {
                setListShown(false, false);
            }
        }
        mHandler.post(mRequestFocus);
    }

    private ExtPoint getScrollPosition() {

        if (mList == null)
            return null;

        int index = mList.getFirstVisiblePosition();
        View view = mList.getChildAt(0);
        int top = (view == null) ? 0 : view.getTop();

        return new ExtPoint(index, top);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_SCROLL_POSITION, getScrollPosition());
    }

    private View.OnClickListener scrollToTopClickAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setSelection(0);
                } catch (Throwable th) {
                    //nothing
                }
            }
        };
    }

    private View.OnClickListener scrollToBottomClickAction() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    setSelection(getCount());
                } catch (Throwable th) {
                    //nothing
                }
            }
        };
    }

    protected void initTopActionBarClick() {
        try {
            ViewGroup decorView = (ViewGroup) getBaseActivity().getWindow().getDecorView();
            View actionBarView = getChild(decorView);
            if (actionBarView != null)
                actionBarView.setOnClickListener(scrollToTopClickAction());
            View title = getTitleView((ViewGroup) actionBarView);
            if (title != null)
                title.setOnClickListener(scrollToTopClickAction());
        } catch (Throwable throwable) {
            Log.e("initTopActionBarClick", throwable);
        }
    }

    protected void initBottomActionBarClick() {
        try {
            ViewGroup decorView = (ViewGroup) getBaseActivity().getWindow().getDecorView();
            View actionBarView = getChild(decorView);
            if (actionBarView != null)
                actionBarView.setOnClickListener(scrollToBottomClickAction());
            View title = getTitleView((ViewGroup) actionBarView);
            if (title != null)
                title.setOnClickListener(scrollToBottomClickAction());
        } catch (Throwable throwable) {
            Log.e("initBottomActionBarClick", throwable);
        }
    }

    private View getChild(ViewGroup viewGroup) {
        if (instanceOfActionBarView(viewGroup)) {
            return viewGroup;
        } else {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    viewGroup = (ViewGroup) viewGroup.getChildAt(i);
                    return getChild(viewGroup);
                }
            }
        }
        return null;
    }

    //hack for >11 android API
    private boolean instanceOfActionBarView(View view) {
        if (view != null) {
            return "ActionBarView".equals(view.getClass().getSimpleName());
        }
        return false;
    }

    private View getTitleView(ViewGroup viewGroup) {
        if (viewGroup == null) return null;

        if (((View) viewGroup) instanceof TextView) {
            return viewGroup;
        } else {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    viewGroup = (ViewGroup) viewGroup.getChildAt(i);
                    return getTitleView(viewGroup);
                }
            }

        }
        return null;
    }

    protected void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        if (mList != null) {
            mList.setOnItemClickListener(listener);
        }
    }

    protected void addHeaderView(View view) {
        if (mList != null) {
            mList.addHeaderView(view, null, false);
        }
    }

    protected void setOnScrollListener(AbsListView.OnScrollListener listener) {
        if (mList != null) {
            mList.setOnScrollListener(listener);
        }
    }

    protected boolean isAdapterEmpty() {
        return mAdapter == null;
    }

    protected void scrollToBottom() {
        if (mList != null && mAdapter != null) {
            mList.setSelection(mAdapter.getCount() - 1);
        }
    }

    protected void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }

    protected void setDivider(Drawable drawable) {
        mList.setDivider(drawable);
    }
}