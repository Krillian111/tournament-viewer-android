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
			view = inflater.inflate(R.layout.item_tournament_stats, null);
		}

		//Handle TextView and display player name
		TextView listItemText = (TextView) view.findViewById(R.id.tournament_stats_item_text_view);
		listItemText.setText(getItem(position).toString());

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
		try {
			return AppManager.getInstance().getMaxScore();
		} catch (AppManagerException e) {
			Log.e(TournamentGamesAdapter.class.toString(), "couldn't load max score");
			AppManager.getInstance().displayFatalError(context);
			return 0;
		}
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
					AppManager.getInstance().finalizeGame(position, np1.getValue(), np2.getValue());
				} catch (AppManagerException e) {
					Log.e(TournamentGamesAdapter.class.toString(), String.format("couldnt finalize game, " +
							"invalid input: position:%d, score1:%d, score2:%d", position, np1.getValue(), np2.getValue()));
				}
				notifyDataSetChanged();
				dialog.dismiss();
			}
		});
	}
}