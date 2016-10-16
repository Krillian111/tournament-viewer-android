package de.tum.kickercoding.tournamentviewer.modes.ladder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;

import java.util.List;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;
import de.tum.kickercoding.tournamentviewer.util.Utils;

import static de.tum.kickercoding.tournamentviewer.util.Utils.createPlayerDialog;
import static de.tum.kickercoding.tournamentviewer.util.Utils.prepareTextView;

class PlayerLadderAdapter extends BaseAdapter implements ListAdapter {

	private Context context;

	private List<Player> playerList;

	PlayerLadderAdapter(Context context, List<Player> playerList) {
		Utils.sortPlayersByElo(playerList);
		this.playerList = playerList;
		this.context = context;
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
			view = inflater.inflate(R.layout.item_player_ladder, null);
		}

		final Player player = (Player) getItem(position);
		prepareTextView(view, R.id.player_ladder_item_rank, "" + (position + 1));
		prepareTextView(view, R.id.player_ladder_item_name, player.getName());
		prepareTextView(view, R.id.player_ladder_item_elo, Utils.prepareEloForView(player.getElo()));

		view.setClickable(true);
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View viewItem) {
				Dialog dialog = createPlayerDialog(context, (Player) getItem(position));
				dialog.show();
			}
		});

		view.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(final View v) {
				Dialog dialog = createAdjustEloDialog(player.getName());
				dialog.show();
				return true;
			}
		});

		return view;
	}

	private Dialog createAdjustEloDialog(final String playerName) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Manually adjust elo rating for " + playerName + ":");
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.dialog_edit_elo, null);
		builder.setView(view);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				Dialog dialogObject = (Dialog) dialog;
				EditText editText = (EditText) dialogObject.findViewById(R.id.dialog_edit_elo_edit_text);
				String eloAsString = editText.getText().toString();
				try {
					double adjustedElo = Double.parseDouble(eloAsString);
					AppManager.getInstance().manuallyAdjustElo(playerName, adjustedElo);
					AppManager.getInstance().commitPlayerList();
					updateInternalList();
				} catch (NumberFormatException e) {
					AppManager.getInstance().displayMessage(context, "Error: Enter valid elo rating!");
				} catch (AppManagerException e) {
					AppManager.getInstance().displayMessage(context, e.getMessage());
				}
			}
		});
		return builder.create();
	}

	private void updateInternalList() {
		playerList = AppManager.getInstance().getAllPlayers();
		Utils.sortPlayersByElo(playerList);
		notifyDataSetChanged();
	}
}