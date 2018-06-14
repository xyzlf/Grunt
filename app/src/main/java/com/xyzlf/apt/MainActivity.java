package com.xyzlf.apt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xyzlf.apt.annotations.BindView;
import com.xyzlf.apt.api.ViewInjector;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView textView;
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewInjector.injectView(this);

        Log.i("MainActivity", "--------------------------");
        Log.i("MainActivity", BindView.class.getCanonicalName());
        Log.i("MainActivity", BindView.class.getName());
        Log.i("MainActivity", BindView.class.getSimpleName());
    }
}
