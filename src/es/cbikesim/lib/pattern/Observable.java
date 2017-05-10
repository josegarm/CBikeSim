package es.cbikesim.lib.pattern;

import java.util.ArrayList;
import java.util.List;

public class Observable {

    private List<Observer> observers = new ArrayList<Observer>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    public void notifyAllObservers(){
        for (Observer observer : observers) observer.update();
    }

}
