package edu.umich.si.inteco.minukucore.event;

/**
 * The EventBus will be an object-agnostic event bus and must use a POJO as an Event type
 * and must allow any type of class to subscribe to any kind of event.
 *
 * This is inspired by the EventBus design by GreenRobot.
 * @see <a href="https://github.com/greenrobot/EventBus/tree/master/EventBus/src/org/greenrobot/eventbus">
 *     Git Repo for Green Robot Event Bus
 *     </a>
 * Created by Neeraj Kumar on 7/12/2016.
 */
public interface EventBus {

    /**
     * Registers an object as a subscriber with the EventBus. The subscriber is responsible for
     * using {@link Subscribe subscribe} annotation with one of the methods within itself as a
     * handler for one of the events which the subscriber is interested in.
     * @param subscriber
     */
    public void register(Object subscriber);

    /**
     * Unregisters an object as a subscriber. This will remove all subscriptions that the object
     * did using the {@link Subscribe subscribe} annotation for the handlers within the object.
     * @param subscriber
     */
    public void unregister(Object subscriber);

}
