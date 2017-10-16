package maksach.myapplication;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.support.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

public class ElectricityFragment extends Fragment {
    private static final String TAG = "ElectricityFragment";
    private Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.electricity_fragment, container, false);

        try {
            InputMethodManager inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {

        }

        return view;
    }

    public void addListenerOnSpinnerItemSelection() {
        spinner = (Spinner)getView().findViewById(R.id.spinner1);
    }
}
