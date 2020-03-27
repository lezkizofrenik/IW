package es.uca.iw;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;

public class AbstractView extends VerticalLayout implements BeforeEnterObserver {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public void beforeEnter(final BeforeEnterEvent event) {
        final boolean accessGranted = SecurityUtils.isAccessGranted(event.getNavigationTarget());

        if (!accessGranted) {
            if (SecurityUtils.isUserLoggedIn()) {
                event.rerouteTo(ErrorView.class);
            } else {
                event.rerouteTo(LoginView.class);
            }
        }
    }
}
