package com.moose.circularmenu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.moose.com.moose.circularmenu.R;


/**
 * Created by farble on 2015/6/18.
 * circle menu
 */
public class CircleMenu extends View {
    private static final String TAG = "CircleMenu";
    /**
     * Radius of Circular Menu
     */
    private static int RADIU;
    /**
     * Toolbar height
     * <li>56dp declared for toolbar's height in material design</li>
     */
    private static final int TOOLBAR_H = 56;
    /**
     * Angle set.This case shows eight options menu
     */
    private static final int[] SITE_ANGLES = {0, 45, 90, 135, 180, 225, 270, 315};
    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    public enum Menu {
        ACCOUNT, APP_INFO, KEFU, MY_ORDER, NEW_ORDER, SEND_ORDER, SET, WAIT_ORDER
    }

    private MenuCallBack callBack;
    /**
     * Screen width
     */
    private int display_w;
    /**
     * Screen height
     */
    private int display_h;
    private Circle circlePoint;
    private Bitmap bitmapLogo;
    /*menu bitmaps*/
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;
    private Bitmap bitmap6;
    private Bitmap bitmap7;
    private Bitmap bitmap8;
    private Paint paintLogo;

    private int offsetinDraw;
    private int offsetDegrees = 0;
    /**
     * Keep the last operation offset angle
     */
    private int offsetHistory = 0;

    public CircleMenu(Context context) {
        super(context);
        init();
    }

    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        display_h = displaymetrics.heightPixels;
        display_w = displaymetrics.widthPixels;

        paintLogo = new Paint();
        paintLogo.setColor(R.color.white);
        paintLogo.setStyle(Paint.Style.STROKE);

