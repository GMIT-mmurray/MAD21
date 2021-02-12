package com.coreservlets.intentfilter1;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabbedActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resources resources = getResources();
        TabHost host = getTabHost();
        Intent intent1 = new Intent(this, LoanCalculatorActivity.class);
        Bundle loanBundle1 = 
            LoanBundler.makeLoanInfoBundle(100000, 7.5, 120);
        intent1.putExtras(loanBundle1);
        Drawable tabIcon = resources.getDrawable(R.drawable.calculator);
        TabSpec tab1Spec = host.newTabSpec("Tab One")
                               .setIndicator("10 Year", tabIcon)
                               .setContent(intent1);
        host.addTab(tab1Spec);
        Uri uriTwentyYear = Uri.parse("loan://coreservlets.com/calc");
        Intent intent2 = new Intent(Intent.ACTION_VIEW, uriTwentyYear);
        Bundle loanBundle2 = 
            LoanBundler.makeLoanInfoBundle(100000, 7.5, 240);
        intent2.putExtras(loanBundle2);
        tabIcon = resources.getDrawable(R.drawable.calculator);
        TabSpec tab2Spec = host.newTabSpec("Tab Two")
                               .setIndicator("20 Year", tabIcon)
                               .setContent(intent2);
        host.addTab(tab2Spec);
        String baseAddress = "loan://coreservlets.com/calc";
        String address =
                String.format("%s?%s&%s&%s",  
                              baseAddress,
                              "loanAmount=100000",
                              "annualInterestRateInPercent=7.5",
                              "loanPeriodInMonths=360");
        Uri uriThirtyYear = Uri.parse(address);
        Intent intent3 = new Intent(Intent.ACTION_VIEW, uriThirtyYear);
        tabIcon = resources.getDrawable(R.drawable.calculator);
        TabSpec tab3Spec = host.newTabSpec("Tab Three")
                               .setIndicator("30 Year", tabIcon)
                               .setContent(intent3);
        host.addTab(tab3Spec);
    }
}