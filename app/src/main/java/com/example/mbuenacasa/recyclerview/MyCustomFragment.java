package com.example.mbuenacasa.recyclerview;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by mbuenacasa on 30/06/16.
 */
public class MyCustomFragment extends Fragment implements View.OnClickListener {

    private Button holaButton;
    private TextView textView;
    private int clickCounter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_my_custom, container, false);

        clickCounter = 0;
        holaButton = (Button) view.findViewById(R.id.holaButton);
        textView = (TextView) view.findViewById(R.id.textView);

        setViews();
        return view;
    }

    private void setViews() {
        holaButton.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        if (view == holaButton) {
            clickCounter++;
            textView.setText("Hola" + Integer.toString(clickCounter));
        }
    }
}
