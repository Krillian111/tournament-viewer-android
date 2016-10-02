package de.tum.kickercoding.tournamentviewer.setup;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class PlayerListAdapter extends BaseAdapter implements ListAdapter {

	Context context;

	public PlayerListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return AppManager.getInstance().getAllPlayers().size();
	}

	@Override
	public Object getItem(int pos) {
		try {
			return AppManager.getInstance().getPlayer(pos);
		} catch (AppManagerException e) {
			Log.e(PlayerListAdapter.class.toString(), "fatal error: getItem() failed", e);
			// fake player to allow user to write down results
			return new Player("FATAL ERROR");
		}
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
			view = inflater.inflate(R.layout.item_player_list, null);
		}

		//Handle TextView and display player name
		TextView listItemText = (TextView) view.findViewById(R.id.player_list_item_text_view);
		final String playerName = ((Player) getItem(position)).getName();
		listItemText.setText(playerName);

		view.setClickable(true);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View viewItem) {
				Dialog dialog = createPlayerDialog(context, viewItem, position);
				dialog.show();
			}
		});

		Button deleteButton = (Button) view.findViewById(R.id.player_list_item_delete_button);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				try {
					AppManager.getInstance().removePlayer(playerName);
				} catch (AppManagerException e) {
					AppManager.getInstance().displayError(context, e.getMessage());
				}
				notifyDataSetChanged();
			}
		});

		Button toggleParticipationButton = (Button) view.findViewById(R.id
				.player_list_item_toggle_participation_button);
		toggleParticipationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				try {
					boolean signedUp = AppManager.getInstance().toggleParticipation((Player) getItem(position));
					adjustBackgroundColor(signedUp, buttonView);
					notifyDataSetChanged();
				} catch (AppManagerException e) {
					AppManager.getInstance().displayError(context, e.getMessage());
				}

			}
		});

		adjustBackgroundColor(AppManager.getInstance().isSignedUp(playerName), toggleParticipationButton);
		return view;
	}

	/**
	 * adjusts the background color of list item to reflect tournament participation
	 *
	 * @param childViewOfItem: must be a child view element of the actual item (e.g. toggle button)
	 */
	private void adjustBackgroundColor(boolean signedUp, View childViewOfItem) {
		RelativeLayout relativeLayout = (RelativeLayout) childViewOfItem.getParent();
		int newBackgroundColor;
		if (signedUp) {
			newBackgroundColor = ContextCompat.getColor(context, R.color.player_signed_up);
		} else {
			newBackgroundColor = ContextCompat.getColor(context, R.color.player_not_signed_up);
		}
		relativeLayout.setBackgroundColor(newBackgroundColor);
	}

	/**
	 * Creates a dialog to display all information of a player. Can be reused in tournament view.
	 *
	 * @param context
	 * @param viewItem
	 * @param position
	 * @return
	 */
	private Dialog createPlayerDialog(Context context, View viewItem, int position) {
		final Dialog dialog = new Dialog(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
				.LAYOUT_INFLATER_SERVICE);
		dialog.setContentView(inflater.inflate(R.layout.dialog_player_details, null));
		dialog.setTitle(R.string.title_player_details);

		Player player = (Player) getItem(position);
		prepareTextView(dialog, R.id.player_details_name, player.getName());
		prepareTextView(dialog, R.id.player_details_rank_global, "?");
		prepareTextView(dialog, R.id.player_details_played_games, "" + player.getPlayedGames());
		prepareTextView(dialog, R.id.player_details_won_games, "" + player.getWonGames());
		prepareTextView(dialog, R.id.player_details_lost_games, "" + player.getLostGames());
		prepareTextView(dialog, R.id.player_details_tied_games, "" + player.getTiedGames());
		prepareTextView(dialog, R.id.player_details_win_rate, "" + player.getWinRate());
		prepareTextView(dialog, R.id.player_details_mmr, "" + player.getMmr());

		setupButtonListener(dialog);
		return dialog;
	}

	private void prepareTextView(Dialog dialog, int id, String text) {
		TextView textView = (TextView) dialog.findViewById(id);
		textView.setText(text);
	}

	private void setupButtonListener(final Dialog dialog) {
		Button backButton = (Button) dialog.findViewById(R.id.button_player_details_back);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}