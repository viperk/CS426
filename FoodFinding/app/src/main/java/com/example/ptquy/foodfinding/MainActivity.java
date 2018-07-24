package com.example.ptquy.foodfinding;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

	private EditText email;
	private EditText password;
	private Button login;
	private TextView register;
	private FirebaseAuth mAuth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAuth = FirebaseAuth.getInstance();
		init();
	}

	public void init(){
		email = (EditText) findViewById(R.id.Email);
		password = (EditText) findViewById(R.id.Password);
		login = (Button) findViewById(R.id.login);
		register = (TextView) findViewById(R.id.register);
	}

	public void registerClicked(View view) {
		Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
		startActivity(intent);
	}

	public void loginClicked(View view) {
		mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d("sign in successfully", "signInWithEmail:success");
							FirebaseUser user = mAuth.getCurrentUser();
							updateUI(user);
						} else {
							// If sign in fails, display a message to the user.
							Log.w("sign in fail", "signInWithEmail:failure", task.getException());
							Toast.makeText(MainActivity.this, "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

	}

	public void updateUI(FirebaseUser user){
		Intent intent = new Intent(MainActivity.this, UserActivity.class);
		if(user != null){
			String userID = user.getUid();
			String email = user.getEmail();
			intent.putExtra("userID", userID);
			intent.putExtra("Email", email);
		}
		startActivity(intent);
	}
}
