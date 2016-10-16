package de.tum.kickercoding.tournamentviewer.setup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.util.Utils;

import static de.tum.kickercoding.tournamentviewer.util.Utils.createPlayerDialog;
import static de.tum.kickercoding.tournamentviewer.util.Utils.prepareTextView;

class PlayerListAdapter extends BaseAdapter implements ListAdapter {

	private Context context;

	private List<Player> playerList;

	PlayerListAdapter(Context context, List<Player> playerList) {
		this.context = context;
		Utils.sortPlayersByName(playerList);
		this.playerList = playerList;
	}

	@Override
	public int getCount() {
		return playerList.size();
	}

	@Override
	public Object getItem(int pos) {
		return playerList.get(pos);
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

		final Player player = (Player) getItem(position);
		//Handle TextView and display player name
		prepareTextView(view, R.id.player_list_item_text_view, player.getName());

		view.setClickable(true);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				if (AppManager.getInstance().isTournamentInProgress()) {
					Dialog dialog = createConfirmToggleDialog(player, buttonView);
					dialog.show();
				} else {
					toggleParticipation(buttonView, player);
				}
			}
		});

		Button deleteButton = (Button) view.findViewById(R.id.button_delete_player);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View buttonView) {
				Dialog dialog = createDeleteDialog(context, player.getName());
				dialog.show();
				updateInternalList();
			}
		});

		Button playerDetailsButton = (Button) view.findViewById(R.id
				.button_player_details);
		playerDetailsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View viewItem) {
				Dialog dialog = createPlayerDialog(context, player);
				dialog.show();
			}
		});

		adjustBackgroundColor(AppManager.getInstance().isSignedUp(player.getName()), view);
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
					updateInternalList();
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
				toggleParticipation(buttonView, player);
			}
		});
		return builder.create();
	}

	private void toggleParticipation(View buttonView, Player player) {
		boolean signedUpAfterToggle = false;
		try {
			signedUpAfterToggle = AppManager.getInstance().toggleParticipation(player);
			adjustBackgroundColor(signedUpAfterToggle, buttonView);
			updateInternalList();
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

	void updateInternalList() {
		List<Player> playerList = AppManager.getInstance().getAllPlayers();
		Utils.sortPlayersByName(playerList);
		this.playerList = playerList;
		notifyDataSetChanged();
	}
}