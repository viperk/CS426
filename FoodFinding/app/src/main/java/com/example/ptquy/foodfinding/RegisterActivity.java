package com.example.ptquy.foodfinding;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

	private EditText email;
	private EditText password;
	private Button buttonRegister;
	private FirebaseAuth mAuth;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		email = (EditText) findViewById(R.id.Email);
		password = (EditText) findViewById(R.id.Password);
		buttonRegister = (Button) findViewById(R.id.btnRes);
		mAuth = FirebaseAuth.getInstance();

		buttonRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if("".equals(email.getText().toString()) || "".equals(password.getText().toString()))
					Toast.makeText(RegisterActivity.this, "Please fill in info", Toast.LENGTH_SHORT).show();
				else {
					mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
							.addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
								@Override
								public void onComplete(@NonNull Task<AuthResult> task) {
									if (task.isSuccessful()) {
										// Sign in success, update UI with the signed-in user's information
										Log.d("register successfully", "createUserWithEmail:success");
										FirebaseUser user = mAuth.getCurrentUser();
										updateUI(user);
									} else {
										// If sign in fails, display a message to the user.
										Log.w("register fail", "createUserWithEmail:failure", task.getException());
										Toast.makeText(RegisterActivity.this, "Authentication failed.",
												Toast.LENGTH_SHORT).show();
									}
								}
							});
				}
			}
		});
	}

	public void updateUI(FirebaseUser user){
		Intent intent = new Intent(RegisterActivity.this, UserActivity.class);
		if(user != null){
			String userEmail = user.getEmail();
		}
		startActivity(intent);
	}
}
