package Server;

import Client.GameListObserver;
import Game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 2017-12-15.
 */
public class GameList {
    private List<GameListObserver> gameListObserverList = new ArrayList<>();
    private ArrayList<Game> games = new ArrayList<>();

    public ArrayList<Game> getGames() {
        return games;
    }

    public void addGame(Game game){
        this.games.add(game);
        //notifyObservers();

    }

    public void attach(GameListObserver gameListObserver){
        this.gameListObserverList.add(gameListObserver);
    }

    public void detach(GameListObserver gameListObserver){
        this.gameListObserverList.remove(gameListObserver);
    }

    public void notifyObservers(){
        for (GameListObserver gameListObserver : gameListObserverList) {
            gameListObserver.update();
        }
    }
}
