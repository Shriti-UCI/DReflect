package edu.umich.si.inteco.minuku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.umich.si.inteco.minuku.manager.AndroidStreamManager;
import edu.umich.si.inteco.minuku_2.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getBaseContext(), AndroidStreamManager.class));
    }
}
