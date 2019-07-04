package com.nick.baseapp.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.nick.baseapp.BuildConfig;
import com.nick.baseapp.R;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import java.net.URISyntaxException;


public class AboutActivity extends AppCompatActivity {
    private static final String HOST_NAME = "smtp.qq.com";
    private static final String USER_NAME = "372943264@qq.com";
    private static final String PASSWORD = "nnakofwbjpvgcabj";
    private static final String EMAIL_FROM = "372943264@qq.com";
    private static final String EMAIL_TO = "3540543408@qq.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.about);
        setContentView(R.layout.activity_about);

    }

    public static class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about);
            findPreference("mPFVersion").setSummary("" + BuildConfig.VERSION_CODE);
            findPreference("mPFAdvice").setOnPreferenceClickListener(this);
            findPreference("mPFExceptional").setOnPreferenceClickListener(this);

        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "mPFAdvice":
                    final EditText mETAdvice = new EditText(getActivity());
                    AlertDialog mDialogAdvice = new AlertDialog.Builder(getContext()).setView(mETAdvice).setTitle(getString(R.string.advice)).create();
                    mDialogAdvice.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        SimpleEmail email = new SimpleEmail();
                                        email.setHostName(HOST_NAME);
                                        email.setAuthentication(USER_NAME, PASSWORD);
                                        email.setCharset("utf-8");
                                        email.setFrom(EMAIL_FROM, "", "utf-8");
                                        email.addTo(EMAIL_TO, "", "utf-8");
                                        email.setSubject("");
                                        email.setMsg(mETAdvice.getText().toString());
                                        email.send();
                                    } catch (EmailException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    });
                    mDialogAdvice.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    mDialogAdvice.show();
                    break;
                case "mPFExceptional":
                    try {
                        String urlCode = "FKX02838SGUKOKOJ8BRE98";
                        Intent intent = Intent.parseUri("intent://platformapi/startapp?saId=10000007&clientVersion=3.7.0.0718&qrcode=https%3A%2F%2Fqr.alipay.com%2F{urlCode}%3F_s%3Dweb-other&_t=1472443966571#Intent;scheme=alipayqr;package=com.eg.android.AlipayGphone;end".replace("{urlCode}", urlCode), 1);
                        startActivity(intent);
                    }
                    catch (URISyntaxException e)
                    {
                        e.printStackTrace();
                    }

                    break;
            }
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, AboutActivity.class));
    }
}
