package com.dolphin.activity.fragment.root;

import android.widget.ListAdapter;
import android.widget.ListView;

import com.dolphin.activity.fragment.BaseListFragment;

public abstract class RootListFragment<T extends ListView, A extends ListAdapter>
        extends BaseListFragment<T, A> implements RootFragment {
    protected boolean closeActivityAfterDestroy = true;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (closeActivityAfterDestroy) {
            getActivity().finish();
        }
    }

    public void notMortalClose() {
        closeActivityAfterDestroy = false;
    }
}
