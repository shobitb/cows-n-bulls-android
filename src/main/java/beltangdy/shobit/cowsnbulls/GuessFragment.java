package beltangdy.shobit.cowsnbulls;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by shobit on 21/09/14.
 */
public class GuessFragment extends Fragment {

    String TAG = getClass().getName();
    private String generatedVal;
    ArrayAdapter<String> adapter;
    private String guessedVal;
    private int guessCounter;
    private Toast hintToast;
    Random rand = new Random();

    public GuessFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generatedVal = getNumber();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.guessfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_hint) {
            showToast(getHint());
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_guessed_number, R.id.guess);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_guesses);
        listView.setAdapter(adapter);
        Button goButton = (Button) rootView.findViewById(R.id.button_go);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                calculate();
            }
        });

        return rootView;
    }

    public void calculate() {
        EditText guessedNum = (EditText) getView().findViewById(R.id.guessed_number);
        guessedVal = guessedNum.getText().toString();
        guessedNum.setText("");
        if (isInputValid(guessedVal)) {
            guessCounter++;
            int[] cnb = match(guessedVal);
            if (cnb[1] == 4) {
                adapter.insert(guessedVal + ": You got it! " + guessCounter + " guesses made", 0);
            } else {
                adapter.insert(guessedVal + " : " + cnb[0] + "C " + cnb[1] + "B", 0);
            }
        } else {
            showToast("Digits should be non-zero and non-repeated");
        }
    }

    private String getNumber() {
        Log.d(TAG, "Getting a number");
        List<String> numbers = new ArrayList<String>();
        for (int i = 1; i < 10; i++) {
            numbers.add(Integer.toString(i));
        }
        Collections.shuffle(numbers);
        String result = "";
        for (int i = 0; i < 4; i++) {
            result += numbers.get(i).toString();
        }
        Log.d(TAG, "Returning " + result);
        return result;
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

    private String getHint() {
        String hintMessage = "";
        if (generatedVal != null) {
            int randomNum = rand.nextInt(4);
            hintMessage = generatedVal.charAt(randomNum) + " is at position " + Integer.toString(randomNum + 1);
        }
        return hintMessage;
    }

    // Main algorithm
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

    private void showToast(String msg) {
        hintToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG);
        hintToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        hintToast.show();
    }
}
