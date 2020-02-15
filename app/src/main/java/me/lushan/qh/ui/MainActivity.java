package me.lushan.qh.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import me.lushan.qh.R;
import me.lushan.qh.Utils.HogUtils;
import me.lushan.qh.Utils.VersionUtils;

import static me.lushan.qh.Utils.ModuleUtils.isModuleActive;

public class MainActivity extends AppCompatActivity {

    private static final String QHVersionText = "当前QH版本: ";
    private static final String QQVersionText = "适配的QQ版本: ";

    private ImageView statusIcon;
    private TextView statusText;
    private TextView statusDesc;

    private TextView QHVersion;
    private TextView QQVersion;

    private ImageView iconGithub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openGithub(new View(this));
        DisplayStatus();
        DisplayVersion();
    }


    private void DisplayStatus() {

        statusIcon = findViewById(R.id.statusIcon);
        statusText = findViewById(R.id.statusTitle);
        statusDesc = findViewById(R.id.statusDesc);

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

    private void DisplayVersion() {

         QHVersion = findViewById(R.id.QHVersion);
         QQVersion = findViewById(R.id.QQVersion);

         int QHversion =  VersionUtils.GetQHVersion();
         String SupportQQversion = VersionUtils.GetSupportQQVersion();

         QHVersion.setText(QHVersionText+QHversion);
         QQVersion.setText(QQVersionText+SupportQQversion);

    }

    private void openGithub(View v) {
        iconGithub = findViewById(R.id.icon_link_github);

        iconGithub.setClickable(true);
        iconGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://github.com/lushann/QQ-Helper"));
                startActivity(intent);
            }
        });
    }

}
