package de.tum.kickercoding.tournamentviewer.setup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import de.tum.kickercoding.tournamentviewer.Constants;
import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;


/**
 * Fragment contains editable basic settings like "max score" and "number of games"
 */
public class BasicSetupFragment extends Fragment {

	public BasicSetupFragment() {
	}

	public static BasicSetupFragment getInstance() {
		return new BasicSetupFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_basic_setup, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		setInitialSetupValues(view);
		attachButtonListener(view);
	}

	private void setInitialSetupValues(View view){
		try {
			String maxScore = "" + AppManager.getInstance().loadMaxScore();
			String numberOfGames = "" + AppManager.getInstance().loadNumberOfGames();
			((EditText) view.findViewById(R.id.editable_max_score)).setText(maxScore);
			((EditText) view.findViewById(R.id.editable_number_games)).setText(numberOfGames);
		} catch (AppManagerException e) {
			AppManager.getInstance().displayError(getActivity(), "could not load settings, default values used");
			Log.d(BasicSetupFragment.class.toString(), "onViewCreated: " + e.getMessage());
			((EditText) view.findViewById(R.id.editable_max_score)).setText(Constants.DEFAULT_MAX_SCORE.toString());
			((EditText) view.findViewById(R.id.editable_number_games)).setText(Constants.DEFAULT_NUMBER_OF_GAMES.toString());
		}
	}

	private void attachButtonListener(View view) {
		Button decrementScoreButton = (Button) view.findViewById(R.id.button_setup_decrement_score);
		decrementScoreButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View buttonView) {
				decrementMaxScore(buttonView);
			}
		});
		Button incrementScoreButton = (Button) view.findViewById(R.id.button_setup_increment_score);
		incrementScoreButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View buttonView) {
				incrementMaxScore(buttonView);
			}
		});
		Button decrementGamesButton = (Button) view.findViewById(R.id.button_setup_decrement_games);
		decrementGamesButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View buttonView) {
				decrementNumberOfGames(buttonView);
			}
		});
		Button incrementGamesButton = (Button) view.findViewById(R.id.button_setup_increment_games);
		incrementGamesButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View buttonView) {
				incrementNumberOfGames(buttonView);
			}
		});
	}

	private void incrementMaxScore(View view) {
		View rootView = view.getRootView();
		EditText editableMaxScore = (EditText) rootView.findViewById(R.id.editable_max_score);
		Integer currentScore = Integer.parseInt(editableMaxScore.getText().toString());
		currentScore++;
		editableMaxScore.setText(currentScore.toString());
	}

	private void decrementMaxScore(View view) {
		View rootView = view.getRootView();
		EditText editableMaxScore = (EditText) rootView.findViewById(R.id.editable_max_score);
		Integer currentScore = Integer.parseInt(editableMaxScore.getText().toString());
		currentScore--;
		editableMaxScore.setText(currentScore.toString());
	}

	private void incrementNumberOfGames(View view) {
		View rootView = view.getRootView();
		EditText editableMaxScore = (EditText) rootView.findViewById(R.id.editable_number_games);
		Integer currentScore = Integer.parseInt(editableMaxScore.getText().toString());
		currentScore++;
		editableMaxScore.setText(currentScore.toString());
	}

	private void decrementNumberOfGames(View view) {
		View rootView = view.getRootView();
		EditText editableMaxScore = (EditText) rootView.findViewById(R.id.editable_number_games);
		Integer currentScore = Integer.parseInt(editableMaxScore.getText().toString());
		currentScore--;
		editableMaxScore.setText(currentScore.toString());
	}
}
