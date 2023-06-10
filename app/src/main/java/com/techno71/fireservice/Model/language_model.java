package com.techno71.fireservice.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.widget.Toast;

import java.util.Locale;

public   class language_model {

   private Context context;
   private SharedPreferences sharedPreferences_lang;

   public language_model(Context context) {
      this.context = context;
   }

   public void setLocal(String language) {

      sharedPreferences_lang=context.getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

      Locale locale=new Locale(language);
      Locale.setDefault(locale);

      Configuration configuration=new Configuration();
      configuration.locale=locale;

      context.getResources().updateConfiguration(configuration,context.getResources().getDisplayMetrics());

      sharedPreferences_lang.edit().putString("language",language).commit();
   }

   public void loadLanguage() {

      sharedPreferences_lang=context.getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);


      if (!sharedPreferences_lang.getString("language", "null").contains("null")) {

           String languane= sharedPreferences_lang.getString("language","null");

         if (!languane.contains("null")) {
            setLocal(languane);
         }
      }
   }
}
