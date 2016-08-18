package com.fusebulb.sidebar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Created by amiteshmaheshwari on 12/07/16.
 */

public class UserSettings extends Activity {

    private Button doneButton;
    private EditText nameEdit;
    private RadioButton selectedLanguageRadio, selectedContentPrefRadio;
    private RadioGroup languagePref, contentSource;
    private String[] SEARCH_FOLDERS = {"Download", "bluetooth", "Bluetooth"};

    public static final String lang_en = "en";
    public static final String lang_hi = "hi";

    public static final String pref_internet = "Internet";
    public static final String pref_bluetooth = "Bluetooth";
    public static final String CONTENT_FILE_NAME = "fusebulb.zip";


    private SharedPreference userSettings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSettings = new SharedPreference();

        setContentView(R.layout.activity_user_settings);

        nameEdit = (EditText) findViewById(R.id.userSettings_UserName);
        languagePref = (RadioGroup) findViewById(R.id.user_language);
        contentSource = (RadioGroup) findViewById(R.id.download_pref);

        Button doneButton = (Button) findViewById(R.id.doneButton);

        String userName = userSettings.getUserName(this);
        String userLanguage = userSettings.getUserLanguage(this);
        final String userContentPref = userSettings.getUserContentPref(this);

        if (userName != null && userLanguage != null && userContentPref != null) {
            nameEdit.setText(userName);

            if (userLanguage.equals(lang_en)) {
                RadioButton b = (RadioButton) findViewById(R.id.lang_en);
                b.setChecked(true);
            } else if (userLanguage.equals(lang_hi)) {
                RadioButton b = (RadioButton) findViewById(R.id.lang_hi);
                b.setChecked(true);
            }

            if (userContentPref.equals(pref_internet)) {
                RadioButton b = (RadioButton) findViewById(R.id.via_internet);
                b.setChecked(true);
            } else if (userContentPref.equals(pref_bluetooth)) {
                RadioButton b = (RadioButton) findViewById(R.id.via_bluetooth);
                b.setChecked(true);
            }
        }

        doneButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Resources rs = getResources();
                String name = nameEdit.getText().toString();
                selectedLanguageRadio = (RadioButton) findViewById(languagePref.getCheckedRadioButtonId());
                selectedContentPrefRadio = (RadioButton) findViewById(contentSource.getCheckedRadioButtonId());
                String language = selectedLanguageRadio.getText().toString();
                String contentPref = selectedContentPrefRadio.getText().toString();
                userSettings.setUserName(UserSettings.this, name);

                if (language.equals(rs.getString(R.string.english))) {
                    userSettings.setUserLanguage(UserSettings.this, lang_en);
                } else if (language.equals(rs.getString(R.string.hindi))) {
                    userSettings.setUserLanguage(UserSettings.this, lang_hi);
                }

                if(checkForContentSource(rs, contentPref) && checkForUserName(rs, name)){
                    Intent mainActivityIntent = new Intent(UserSettings.this, MainActivity.class);
                    startActivity(mainActivityIntent);
                }
            }
        });
    }

    private boolean checkForUserName(Resources rs, String name){
        if (name.equals("")) {
            nameEdit.setError(rs.getString(R.string.error_user_name));
            return false;
        }
        return true;
    }

    private boolean checkForContentSource(Resources rs, String pref){
        if (pref.equals(rs.getString(R.string.via_internet))) {
            userSettings.setUserContentPref(UserSettings.this, pref_internet);
        } else if (pref.equals(rs.getString(R.string.via_bluetooth))) {
            userSettings.setUserContentPref(UserSettings.this, pref_bluetooth);
            if (!checkForContentFile()) {
                TextView contentNotFoundErrorView = (TextView) findViewById(R.id.userSettings_ContentNotFoundError);
                contentNotFoundErrorView.setVisibility(View.VISIBLE);
                return false;
            }
        }
        return true;
    }

    private boolean checkForContentFile(){
        String state = Environment.getExternalStorageState();

        if(state.equals(android.os.Environment.MEDIA_MOUNTED)){
            for (String folderPath : SEARCH_FOLDERS){
                File folderFile= new File(Environment.getExternalStorageDirectory(), folderPath);

                if(folderFile.exists()){
                    if(findContentFile(folderFile, CONTENT_FILE_NAME)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean findContentFile(File dir, String contentFile){
        File listFile[] = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    if (findContentFile(listFile[i], contentFile)) {
                        return true;
                    }
                } else {
                    if (listFile[i].getName().equals(contentFile)) {
                        try {
                            unzip(listFile[i], getFilesDir());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }

}
