package de.tum.kickercoding.tournamentviewer.entities;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GameUnitTest {

    Player[] players2on2;

    Player[] players1on1;

    @Before
    public void preparePlayerList(){
        players2on2 = new Player[]{new Player("p1"),new Player("p2"),new Player("p3"),new Player("p4")};
        players1on1 = new Player[]{new Player("p1"),new Player("p2")};
    }

    @Test
    public void test2on2Getters(){
        Game game = new Game(players2on2);

        Player[] team1 = game.getTeam1();
        assertEquals(2, team1.length);
        String[] team1Names = game.getTeam1PlayerNames();
        assertArrayEquals(new String[]{"p1","p2"},team1Names);

        Player[] team2 = game.getTeam2();
        assertEquals(2, team2.length);
        String[] team2Names = game.getTeam2PlayerNames();
        assertArrayEquals(new String[]{"p3","p4"},team2Names);

        game.setScoreTeam1(2);
        game.setScoreTeam2(4);
        assertEquals(2,game.getScoreTeam1());
        assertEquals(4,game.getScoreTeam2());
    }

    @Test
    public void test1on1Getters(){
        Game game = new Game(players1on1);

        Player[] team1 = game.getTeam1();
        assertEquals(2, team1.length);
        String[] team1Names = game.getTeam1PlayerNames();
        assertArrayEquals(new String[]{"p1","p2"},team1Names);

        Player[] team2 = game.getTeam2();
        assertEquals(2, team2.length);
        String[] team2Names = game.getTeam2PlayerNames();
        assertArrayEquals(new String[]{"p3","p4"},team2Names);

        game.setScoreTeam1(2);
        game.setScoreTeam2(4);
        assertEquals(2,game.getScoreTeam1());
        assertEquals(4,game.getScoreTeam2());
    }
}
