package com.dolphin.component;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class WrapContentHeightViewPager extends ViewPager {

    /**
     * Constructor
     *
     * @param context the context
     */
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    /**
     * Constructor
     *
     * @param context the context
     * @param attrs the attribute set
     */
    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        boolean wrapHeight = View.MeasureSpec.getMode(heightMeasureSpec)
                == View.MeasureSpec.AT_MOST;

        if(wrapHeight) {
            /**
             * The first super.onMeasure call made the pager take up all the
             * available height. Since we really wanted to wrap it, we need
             * to remeasure it. Luckily, after that call the first child is
             * now available. So, we take the height from it.
             */

            int width = getMeasuredWidth(), height = getMeasuredHeight();

            // Use the previously measured width but simplify the calculations
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);

            /* If the pager actually has any children, take the first child's
             * height and call that our own */
            if(getChildCount() > 0) {
                View firstChild = getChildAt(0);

                /* The child was previously measured with exactly the full height.
                 * Allow it to wrap this time around. */
                firstChild.measure(widthMeasureSpec,
                        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST));

                height = firstChild.getMeasuredHeight();
            }

            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
