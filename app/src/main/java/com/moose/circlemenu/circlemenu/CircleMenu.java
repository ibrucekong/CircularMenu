package com.moose.circlemenu.circlemenu;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by farble on 2015/6/18.
 * circle menu
 */
public class CircleMenu extends View {
    private static final String TAG = "CircleMenu";
    private static int RADIU;
    private static final int TOOLBAR_H = 56;//height of toolbar
    private static final int[] AEGS = {0, 45, 90, 135, 180, 225, 270, 315};

    public enum Menu {
        ACCOUNT, APP_INFO, KEFU, MY_ORDER, NEW_ORDER, SEND_ORDER, SET, WAIT_ORDER
    }

    private MenuCallBack callBack;
    private int display_w;
    private int display_h;
    private Circle circlePoint;
    private Bitmap bitmapLogo;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private Bitmap bitmap4;
    private Bitmap bitmap5;
    private Bitmap bitmap6;
    private Bitmap bitmap7;
    private Bitmap bitmap8;
    private Paint paintLogo;
    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG;

    private int offsetinDraw;
    private int offsetDrgee = 0;
    private int offsetHistory = 0;//save history

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
        canvas.drawBitmap(bitmapLogo, circlePoint.getX() - bitmapLogo.getWidth() / 2, circlePoint.getY() - bitmapLogo.getHeight() / 2, paintLogo);
        canvas.translate(circlePoint.getX(), circlePoint.getY());//translate
        canvas.save();
        canvas.saveLayerAlpha(0, 0, display_w, display_h, 0xff, LAYER_FLAGS);
        canvas.restore();
        Bitmap[] bimapArr = {bitmap1, bitmap2, bitmap3, bitmap4, bitmap5, bitmap6, bitmap7, bitmap8};
        offsetinDraw = (offsetDrgee + offsetHistory) % 360;
        for (int i = 0; i < 8; i++) {
            canvas.save();
            canvas.rotate(AEGS[i] + offsetinDraw);
            canvas.drawBitmap(bimapArr[i], RADIU - getBitmapWidth(bimapArr[i]) / 2, 0 - getBitmapHeight(bimapArr[i]) / 2, paintLogo);
            canvas.restore();
        }

        canvas.restore();

    }


    class Circle {
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

    int getBitmapWidth(Bitmap b) {
        if (b == null) {
            Log.i(TAG, "getBitmapWidth is null");
        } else {
            return b.getWidth();
        }
        return 0;
    }

    int getBitmapHeight(Bitmap b) {
        if (b == null) {
            Log.i(TAG, "getBitmapHeight is null");
        } else {
            return b.getHeight();
        }
        return 0;
    }

    int getMinCircleRad(Bitmap b) {
        return b.getHeight() > b.getWidth() ? b.getWidth() : b.getHeight();
    }

    @SuppressWarnings("SameParameterValue")
    int dp2px(float dp) {
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public int getOffsetDrgee() {
        return this.offsetDrgee;
    }

    int getOffsetHistory() {
        return this.offsetHistory;
    }

    public synchronized void setOffsetDrgee(int offsetDrgee) {
        this.offsetDrgee = offsetDrgee;
        invalidate();
    }

    public synchronized void drawComplete() {
        offsetHistory = offsetHistory + offsetDrgee;
        if (offsetHistory > 0) {
            offsetHistory = offsetHistory % 360;
        } else {
            while ((offsetHistory + 360) < 0)
                offsetHistory = offsetHistory + 360;
        }
        this.offsetDrgee = 0;
    }

    private synchronized void remeberHistory(int progress) {
        this.offsetHistory = progress;
        invalidate();
    }

    public void addMenuCallBack(MenuCallBack menuCallBack) {
        this.callBack = menuCallBack;
    }

    public boolean touchDisplay(int tx, int ty) {
        if (callBack == null)
            throw new NullPointerException("MenuCallBack is null,you should call addMenuCallBack() method");
        if (isInside(tx, ty, bitmap1, AEGS[0])) {
            callBack.clickMenu(Menu.ACCOUNT);
            return true;
        } else if (isInside(tx, ty, bitmap2, AEGS[1])) {
            callBack.clickMenu(Menu.APP_INFO);
            return true;
        } else if (isInside(tx, ty, bitmap3, AEGS[2])) {
            callBack.clickMenu(Menu.KEFU);
            return true;
        } else if (isInside(tx, ty, bitmap4, AEGS[3])) {
            callBack.clickMenu(Menu.MY_ORDER);
            return true;
        } else if (isInside(tx, ty, bitmap5, AEGS[4])) {
            callBack.clickMenu(Menu.NEW_ORDER);
            return true;
        } else if (isInside(tx, ty, bitmap6, AEGS[5])) {
            callBack.clickMenu(Menu.SEND_ORDER);
            return true;
        } else if (isInside(tx, ty, bitmap7, AEGS[6])) {
            callBack.clickMenu(Menu.SET);
            return true;
        } else if (isInside(tx, ty, bitmap8, AEGS[7])) {
            callBack.clickMenu(Menu.WAIT_ORDER);
            return true;
        }
        return false;
    }

    private boolean isInside(int ix, int iy, Bitmap btm, int offsetar) {
        int radi = getOffsetHistory() + offsetar;
        double x0 = RADIU * Math.cos(Math.toRadians(radi)) + circlePoint.getX();//转换圆心坐标
        double y0 = RADIU * Math.sin(Math.toRadians(radi)) + circlePoint.getY();
        // android.util.Log.e(TAG,"radi:"+radi+"--------------"+RADIU * Math.cos(Math.toRadians(radi))+"--------dead-------"+dd * Math.cos(Math.toRadians(-270)));
        // android.util.Log.e(TAG,"圆心坐标:x0 "+x0+" y0:"+y0+"  点击坐标"+" x:"+ix+"  y:"+iy);
        return ((x0 - ix) * (x0 - ix) + (y0 - iy) * (y0 - iy)) <= getMinCircleRad(btm) * getMinCircleRad(btm);
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

        remeberHistory(ss.progress);
    }

    public static interface MenuCallBack {
        void clickMenu(Menu menu);
    }

}
