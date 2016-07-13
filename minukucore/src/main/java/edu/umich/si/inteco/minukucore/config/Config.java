package edu.umich.si.inteco.minukucore.config;

/**
 * Created by Neeraj Kumar on 7/12/2016.
 *
 * Config is used to CRUD config values for the application.
 * The config values must be persisted across session runs.
 * The specification of where such persistence is done is
 * out of the scope of the interface.
 */
public interface Config {

    /**
     * Given a configName and configValue, updates the
     * configValue if configName already exists, or
     * creates and stores a new configName with the
     * given configValue.
     *
     * @param configName
     */
    public void create(String configName, String configValue);

    /**
     * Returns the configValue associated with a configName.
     *
     * @param configName
     * @return
     */
    public String get(String configName);

    /**
     * Removes the entry for configName from the persisted storage.
     *
     * @param configName
     * @return
     */
    public boolean remove(String configName);


}
