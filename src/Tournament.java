/**
 *  Runs a tournament of given games rounds between two players.
 */
public class Tournament {

    private static final String END_OF_TOURNAMENT_MESSAGE = "######### Results #########\n" +
            "Player 1, %s won: %d rounds\n" +
            "Player 2, %s won: %d rounds\n" +
            "Ties: %d";
    private int rounds;
    private Renderer renderer;
    private Player player1;
    private Player player2;

    /**
     * Constructs a tournament between two players with the given rounds number and a renderer type.
     * @param rounds The number of rounds the tournament will be.
     * @param renderer The renderer type that will be display on the screen.
     * @param player1 The first player of the tournament.
     * @param player2 The second player of the tournament.
     */
    public Tournament(int rounds, Renderer renderer, Player player1, Player player2) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Performs a series of Tic-Tac-Toe games (rounds) between two players in a certain
     * rendering interface, when:
     * In the first round, the first player plays an X and the second a O,
     * and at the end of each round they switch signs.
     * In this way, in even-indexed the first player is X, while in
     * odd-indexed ones it is the other way around.
     * At the end of each tournament, the final result is printed on the screen.
     * @param size The size of the board game.
     * @param winStreak the streak of marks needed to win a single game.
     * @param playerName1 The name of the first player.
     * @param playerName2 The name of the second player.
     */
    public void playTournament(int size, int winStreak, String playerName1, String playerName2) {
        int player1Wins = 0;
        int player2Wins = 0;
        int ties = 0;
        for (int i = 0; i < rounds; i++) {
            Game game;
            if (i % 2 == 0) {
                game = new Game(player1, player2, size, winStreak, renderer);
            }
            else {
                game = new Game(player2, player1, size, winStreak, renderer);
            }
            Mark winMark = game.run();
            if (winMark == Mark.X) {
                if (i % 2 == 0) {
                    player1Wins++;
                }
                else {
                    player2Wins++;
                }
            }
            else if (winMark == Mark.O) {
                if (i % 2 == 0) {
                    player2Wins++;
                }
                else {
                    player1Wins++;
                }
            }
            else {
                ties++;
            }
        }
        System.out.println(String.format(END_OF_TOURNAMENT_MESSAGE,
                playerName1, player1Wins, playerName2, player2Wins, ties));
    }

    /**
     * The main method.
     * Manages the entire tournament according to the given arguments of the program.
     * @param args Command line arguments where:
     *             args[0] is the rounds the tournament will be.
     *             args[1] is the size of the board game.
     *             args[2] is the streak needed to win a single game.
     *             args[3] is the renderer type for displaying the board.
     *             args[4] is the type of player for the first one.
     *             args[5] is the type of player for the second one.
     */
    public static void main(String[] args) {
        int roundsNumber = Integer.parseInt(args[0]);
        int boardSize = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);
        String rendererType = args[3].toLowerCase();
        String player1Name = args[4].toLowerCase();
        String player2Name = args[5].toLowerCase();
        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer1 = rendererFactory.buildRenderer(rendererType, boardSize);
        if (renderer1 == null) {
            System.out.println(Constants.UNKNOWN_RENDERER_NAME);
            return;
        }
        PlayerFactory playerFactory = new PlayerFactory();
        Player player1 = playerFactory.buildPlayer(player1Name);
        Player player2 = playerFactory.buildPlayer(player2Name);
        if (player1 == null || player2 == null) {
            System.out.println(Constants.UNKNOWN_PLAYER_NAME);
            return;
        }
        Tournament tournament = new Tournament(roundsNumber, renderer1, player1, player2);
        tournament.playTournament(boardSize, winStreak, player1Name, player2Name);
    }
}
