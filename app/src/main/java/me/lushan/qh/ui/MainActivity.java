package me.lushan.qh.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import me.lushan.qh.R;
import me.lushan.qh.Utils.HogUtils;

import static me.lushan.qh.Utils.ModuleUtils.isModuleActive;

public class MainActivity extends AppCompatActivity {

    private ImageView statusIcon;
    private TextView statusText;
    private TextView statusDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusIcon = findViewById(R.id.statusIcon);
        statusText = findViewById(R.id.statusTitle);
        statusDesc = findViewById(R.id.statusDesc);

        checkStatus();
    }


    private void checkStatus() {
        if (isModuleActive()) {
            HogUtils.i("Module Enabled");
            statusIcon.setBackgroundResource(R.drawable.status_on);
            statusText.setText(R.string.module_status_on_title);
            statusDesc.setText(R.string.module_status_on_desc);
        } else {
            HogUtils.w("Module Disable");
            statusIcon.setBackgroundResource(R.drawable.status_off);
            statusText.setText(R.string.module_status_off_title);
            statusDesc.setText(R.string.module_status_off_desc);
        }
    }
}
