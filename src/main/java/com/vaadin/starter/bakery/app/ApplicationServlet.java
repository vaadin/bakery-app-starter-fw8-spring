package com.vaadin.starter.bakery.app;

import javax.servlet.ServletException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServletService;
import com.vaadin.spring.server.SpringVaadinServlet;

public class ApplicationServlet extends SpringVaadinServlet implements SessionInitListener {

	private static final String[] ICON_RELS = { "icon", "apple-touch-icon" };
	private static final int[] ICON_SIZES = { 192, 96 };

	@Override
	protected void servletInitialized() throws ServletException {
		super.servletInitialized();
		VaadinServletService service = getService();
		service.addSessionInitListener(this);
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		event.getSession().addBootstrapListener(new BootstrapListener() {

			@Override
			public void modifyBootstrapPage(BootstrapPageResponse response) {
				// Generate link-tags for "add to homescreen" icons
				final Document document = response.getDocument();
				final Element head = document.getElementsByTag("head").get(0);
				for (String rel : ICON_RELS) {
					for (int size : ICON_SIZES) {
						String iconUri = "theme://icon-" + size + ".png";
						String href = response.getUriResolver().resolveVaadinUri(iconUri);
						String sizes = size + "x" + size;
						Element element = document.createElement("link");
						element.attr("rel", rel);
						element.attr("href", href);
						element.attr("sizes", sizes);
						head.appendChild(element);
					}
				}
			}

			@Override
			public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
				// NOP
			}
		});

	}

}
