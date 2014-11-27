package com.example.triage;

import defaultPackage.EmergencyRoom;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * LAUNCHER activity
 * Activity to manage logging in into the application.
 */
public class LoginActivity extends Activity {

	@Override
	/**
	 * Creates the activity, inflating the appropriate layout.
	 * Passes context to EmergencyRoom for reading from assets.
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		EmergencyRoom.getInstance().setContext(this);
		setTitle("Log In");
		getIntent();
	}


	/**
	 * Attempts to validate a users credentials. If successful launches PatientsDisplayActivity.
	 * If unsuccessful displays a Toast informing the user.
	 * @param view the view of the clicked button.
	 */
	public void logInUser(View view) {
		String username = ((EditText) this.findViewById(R.id.userIdfield)).getText().toString();
		String password = ((EditText) this.findViewById(R.id.passwordfield)).getText().toString();
		boolean loginSuccessful = EmergencyRoom.getInstance().logIn(username, password);
		
		if(loginSuccessful){
			Intent intent = new Intent(this, PatientsDisplayActivity.class);
			startActivity(intent);
		}else{
			Toast.makeText(this, "Incorrect username and password combination", Toast.LENGTH_SHORT).show();
		}
		

	}

	@Override
	/**
	 * Creates the menu, inflating the given menu.
	 * @param The menu to inflate.
	 * @return return if the menu was successfully created.
	 */
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	/**
	 * Handles selection of menu item.
	 * @param the selected menu item.
	 * @return if the selection was successfully handled.
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
