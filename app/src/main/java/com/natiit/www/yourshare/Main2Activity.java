package com.natiit.www.yourshare;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.round;

public class Main2Activity extends AppCompatActivity {

    private Button calculate;
    private EditText orderinput;
    private TextInputLayout orderlayout;
    private ArrayList<TextInputLayout> inputs = new ArrayList();
    private LinearLayout newinputlayout;
    private LinearLayout item;
    private TextView result;
    private Spinner vat;
    private Spinner serviceCharge;
    private int count =1;
    private int theme;
    public boolean isFirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences setting = getSharedPreferences("YourShare",MODE_PRIVATE);
        theme = setting.getInt("theme",R.style.AppTheme);
        setTheme(theme);

        Bitmap bm = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_custom_round);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme =getTheme();
        theme.resolveAttribute(R.attr.colorPrimary,typedValue,true);
        int color = typedValue.data;
        ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(getString(R.string.app_name),bm,color);
        setTaskDescription(taskDescription);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences getSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

                //  Check either activity or app is open very first time or not and do action
                if (isFirstStart) {

                    //  Launch application introduction screen
                    Intent i = new Intent(Main2Activity.this, MyIntro.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getSharedPreferences.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }
            }
        });
        thread.start();

        calculate = (Button)findViewById(R.id.calculate);
        orderinput = (EditText)findViewById(R.id.order);
        orderlayout = (TextInputLayout)findViewById(R.id.orderlayout);
        newinputlayout = (LinearLayout)findViewById(R.id.newInputLinerLayout);
        vat = (Spinner)findViewById(R.id.vat);
        serviceCharge = (Spinner)findViewById(R.id.servicecharge);
        result = (TextView)findViewById(R.id.result);

        orderinput.addTextChangedListener(new Main2Activity.MyTextWatcher(orderinput,orderlayout));

        inputs.add(orderlayout);

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateShare(inputs);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences setting = getSharedPreferences("YourShare",MODE_PRIVATE);
        theme = setting.getInt("theme",R.style.AppTheme);
        setTheme(theme);
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences setting = getSharedPreferences("YourShare",MODE_PRIVATE);
        SharedPreferences.Editor e = setting.edit();
        e.putInt("theme",theme);
        e.commit();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater i =getMenuInflater();
        i.inflate(R.menu.night_mode_cheack_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nightMode:
                changeTheme();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void calculateShare(ArrayList<TextInputLayout> txtlayout) {
        for (int i=0;i<txtlayout.size();i++){
            if(!validateInputs(txtlayout.get(i))){
                return;
            }
        }

        String selectedvat = vat.getSelectedItem().toString();
        String selectedServiceCharge = serviceCharge.getSelectedItem().toString();
        String outputResult = "|---- Item ----- Price - S.C --- VAT --- UnitTotal \n|_________________________________________\n";
        Double subTotal = 0.0;
        Double vatRate=0.0;
        Double servicechargeRate=0.0;

        switch (selectedvat){
            case "0 %":
                vatRate=0.0;
                break;
            case "5 %":
                vatRate=5.0/100.0;
                break;
            case "10 %":
                vatRate=10.0/100.0;
                break;
            case "15 %":
                vatRate=15.0/100.0;
                break;
        }

        switch (selectedServiceCharge){
            case "0 %":
                servicechargeRate=0.0;
                break;
            case "3 %":
                servicechargeRate=3.0/100.0;
                break;
            case "5 %":
                servicechargeRate=5.0/100.0;
                break;
            case "10 %":
                servicechargeRate=10.0/100.0;
                break;
        }

        for (int i=0;i<txtlayout.size();i++){
            String lable = "Order "+(i+1);
            String price = txtlayout.get(i).getEditText().getText().toString();
            outputResult=outputResult+"| * "+lable+" --- "+price+" --- "+(Double.parseDouble(price)*servicechargeRate)+" --- "+((Double.parseDouble(price)+Double.parseDouble(price)*servicechargeRate)*vatRate)+" ---> "+(String.format("%.2f",((Double.parseDouble(price)+Double.parseDouble(price)*servicechargeRate)+((Double.parseDouble(price)+Double.parseDouble(price)*servicechargeRate)*vatRate))))+"\n";
            subTotal=subTotal+(Double.parseDouble(price));
        }
        subTotal=subTotal+(subTotal*servicechargeRate);
        outputResult=outputResult+"\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t     Subtotal -> "+String.format("%.2f",subTotal)+" Birr\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t     Total -> "+String.format ("%.2f",(subTotal+(subTotal*vatRate)))+" Birr";

        result.setText(outputResult);
        result.setVisibility(View.VISIBLE);

    }

    private boolean validateInputs(TextInputLayout txtlayout) {
        if (txtlayout.getEditText().getText().toString().trim().isEmpty()) {
            txtlayout.setError(getString(R.string.err_msg_order));
            txtlayout.requestFocus();
            return false;
        } else {
            txtlayout.setErrorEnabled(false);
        }

        return true;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;
        private TextInputLayout txtlayout;

        private MyTextWatcher(View view,TextInputLayout txtlayout) {
            this.view = view;
            this.txtlayout=txtlayout;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
                    validateInputs(txtlayout);
        }
    }

    public void addtextfield(View view) {

        final TextInputLayout txtlayout = new TextInputLayout(this);
        EditText edittxtinput = new EditText(this);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(dpToPx(200,Main2Activity.this),
                ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(dpToPx(8,Main2Activity.this));
        txtlayout.setLayoutParams(params);
        edittxtinput.setHint("Order "+(count+1));
        edittxtinput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        edittxtinput.addTextChangedListener(new Main2Activity.MyTextWatcher(edittxtinput,txtlayout));
        txtlayout.addView(edittxtinput);

        inputs.add(txtlayout);

        final ImageButton removeInputBtn = new ImageButton(this);
        removeInputBtn.setImageResource(R.drawable.ic_remove_circle_black_24dp);
        removeInputBtn.setBackgroundColor(Color.TRANSPARENT);
        removeInputBtn.setPadding(0,dpToPx(20,this),0,0);
        removeInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeinput(txtlayout,removeInputBtn);
            }
        });

        item=new LinearLayout(Main2Activity.this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,dpToPx(1,Main2Activity.this),0,0);
        item.setLayoutParams(p);
        item.addView(txtlayout);
        item.addView(removeInputBtn);
        //newinputlayout.addView(txtlayout);
        newinputlayout.addView(item);

        count+=1;

    }

    private void removeinput(TextInputLayout txtlayout,ImageButton removeInputBtn) {
        inputs.remove(txtlayout);
        txtlayout.removeAllViews();
        txtlayout.removeView(txtlayout);
        ViewGroup v = (ViewGroup) removeInputBtn.getParent();
        v.removeView(removeInputBtn);
        Toast.makeText(this, txtlayout.getHint()+" Removed !", Toast.LENGTH_SHORT).show();
    }

    public static int dpToPx(int dp, Context context){
        float density = context.getResources().getDisplayMetrics().density;
        return round((float)dp * density);
    }

    public void changeTheme() {
        if(theme==R.style.AppTheme){
            setTheme(R.style.AppThemeDark);
            theme=R.style.AppThemeDark;
            recreate();
        }else{
            setTheme(R.style.AppTheme);
            theme=R.style.AppTheme;
            recreate();
        }
    }
}
