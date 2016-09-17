package de.tum.kickercoding.tournamentviewer.modes.monsterdyp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import de.tum.kickercoding.tournamentviewer.R;
import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.AppManagerException;
import de.tum.kickercoding.tournamentviewer.manager.AppManager;

public class PlayerListAdapter extends BaseAdapter implements ListAdapter {

    Context context;

    public PlayerListAdapter (Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return AppManager.getInstance().getAllPlayers().size();
    }

    @Override
    public Object getItem(int pos) {
        return AppManager.getInstance().getAllPlayers().get(pos);
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

        Button deleteButton = (Button) view.findViewById(R.id.player_list_item_delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    AppManager.getInstance().removePlayer(playerName);
                } catch (AppManagerException e) {
                    AppManager.getInstance().displayError(context, e.getMessage());
                }
                notifyDataSetChanged();
            }
        });

        Button addToTournamentButton = (Button) view.findViewById(R.id.player_list_item_toggle_participation_button);
		addToTournamentButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    AppManager.getInstance().toggleParticipation(AppManager.getInstance().getPlayer(position));
                } catch (AppManagerException e) {
                    AppManager.getInstance().displayError(context, e.getMessage());
                }
                notifyDataSetChanged();
            }
        });
        return view;
    }
}