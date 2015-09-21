package edu.ntu.vison.smallfarmer01.view;

import android.content.Context;
import android.util.AttributeSet;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by Vison on 2015/9/20.
 */
public class MyRoundedImageView extends RoundedImageView {

    public MyRoundedImageView(Context context) {
        this(context, null);
    }

    public MyRoundedImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRoundedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();


        setMeasuredDimension(width, width);
        setCornerRadius(width/2);
    }

}
