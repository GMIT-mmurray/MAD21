package com.coreservlets.intentfilter2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

/** Demonstrates a more complex form of Activity switching: 
 *  invoking another Activity with a URI. The other Activity
 *  registers an intent filter for either the MIME type or
 *  for a specific part of the URL. There are
 *  two variations here: invoking the other Activity
 *  with "extras" data, and invoking the other Activity 
 *  with data embedded in the URL. Also see IntentFilter1Activity,
 *  which demonstrates a simpler form of Activity switching: 
 *  invoking another Activity by class name.
 **/

public class IntentFilter2Activity extends Activity {
    /** Initializes the app when it is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    /** Switches to the LoanCalculatorActivity when the associated button is clicked. 
     *  Sends data in the "extras" Bundle. 
     */
    public void showLoanPayments1(View clickedButton) {
        Uri uri = Uri.parse("loan://coreservlets.com/calc");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtras(LoanBundler.makeRandomizedLoanInfoBundle());
        startActivity(intent);
    }
    
    /** Switches to the LoanCalculatorActivity when the associated button is clicked. 
     *  Sends data in the query parameters of the URI. 
     */
    public void showLoanPayments2(View clickedButton) {
        String address = makeLoanAddressFromEditTextInputs();
        Uri uri = Uri.parse(address);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    
    /** Builds a String in the format used by the LoanCalculatorActivity.
     *  The format is loan://coreservlets.com/calc?loanAmount=xxx&annualInterestRateInPercent=yyy&loanPeriodInMonths=zzz
     */
    private String makeLoanAddressFromEditTextInputs() {
        EditText loanAmountInput = (EditText)findViewById(R.id.loan_amount);
        Editable loanAmount = loanAmountInput.getText();
        String loanAmountParam = 
                String.format("loanAmount=%s", loanAmount);
        EditText interestRateInput = (EditText)findViewById(R.id.interest_rate);
        Editable interestRate = interestRateInput.getText();
        String interestRateParam = 
                String.format("annualInterestRateInPercent=%s", interestRate);
        EditText loanPeriodInput = (EditText)findViewById(R.id.loan_period);
        Editable loanPeriod = loanPeriodInput.getText();
        String loanPeriodParam = 
                String.format("loanPeriodInMonths=%s", loanPeriod);
        String baseAddress = "loan://coreservlets.com/calc";
        String address =
                String.format("%s?%s&%s&%s", baseAddress, loanAmountParam,
                              interestRateParam, loanPeriodParam);
        return(address);
    }
}