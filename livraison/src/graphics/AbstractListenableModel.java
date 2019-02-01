package graphics;

import java.util.ArrayList;

/**
 * Abstract class to define a listenable model.
 * 
 */
public abstract class AbstractListenableModel implements ListenableModel{

    private final ArrayList<ModelListener> listeners;

    /**
     * Constructor defining ArrayList of model listeners.
     */
    public AbstractListenableModel(){
        this.listeners = new ArrayList<>();
    }

    /**
     * Add a new model listener.
     * @param listener 
     */
    @Override
    public void addListener(ModelListener listener){
        listeners.add(listener);
    }

    /**
     * Remove a model listener.
     * @param listener 
     */
    @Override
    public void removeListener(ModelListener listener){
        listeners.remove(listener);
    }

    /**
     * Method called to prevents listeners the model has been updated.
     */
    public void stateChange() {
        for(ModelListener listener : this.listeners) {                
            listener.update(this);
        }
    }
}
