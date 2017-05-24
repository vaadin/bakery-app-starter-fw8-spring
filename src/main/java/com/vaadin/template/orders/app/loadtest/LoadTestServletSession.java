package com.vaadin.template.orders.app.loadtest;

import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Extended VaadinSession to enable stable load testing with component ids used
 * for communication
 */
public class LoadTestServletSession extends VaadinSession {

    public LoadTestServletSession(VaadinService service) {
        super(service);
    }

    private Map<Class<? extends ClientConnector>, Integer> sequences = new HashMap<Class<? extends ClientConnector>, Integer>();
    private Set<String> usedConnectorIds = new HashSet<String>();

    @SuppressWarnings("deprecation")
    @Override
    public String createConnectorId(ClientConnector connector) {
        String connectorId = "";

        if (connector instanceof Component) {
            Component component = (Component) connector;

            if (!usedConnectorIds.contains(component.getId())) {
                connectorId = component.getId() == null ? super
                        .createConnectorId(connector) : component.getId();
            }
            else {
                // prevent two accidentally identical connector ids
                connectorId = component.getId() == null ? super
                        .createConnectorId(connector) : component.getId()
                        + nextId(connector.getClass());
            }
        } else if (connector.getClass().equals(DataCommunicator.class)) {
            connectorId = "datareqrpc-" + nextId(connector.getClass());
        } else {
            connectorId = super.createConnectorId(connector);
        }
        usedConnectorIds.add(connectorId);
        return connectorId;
    }

    private int nextId(Class<? extends ClientConnector> c) {
        Integer nextid = 0;
        if (sequences.get(c) != null) {
            nextid = sequences.get(c);
        }
        sequences.put(c, nextid + 1);
        return nextid;
    }
}
