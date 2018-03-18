package fallenleafapps.com.tripplanner.ui.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.database.DatabaseContract;
import fallenleafapps.com.tripplanner.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.btn_login)
    AppCompatButton btnLogin;

    @BindView(R.id.input_email)
    TextInputEditText inputEmail;

    @BindView(R.id.input_password)
    TextInputEditText inputPassword;

    @BindView(R.id.link_signup)
    TextView linkSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=inputEmail.getText().toString();
                String password=inputPassword.getText().toString();
                Log.i("user",email+":"+password);

                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            }
        });


    }
}
