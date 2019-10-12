package com.example.parallax;

import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ParallaxListView mParallaxlv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        mParallaxlv = findViewById( R.id.parallaxlv );
        final View header = View.inflate(MainActivity.this, R.layout.view_header, null);
        final ImageView img_header = header.findViewById( R.id.img_header );
        header.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //当布局填充之后，此方法会被调用
                mParallaxlv.setParallaxImg(img_header);
                header.getViewTreeObserver().removeOnGlobalLayoutListener( this );
            }
        } );
       
      
        mParallaxlv.addHeaderView(header);
        mParallaxlv.setAdapter( new ArrayAdapter<String>( this,android.R.layout.simple_expandable_list_item_1, Utils.NAMES) );
    }
}
