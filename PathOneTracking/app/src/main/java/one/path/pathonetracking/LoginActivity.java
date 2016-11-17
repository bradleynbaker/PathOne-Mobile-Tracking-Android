package one.path.pathonetracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    // Login
    public void doLogin(View view) {
        Intent myIntent = new Intent(LoginActivity.this, RacePickerActivity.class);
        // myIntent.putExtra("key", value); //Optional parameters
        LoginActivity.this.startActivity(myIntent);
    }
}
