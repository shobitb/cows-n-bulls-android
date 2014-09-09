package beltangdy.shobit.cowsnbulls;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static String generatedVal;
    private static int guessCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generatedVal = getNumber();
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    private String getNumber() {
        List<String> numbers = new ArrayList<String>();
        for (int i = 1; i < 10; i++) {
            numbers.add(Integer.toString(i));
        }

        Collections.shuffle(numbers);

        String result = "";
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }

        return result;
    }

    public void calculate(View view) {
        EditText editText = (EditText) findViewById(R.id.guessed_number);
        String guessedVal = editText.getText().toString();
        if (isInputValid(guessedVal)) {
            guessCounter++;
            editText.setText("");
            int[] cAndBCount = match(guessedVal);
            TextView result = (TextView) findViewById(R.id.result);
            TextView history = (TextView) findViewById(R.id.history);

            if (cAndBCount[1] == 4) {
                result.setText(String.format(
                        "%s: You got it! %s guesses made.", guessedVal,
                        guessCounter));
            } else {
                history.setText(result.getText().toString() + "\n"
                        + history.getText().toString());
                result.setText(String.format("%s: %s C, %s B", guessedVal,
                        cAndBCount[0], cAndBCount[1]));
            }
        } else {
            return;
        }
    }

    private boolean isInputValid(String input) {
        if (input != null && !input.equals("")) {
            char[] inputArr = input.toCharArray();
            Arrays.sort(inputArr);
            int i = 1;
            while (i < inputArr.length && inputArr[i] != inputArr[i - 1]) {
                i++;
            }

            return i == 4 ? true : false;
        }

        return false;
    }

    // algorithm
    //
    private int[] match(String guessedVal) {
        int cows = 0;
        int bulls = 0;

        for (int i = 0; i < 4; i++) {
            if (guessedVal.charAt(i) == generatedVal.charAt(i)) {
                bulls++;
            } else {
                String substr = generatedVal.substring(0, i)
                        + generatedVal.substring(i + 1, 4);
                if (substr.indexOf(guessedVal.charAt(i)) > -1)
                    cows++;
            }
        }

        return new int[]{cows, bulls};
    }
}
