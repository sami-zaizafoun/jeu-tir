package graphics;

import java.util.ArrayList;


public interface ListenableModel {
    ArrayList<ModelListener> listeners = new ArrayList<>();
    
    void addListener(ModelListener listener);
    
    void removeListener(ModelListener listener);

}
