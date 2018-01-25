# TournamentViewerAndroid
Android app for displaying and managing tournament progress (was tailored towards the needs of the tournament at my local bar)
Mode: MonsterDYP  (draw new partner every game)

Main purpose of this project was for me to take my first baby steps regarding android development.
As this was my first real project outside school, the code is messy.
Feel free to change/adapt stuff but I won't as I decided to start from scratch with another project 


Here is a guideline on how to use the app which I wrote for friends:

#####
INSTALL
#####
For instructions, use a standard tutorial like https://www.wikihow.tech/Install-APK-Files-on-Android

#####
FEATURE OVERVIEW
#####
- MonsterDYP with single game matches
- adjustable max score (see settings screen)
- Fair matchmaking based on a ranking number (simple averaging implementation of the elo system)


#####
HOW TO USE (screen by screen)
#####
------Home screen------
- (A) start a new tournament
- (B) load a running tournament (e.g. after app crash)
- (C) ladder overview

------(A) Start a tournament------
- adjust max score
- adjust best of X for playoffs only (experimental, doesnt work well with elo, probably keep this at 1)
- 1v1 or 2v2 (default)
- proceed to player selection (D)

------(C) Ladder overview------
- displays the ranking number for each player
- long press on player allows to adjust ranking

------(D) Player Selection------
- add players to global player list
- delete players (with confirmation prompt)
- list player stats
- add/remove player from current tournament (green background = added)
- proceed to tournament view (E)

------(E) Tournament view (bottom buttons)------
- [Playoffs] (Experimental) generate X games (set in (A)) for the following matchups: (1+5) vs (4+8) and (2+6) vs (3+7); once these are finished another click generates X games for winner of first game vs winner of second game; for 1v1 the initial games are 1 vs 4 and 2 vs 3
- [Players] return to (D) to add/delete players (BE CAREFUL DUE TO BUG: THIS MIGHT DELETE GAMES WITHOUT A SET SCORE; SET AN ARTICIAL SCORE TO MAKE SURE THIS DOES NOT HAPPEN)
- [Finish] End tournament: disables all changes to the tournament and makes all changes to player stats permanent (else the change in rating etc. is not saved)

------(E) Tournament view (stats tab)------
- list players using the following order of stats; next stat only used for ties: (1) win rate (2) number of games (3) goal difference (4) inverse rating [lower rating higher]
- click player name for player stats

------(E) Tournament view (games tab)------
- displays games
- generate game (one game)
- generate round (#players modulo 2/4 many games; depending on 1v1/2v2)
- short press game: set score; revert a finished game to allow score adjustment
- long pres game: delete game (with confirmation prompt)
