package com.example.mohdz.spiceai.view.interfaces;

import android.view.View;
import android.widget.Button;

/**
 * Created by zeeshan on 2017-10-30.
 */

public interface DashboardFragmentListener {

    void buttonPressed(View v, int id);
    void search(View v, int id);
    void listButton(Button v);
}
