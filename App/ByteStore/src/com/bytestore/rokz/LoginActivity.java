package com.bytestore.rokz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	EditText editUser;
	EditText editPass;
	Sesion sesion = new Sesion();

	public void SignIn(View view) {
		try {
			editUser = (EditText) findViewById(R.id.user);
			editPass = (EditText) findViewById(R.id.password);
			sesion.IniciarSesion(editUser.getText().toString(), editPass
					.getText().toString());
			Intent returnIntent = new Intent();
			returnIntent.putExtra("result", "Salir de sesion");
			setResult(RESULT_OK, returnIntent);
			this.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
}
