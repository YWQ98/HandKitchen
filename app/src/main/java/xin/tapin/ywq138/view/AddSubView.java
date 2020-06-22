package xin.tapin.ywq138.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.TintTypedArray;

import xin.tapin.ywq138.R;


public class AddSubView extends LinearLayout implements View.OnClickListener {
    private ImageView btn_sub;
    private ImageView btn_add;
    private TextView tv_count;
    private int value = 1;
    private int minValue = 1;
    private int maxValue = 10;
    public int getValue() {
        String countStr = tv_count.getText().toString().trim();
        if (countStr != null) {
            value = Integer.valueOf(countStr);
        }
        return value;
    }
    public void setValue(int value) {
        this.value = value;
        tv_count.setText(String.valueOf(value));
    }
    public int getMinValue() {
        return minValue;
    }
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }
    public int getMaxValue() {
        return maxValue;
    }
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    public AddSubView(Context context) {
        this(context, null);
    }
    public AddSubView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public AddSubView(Context context, AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.add_sub_layout, this);

        btn_sub = findViewById(R.id.btn_sub);
        btn_add = findViewById(R.id.btn_add);
        tv_count = findViewById(R.id.tv_count);
        getValue();

        btn_add.setOnClickListener(this);
        btn_sub.setOnClickListener(this);
        if (attrs != null) {
            TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(context, attrs,
                            R.styleable.AddSubView);
            int value = tintTypedArray.getInt(R.styleable.AddSubView_value, 0);
            if (value > 0) {
                setValue(value);
            }
            int minValue = tintTypedArray.getInt(R.styleable.AddSubView_minValue, 0);
            if (value > 0) {
                setMinValue(minValue);
            }
            int maxValue = tintTypedArray.getInt(R.styleable.AddSubView_maxValue, 0);
            if (value > 0) {
                setMaxValue(maxValue);
            }
            Drawable addDrawable = tintTypedArray.getDrawable(R.styleable.AddSubView_numberAddBackground);
            if (addDrawable != null) {
                btn_add.setImageDrawable(addDrawable);
            }
            Drawable subDrawable = tintTypedArray.getDrawable(R.styleable.AddSubView_numberSubBackground);
            if (subDrawable != null) {
                btn_sub.setImageDrawable(subDrawable);
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_add) {
            addNumber();
            if (onNumberChangeListener != null) {
                onNumberChangeListener.addNumber(v, value);
            }
        } else {
            subNumber();
            if (onNumberChangeListener != null) {
                onNumberChangeListener.subNumber(v, value);
            }
        }
//        Log.i("TAG", "onClick: " +value);
    }
    private void subNumber() {
        if (value > minValue) {
            value -= 1;
        }
        setValue(value);
    }
    private void addNumber() {
        if (value < maxValue) {
            value += 1;
        }
        setValue(value);
    }
    public interface OnNumberChangeListener {
        void addNumber(View view, int value);
        void subNumber(View view, int value);
    }
    private OnNumberChangeListener onNumberChangeListener;
    public void setOnNumberChangeListener(OnNumberChangeListener
                                                  onNumberChangeListener) {
        this.onNumberChangeListener = onNumberChangeListener;
    }
}
