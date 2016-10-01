package de.tum.kickercoding.tournamentviewer.tournament;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Game;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class TournamentGamesAdapter extends BaseAdapter implements ListAdapter {

	Context context;

	public TournamentGamesAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return AppManager.getInstance().getGamesForTournament().size();
	}

	@Override
	public Object getItem(int pos) {
		return AppManager.getInstance().getGamesForTournament().get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_tournament_games, null);
		}

		//Handle TextView and display player name
		populateTextViews(view, position);
		// add alertDialog to allow changing values of games
		view.setClickable(true);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View viewItem) {
				Dialog dialog = createEditGameDialog(context, viewItem, position);
				dialog.show();
			}
		});
		return view;
	}

	private void populateTextViews(View view, int position) {
		Game game = (Game) getItem(position);
		List<String> namesTeam1 = game.getTeam1PlayerNames();
		String team1InView = namesTeam1.get(0) + "\n" + namesTeam1.get(1);
		List<String> namesTeam2 = game.getTeam2PlayerNames();
		String team2InView = namesTeam2.get(0) + "\n" + namesTeam2.get(1);
		prepareTextView(view, R.id.tournament_game_item_team_1, team1InView);
		prepareTextView(view, R.id.tournament_game_item_team_2, team2InView);
		prepareTextView(view, R.id.tournament_game_item_score, game.getScoreTeam1() + ":" + game.getScoreTeam2());

	}

	private void prepareTextView(View view, int id, String text) {
		TextView textView = (TextView) view.findViewById(id);
		textView.setText(text);
	}

	private Dialog createEditGameDialog(Context context, View viewItem, int position) {
		final Dialog dialog = new Dialog(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
				.LAYOUT_INFLATER_SERVICE);
		dialog.setContentView(inflater.inflate(R.layout.dialog_edit_game, null));
		dialog.setTitle(R.string.title_edit_game);

		int maxScore = getMaxScore(viewItem.getContext());
		int currentScore1 = ((Game) getItem(position)).getScoreTeam1();
		int currentScore2 = ((Game) getItem(position)).getScoreTeam2();
		NumberPicker np1 = setupNumberPicker((NumberPicker) dialog.findViewById(R.id
				.edit_game_number_picker_1), currentScore1, maxScore);
		NumberPicker np2 = setupNumberPicker((NumberPicker) dialog.findViewById(R.id
				.edit_game_number_picker_2), currentScore2, maxScore);
		setupButtonListener(dialog, position, np1, np2);
		return dialog;
	}

	private int getMaxScore(Context context) {
		return AppManager.getInstance().getMaxScoreFromTournament();
	}


	private NumberPicker setupNumberPicker(NumberPicker numberPicker, int currentValue, int maxScore) {
		numberPicker.setMaxValue(maxScore);
		numberPicker.setMinValue(0);
		numberPicker.setValue(currentValue);
		numberPicker.setWrapSelectorWheel(false);
		return numberPicker;
	}


	private void setupButtonListener(final Dialog dialog, final int position, final NumberPicker np1, final
	NumberPicker np2) {
		Button cancelButton = (Button) dialog.findViewById(R.id.button_edit_game_cancel);
		cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		Button confirmButton = (Button) dialog.findViewById(R.id.button_edit_game_confirm);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (np1.getValue() == getMaxScore(context) || np2.getValue() == getMaxScore(context)) {
						AppManager.getInstance().finalizeGame(position, np1.getValue(), np2.getValue());
						notifyDataSetChanged();
						dialog.dismiss();
					} else {
						AppManager.getInstance().displayError(context, "At least one team needs to have maximum " +
								"score!");
					}
				} catch (AppManagerException e) {
					Log.e(TournamentGamesAdapter.class.toString(), String.format("couldnt finalize game, " +
							"invalid input: position:%d, score1:%d, score2:%d", position, np1.getValue(), np2.getValue
							()));
				}
			}
		});
	}
}