package com.evv.java.customtouchlistener;

import android.content.Context;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class MyTouchListener implements View.OnTouchListener {
  Context context;
  TextView tvOut;

  float xS[] = new float[10];
  float yS[] = new float[10];

  private int maxX;
  private int maxY;

  public MyTouchListener(Context context, TextView tvOut) {
    this.context = context;
    this.tvOut   = tvOut;

    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();

    maxX = display.getWidth();
    maxY = display.getHeight();
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    int action = event.getAction() & MotionEvent.ACTION_MASK;
    int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
    int pointerID = event.getPointerId(pointerIndex);

    switch (action){
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN:
        xS[pointerID] = event.getX(pointerIndex);
        yS[pointerID] = event.getY(pointerIndex);

        if(yS[0] <= maxY/3) tvOut.setText("section 1");
        else if(yS[0] <= 2*maxY/3) tvOut.setText("section 2");
        else tvOut.setText("#");

        break;

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
      case MotionEvent.ACTION_CANCEL:

        break;

      case MotionEvent.ACTION_MOVE:
        break;
    }

    return true;
  }
}
