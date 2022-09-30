package com.evv.java.customtouchlistener;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

  ConstraintLayout layout;
  TextView tvOut;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    layout = findViewById(R.id.layout);
    tvOut  = findViewById(R.id.tvOut);

    layout.setOnTouchListener(new MyTouchListener(this, tvOut));
  }


}