package com.example.show.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

public class MarqueeText extends TextView{
	
	public MarqueeText(Context context, AttributeSet attrs){
		super(context, attrs);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// TODO Auto-generated method stub
		if (focused) {
			super.onFocusChanged(focused, direction, previouslyFocusedRect);
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		// TODO Auto-generated method stub
		if (hasWindowFocus) {
			super.onWindowFocusChanged(hasWindowFocus);
		}
	}
	
	@Override
	public boolean isFocused() {
		// TODO Auto-generated method stub
		return true;
	}
}