        bitmapLogo = BitmapFactory.decodeResource(getResources(), R.mipmap.account_small);
        RADIU = getBitmapWidth(bitmapLogo) * 3 / 4;
        bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_account);
        bitmap2 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_appinfo);
        bitmap3 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_kefu);
        bitmap4 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_my_order);
        bitmap5 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_neworder);
        bitmap6 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_send_order);
        bitmap7 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_set);
        bitmap8 = BitmapFactory.decodeResource(getResources(), R.mipmap.circle_wait_order);
        circlePoint = new Circle(bitmapLogo.getWidth() / 2 - getBitmapWidth(bitmapLogo) / 4, (display_h - dp2px(TOOLBAR_H)) / 2);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmapLogo, circlePoint.getX() - bitmapLogo.getWidth() / 2, circlePoint.getY() - bitmapLogo.getHeight() / 2, paintLogo);
        /**Moving axes*/
        canvas.translate(circlePoint.getX(), circlePoint.getY());
        canvas.save();
        canvas.saveLayerAlpha(0, 0, display_w, display_h, 0xff, LAYER_FLAGS);
        canvas.restore();
        Bitmap[] menuSet = {bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7, bitmap8};
        offsetinDraw = (offsetDegrees + offsetHistory) % 360;
        for (int i = 0; i < 8; i++) {
            canvas.save();
            canvas.rotate(SITE_ANGLES[i] + offsetinDraw);
            canvas.clipRect(RADIU - getBitmapWidth(menuSet[i]) / 2 - 2, 0 - getBitmapHeight(menuSet[i]) / 2 - 2, RADIU + getBitmapWidth(menuSet[i]) / 2 + 2, getBitmapHeight(menuSet[i]) / 2 + 2);
            canvas.drawBitmap(menuSet[i], RADIU - getBitmapWidth(menuSet[i]) / 2, 0 - getBitmapHeight(menuSet[i]) / 2, paintLogo);
            canvas.restore();
        }

        canvas.restore();

    }


    private class Circle {
        int x;
        int y;

        Circle(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    /**
     * Get the bitmap width
     *
     * @param b
     * @return width of bitmap
     */
    int getBitmapWidth(Bitmap b) {
        if (b == null) {
            throw new NullPointerException("[" + TAG + "]" + "bitmap is null in method getBitmapWidth(Bitmap b)");
        } else {
            return b.getWidth();
        }
    }

    /**
     * Get the bitmap height
     *
     * @param b
     * @return height of bitmap
     */
    int getBitmapHeight(Bitmap b) {
        if (b == null) {
            throw new NullPointerException("[" + TAG + "]" + "bitmap is null in method getBitmapHeight(Bitmap b)");
        } else {
            return b.getHeight();
        }
    }

    int getMinCircleRadius(Bitmap b) {
        return b.getHeight() > b.getWidth() ? b.getWidth() : b.getHeight();
    }

    @SuppressWarnings("SameParameterValue")
    int dp2px(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    int getOffsetDegrees() {
        return this.offsetDegrees;
    }

    int getOffsetHistory() {
        return this.offsetHistory;
    }

    public synchronized void setOffsetDegrees(int offsetDegrees) {
        this.offsetDegrees = offsetDegrees;
        invalidate();
    }

    /**
     * Save the last operation offset angle ,calling this while your finger from the screen
     */
    public synchronized void drawComplete() {
        offsetHistory = offsetHistory + offsetDegrees;
        if (offsetHistory > 0) {
            offsetHistory = offsetHistory % 360;
        } else {
            while ((offsetHistory + 360) < 0)
                offsetHistory = offsetHistory + 360;
        }
        this.offsetDegrees = 0;
    }

    private synchronized void recoveryOffsetHistory(int progress) {
        this.offsetHistory = progress;
        invalidate();
    }

    public void addMenuCallBack(MenuCallBack menuCallBack) {
        this.callBack = menuCallBack;
    }

    public boolean touchDisplay(int tx, int ty) {
        if (callBack == null)
            throw new NullPointerException("MenuCallBack is null,you should call addMenuCallBack() method");
        if (isMenuTouched(tx, ty, bitmap1, SITE_ANGLES[0])) {
            callBack.clickMenu(Menu.ACCOUNT);
            return true;
        } else if (isMenuTouched(tx, ty, bitmap2, SITE_ANGLES[1])) {
            callBack.clickMenu(Menu.APP_INFO);
            return true;
        } else if (isMenuTouched(tx, ty, bitmap3, SITE_ANGLES[2])) {
            callBack.clickMenu(Menu.KEFU);
            return true;
        } else if (isMenuTouched(tx, ty, bitmap4, SITE_ANGLES[3])) {
            callBack.clickMenu(Menu.MY_ORDER);
            return true;
        } else if (isMenuTouched(tx, ty, bitmap5, SITE_ANGLES[4])) {
            callBack.clickMenu(Menu.NEW_ORDER);
            return true;
        } else if (isMenuTouched(tx, ty, bitmap6, SITE_ANGLES[5])) {
            callBack.clickMenu(Menu.SEND_ORDER);
            return true;
        } else if (isMenuTouched(tx, ty, bitmap7, SITE_ANGLES[6])) {
            callBack.clickMenu(Menu.SET);
            return true;
        } else if (isMenuTouched(tx, ty, bitmap8, SITE_ANGLES[7])) {
            callBack.clickMenu(Menu.WAIT_ORDER);
            return true;
        }
        return false;
    }

    /**
     * @param b          Bitmap menu representative
     * @param ix         X coordinate position
     * @param iy         Y coordinate position
     * @param siteOffset the site offset degrees of menu
     * @return <li>true menu touched</li>
     * <li>false empty area touched</li>
     */
    private boolean isMenuTouched(int ix, int iy, Bitmap b, int siteOffset) {
        int angle = getOffsetHistory() + siteOffset;
        double x0 = RADIU * Math.cos(Math.toRadians(angle)) + circlePoint.getX();
        double y0 = RADIU * Math.sin(Math.toRadians(angle)) + circlePoint.getY();
        return ((x0 - ix) * (x0 - ix) + (y0 - iy) * (y0 - iy)) <= getMinCircleRadius(b) * getMinCircleRadius(b);
    }

    static class SavedState extends BaseSavedState {
        int progress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        SavedState(Parcel in) {
            super(in);
            progress = in.readInt();
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(progress);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);

        ss.progress = offsetHistory;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        recoveryOffsetHistory(ss.progress);
    }

    public static interface MenuCallBack {
        void clickMenu(Menu menu);
    }

}
