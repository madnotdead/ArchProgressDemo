package com.madnotdead.archprogressdemo;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by madnotdead on 2016-07-19.
 */
public class ArchProgressView extends View {

    private Bitmap mBitmap;
    private Bitmap mBgBitmap;
    private Paint mPaint;
    private Paint mPaintBg;
    private RectF mOval;
    private Paint mTextPaint;


    private float _progress = 0;
    private int _maxProgress = 3000;
    private int _duration = 0;
    private  int _numericProgress;
    private boolean _useText = true;
    private String _font = "";

    final String TAG = ArchProgressView.class.getSimpleName();
    final int INITIAL_DURATION = 1500;

    public ArchProgressView(Context context){
            super(context,null);
    }

    public ArchProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ArchProgressView,
                0, 0);

        try {
            _useText = a.getBoolean(R.styleable.ArchProgressView_useTextControl, true);
            _font = a.getString(R.styleable.ArchProgressView_customFont);
            _maxProgress = a.getInteger(R.styleable.ArchProgressView_maxProgress,3000);
        } finally {
            a.recycle();
        }


        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.daily_goal_ring_blur);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.daily_goal_color_meter);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setPathEffect(new CornerPathEffect(10) );
        mPaintBg = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOval = new RectF();

        /*Texto propio del control*/
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(85);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);

        /*Seteo del custom font*/
        try {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"fonts/" + _font);
            mTextPaint.setTypeface(tf);
        }
        catch (Exception ex){
            mTextPaint.setTypeface( Typeface.DEFAULT_BOLD);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);

        Matrix m = new Matrix();
        Matrix m2 = new Matrix();
        RectF src1 = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        RectF dst1 = new RectF(0, 0, w, h);
        m.setRectToRect(src1, dst1, Matrix.ScaleToFit.CENTER);
        Shader shader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader.setLocalMatrix(m);
        mPaint.setShader(shader);

        RectF src2 = new RectF(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight());
        RectF dst2 = new RectF(0, 0, w, h);
        m2.setRectToRect(src2, dst2, Matrix.ScaleToFit.CENTER);
        Shader shader2 = new BitmapShader(mBgBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        shader2.setLocalMatrix(m2);
        mPaintBg.setShader(shader2);

        m.mapRect(mOval, src1);
        m2.mapRect(mOval, src2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mAngle = 345 * _progress / 100;

        canvas.drawArc(mOval, -265, mAngle, true, mPaint);

        canvas.drawArc(mOval, -265, 360, true, mPaintBg);

        float progressNumber = _maxProgress * _progress / 100;

        if(_useText && progressNumber > 0) {

            canvas.drawText(String.valueOf((int) progressNumber), getWidth() / 2, getHeight() / 2, mTextPaint);
        }
    }

    public float getNumericProgress(){

        return _numericProgress;
    }
    public float getProgress() {

        return _progress;
    }

    public void setNumericProgress(int  numericProgress){

        _numericProgress = numericProgress;
    }

    public void setProgress(float _progress) {
        this._progress = (_progress <=100) ? _progress : 100;

        invalidate();
    }

    public int getAnimationDuration(){

        return _duration;
    }

    public void setProgressWithAnimation(float progress) {

        setProgress(0);

        setProgressWithAnimation(progress, INITIAL_DURATION);
    }

    public void setProgressWithAnimation(float progress, int duration) {
        /*Estos hacks son para poder usar las variables luego de setear el progreso en un control externo*/
        _duration = duration;
        setNumericProgress( _maxProgress * (int)progress / 100);

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "progress", progress);
        objectAnimator.setDuration(_duration);
        objectAnimator.setInterpolator(new DecelerateInterpolator());
        objectAnimator.start();
    }
}
