package com.dolphin.activity.fragment.root;

import com.dolphin.activity.fragment.BaseFragment;

public class RootBaseFragment extends BaseFragment implements RootFragment {
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
