package de.tum.kickercoding.tournamentviewer.setup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.util.Constants;


/**
 * Fragment contains editable basic settings like "max score" and "number of games"
 */
public class BasicSetupFragment extends Fragment {

	public BasicSetupFragment() {
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

	private void setInitialSetupValues(View view) {
		try {
			String maxScore = "" + AppManager.getInstance().getMaxScoreFromSettings();
			String numberOfGames = "" + AppManager.getInstance().getNumberOfGames();
			((TextView) view.findViewById(R.id.var_max_score)).setText(maxScore);
			((TextView) view.findViewById(R.id.var_number_games)).setText(numberOfGames);
		} catch (AppManagerException e) {
			AppManager.getInstance().displayError(getActivity(), "could not load settings, default values used");
			Log.e(BasicSetupFragment.class.toString(), "setInitialSetupValues: " + e.getMessage());
			((TextView) view.findViewById(R.id.var_max_score)).setText(Constants.DEFAULT_MAX_SCORE.toString());
			((TextView) view.findViewById(R.id.var_number_games)).setText(Constants.DEFAULT_NUMBER_OF_GAMES
					.toString());
		}
	}

	private void attachButtonListener(View view) {
		Button decrementScoreButton = (Button) view.findViewById(R.id.button_setup_decrement_score);
		decrementScoreButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				decrementMaxScore(buttonView);
			}
		});
		Button incrementScoreButton = (Button) view.findViewById(R.id.button_setup_increment_score);
		incrementScoreButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				incrementMaxScore(buttonView);
			}
		});
		Button decrementGamesButton = (Button) view.findViewById(R.id.button_setup_decrement_games);
		decrementGamesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				decrementNumberOfGames(buttonView);
			}
		});
		Button incrementGamesButton = (Button) view.findViewById(R.id.button_setup_increment_games);
		incrementGamesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				incrementNumberOfGames(buttonView);
			}
		});
	}

	private void incrementMaxScore(View view) {
		View rootView = view.getRootView();
		TextView textViewMaxScore = (TextView) rootView.findViewById(R.id.var_max_score);
		Integer currentScore = Integer.parseInt(textViewMaxScore.getText().toString());
		currentScore++;
		textViewMaxScore.setText(currentScore.toString());
	}

	private void decrementMaxScore(View view) {
		View rootView = view.getRootView();
		TextView textViewMaxScore = (TextView) rootView.findViewById(R.id.var_max_score);
		Integer currentScore = Integer.parseInt(textViewMaxScore.getText().toString());
		currentScore--;
		textViewMaxScore.setText(currentScore.toString());
	}

	private void incrementNumberOfGames(View view) {
		View rootView = view.getRootView();
		TextView textViewNumberOfGames = (TextView) rootView.findViewById(R.id.var_number_games);
		Integer currentScore = Integer.parseInt(textViewNumberOfGames.getText().toString());
		currentScore++;
		textViewNumberOfGames.setText(currentScore.toString());
	}

	private void decrementNumberOfGames(View view) {
		View rootView = view.getRootView();
		TextView textViewNumberOfGames = (TextView) rootView.findViewById(R.id.var_number_games);
		Integer currentScore = Integer.parseInt(textViewNumberOfGames.getText().toString());
		currentScore--;
		textViewNumberOfGames.setText(currentScore.toString());
	}
}
