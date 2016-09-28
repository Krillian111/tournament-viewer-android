package de.tum.kickercoding.tournamentviewer.manager;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import de.tum.kickercoding.tournamentviewer.entities.Player;
import de.tum.kickercoding.tournamentviewer.exceptions.PlayerManagerException;
import de.tum.kickercoding.tournamentviewer.util.Constants;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


public class PlayerManagerUnitTest {

	private PlayerManager playerManager;

	@Before
	public void initialize() {
		playerManager = PlayerManager.getInstance();
		try {
			// reset the player list
			Field privatePlayerList = PlayerManager.class.getDeclaredField("players");
			privatePlayerList.setAccessible(true);
			privatePlayerList.set(playerManager, new ArrayList<Player>());
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Test
	public void addPlayer() {
		for (int i = 0;i < 10;i++) {
			try {
				playerManager.addPlayer("TestPlayer" + i);
			} catch (PlayerManagerException e) {
				fail(e.toString());
			}
			assertEquals(i + 1, playerManager.getNumberOfPlayers());
		}
	}

	@Test
	public void addDuplicatedPlayer() {
		try {
			playerManager.addPlayer("TestPlayer");
		} catch (PlayerManagerException e) {
			fail(e.toString());
		}
		try {
			playerManager.addPlayer("TestPlayer");
		} catch (PlayerManagerException e) {
			return;
		}
		fail("Duplicated player not recognized");
	}

	@Test
	public void addPlayerWithDelimiter() {
		try {
			playerManager.addPlayer("TestPlayer" + Constants.DELIMITER);
		} catch (PlayerManagerException e) {
			return;
		}
		fail(String.format("Symbol %s not allowed in player name", Constants.DELIMITER));
	}

	@Test
	public void removePlayer() {
		for (int i = 0;i < 10;i++) {
			try {
				playerManager.addPlayer("TestPlayer" + i);
				playerManager.addPlayer("TestPlayer" + i + i);
				playerManager.addPlayer("TestPlayer" + i + i + i);

				playerManager.removePlayer("TestPlayer" + i + i);
				assertEquals(i + 2, playerManager.getNumberOfPlayers());

				playerManager.removePlayer("TestPlayer" + i + i + i);
				assertEquals(i + 1, playerManager.getNumberOfPlayers());
			} catch (PlayerManagerException e) {
				fail(e.toString());
			}
		}
	}

	@Test
	public void removeNonExistingPlayer() {
		try {
			playerManager.addPlayer("TestPlayer");
		} catch (PlayerManagerException e) {
			fail(e.toString());
		}
		assertFalse("Non existing player cannot return a succesful removal", playerManager.removePlayer
				("NonExistingPlayer"));
	}
}