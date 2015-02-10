package com.dolphin.activity.fragment;

public class MainFragment extends BaseFragment implements RootFragment {
    protected boolean closeActivityAfterDestroy = true;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (closeActivityAfterDestroy) {
            getActivity().finish();
        }
    }

    public void notMortalClose() {
        closeActivityAfterDestroy = false;
    }
}
