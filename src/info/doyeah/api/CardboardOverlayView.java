/*
 * Copyright 2014 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.doyeah.api;

import java.util.Timer;
import java.util.TimerTask;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Contains two sub-views to provide a simple stereo HUD.
 */
public class CardboardOverlayView extends LinearLayout implements GestureDetector.OnDoubleTapListener, OnGestureListener {
    private final CardboardOverlayEyeView mLeftView;
    private final CardboardOverlayEyeView mRightView;
    private AlphaAnimation mTextFadeAnimation;
	private MainActivity mainActivity;
	private GestureDetector gestureDetector;
	private Handler mHandler = new Handler();
	private Time time = new Time();

	public CardboardOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);

        LayoutParams params = new LayoutParams(
            LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f);
        params.setMargins(0, 0, 0, 0);

        mLeftView = new CardboardOverlayEyeView(context, attrs);
        mLeftView.setLayoutParams(params);
        addView(mLeftView);

        mRightView = new CardboardOverlayEyeView(context, attrs);
        mRightView.setLayoutParams(params);
        addView(mRightView);

        setDepthOffset(0.0016f);
        setColor(Color.rgb(150, 255, 180));
        setVisibility(View.VISIBLE);
        
        mTextFadeAnimation = new AlphaAnimation(1.0f, 0.0f);
        mTextFadeAnimation.setDuration(2000);
        gestureDetector = new GestureDetector(context, this);

        Timer mTimer = new Timer();
        
        mTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                mHandler.post( new Runnable() {
                    @SuppressLint("DefaultLocale")
					public void run() {
                    	time.setToNow();
                    	String timeString = String.format("%04d/%02d/%02d%n%02d:%02d:%02d%nscore %d"
                    			, time.year
                    			, time.month + 1
                    			, time.monthDay
                    			, time.hour
                    			, time.minute
                    			, time.second
                    			, mainActivity.game.score);
                    	showStatus(timeString);
                    }
                });
            }
        }, 100, 1000);
        
    }

    public void showStatus(String message) {
        setStatusText(message);
        setStatusTextAlpha(1f);
    }

    public void show3DToast(String message) {
        setToastText(message);
        setToastTextAlpha(1f);
        
        mTextFadeAnimation.setAnimationListener(new EndAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                setToastTextAlpha(0f);
            }
        });
        mLeftView.toastTextView.startAnimation(mTextFadeAnimation);
        mRightView.toastTextView.startAnimation(mTextFadeAnimation);
    }
    public void showLast3DToast(String message) {
        setToastText(message);
        setToastTextAlpha(1f);
    }
    
    private abstract class EndAnimationListener implements Animation.AnimationListener {
        @Override public void onAnimationRepeat(Animation animation) {}
        @Override public void onAnimationStart(Animation animation) {}
    }
    

    private void setDepthOffset(float offset) {
        mLeftView.setOffset(offset);
        mRightView.setOffset(-offset);
    }

    private void setStatusText(String text) {
        mLeftView.setStatusText(text);
        mRightView.setStatusText(text);
    }

    private void setStatusTextAlpha(float alpha) {
        mLeftView.setStatusTextViewAlpha(alpha);
        mRightView.setStatusTextViewAlpha(alpha);
    }

    private void setToastText(String text) {
        mLeftView.setToastText(text);
        mRightView.setToastText(text);
    }

    private void setToastTextAlpha(float alpha) {
        mLeftView.setToastTextViewAlpha(alpha);
        mRightView.setToastTextViewAlpha(alpha);
    }

    private void setColor(int color) {
        mLeftView.setStatusColor(color);
        mLeftView.setToastColor(color);
        mRightView.setStatusColor(color);
        mRightView.setToastColor(color);
    }

	public void setMain(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}

    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent e) {
    	gestureDetector.onTouchEvent(e);
		return true;
    }

    private class CardboardOverlayEyeView extends ViewGroup {
        private final ImageView imageView;
        private final TextView statusTextView;
        private final TextView toastTextView;
        private float offset;

        public CardboardOverlayEyeView(Context context, AttributeSet attrs) {
            super(context, attrs);
            imageView = new ImageView(context, attrs);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setAdjustViewBounds(true);  // Preserve aspect ratio.
            addView(imageView);

            statusTextView = new TextView(context, attrs);
            statusTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14.0f);
            statusTextView.setTypeface(statusTextView.getTypeface(), Typeface.BOLD);
            statusTextView.setGravity(Gravity.START);
            statusTextView.setShadowLayer(3.0f, 0.0f, 0.0f, Color.DKGRAY);
            addView(statusTextView);

            toastTextView = new TextView(context, attrs);
            toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.0f);
            toastTextView.setTypeface(toastTextView.getTypeface(), Typeface.BOLD);
            toastTextView.setGravity(Gravity.CENTER);
            toastTextView.setShadowLayer(3.0f, 0.0f, 0.0f, Color.DKGRAY);
            addView(toastTextView);
        }

        public void setStatusColor(int color) {
            imageView.setColorFilter(color);
            statusTextView.setTextColor(color);
        }

        public void setStatusText(String text) {
            statusTextView.setText(text);
        }

        public void setStatusTextViewAlpha(float alpha) {
            statusTextView.setAlpha(alpha);
        }

        public void setToastColor(int color) {
            toastTextView.setTextColor(color);
        }

        public void setToastText(String text) {
            toastTextView.setText(text);
        }

        public void setToastTextViewAlpha(float alpha) {
            toastTextView.setAlpha(alpha);
        }
        public void setOffset(float offset) {
            this.offset = offset;
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            // Width and height of this ViewGroup.
            final int width = right - left;
            final int height = bottom - top;

            // The size of the image, given as a fraction of the dimension as a ViewGroup. We multiply
            // both width and heading with this number to compute the image's bounding box. Inside the
            // box, the image is the horizontally and vertically centered.
            final float imageSize = 0.12f;

            // The fraction of this ViewGroup's height by which we shift the image off the ViewGroup's
            // center. Positive values shift downwards, negative values shift upwards.
            final float verticalImageOffset = -0.07f;

            // Vertical position of the text, specified in fractions of this ViewGroup's height.
            final float verticalTextPos = 0.52f;

            // Layout ImageView
            float imageMargin = (1.0f - imageSize) / 2.0f;
            float leftMargin = (int) (width * (imageMargin + offset));
            float topMargin = (int) (height * (imageMargin + verticalImageOffset));
            imageView.layout(
                (int) leftMargin, (int) topMargin,
                (int) (leftMargin + width * imageSize), (int) (topMargin + height * imageSize));

            // Layout statusTextView
            leftMargin = 100;
            topMargin = 100;
            statusTextView.layout(
                (int) leftMargin, (int) topMargin,
                (int) (leftMargin + width), (int) (topMargin + height * (1.0f - verticalTextPos)));

            // Layout toastTextView
            leftMargin = 0;
            topMargin = 750;
            toastTextView.layout(
                    (int) leftMargin, (int) (topMargin),
                    (int) (leftMargin + width), (int) (topMargin + 300));
        }
    }

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		switch(e.getAction()) {
		case MotionEvent.ACTION_UP:
			mainActivity.onDoubleTap();
			return false;
		default:
			return false;
		}
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		mainActivity.onCardboardTrigger();
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}
