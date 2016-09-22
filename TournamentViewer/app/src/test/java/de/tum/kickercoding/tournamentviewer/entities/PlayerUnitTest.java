package de.tum.kickercoding.tournamentviewer.entities;

import org.junit.Test;

import de.tum.kickercoding.tournamentviewer.util.Constants;

import static org.junit.Assert.assertEquals;

//TODO: refactor using the multiargument constructor of player
public class PlayerUnitTest {

    // delimiter reference for String.format (first argument)
    private static final String _ = Constants.DELIMITER;

    @Test
    public void testBasicPlayerToString(){
        Player p = new Player("name");
        String playerString = "name" + _ + "0" + _ + "0" + _ + "0" + _ + "0" + _ + "0.00";
        assertEquals(playerString, p.toString());
    }

    @Test
    public void testAllFieldsToString(){
        Player p1 = new Player("name1");
        p1.setPlayedGames(2);
        p1.setWonGames(4);
        p1.setLostGames(6);
        p1.setTiedGames(8);
        p1.setRankingScore(10);
        String p1String = "name1" + _ + "2" + _ + "4" + _ + "6" + _ + "8" + _ + "10.00";
        assertEquals(p1String,p1.toString());


        Player p2 = new Player("name2");
        p2.setPlayedGames(2);
        p2.setWonGames(4);
        p2.setLostGames(6);
        p2.setTiedGames(8);
        p2.setRankingScore(0);
        String p2String = "name2" + _ + "2" + _ + "4" + _ + "6" + _ + "8" + _ + "0.00";
        assertEquals(p2String,p2.toString());

        Player p3 = new Player("name3");
        p3.setPlayedGames(2);
        p3.setWonGames(4);
        p3.setLostGames(6);
        p3.setTiedGames(8);
        p3.setRankingScore(12.3456);
        String p3String = "name3" + _ + "2" + _ + "4" + _ + "6" + _ + "8" + _ + "12.3456";
        assertEquals(p3String,p3.toString());
    }

    @Test
    public void testBasicFromString() {
        String p1String = "name1" + _ + "2" + _ + "4" + _ + "6" + _ + "8" + _ + "10.00";
        Player p1 = Player.fromString(p1String);
        assertEquals("name1",p1.getName());
        assertEquals(2,p1.getPlayedGames());
        assertEquals(4,p1.getWonGames());
        assertEquals(6,p1.getLostGames());
        assertEquals(8,p1.getTiedGames());
        assertEquals(10.00,p1.getRankingScore(),0.001);

        String p2String = "name1" + _ + "2" + _ + "4" + _ + "6" + _ + "8" + _ + "9.2134";
        Player p2 = Player.fromString(p2String);
        assertEquals(9.2134,p2.getRankingScore(),0.001);
    }

}
