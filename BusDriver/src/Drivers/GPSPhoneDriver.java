package Drivers;
//N'oubliez pas de déclarer le bon package dans lequel se trouve le fichier !

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GPSPhoneDriver extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      TextView text = new TextView(this);
      text.setText("Bonjour, vous me devez 1 000 000€.");
      setContentView(text);
  }
}
