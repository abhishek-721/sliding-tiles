package com.creativeideas.slidingtiles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import java.util.Random;

public class TileView extends View {

    private int mXTileSize;
    private int mYTileSize;
    private int mTileCount;

    private int mXOffset;
    private int mYOffset;

    private int mDividerSize;

    private int[][] mXDisplacement;
    private int[][] mYDisplacement;

    private int mImageResourceId;

    private Bitmap[] mTileArray;
    private int[][] mTileGrid;

    private final Paint mPaint = new Paint();

    private final TileSlider mSlider = new TileSlider();

    private TileViewListener mListener;

    public TileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);

        mTileCount = a.getInt(R.styleable.TileView_tileCount, 3);
        mDividerSize = a.getInt(R.styleable.TileView_dividerSize, 2);

        a.recycle();

        mTileArray = new Bitmap[mTileCount * mTileCount];
        mTileGrid = new int[mTileCount][mTileCount];

        mXDisplacement = new int[mTileCount][mTileCount];
        mYDisplacement = new int[mTileCount][mTileCount];
    }

    public TileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TileView);

        mTileCount = a.getInt(R.styleable.TileView_tileCount, 3);
        mDividerSize = a.getInt(R.styleable.TileView_dividerSize, 2);

        a.recycle();


        mTileArray = new Bitmap[mTileCount * mTileCount];
        mTileGrid = new int[mTileCount][mTileCount];

        mXDisplacement = new int[mTileCount][mTileCount];
        mYDisplacement = new int[mTileCount][mTileCount];
    }

    public TileView(Context context, int resourceID, int tileCount) {
        super(context);

        mImageResourceId = resourceID;
        mTileCount = tileCount;
        mDividerSize = 2;

        mTileArray = new Bitmap[mTileCount * mTileCount];
        mTileGrid = new int[mTileCount][mTileCount];

        mXDisplacement = new int[mTileCount][mTileCount];
        mYDisplacement = new int[mTileCount][mTileCount];
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wDesiredSize = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int hDesiredSize = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();

        int wResultant;
        int hResultant;

        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY) {
            wResultant = wSpecSize;
        } else {
            wResultant = wDesiredSize;
            if (wMode == MeasureSpec.AT_MOST) {
                wResultant = Math.min(wResultant, wSpecSize);
            }
        }

        if (hMode == MeasureSpec.EXACTLY) {
            hResultant = hSpecSize;
        } else {
            hResultant = hDesiredSize;
            if (hMode == MeasureSpec.AT_MOST) {
                hResultant = Math.min(hResultant, hSpecSize);
            }
        }

        setMeasuredDimension(wResultant, hResultant);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int dividerTotal = (mTileCount - 1) * mDividerSize;
        int tilesWidth = w - dividerTotal;
        int tilesHeight = h - dividerTotal;

        mXTileSize = (int) Math.floor(tilesWidth / mTileCount);
        mYTileSize = (int) Math.floor(tilesHeight / mTileCount);

        mXOffset = ((tilesWidth - (mXTileSize * mTileCount)) / 2);
        mYOffset = ((tilesHeight - (mYTileSize * mTileCount)) / 2);

        loadTile(tilesWidth, tilesHeight);
    }

    private void loadTile(int w, int h) {
        Bitmap src = BitmapFactory.decodeResource(getResources(), mImageResourceId);
        src = Bitmap.createScaledBitmap(src, w, h, true);

        int key = 1;
        for (int x = 0; x < mTileCount; x++) {
            for (int y = 0; y < mTileCount; y++) {
                if (key < (mTileCount * mTileCount)) {
                    Bitmap bitmap = cropBitmap(src, y * mXTileSize, x * mYTileSize, mXTileSize, mYTileSize);
                    bitmap = printNum(bitmap, String.valueOf(key));
                    mTileArray[key] = bitmap;
                    key++;
                }
            }
        }
    }

    private void reset(int[] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            if ((i + 1) < tiles.length) {
                tiles[i] = i + 1;
            }
        }
    }

    public void setTile(int tileIndex, int x, int y) {
        mTileGrid[x][y] = tileIndex;
    }

    public void clearDisplacements() {
        for (int x = 0; x < mTileCount; x++) {
            for (int y = 0; y < mTileCount; y++) {
                mXDisplacement[x][y] = 0;
                mYDisplacement[x][y] = 0;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < mTileCount; x += 1) {
            for (int y = 0; y < mTileCount; y += 1) {
                if (mTileGrid[x][y] > 0) {
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]],
                            mXOffset + mDividerSize * (float) Math.floor(((y + mTileCount) % mTileCount)) + (y * mXTileSize) + mXDisplacement[x][y],
                            mYOffset + mDividerSize * (float) Math.floor(((x + mTileCount) % mTileCount)) + (x * mYTileSize) + mYDisplacement[x][y],
                            mPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mSlider.onHandleTouch(event);
    }

    class TileSlider {

        private int mActivePointerId = MotionEvent.INVALID_POINTER_ID;

        private float downX;
        private float downY;

        private int emptyTileX;
        private int emptyTileY;

        private int targetTileX;
        private int targetTileY;

        boolean onHandleTouch(MotionEvent event) {

            final int action = event.getActionMasked();

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    final int pointerIndex = event.getActionIndex();
                    final float x = event.getX(pointerIndex);
                    final float y = event.getY(pointerIndex);
                    downX = x;
                    downY = y;
                    mActivePointerId = event.getPointerId(pointerIndex);
                    initEmptyTileIndex();
                    initGridIndex();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    final int pointerIndexMove = event.findPointerIndex(mActivePointerId);
                    final float l = event.getX(pointerIndexMove);
                    final float m = event.getY(pointerIndexMove);

                    //LEFT
                    if (((emptyTileY - 1) == targetTileY) && (emptyTileX == targetTileX) && ((emptyTileY - 1) > -1)) {
                        final int displacement = (int) Math.floor(l - downX);
                        if (mXDisplacement[targetTileX][targetTileY] >= 0 && mXDisplacement[targetTileX][targetTileY] <= (mXTileSize + mDividerSize)) {
                            if (displacement <= 0) {
                                mXDisplacement[targetTileX][targetTileY] = 0;
                            } else if (displacement >= (mXTileSize + mDividerSize)) {
                                mXDisplacement[targetTileX][targetTileY] = (mXTileSize + mDividerSize);
                            } else {
                                mXDisplacement[targetTileX][targetTileY] = displacement;
                            }
                        }//RIGHT
                    } else if (((emptyTileY + 1) == targetTileY) && (emptyTileX == targetTileX) && ((emptyTileY) < mTileCount)) {
                        final int displacement = (int) Math.floor((l - downX));
                        if (mXDisplacement[targetTileX][targetTileY] >= (-1) * (mXTileSize + mDividerSize) && mXDisplacement[targetTileX][targetTileY] <= 0) {
                            if (displacement <= (-1) * (mXTileSize + mDividerSize)) {
                                mXDisplacement[targetTileX][targetTileY] = (-1) * (mXTileSize + mDividerSize);
                            } else if (displacement >= 0) {
                                mXDisplacement[targetTileX][targetTileY] = 0;
                            } else {
                                mXDisplacement[targetTileX][targetTileY] = displacement;
                            }
                        }//TOP
                    } else if (((emptyTileX - 1) == targetTileX) && (emptyTileY == targetTileY) && ((emptyTileX - 1) > -1)) {
                        final int displacement = (int) Math.floor((m - downY));
                        if (mYDisplacement[targetTileX][targetTileY] >= 0 && mYDisplacement[targetTileX][targetTileY] <= (mYTileSize + mDividerSize)) {
                            if (displacement <= 0) {
                                mYDisplacement[targetTileX][targetTileY] = 0;
                            } else if (displacement >= (mYTileSize + mDividerSize)) {
                                mYDisplacement[targetTileX][targetTileY] = (mYTileSize + mDividerSize);
                            } else {
                                mYDisplacement[targetTileX][targetTileY] = displacement;
                            }
                        }//BOTTOM
                    } else if (((emptyTileX + 1) == targetTileX) && (emptyTileY == targetTileY) && ((emptyTileX) < mTileCount)) {
                        final int displacement = (int) Math.floor((m - downY));
                        if (mYDisplacement[targetTileX][targetTileY] >= (-1) * (mYTileSize + mDividerSize) && mYDisplacement[targetTileX][targetTileY] <= 0) {
                            if (displacement <= (-1) * (mYTileSize + mDividerSize)) {
                                mYDisplacement[targetTileX][targetTileY] = (-1) * (mYTileSize + mDividerSize);
                            } else if (displacement >= 0) {
                                mYDisplacement[targetTileX][targetTileY] = 0;
                            } else {
                                mYDisplacement[targetTileX][targetTileY] = displacement;
                            }
                        }
                    }
                    invalidate();
                    return true;
                case MotionEvent.ACTION_UP:
                    mActivePointerId = MotionEvent.INVALID_POINTER_ID;
                    if ((((emptyTileX - 1) == targetTileX) && (emptyTileY == targetTileY) && ((emptyTileX - 1) > -1)) || (((emptyTileX + 1) == targetTileX) && (emptyTileY == targetTileY) && ((emptyTileX + 1) < mTileCount))) {
                        if (Math.abs(mYDisplacement[targetTileX][targetTileY]) > (mYTileSize / 2)) {
                            setTile(mTileGrid[targetTileX][targetTileY], emptyTileX, emptyTileY);
                            setTile(0, targetTileX, targetTileY);
                            mListener.onSwap();
                        }
                    } else if ((((emptyTileY - 1) == targetTileY) && (emptyTileX == targetTileX) && ((emptyTileY - 1) > -1)) || (((emptyTileY + 1) == targetTileY) && (emptyTileX == targetTileX) && ((emptyTileY + 1) < mTileCount))) {
                        if (Math.abs(mXDisplacement[targetTileX][targetTileY]) > (mXTileSize / 2)) {
                            setTile(mTileGrid[targetTileX][targetTileY], emptyTileX, emptyTileY);
                            setTile(0, targetTileX, targetTileY);
                            mListener.onSwap();
                        }
                    }
                    invalidate();
                    clearDisplacements();
                    if (isSolved()) {
                        if (mListener != null) {
                            mListener.onSolved();
                        }
                    }
                    return true;
                case MotionEvent.ACTION_POINTER_UP:
                    final int pointerIndexUp = event.getActionIndex();
                    final int pointerId = event.getPointerId(pointerIndexUp);
                    if (pointerId == mActivePointerId) {
                        final int newPointerIndex = pointerIndexUp == 0 ? 1 : 0;
                        downX = event.getX(newPointerIndex);
                        downY = event.getY(newPointerIndex);
                        mActivePointerId = event.getPointerId(newPointerIndex);
                    }
                    invalidate();
                    return true;
                default:
                    return true;
            }
        }

        private void initGridIndex() {
            for (int x = 0; x < mTileCount; x++) {
                for (int y = 0; y < mTileCount; y++) {
                    if ((downX >= (y * mXTileSize + 2 * y)) && (downX <= (y + 1) * mXTileSize + (2 * y - 1)) && (downY >= (x * mYTileSize + 2 * x)) && (downY <= (x + 1) * mYTileSize + 2 * x - 1)) {
                        targetTileX = x;
                        targetTileY = y;
                    }
                }
            }
        }

        private void initEmptyTileIndex() {
            for (int x = 0; x < mTileCount; x++) {
                for (int y = 0; y < mTileCount; y++) {
                    if (mTileGrid[x][y] == 0) {
                        emptyTileX = x;
                        emptyTileY = y;
                    }
                }
            }
        }
    }

    private Bitmap cropBitmap(Bitmap src, int startX, int startY, int width, int height) {
        return Bitmap.createBitmap(src, startX, startY, width, height);
    }

    public Bitmap printNum(Bitmap bitmap, String text) {
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
        float posX = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        float posY = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.amethysta);
        paint.setTypeface(font);
        paint.setTextSize(textSize);
        canvas.drawText(text, posX, posY, paint);
        return bitmap;
    }

    private boolean isSolvable(int[] tiles) {
        int inversionsCount = 0;

        int indexSize = tiles.length - 1;

        for (int i = 0; i < indexSize; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i]) {
                    inversionsCount++;
                }
            }
        }

        return (inversionsCount % 2 == 0);
    }

    private void shuffle(int[] tiles) {
        Random RNG = new Random();
        int n = tiles.length - 1;

        while (n > 1) {
            int r = RNG.nextInt(n--);
            int temp = tiles[r];
            tiles[r] = tiles[n];
            tiles[n] = temp;
        }
    }

    public void initNew() {
        int[] tiles = new int[mTileCount * mTileCount];

        do {
            reset(tiles);
            shuffle(tiles);
        } while (!isSolvable(tiles));

        int key = 0;
        for (int x = 0; x < mTileCount; x++) {
            for (int y = 0; y < mTileCount; y++) {
                setTile(tiles[key], x, y);
                key++;
            }
        }
    }

    private boolean isSolved() {
        int key = 1;
        for (int x = 0; x < mTileCount; x++) {
            for (int y = 0; y < mTileCount; y++) {
                if (key == mTileCount * mTileCount) {
                    key = 0;
                }
                if ((mTileGrid[x][y] != key)) {
                    return false;
                }
                key++;
            }
        }
        return true;
    }

    public interface TileViewListener {
        void onSolved();

        void onSwap();
    }

    public void setTileViewListener(TileViewListener listener) {
        mListener = listener;
    }
}
