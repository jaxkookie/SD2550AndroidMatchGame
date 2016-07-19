package com.example.kookiesmemorygame;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.view.View.OnClickListener;
import android.view.Gravity;
import android.view.View;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Game extends Activity {
	private static int r_count = -1;
	private static int c_count = -1;
	private Context context;
	private Drawable backImage, bI2, bI3, bI4, bI5, bI6;
	private TextView textView, textView01, textView02, textView03, textView04, textView05, textView06;
	private int[][] cards;
	private List<Drawable> images;
	private Card c1, c2;
	private RadioGroup radioGroup;
	private RadioButton guest, user;

	private ButtonListener buttonListener;
	private Button b2, b3, b4, b5, b6, b7, b8, b9;
	private static Object lock = new Object();
	private String gst = "guest";
	int turn, match, gamecount, r = 0;

	private TableLayout mainTable;
	private View mViewGroup,mViewGroup08,mViewGroup09,mViewGroup10,mViewGroup11,mViewGroup12;
	private String str, outPut, d, ghs, hs = "";

	private UpdateCardsHandler handler;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	MediaPlayer error, shuffle, dong, cardchange, gowin, golose, success, badchoicebuzz, level, eb, noscore, hiscore,
			lost, menu, lightspeed;

	private Highscore highscore;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		handler = new UpdateCardsHandler();
		loadImages();
		setContentView(R.layout.main);

		error = MediaPlayer.create(this, R.raw.error);
		shuffle = MediaPlayer.create(this, R.raw.shuffle);
		dong = MediaPlayer.create(this, R.raw.dong);
		cardchange = MediaPlayer.create(this, R.raw.cardchange);
		badchoicebuzz = MediaPlayer.create(this, R.raw.badchoicebuzz);
		gowin = MediaPlayer.create(this, R.raw.timelift);
		level = MediaPlayer.create(this, R.raw.levelup);
		eb = MediaPlayer.create(this, R.raw.electrobase1);
		noscore = MediaPlayer.create(this, R.raw.noscore);
		hiscore = MediaPlayer.create(this, R.raw.highscore);
		lost = MediaPlayer.create(this, R.raw.battleislost);
		menu = MediaPlayer.create(this, R.raw.openclosemenu);
		lightspeed = MediaPlayer.create(this, R.raw.lightspeed);
		success = MediaPlayer.create(this, R.raw.success);

		gst += randInt(1000, 10000);

		// create a new Instance
		// "this" is the Context
		highscore = new Highscore(this);

		b2 = (Button) findViewById(R.id.button02);
		b2.setVisibility(1);
		b3 = (Button) findViewById(R.id.button03);
		b3.setVisibility(1);
		b4 = (Button) findViewById(R.id.button04);
		b4.setVisibility(1);
		b5 = (Button) findViewById(R.id.button05);
		b5.setVisibility(1);
		b6 = (Button) findViewById(R.id.button06);
		b6.setVisibility(1);
		b7 = (Button) findViewById(R.id.button07);
		b7.setVisibility(1);
		b8 = (Button) findViewById(R.id.button08);
		b8.setVisibility(1);

		addListenerOnButton2();
		addListenerOnButton3();
		addListenerOnButton4();
		addListenerOnButton5();
		addListenerOnButton6();
		addListenerOnButton7();
		addListenerOnButton8();

		bI2 = ContextCompat.getDrawable(this, R.drawable.cb2);
		bI3 = ContextCompat.getDrawable(this, R.drawable.cb3);
		bI4 = ContextCompat.getDrawable(this, R.drawable.cb4);
		bI5 = ContextCompat.getDrawable(this, R.drawable.cb5);
		bI6 = ContextCompat.getDrawable(this, R.drawable.cb6);

		backImage = bI4;
		textView = ((TextView) findViewById(R.id.deck_back));
		textView01 = ((TextView) findViewById(R.id.tv01));
		textView02 = ((TextView) findViewById(R.id.tv02));
		textView03 = ((TextView) findViewById(R.id.tv03));
		textView04 = ((TextView) findViewById(R.id.tv04));
		textView05 = ((TextView) findViewById(R.id.tv05));
		textView06 = ((TextView) findViewById(R.id.tv06));
		textView.setText(getResources().getString(R.string.deck_back));

		buttonListener = new ButtonListener();

		mainTable = (TableLayout) findViewById(R.id.tl03);

		mViewGroup08 = (View) findViewById(R.id.tr08);
		
		mViewGroup09 = (View) findViewById(R.id.tr09);
		mViewGroup10 = (View) findViewById(R.id.tr10);
		mViewGroup11 = (View) findViewById(R.id.tr11);
		mViewGroup12 = (View) findViewById(R.id.tr12);

		mViewGroup08.setVisibility(View.INVISIBLE);
		mViewGroup09.setVisibility(View.GONE);
		mViewGroup10.setVisibility(View.GONE);
		mViewGroup11.setVisibility(View.GONE);
		mViewGroup12.setVisibility(View.GONE);

		context = mainTable.getContext();
		// would like to add choice username input or play as guest
		// then use the choice in highscores
		radioGroup = (RadioGroup) findViewById(R.id.myRadioGroup);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// find which radio button is selected
				if (checkedId == R.id.guest) {
					Toast.makeText(getApplicationContext(), "choice: Guest", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "choice: Username", Toast.LENGTH_SHORT).show();
				}
			}
		});
		guest = (RadioButton) findViewById(R.id.guest);
		user = (RadioButton) findViewById(R.id.username);
		
		 b9 = (Button)findViewById(R.id.chooseBtn);
		    b9.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int selectedId = radioGroup.getCheckedRadioButtonId();
					
					// find which radioButton is checked by id
					if(selectedId == guest.getId()) {
						//textView.setText("You chose 'guest' option");
					} else if(selectedId == user.getId()) {
						//textView.setText("You chose 'user' option");
					} else {
						//textView.setText("Make a choice");
					}	
				}
		    });

		Spinner s = (Spinner) findViewById(R.id.spinner01);
		ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.grid,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s.setAdapter(adapter);

		s.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(

					android.widget.AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// sound
				menu.start();
				((Spinner) findViewById(R.id.spinner01)).setSelection(0);

				int x, y;

				switch (pos) {
				// need to add a reset but this here just loops
				// consider adding case 7: as reset option
				//case 0:
					//setContentView(R.layout.main);
					//Intent intent = getIntent();
					//finish();
					//startActivity(intent);
					//break;
				
				case 1:
					x = 2;
					y = 2;
					//newGame(x, y);
					break;
				case 2:
					x = 3;
					y = 4;
					//newGame(x, y);
					break;
				case 3:
					x = 4;
					y = 4;
					//newGame(x, y);
					break;
				case 4:
					x = 4;
					y = 5;
					//newGame(x, y);
					break;
				case 5:
					x = 4;
					y = 6;
					//newGame(x, y);
				case 6:
					x = 5;
					y = 6;
					//newGame(x, y);
					break;
				default:
					return;
				}
				newGame(x, y);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		Spinner s2 = (Spinner) findViewById(R.id.spinner02);
		ArrayAdapter<?> adapter2 = ArrayAdapter.createFromResource(this, R.array.background,
				android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		s2.setAdapter(adapter2);

		s2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(android.widget.AdapterView<?> arg0, View arg1, int pos, long arg3) {
				// sound
				menu.start();
				((Spinner) findViewById(R.id.spinner02)).setSelection(0);

				switch (pos) {
				case 1:
					mainTable.setBackgroundResource(R.drawable.bggrayo70);
					break;
				case 2:
					mainTable.setBackgroundResource(R.drawable.bgblueo70);
					break;
				case 3:
					mainTable.setBackgroundResource(R.drawable.bgredo70);
					break;
				case 4:
					mainTable.setBackgroundResource(R.drawable.bgtealo70);
					break;
				case 5:
					mainTable.setBackgroundResource(R.drawable.bgorangeo70);
					break;
				case 6:
					mainTable.setBackgroundResource(R.drawable.bgpurpleo70);
					break;
				case 7:
					mainTable.setBackgroundResource(R.drawable.bggreeno70);
					break;
				default:

					return;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		/*
		 * countDownTimer = new CountDownTimer(startTime, interval) {
		 * 
		 * public void onTick(long millisUntilFinished) {
		 * 
		 * //((TextView)findViewById(R.id.tv05)).setText("Time remaining: " +
		 * millisUntilFinished / 1000); }
		 * 
		 * public void onFinish() {
		 * //((TextView)findViewById(R.id.tv05)).setText("done!"); } };
		 */
		// set initial bkgrnd color
		mainTable.setBackgroundResource(R.drawable.bggrayo70);

	}

	public void addListenerOnButton2() {
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				backImage = bI2;
				b3.setVisibility(View.GONE);
				b4.setVisibility(View.GONE);
				b5.setVisibility(View.GONE);
				b6.setVisibility(View.GONE);
				textView.setText(getResources().getString(R.string.deck_c2));
				// Log.i("backImage 2",backImage + " - " + bI2);
				dong.start();

			}
		});
	}

	public void addListenerOnButton3() {
		b3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				backImage = bI3;
				b2.setVisibility(View.GONE);
				b4.setVisibility(View.GONE);
				b5.setVisibility(View.GONE);
				b6.setVisibility(View.GONE);
				textView.setText(getResources().getString(R.string.deck_c3));
				// Log.i("backImage 3",backImage + " - " + bI3);
				dong.start();
			}
		});
	}

	public void addListenerOnButton4() {
		b4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				backImage = bI4;
				b2.setVisibility(View.GONE);
				b3.setVisibility(View.GONE);
				b5.setVisibility(View.GONE);
				b6.setVisibility(View.GONE);
				textView.setText(getResources().getString(R.string.deck_c4));
				// Log.i("backImage 4",backImage + " - " + bI4);
				dong.start();
			}
		});
	}

	public void addListenerOnButton5() {
		b5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				backImage = bI5;
				b2.setVisibility(View.GONE);
				b3.setVisibility(View.GONE);
				b4.setVisibility(View.GONE);
				b6.setVisibility(View.GONE);
				textView.setText(getResources().getString(R.string.deck_c5));
				// Log.i("backImage 5",backImage + " - " + bI5);
				dong.start();
			}
		});
	}

	public void addListenerOnButton6() {
		b6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				backImage = bI6;
				b2.setVisibility(View.GONE);
				b3.setVisibility(View.GONE);
				b4.setVisibility(View.GONE);
				b5.setVisibility(View.GONE);
				textView.setText(getResources().getString(R.string.deck_c6));
				// Log.i("backImage 6",backImage + " - " + bI6);
				dong.start();
			}
		});
	}

	public void addListenerOnButton7() {
		b7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// Open Menu new game board
				// ((Spinner) findViewById(R.id.spinner01)).performClick();
				// new game same board
				lightspeed.stop();
				success.start();
				newGame(r_count, c_count);
			}
		});
	}

	public void addListenerOnButton8() {
		b8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// clear highscores
				highscore.clearHS(context);

				// Log.i("highscore clearHS","HERE");
				// set textview blank
				textView06.setText(" ");
				// get high scores and write to textview
				ghs = getHighscores();
				textView06.setText(ghs);
			}
		});
	}

	public String getHighscores() {
		// build string for all highscores
		hs = "";
		hs += Html.fromHtml(getResources().getString(R.string.hs_label));

		for (int x = 0; x < 10; x++) {
			r = (x + 1);
			if (x == 9) {
				hs += r;
			} else {
				hs += "  " + r;
			}
			hs += ". ";
			hs += Html.fromHtml(highscore.getName(x));
			hs += " - ";
			hs += Html.fromHtml(highscore.getDt(x));
			hs += " - ";
			hs += Html.fromHtml(highscore.getScore(x) + "<br/>");

		}
		return hs;
	}

	private void newGame(int c, int r) {
		// sound
		shuffle.start();
		r_count = r;
		c_count = c;

		cards = new int[c_count][r_count];
		// setting up views for layout
		((TableRow) findViewById(R.id.tr03)).setVisibility(View.VISIBLE);
		mViewGroup08.setVisibility(View.INVISIBLE);
		mainTable.removeView(findViewById(R.id.tr01));
		mainTable.removeView(findViewById(R.id.tr02));
		mainTable.removeView(findViewById(R.id.tr06));
		mainTable.removeView(findViewById(R.id.tr07));
		
		mainTable.removeView(findViewById(R.id.tr09));
		mainTable.removeView(findViewById(R.id.tr10));
		mainTable.removeView(findViewById(R.id.tr11));
		mainTable.removeView(findViewById(R.id.tr12));

		TableRow tr = ((TableRow) findViewById(R.id.tr03));
		tr.removeAllViews();

		mainTable = new TableLayout(context);
		tr.addView(mainTable);

		for (int y = 0; y < r_count; y++) {
			mainTable.addView(createRow(y));
		}

		c1 = null;
		loadCards();

		turn = 0;
		match = 0;
		gamecount = (r_count * c_count) / 2;
		textView01.setText("Attempts: " + turn);
		textView02.setText("Matches: " + match);
		textView03.setText("Max: " + gamecount);

	}

	private void loadImages() {
		images = new ArrayList<Drawable>();

		// images.add(getResources().getDrawable(R.drawable.c1));
		images.add(ContextCompat.getDrawable(this, R.drawable.c1));
		images.add(ContextCompat.getDrawable(this, R.drawable.c2));
		images.add(ContextCompat.getDrawable(this, R.drawable.c3));
		images.add(ContextCompat.getDrawable(this, R.drawable.c4));
		images.add(ContextCompat.getDrawable(this, R.drawable.c5));
		images.add(ContextCompat.getDrawable(this, R.drawable.c6));
		images.add(ContextCompat.getDrawable(this, R.drawable.c7));
		images.add(ContextCompat.getDrawable(this, R.drawable.c8));
		images.add(ContextCompat.getDrawable(this, R.drawable.c9));
		images.add(ContextCompat.getDrawable(this, R.drawable.c10));
		images.add(ContextCompat.getDrawable(this, R.drawable.c11));
		images.add(ContextCompat.getDrawable(this, R.drawable.c12));
		images.add(ContextCompat.getDrawable(this, R.drawable.c13));
		images.add(ContextCompat.getDrawable(this, R.drawable.c14));
		images.add(ContextCompat.getDrawable(this, R.drawable.c15));
		images.add(ContextCompat.getDrawable(this, R.drawable.c16));
		images.add(ContextCompat.getDrawable(this, R.drawable.c17));
		images.add(ContextCompat.getDrawable(this, R.drawable.c18));
		images.add(ContextCompat.getDrawable(this, R.drawable.c19));
		images.add(ContextCompat.getDrawable(this, R.drawable.c20));
		images.add(ContextCompat.getDrawable(this, R.drawable.c21));

	}

	private void loadCards() {
		try {
			int size = r_count * c_count;

			// Log.i("loadCards","size= " + size);

			ArrayList<Integer> list = new ArrayList<Integer>();

			for (int i = 0; i < size; i++) {
				list.add(new Integer(i));
			}

			Random r = new Random();

			for (int i = size - 1; i >= 0; i--) {
				int t = 0;

				if (i > 0) {
					t = r.nextInt(i);
				}

				t = list.remove(t).intValue();
				cards[i % c_count][i / c_count] = t % (size / 2);

				// Log.i("loadCards()", "card["+(i%c_count)+
				// "]["+(i/c_count)+"]=" + cards[i%c_count][i/c_count]);
			}
		} catch (Exception e) {
			Log.e("loadCards", e + "");
		}

	}

	private TableRow createRow(int y) {
		TableRow row = new TableRow(context);
		row.setHorizontalGravity(Gravity.CENTER);

		for (int x = 0; x < c_count; x++) {
			row.addView(createImageButton(x, y));
		}
		return row;
	}

	private View createImageButton(int x, int y) {
		Button button = new Button(context);
		button.setBackground(backImage);
		button.setId(100 * x + y);
		button.setOnClickListener(buttonListener);
		return button;
	}

	class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			synchronized (lock) {
				if (c1 != null && c2 != null) {
					return;
				}
				int id = v.getId();
				int x = id / 100;
				int y = id % 100;

				turnCard((Button) v, x, y);
				//Log.i("turncard()", "c");

			}

		}

		private void turnCard(Button button, int x, int y) {
			// dong.start();
			button.setBackground(images.get(cards[x][y]));

			if (c1 == null) {
				c1 = new Card(button, x, y);
			} else {

				if (c1.x == x && c1.y == y) {
					return; // the user pressed the same card
				}

				c2 = new Card(button, x, y);

				turn++;

				textView01.setText("Attempts: " + turn);

				TimerTask tt = new TimerTask() {

					@Override
					public void run() {
						try {
							synchronized (lock) {
								handler.sendEmptyMessage(0);
							}
						} catch (Exception e) {
							Log.e("E1", e.getMessage());
						}
					}
				};

				Timer t = new Timer(false);
				t.schedule(tt, 1300);
			}

		}

	}

	class UpdateCardsHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			synchronized (lock) {
				checkCards();
			}
		}

		public void checkCards() {

			Date now = new Date();

			DateFormat sdf;
			sdf = new SimpleDateFormat("EEE MMM dd HH:mm yyyy");
			d = sdf.format(now);

			if (cards[c2.x][c2.y] == cards[c1.x][c1.y]) {
				// dong.start();
				c1.button.setVisibility(View.INVISIBLE);
				c2.button.setVisibility(View.INVISIBLE);

				match++;
				level.start();
				gamecount = (r_count * c_count) / 2;
				// if(match == size)
				textView02.setText("Matches: " + match);
				textView03.setText("Max: " + gamecount);

				if ((match > 1) && (match == gamecount)) {
					mViewGroup08.setVisibility(View.VISIBLE);
					// mViewGroup09.setVisibility(View.VISIBLE);
					long s = (100 * gamecount) / turn;

					// add a Score to the Highscore-List
					boolean m = highscore.addScore(gst, d, s);

					str = getResources().getString(R.string.go_str);
					if (s == 100)
						outPut = getResources().getString(R.string.go_lvl1);
					else if ((s <= 99) && (s >= 90))
						outPut = getResources().getString(R.string.go_lvl2);
					else if ((s <= 89) && (s >= 80))
						outPut = getResources().getString(R.string.go_lvl3);
					else if ((s <= 79) && (s >= 70))
						outPut = getResources().getString(R.string.go_lvl4);
					else if ((s <= 69) && (s >= 60))
						outPut = getResources().getString(R.string.go_lvl5);
					else
						outPut = getResources().getString(R.string.go_lvl6);

					((TableRow) findViewById(R.id.tr03)).setVisibility(View.GONE);
					textView04.setText(Html.fromHtml(str));
					textView05.setText(
							Html.fromHtml(outPut + "<br/><br/>Accuracy: " + String.valueOf(s) + "%" + "<br/>"));

					if (m == false) {
						// did not make HS list
						Toast.makeText(context, getResources().getString(R.string.not_leader), Toast.LENGTH_LONG)
								.show();
						noscore.start();
					} else {
						// sounds on complete and making the HS list
						hiscore.start();
						lightspeed.start();
					}
					textView06.setText(getHighscores());

				} else {

				}
			} else {
				badchoicebuzz.start();
				c2.button.setBackground(backImage);
				c1.button.setBackground(backImage);
			}

			c1 = null;
			c2 = null;
		}

	}

	
	
	public static int randInt(int min, int max) {

		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

}
