package com.dhobs.cowsbulls;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Cvpgame extends Activity {
	private int x = 0;
	private int y = 0;
	private int n = 1;
	private int countg = 1;
	int b, a;
	private String wor, wore;
	private String guesses[] = { "", "", "", "", "", "", "", "", "", "", "" };
	private int y1, y2, y3, y4, y5 = 0;
	int[] val = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0 };
	static Tracker t;

	@Override
	protected void onStart() {
		super.onStart();
			GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		if(a==0){
			Toast save = Toast.makeText(this, "Game saved",
					Toast.LENGTH_LONG);
			save.show();
		}
	}


	@Override
	protected void onStop() {
		super.onStop();
		GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("y1", y1);
		outState.putInt("y2", y2);
		outState.putInt("y3", y3);
		outState.putInt("y4", y4);
		outState.putInt("y5", y5);
		outState.putStringArray("guesses", guesses);
		outState.putInt("x", x);
		outState.putInt("y", y);
		outState.putInt("countg", countg);
		outState.putIntArray("val", val);
	}

	protected void onCreate(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			y1 = savedInstanceState.getInt("y1");
			y2 = savedInstanceState.getInt("y2");
			y3 = savedInstanceState.getInt("y3");
			y4 = savedInstanceState.getInt("y4");
			y5 = savedInstanceState.getInt("y5");
			guesses = savedInstanceState.getStringArray("guesses");
			x = savedInstanceState.getInt("x");
			y = savedInstanceState.getInt("y");
			countg = savedInstanceState.getInt("countg");
			val = savedInstanceState.getIntArray("val");
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		t = ((MyApplication) getApplication())
				.getTracker(MyApplication.TrackerName.APP_TRACKER);
		loadBackground();
		LinearLayout lp = (LinearLayout) findViewById(R.id.mainlayout);
		if (b == 1)
			lp.setBackgroundResource(R.drawable.wall);
		else
			lp.setBackgroundResource(R.drawable.background_cross);
		Intent play = getIntent();
		wore = play.getExtras().getString("word");
		loadP();
		final String word = wor.toUpperCase(Locale.US);
		n = word.length();
		final MediaPlayer s_button = MediaPlayer.create(this, R.raw.button);
		TextView displaytextview1 = (TextView) findViewById(R.id.textView1);
		Typeface tf = Typeface.createFromAsset(this.getAssets(),
				"fonts/stainy.ttf");
		final Typeface med = Typeface.createFromAsset(this.getAssets(),
				"fonts/Roboto-Light.ttf");
		Typeface bold_italic = Typeface.createFromAsset(this.getAssets(),
				"fonts/Roboto-BoldItalic.ttf");
		displaytextview1.setTypeface(tf);
		TextView cows = (TextView) findViewById(R.id.cows);
		cows.setTypeface(bold_italic);
		TextView bulls = (TextView) findViewById(R.id.bulls);
		bulls.setTypeface(bold_italic);
		displaytextview1.setText("Length: " + n + " letters. Best of luck!");
		Button guess = (Button) findViewById(R.id.guess);
		final TextView chances = (TextView) findViewById(R.id.enter_guess);
		chances.setTypeface(med);
		final Chronometer chrono = (Chronometer) findViewById(R.id.chronometer1);
		y = 10 - x;
		chances.setText("You have " + y + " chances remaining.");
		guess.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				s_button.start();
				x++;
				y = (10 - x);
				int n2 = n;
				char[] word2 = word.toCharArray();
				TextView nbulls = (TextView) findViewById(R.id.nbulls);
				TextView ncows = (TextView) findViewById(R.id.ncows);
				EditText Input = (EditText) findViewById(R.id.input_guess);
				nbulls.setTypeface(med);
				ncows.setTypeface(med);
				String input = Input.getText().toString();
				String input_2 = input.toUpperCase(Locale.US);
				input_2 = input_2.trim();
				int good = checkword(input_2);
				if (good == 0) {
					int n1 = input_2.length();
					if (!(n1 == n2)) {
						x--;
						AlertDialog.Builder al = new AlertDialog.Builder(
								Cvpgame.this);
						al.setTitle("RULE VIOLATION");
						al.setIcon(R.drawable.ic_launcher);
						al.setMessage("The word should be of " + n
								+ " characters");
						al.setNeutralButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						AlertDialog al1 = al.create();
						al1.show();
					} else {
						char[] b = input_2.toCharArray();
						int flag = 0;
						for (int i = 0; i < n; i++) { // Checking if the letters
														// are
							// repeated.
							for (int j = 0; j < n; j++) {
								if (i == j)
									continue;
								if (b[i] == b[j] && flag == 0)
									flag = 1;
							}
						}
						if (flag == 1) {
							x--;
							AlertDialog.Builder al = new AlertDialog.Builder(
									Cvpgame.this);
							al.setTitle("RULE VIOLATION");
							al.setIcon(R.drawable.ic_launcher);
							al.setMessage("There should be no repitions of the letters in the word.");
							al.setNeutralButton(R.string.ok,
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.cancel();
										}
									});
							AlertDialog al1 = al.create();
							al1.show();
						} else {
							int flagb = 0;
							int flagc = 0;
							for (int i = 0; i < n1; i++) { // Counting cows and
															// bulls.
								if (word2[i] == b[i]) {
									flagb++;
									continue;
								}
								for (int j = 0; j < n1; j++) {
									if (word2[j] == b[i])
										flagc++;
								}
							}
							guesses[countg] = countg + ". " + new String(b)
									+ "- " + flagb + " Bulls and " + flagc
									+ " Cows.";
							countg++;
							nbulls.setText(String.valueOf(flagb) + "  -  ");
							ncows.setText(String.valueOf(flagc) + "  -  ");
							Input.setText("");
							if (flagb == n1) { // User wins
								MediaPlayer applause = MediaPlayer.create(
										Cvpgame.this, R.raw.applause);
								AlertDialog.Builder builder1 = new AlertDialog.Builder(
										Cvpgame.this);
								builder1.setTitle("Won!");
								builder1.setMessage("Congratulations! You've won the game with "
										+ y + " chances remaining!");
								builder1.setCancelable(false);
								builder1.setIcon(R.drawable.ic_launcher);
								builder1.setNeutralButton(R.string.ok,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();
												finish();
												clearP();
											}
										});
								AlertDialog alert11 = builder1.create();
								alert11.show();
								applause.start();
								chrono.stop();
							} else if (y == 0) {
								MediaPlayer boo = MediaPlayer.create(
										Cvpgame.this, R.raw.boo);
								AlertDialog.Builder al = new AlertDialog.Builder(
										Cvpgame.this);
								al.setMessage("Game Over! You lost the game. The word was "
										+ word + ". CPU wins!!");
								al.setTitle("Game Over!");
								al.setCancelable(false);
								al.setIcon(R.drawable.ic_launcher);
								al.setNeutralButton(R.string.ok,
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
												finish();
												clearP();
											}
										});
								AlertDialog al1 = al.create();
								boo.start();
								al1.show();
								chrono.stop();
							}
							chances.setText("You have " + y
									+ " chances remaining.");
							if (a == 0)
								saveP();
							chrono.start();
						}
					}
				} else {
					x--;
					AlertDialog.Builder al = new AlertDialog.Builder(
							Cvpgame.this);
					al.setMessage("Word does not exist in dictionary.");
					al.setTitle("Invalid word");
					al.setIcon(R.drawable.ic_launcher);
					al.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					AlertDialog al1 = al.create();
					al1.show();
				}
			}
		});
	}

	public int checkword(String word) {
		try {
			Resources res = getResources();
			InputStream in = res.openRawResource(R.raw.dictionary);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = br.readLine();
			while (line != null) {
				if (line.equalsIgnoreCase(word)) {
					return 0;
				}
				line = br.readLine();
			}
			in.close();
		} catch (IOException e) {
		}
		return 1;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.history:
			Intent pass_guess = new Intent("com.dhobs.cowsbulls.HISTORY");
			Bundle bun = new Bundle();
			bun.putStringArray("guess", guesses);
			pass_guess.putExtras(bun);
			startActivity(pass_guess);
			return true;
		case R.id.mark:
			Intent m = new Intent("com.dhobs.cowsbulls.MARK");
			Bundle mark = new Bundle();
			mark.putInt("y1", y1);
			mark.putInt("y2", y2);
			mark.putInt("y3", y3);
			mark.putInt("y4", y4);
			mark.putInt("y5", y5);
			mark.putInt("no", n);
			mark.putIntArray("val", val);
			m.putExtras(mark);
			startActivityForResult(m, 1);
			return true;
		case R.id.save:
			saveP();
			Toast t = Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT);
			t.show();

		default:
			return super.onOptionsItemSelected(item);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		y1 = data.getExtras().getInt("y1");
		y2 = data.getExtras().getInt("y2");
		y3 = data.getExtras().getInt("y3");
		y4 = data.getExtras().getInt("y4");
		y5 = data.getExtras().getInt("y5");
		val = data.getExtras().getIntArray("val");
	}

	long lastPress;

	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastPress > 4000) {
			Toast.makeText(getBaseContext(), "Press back again to exit",
					Toast.LENGTH_LONG).show();
			lastPress = currentTime;
		} else {
			if (a != 0) {
				AlertDialog.Builder al = new AlertDialog.Builder(Cvpgame.this);
				al.setMessage("Do you want to save your progress?");
				al.setTitle("Save Game?");
				al.setCancelable(true);
				al.setIcon(R.drawable.ic_launcher);
				al.setPositiveButton("Save and Exit",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								saveP();
								dialog.dismiss();
								finish();
							}
						});
				al.setNegativeButton("Exit without saving",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								clearP();
								finish();
							}
						});
				AlertDialog al1 = al.create();
				al1.show();
			} else {
				Toast save = Toast.makeText(this, "Game saved",
						Toast.LENGTH_LONG);
				save.show();
				finish();
			}
		}
	}

	private void loadBackground() {
		SharedPreferences back = PreferenceManager
				.getDefaultSharedPreferences(this);
		b = back.getInt("b", 0);
		a = back.getInt("auto", 0);
	}

	private void loadP() {
		SharedPreferences sp = this.getSharedPreferences("SaveGame",
				Context.MODE_PRIVATE);
		countg = sp.getInt("countg", 1);
		for (int i = 1; i <= countg; i++) {
			guesses[i] = sp.getString(String.valueOf(i), "");
		}
		for (int i = 0; i < 26; i++) {
			val[i] = sp.getInt("val" + String.valueOf(i), 0);
		}
		y1 = sp.getInt("y1", 0);
		y2 = sp.getInt("y2", 0);
		y3 = sp.getInt("y3", 0);
		y4 = sp.getInt("y4", 0);
		y5 = sp.getInt("y5", 0);
		x = sp.getInt("x", 0);
		y = sp.getInt("y", 0);
		b = sp.getInt("b", 0);
		wor = sp.getString("W", wore);

	}

	private void saveP() {
		SharedPreferences sp = this.getSharedPreferences("SaveGame",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		for (int i = 1; i < countg; i++) {
			e.putString(String.valueOf(i), guesses[i]);
		}
		for (int i = 0; i < 26; i++) {
			e.putInt("val" + String.valueOf(i), val[i]);
		}
		e.putInt("y1", y1);
		e.putInt("y2", y2);
		e.putInt("y3", y3);
		e.putInt("y4", y4);
		e.putInt("y5", y5);
		e.putInt("x", x);
		e.putInt("y", y);
		e.putInt("countg", countg);
		e.putString("W", wor);
		String date = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
				.format(new Date());
		e.putString("date", date);
		e.commit();
	}

	private void clearP() {
		SharedPreferences sp = this.getSharedPreferences("SaveGame",
				Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.clear();
		e.commit();
	}
}
