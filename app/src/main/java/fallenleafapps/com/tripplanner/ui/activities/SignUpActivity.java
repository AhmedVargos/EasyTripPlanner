package fallenleafapps.com.tripplanner.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import fallenleafapps.com.tripplanner.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        firebaseAuth=FirebaseAuth.getInstance();
        inputEmail=findViewById(R.id.inputS_email);
        inputUsername=findViewById(R.id.inputS_username);
        inputPassword=findViewById(R.id.inputS_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=inputEmail.getText().toString().trim();
                String password=inputPassword.getText().toString().trim();
                String userName=inputUsername.getText().toString().trim();

                if(email.equals("") ){
                    Toast.makeText(SignUpActivity.this, "Email cann't be empty", Toast.LENGTH_SHORT).show();
                }
                if (password.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Password cann't be empty", Toast.LENGTH_SHORT).show();
                }
                if (userName.equals("")){
                    Toast.makeText(SignUpActivity.this, "UserName cann't be empty", Toast.LENGTH_SHORT).show();
                }

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SignUpActivity.this, "Failed to register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
