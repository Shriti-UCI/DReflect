package edu.umich.si.inteco.minukucore.action;

import edu.umich.si.inteco.minukucore.event.ActionEvent;
import edu.umich.si.inteco.minukucore.event.Subscribe;

/**
 * Action {@link edu.umich.si.inteco.minukucore.event.Subscribe subscribes} to an event
 * {@link edu.umich.si.inteco.minukucore.event.ActionEvent} and handles it.
 * This decouples an Action from other parts of the system.
 *
 * Created by neerajkumar on 7/12/16.
 */
public interface Action {

    @Subscribe
    public <T extends ActionEvent> void handleEvent(T actionEvent);

}
