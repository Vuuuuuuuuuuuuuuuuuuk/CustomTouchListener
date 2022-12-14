package com.evv.java.customtouchlistener;

import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;


public class MyTouchListener implements View.OnTouchListener {
  Context context;
  TextView tvOut;

  boolean isSwiped = false;
  boolean isMoved = false;
  boolean isPressed = false;
  boolean isLongClicked = false;
  int timeDelay = 1000;
  int countOfHandlers = 0;

  float dif  = 5.0f;
  float xS[] = new float[10];
  float yS[] = new float[10];

  private int maxX;
  private int maxY;

  private float size, old_scale;
  private float x1, y1, x2, y2, xa, ya, ra, toDegree = (float) (180/ Math.PI);

  long difTime = 100;

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
    int count = event.getPointerCount();

    switch (action){
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_POINTER_DOWN:
        if(count == 1) {
          xS[pointerID] = event.getX(pointerIndex);
          yS[pointerID] = event.getY(pointerIndex);

          isSwiped = false;
          isMoved = false;
          isPressed = true;
          isLongClicked = false;
          countOfHandlers++;
          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              if (--countOfHandlers == 0 && isPressed == true && isMoved == false && isSwiped == false) {
                tvOut.setText("LongClick");             //do action for long click
                isLongClicked = true;
              }
            }
          }, timeDelay);
        }else if(count == 2){
          isLongClicked = isMoved = isPressed = isSwiped = false;

          xS[pointerID] = event.getX(pointerIndex);
          yS[pointerID] = event.getY(pointerIndex);

          x1 = xS[0];
          y1 = yS[0];
          x2 = xS[1];
          y2 = yS[1];
          xa = x2 - x1;
          ya = y2 - y1;
          ra = (float) Math.sqrt(xa*xa + ya*ya);

          size =(float) Math.sqrt((xS[0] - xS[1]) * (xS[0] - xS[1]) + (yS[0] - yS[1])*(yS[0] - yS[1]));
          old_scale = tvOut.getScaleX();
        }
        break;

      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_POINTER_UP:
      case MotionEvent.ACTION_CANCEL:

        isPressed = false;
        if(!isLongClicked && !isMoved && !isSwiped){
          isLongClicked = false;
          if(yS[0] <= maxY/3) tvOut.setText("section 1");
          else if(yS[0] <= 2*maxY/3) tvOut.setText("section 2");
          else tvOut.setText("#");
        }
        else if(isSwiped){
          float dx = event.getX(pointerIndex) - xS[0];                         //SWIPE !!!
          if(dx > 0) tvOut.setText("Swipe toward right");
          else tvOut.setText("Swipe without action");
        }

        isMoved   = false;
        isSwiped  = false;

        break;

      case MotionEvent.ACTION_MOVE:

        if(count == 2){
          float x3 = (int) event.getX(event.getPointerId(0));
          float y3 = (int) event.getY(event.getPointerId(0));

          float x4 = (int) event.getX(event.getPointerId(1));
          float y4 = (int) event.getY(event.getPointerId(1));

          float xb = x4 - x3;
          float yb = y4 - y3;

          float rb = (float) Math.sqrt(xb*xb + yb*yb);
          float new_scale = rb/size;

          tvOut.setScaleX(new_scale*old_scale);

          float new_angle = (float) Math.acos( (xa*xb + ya*yb) / (ra*rb))*toDegree;

          //translation
          x4 += x1 - x3;
          y4 += y1 - y3;
          if( (x4 - x1)*ya - (y4 - y1)*xa < 0) new_angle = -new_angle;

          tvOut.setText(""+new_angle);
          break;
        }

        if(isLongClicked || isSwiped) break;

        if(isMoved == true){
          tvOut.setText("Move : " + event.getX(pointerIndex) + " " + event.getY(pointerIndex));  //correct !!!
        }
        else{
          if( Math.abs(event.getX(pointerIndex) - xS[0] ) > dif || Math.abs(event.getY(pointerIndex) - yS[0] ) > dif ){
            long tempDifTime = event.getEventTime() - event.getDownTime();

            if(tempDifTime <= difTime) isSwiped = true;
            else isMoved = true;
          }
        }
        break;
    }

    return true;
  }
}