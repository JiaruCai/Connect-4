import java.util.Random;

//public class minimax_916863014_Jiaru {

    public class minimax_916863014_Jiaru extends AIModule
    {
        int player;
        int opponent;
        int maxDepth = 5;
        int bestMoveSeen;

        public void getNextMove(final GameStateModule game)
        {
            player = game.getActivePlayer();
            opponent = (game.getActivePlayer() == 1?2:1);
            //begin recursion
            maxDepth = 5;
            while(!terminate){
                minimax(game, 0, player);
                if(!terminate)
                    chosenMove = bestMoveSeen;
                maxDepth++;
            }
//            System.out.print("Got to depth: ");
//            System.out.println(maxDepth - 1);
            if(game.canMakeMove(chosenMove))
                game.makeMove(chosenMove);
        }

        private int minimax(final GameStateModule state, int depth, int playerID) {
            if (terminate)
                return 0;
            if (depth == maxDepth) {
                return eval(state);
            }
            depth++;
            int value = 0;
            //max's turn
            int bestVal = Integer.MIN_VALUE;
            if(playerID == player){
                value = Integer.MIN_VALUE + 1;
                for(int i = 0; i < state.getWidth(); i++){
                    if(state.canMakeMove(i)) {
                        state.makeMove(i);
                        value = Math.max(value, minimax(state, depth, opponent));
                        state.unMakeMove();
                        if (value > bestVal){
                            bestVal = value;
                            if (depth == 1) { //top of recursion, make our move choice
                                bestMoveSeen = i;
                            }
                        }
                    }
                }
                return value;
            }

            else { //min's turn
                value = Integer.MAX_VALUE;
                for(int i = 0; i < state.getWidth(); i++) {
                    if (state.canMakeMove(i)) {
                        state.makeMove(i);
                        value = Math.min(value, minimax(state, depth, player));
                        state.unMakeMove();
                    }
                }
                return value;
            }
        }

        // randomly assigns a value to a state
        //change this to something else
        private int eval(final GameStateModule state){
            int score = 0;
            for (int i=0; i< state.getWidth(); i++){
                for (int j=0; j<state.getHeight(); j++){
                    score+=checkRight(state, i, j);
                    score+=checkDown(state, i, j);
                    score+=checkUpRight(state, i, j);
                    score+=checkDownRight(state, i, j);
                }
            }
            return score;
        }

        public int check(final GameStateModule state, int x0, int y0, int x1, int y1, int x2, int y2, int x3, int y3) {
            // Check if any locations out of bounds
            if (x1>=state.getWidth() || x2>= state.getWidth() || x3>=state.getWidth()||
                    y1>=state.getHeight() || y2>= state.getHeight() || y3>=state.getHeight()) {
                return 0;
            }

            int num_each = (1 << (3 * state.getAt(x0, y0)))
                    + (1 << (3 * state.getAt(x1, y1)))
                    + (1 << (3 * state.getAt(x2, y2)))
                    + (1 << (3 * state.getAt(x3, y3)));

            int num_empty = num_each & 0x7;
            int num_p1 = (num_each >> 3) & 0x7;
            int num_p2 = (num_each >> 6) & 0x7;
            //assume i am 1 and the opponent is 2 for right now
            int my_turn = (player == 1) ? 1 : -1;
//        int my_turn = 1;
            if (num_empty == 4) {
                return 0;
            } else if (num_p1 > 0 && num_p2 > 0) {
                return 0;
            } else if (num_p1==1 && num_empty ==3) {
                return 1 * my_turn;
            } else if (num_p1==2 && num_empty ==2) {
                return 10 * my_turn;
            } else if (num_p1==3 && num_empty ==1) {
//            return state.getActivePlayer() == 1 ? 1000 : 100;
                return 100 * my_turn;
            } else if (num_p1==4) {
                return 1000000000 / state.getCoins() * my_turn;
            } else if (num_p2 ==1 && num_empty ==3) {
                return -1 * my_turn;
            } else if (num_p2 ==2 && num_empty ==2) {
                return -10 * my_turn;
            } else if (num_p2==3 && num_empty ==1) {
//            return state.getActivePlayer() == 1 ? -100 : -1000;
                return -100 * my_turn;
            } else if (num_p2 == 4) {
                return -1000000000 / state.getCoins() * my_turn;
            }
            return 0;
        }

        public int checkRight(final GameStateModule state, int i, int j){
            return check(state, i, j, i + 1, j, i + 2, j, i + 3, j);
        }
        public int checkDown(final GameStateModule state, int i, int j){
            return check(state, i, j, i, j + 1, i, j + 2, i, j + 3);
        }

        public int checkDownRight(final GameStateModule state, int i, int j){
            return check(state, i, j, i + 1, j + 1, i + 2, j + 2, i + 3, j + 3);
        }

        public int checkUpRight(final GameStateModule state, int i, int j){
            return check(state, i, j, i + 1, j - 1, i + 2, j - 2, i + 3, j - 3);
        }


    }
//}
