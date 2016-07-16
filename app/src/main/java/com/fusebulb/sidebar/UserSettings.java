package com.fusebulb.sidebar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


/**
 * Created by amiteshmaheshwari on 12/07/16.
 */

public class UserSettings extends Activity {

    private Button doneButton;
    private EditText nameEdit;
    private RadioButton selectedLanguageRadio;
    private RadioGroup languagePref;

    public static final String lang_en = "en";
    public static final String lang_hi = "hi";


    private SharedPreference userSettings;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        userSettings = new SharedPreference();

        setContentView(R.layout.activity_user_settings);

        nameEdit = (EditText)findViewById(R.id.user_name);
        languagePref = (RadioGroup)findViewById(R.id.user_language);

        Button doneButton = (Button)findViewById(R.id.doneButton);

        String userName = userSettings.getUserName(this);
        String userLanguage = userSettings.getUserLanguage(this);

        if(userName!=null && userLanguage !=null){
            nameEdit.setText(userName);

            if(userLanguage.equals(lang_en)){
                RadioButton b = (RadioButton) findViewById(R.id.lang_en);
                b.setChecked(true);
            }else if(userLanguage.equals(lang_hi)){
                RadioButton b = (RadioButton) findViewById(R.id.lang_hi);
                b.setChecked(true);
            }
        }

        doneButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                String name = nameEdit.getText().toString();
                selectedLanguageRadio = (RadioButton)findViewById(languagePref.getCheckedRadioButtonId());
                String language = selectedLanguageRadio.getText().toString();

                userSettings.setUserName(UserSettings.this, name);

                if(language.equals("English")){
                    userSettings.setUserLanguage(UserSettings.this, lang_en);
                }else if(language.equals("Hindi")){
                    userSettings.setUserLanguage(UserSettings.this, lang_hi);
                }

                Intent mainActivityIntent= new Intent(UserSettings.this, MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });



    }

}
