package id.go.kemlu.legalisasidokumen.utils.animationtoggle;

import android.view.View;
import android.view.animation.Animation;

public class AnimUtil {
    public static void animateOut(View view, Animation animation) {
        view.clearAnimation();
        animation.reset();
        animation.setStartTime(0);
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {

            }
        });
        view.setVisibility(View.GONE);
        view.startAnimation(animation);
    }

    public static void animateIn(View view, Animation animation) {
        if (view.getVisibility() != View.VISIBLE) {
            view.clearAnimation();
            animation.reset();
            animation.setStartTime(0);
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        }
    }
}
