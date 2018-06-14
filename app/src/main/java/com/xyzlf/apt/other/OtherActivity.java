package com.xyzlf.apt.other;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.xyzlf.apt.R;
import com.xyzlf.apt.annotations.BindView;
import com.xyzlf.apt.api.ViewInjector;

public class OtherActivity extends AppCompatActivity {

    @BindView(R.id.text)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        ViewInjector.injectView(this);
    }
}
