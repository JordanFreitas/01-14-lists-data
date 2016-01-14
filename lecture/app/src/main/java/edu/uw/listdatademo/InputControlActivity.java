package edu.uw.listdatademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputControlActivity extends AppCompatActivity {

    private static final String TAG = "InputControl";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_control_layout);

        //use final to use the variable and to not change it elsewhere
        final EditText text = (EditText)findViewById(R.id.txtSearch);

        final Button button = (Button) findViewById(R.id.btnSearch);//sets findViewById to use later, casts to a button(Button)

        button.setOnClickListener(new View.OnClickListener() {//listener that listens for clicks
            private int count = 1;
            @Override
            public void onClick(View v) {//implementing since onClick is an interface

                Log.v(TAG, "Button was pressed! "+count+" times!");//logs in android studio when it happens
                count ++;
                String typed = text.getText().toString();//Sees what the text is typed
                Log.v(TAG, "You typed: "+typed);
            }
        });

    }
}
