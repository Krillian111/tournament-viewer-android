package de.tum.kickercoding.tournamentviewer.setup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import de.tum.kickercoding.tournamentviewer.util.Utils;

import static de.tum.kickercoding.tournamentviewer.util.Utils.prepareTextView;

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
			view = inflater.inflate(R.layout.item_select_players, null);
		}

		//Handle TextView and display player name
		TextView listItemText = (TextView) view.findViewById(R.id.player_list_item_text_view);
		final String playerName = ((Player) getItem(position)).getName();
		listItemText.setText(playerName);

		view.setClickable(true);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				Player player = (Player) getItem(position);
				if (AppManager.getInstance().isTournamentInProgress()) {
					Dialog dialog = createConfirmToggleDialog(player, buttonView);
					dialog.show();
				} else {
					toggleAndNotify(buttonView, player);
				}
			}
		});

		Button deleteButton = (Button) view.findViewById(R.id.button_delete_player);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				Dialog dialog = createDeleteDialog(context, playerName);
				dialog.show();
				notifyDataSetChanged();
			}
		});

		Button playerDetailsButton = (Button) view.findViewById(R.id
				.button_player_details);
		playerDetailsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View viewItem) {
				Dialog dialog = createPlayerDialog(context, viewItem, position);
				dialog.show();
			}
		});

		adjustBackgroundColor(AppManager.getInstance().isSignedUp(playerName), view);
		return view;
	}

	private Dialog createDeleteDialog(final Context context, final String playerName) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Delete player?");
		builder.setMessage("Caution: It is not possible to restore details about a deleted player!");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				try {
					AppManager.getInstance().removePlayer(playerName);
				} catch (AppManagerException e) {
					AppManager.getInstance().displayMessage(context, e.getMessage());
				}
			}
		});
		return builder.create();
	}

	private Dialog createConfirmToggleDialog(final Player player, final View buttonView) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		boolean currentlySignedUp = AppManager.getInstance().isSignedUp((player.getName()));
		builder.setTitle(currentlySignedUp ? "Remove player?" : "Add player?");
		if (currentlySignedUp) {
			builder.setMessage("Tournament is currently running: Removing player does not affect games already " +
					"finished!");
		}
		builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				toggleAndNotify(buttonView, player);
			}
		});
		return builder.create();
	}

	private void toggleAndNotify(View buttonView, Player player) {
		boolean signedUpAfterToggle = false;
		try {
			signedUpAfterToggle = AppManager.getInstance().toggleParticipation(player);
			adjustBackgroundColor(signedUpAfterToggle, buttonView);
			notifyDataSetChanged();
		} catch (AppManagerException e) {
			AppManager.getInstance().displayMessage(context, e.getMessage());
		}
	}

	/**
	 * adjusts the background color of list item to reflect tournament participation
	 *
	 * @param containerView: must be a the container view element of the actual item
	 */
	private void adjustBackgroundColor(boolean signedUp, View containerView) {
		RelativeLayout relativeLayout = (RelativeLayout) containerView.findViewById(R.id.player_list_item_root);
		int newBackgroundColor;
		if (signedUp) {
			newBackgroundColor = ContextCompat.getColor(context, R.color.player_signed_up);
		} else {
			newBackgroundColor = ContextCompat.getColor(context, R.color.player_not_signed_up);
		}
		relativeLayout.setBackgroundColor(newBackgroundColor);
	}

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
		prepareTextView(dialog, R.id.player_details_win_rate, Utils.prepareWinRateForView(player.getWinRate()));
		prepareTextView(dialog, R.id.player_details_goal_difference, "" + player.getGoalDifference());
		prepareTextView(dialog, R.id.player_details_mmr, "" + player.getMmr());

		setupButtonListener(dialog);
		return dialog;
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