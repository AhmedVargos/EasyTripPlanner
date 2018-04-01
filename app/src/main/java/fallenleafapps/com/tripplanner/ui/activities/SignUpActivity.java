package fallenleafapps.com.tripplanner.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;
import fallenleafapps.com.tripplanner.models.UserModel;
import fallenleafapps.com.tripplanner.network.FirebaseHelper;
import fallenleafapps.com.tripplanner.utils.Functions;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.inputS_email)
    TextInputEditText inputEmail;
    @BindView(R.id.inputS_username)
    TextInputEditText inputUsername;
    @BindView(R.id.inputS_password)
    TextInputEditText inputPassword;
    @BindView(R.id.btnS_signup)
    AppCompatButton btnLogin;

    FirebaseAuth firebaseAuth;
    @BindView(R.id.link_login)
    TextView linkLogin;
    @BindView(R.id.signup_layout_main)
    LinearLayout signupLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        inputEmail = findViewById(R.id.inputS_email);
        inputUsername = findViewById(R.id.inputS_username);
        inputPassword = findViewById(R.id.inputS_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Functions.isInternetConnected(SignUpActivity.this)) {
                    Snackbar snackbar = Snackbar.make(signupLayoutMain, "No Internet Connection !", Snackbar.LENGTH_LONG);
                    snackbar.getView().getBackground().setColorFilter(Color.RED, PorterDuff.Mode.ADD);
                    snackbar.show();
                } else {

                    final String email = inputEmail.getText().toString().trim();
                    final String password = inputPassword.getText().toString().trim();
                    final String userName = inputUsername.getText().toString().trim();

                    if (email.equals("")) {
                        inputEmail.setError("Can't be empty!");
                        return;
                    }
                    if (password.equals("")) {
                        inputPassword.setError("Can't be empty!");
                        return;
                    } if (password.length() < 8) {
                        inputPassword.setError("Can't be less than 8 characters");
                        return;
                    }
                    if (userName.equals("")) {
                        inputUsername.setError("Can't be empty!");
                        return;
                    }


                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                Log.i("hello", userId);
                                FirebaseHelper.getInstance().addUser(new UserModel(userName, email, password), userId);

                                Intent signupIntent = new Intent(SignUpActivity.this, HomeActivity.class);
                                startActivity(signupIntent);
                                finish();

                            } else {
                                Toast.makeText(SignUpActivity.this, "Error while creating the account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
    //for the click to finish the keypad
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
