# schedule #

project is divided into logical parts/teams: Logic_demo, gui, ai, network

until 28.10
- all teams: each build a simple isolated prototype with the goal of finding out what each team needs of whom
finish defining communication interfaces on how to communicate between modules/teams

until 04.11
- all teams: 	build one proof of concept prototype together, limited features but able to create games with 2 players (human vs. human/ai/network), simple limited features gui, not yet "playable"
- Logic_demo:		support basic game loop communication (shoot, answer, repeat), being able to set ships, but without basic rules (being one field apart other ships)
- ai: 		random agent, no intelligence, only able to take part in game
- network:	provide all needed communication for basic game
- gui:		1 gui frame able to display gamestate (2 matrices, turn, input (not yet working))


until 11.11
- Logic_demo:	add basic rules, add levelselect
- gui:		working gui frame for main game input and display
- network:	finish v1 protocol

until 18.11
- all teams:	build first working prototype, playable game with all parties (human/ai/network)
support semester functions
- Logic_demo:		support all rules / being able to save and load savestate
- gui:		add gui to create/join etc
- ai:			pseudo random agent, "hunts" hit ships

until 25.11
- gui:		add sound

until 02.12
- ai:			add smarter ai

until 09.12
- finish developing features
- add all documents to git

until 16.12 deadline
- finish fixing bugs
