package fallenleafapps.com.tripplanner.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SwipeLessFragmentPager extends ViewPager {

    public SwipeLessFragmentPager(Context context) {
        super(context);
    }

    public SwipeLessFragmentPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}