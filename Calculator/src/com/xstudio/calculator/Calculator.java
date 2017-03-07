package com.xstudio.calculator;

import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.string;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * Title: Calculator.java<／p>
 * <p>
 * Description: <／p>
 * <p>
 * Copyright: Copyright (c) 2014<／p>
 * 
 * @author Kevin Xu
 * @date Jan 7, 2014
 * @version 1.6
 */
//Simple GUI designed to model a calculator
public class Calculator extends Activity implements OnClickListener {
	//declarations
	private TextView tv_show; //text display
	private Button btn_clear; //clear text field button
	private StringBuffer str_show = new StringBuffer(""); //string buffer for text display
	private BigDecimal num1, num2; //numbers (user inputs) to perform operations on
	private boolean flag_dot = true;
	private boolean flag_num1 = false;
	private String str_oper = null; //operator
	private String str_result = null; //result
	private int scale = 2; //determines size of GUI
	private boolean isScaleChanged = false;
	private boolean flag_minus = false;

	//set size of GUI
	public void setScale(int scale) {
		this.scale = scale;
	}

	@Override
	//upon creating Calculator
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	//init method. No return value
	private void initView() {
		tv_show = (TextView) findViewById(R.id.tv_show);
		findViewById(R.id.btn0).setOnClickListener(this);
		findViewById(R.id.btn1).setOnClickListener(this);
		findViewById(R.id.btn2).setOnClickListener(this);
		findViewById(R.id.btn3).setOnClickListener(this);
		findViewById(R.id.btn4).setOnClickListener(this);
		findViewById(R.id.btn5).setOnClickListener(this);
		findViewById(R.id.btn6).setOnClickListener(this);
		findViewById(R.id.btn7).setOnClickListener(this);
		findViewById(R.id.btn8).setOnClickListener(this);
		findViewById(R.id.btn9).setOnClickListener(this);
		findViewById(R.id.btn_div).setOnClickListener(this);
		findViewById(R.id.btn_add).setOnClickListener(this);
		findViewById(R.id.btn_mul).setOnClickListener(this);
		findViewById(R.id.btn_equal).setOnClickListener(this);
		findViewById(R.id.btn_point).setOnClickListener(this);
		findViewById(R.id.btn_sub).setOnClickListener(this);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		btn_clear.setOnClickListener(this);
		btn_clear.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// flag_longClick = true;
				tv_show.setText("");
				str_show = new StringBuffer("");
				flag_dot = true;
				flag_num1 = false;
				flag_minus = false;
				str_oper = null;
				return true;
			}
		});
	}

	@Override
	//controls input buttons. When buttons are pressed. No return value
	public void onClick(View v) {
		Button btn = (Button) v;
		switch (v.getId()) {
		case R.id.btn_point:
			if (str_show.toString() == "") {
				break;
			} else if (flag_dot) {
				str_show.append(".");
				showInTextView(str_show.toString());
				flag_dot = false;
			}
			break;
		//what occurs upon pressing the clear button
		case R.id.btn_clear:
			if (!(str_show.toString() == "")) {
				if (!flag_dot) {
					String lastStr = String.valueOf(str_show.charAt(str_show
							.length() - 1));
					if (lastStr.equals(".")) {
						flag_dot = true;
					}
				}
				str_show.deleteCharAt(str_show.length() - 1);
				if(str_show.toString().equals("")){
					flag_minus = false;
				}
				showInTextView(str_show.toString());
			} else {
				showInTextView("");
				str_result = null;
				str_show = new StringBuffer("");
				flag_dot = true;
				flag_minus = false;
			}
			flag_num1 = false;
			break;
		case R.id.btn_add:
			setNum1(btn.getText().toString());
			break;
		case R.id.btn_sub:
			if (!flag_minus) {
				if (str_show.toString().equals("")) {
					str_show.append("-");
					showInTextView(str_show.toString());
					flag_minus = true;
					break;
				}
			}
			setNum1(btn.getText().toString());
			break;
		case R.id.btn_mul:
			setNum1(btn.getText().toString());
			break;
		case R.id.btn_div:
			setNum1(btn.getText().toString());
			break;
		case R.id.btn_equal:
			if (str_oper == null || str_show.toString().equals("")
					|| !flag_num1)
				break;
			calculate();
			break;
		default:
			str_show.append(btn.getText().toString());
			showInTextView(str_show.toString());
			break;
		}
	}
	//operator control. No return value
	private void setNum1(String oper) {
		if (str_oper != null && !str_show.toString().equals("") && flag_num1) {
			calculate();
		}
		str_oper = oper;
		if (!(str_show.toString() == "") && !str_show.toString().equals("-")) {
			num1 = new BigDecimal(str_show.toString());
			showInTextView(str_show.toString());
			str_show = new StringBuffer("");
			str_result = null;
			flag_num1 = true;
			flag_minus = false;
		} else if (str_result != null) {
			num1 = new BigDecimal(str_result);
			showInTextView(str_result);
			str_result = null;
			flag_num1 = true;
			flag_minus = false;
		}
		flag_dot = true;
	}

	/*handles calculations. Gets string operator from input and performs calculations based on them.
	No return value.*/
	private void calculate() {
		if(str_show.toString().equals("-")) return;
		double result = 0;
		num2 = new BigDecimal(str_show.toString());
		//addition
		if (str_oper.equals("+")) {
			result = Calculate.add(num1, num2);
		}
		//subtraction
		if (str_oper.equals("-")) {
			result = Calculate.sub(num1, num2);
		}
		//multiplication
		if (str_oper.equals("*")) {
			result = Calculate.mul(num1, num2);
		}
		//division
		if (str_oper.equals("/")) {
			if (!num2.equals(BigDecimal.ZERO)) {
				result = Calculate.div(num1, num2, scale);
			} else { //error handling
				Toast.makeText(Calculator.this, "除数不能为零！", Toast.LENGTH_LONG)
						.show();
				showInTextView("");
				str_show = new StringBuffer("");
				str_oper = null;
				flag_num1 = false;
				flag_dot = true;
				return;
			}
		}
		//convert result to string and stores it in str_result
		str_result = String.valueOf(Calculate.round(result, scale));
		String[] resultStrings = str_result.split("\\.");
		if (resultStrings[1].equals("0")) {
			str_result = resultStrings[0];
		}
		
		//displays str_result in text box
		showInTextView(str_result);
		str_show = new StringBuffer("");
		flag_dot = true;
		flag_num1 = false;
		str_oper = null;
		flag_minus = true;
	}

	//sets tv_show as input
	private void showInTextView(String str) {
		tv_show.setText(str);
	}

	@Override
	//animations for opening Options Menu. Returns superclass menu.
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	//animations for when item is selected. Returns superclass selected item.
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_set_precision:
			openScaleSelectDialog();
			return true;
		case R.id.action_about:
			openAboutPage();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	//controls opening of about page. No Return value
	private void openAboutPage() {
		Intent intent = new Intent(Calculator.this, AboutPage.class);
		startActivity(intent);
	}

	//controls size of calculator. No return value
	private void openScaleSelectDialog() {
		//default size of calculator
		NumberPicker precisionPicker = new NumberPicker(this);
		precisionPicker.setMaxValue(20);
		precisionPicker.setMinValue(1);
		precisionPicker.setValue(scale);
		precisionPicker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			//upon changing size of calculator
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				isScaleChanged = true;
				scale = newVal;
			}
		});
		Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setView(precisionPicker);
		alertDialog.setTitle(R.string.action_set_precision);
		alertDialog.setPositiveButton(R.string.confirm,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (isScaleChanged)
							setScale(scale);
					}
				});
		//upon selecting negative button
		alertDialog.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog.show();
	}

}
