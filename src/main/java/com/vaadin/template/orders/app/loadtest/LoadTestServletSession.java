package com.vaadin.template.orders.app.loadtest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;

/**
 * Extended VaadinSession to enable stable load testing with component ids used
 * for communication
 */
public class LoadTestServletSession extends VaadinSession {

	private Map<Class<? extends ClientConnector>, Integer> sequences = new HashMap<>();
	private Set<String> usedConnectorIds = new HashSet<>();

	public LoadTestServletSession(VaadinService service) {
		super(service);
	}

	/**
	 * Override default implementation for making the connector ids more
	 * suitable for load testing
	 *
	 * @param connector
	 * @deprecated do not use unless in load testing mode
	 * @return
	 */
	@Deprecated
	@Override
	public String createConnectorId(ClientConnector connector) {
		String connectorId = "";

		if (connector instanceof Component) {
			Component component = (Component) connector;

			if (!usedConnectorIds.contains(component.getId())) {
				connectorId = component.getId() == null ? super.createConnectorId(connector) : component.getId();
			} else {
				// prevent two accidentally identical connector ids
				connectorId = component.getId() == null ? super.createConnectorId(connector)
						: component.getId() + nextId(connector.getClass());
			}
		} else if (connector.getClass().equals(DataCommunicator.class)) {
			// treat separately since it's not possible set id for this
			connectorId = "datareqrpc-" + nextId(connector.getClass());
		} else {
			connectorId = super.createConnectorId(connector);
		}
		usedConnectorIds.add(connectorId);
		return connectorId;
	}

	private int nextId(Class<? extends ClientConnector> c) {
		Integer nextId = 0;
		if (sequences.get(c) != null) {
			nextId = sequences.get(c);
		}
		sequences.put(c, nextId + 1);
		return nextId;
	}
}
