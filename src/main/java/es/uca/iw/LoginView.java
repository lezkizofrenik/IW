package es.uca.iw;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginOverlay;
import es.uca.iw.backend.Usuario;
import es.uca.iw.backend.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.*;

@HtmlImport("frontend://bower_components/iron-form/iron-form.html") //
/**
 * A Designer generated component for the log-in template.
 *
 * Designer will add and remove fields with @Id mappings but
 * does not overwrite or otherwise change this file.
 */
@Route("")
@Tag("login")
@PageTitle("Bienvenidos a Costa Vespertino")
@CssImport("./styles/style.css")
public class LoginView extends HorizontalLayout {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
	public static final CharSequence ROUTE = "";
	private UsuarioService service;
	private AuthenticationManager authenticationManager;
	private LoginOverlay login;

    /**
     * Creates a new LogIn.
     */
    @Autowired
    public LoginView(UsuarioService service, AuthenticationManager authenticationManager) {
    	
    	this.service = service;
        this.authenticationManager = authenticationManager;

        this.login = new LoginOverlay();
        login.setOpened(true);
        login.setI18n(createSpanishI18n());
        login.setForgotPasswordButtonVisible(false);
        add(login);

        Image logo = new Image("https://www.pngrepo.com/png/272288/170/ocean.png", "Logo no encontrado");
        logo.setClassName("imgLogin");

        login.addLoginListener( e->signIn(e));

        add(login);


    }
    private void signIn(AbstractLogin.LoginEvent e)
    {
        if (authenticate(e.getUsername(), e.getPassword()))
        {
        	Usuario user = service.loadUserByUsername(e.getUsername());
            UI.getCurrent().getSession().setAttribute(Usuario.class, user);
            if(user.getRole().equals("Admin") || user.getRole().equals("User") || user.getRole().equals("Gerente")){
            	UI.getCurrent().navigate("mainview");
                UI.getCurrent().getPage().reload();
            }
            
        } else {
            this.login.setError(true);
        }
    }

    public boolean authenticate(String username, String password)
    {
        try{
            Authentication token = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(username,password));
            SecurityContextHolder.getContext().setAuthentication(token);
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    
    }
    private LoginI18n createSpanishI18n() {
        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.setForm(new LoginI18n.Form());
        i18n.getHeader().setTitle("Costa-Vespertino Cruceros");
        i18n.getForm().setUsername("Usuario");
        i18n.getForm().setTitle("Acceda a su cuenta");
        i18n.getForm().setSubmit("Iniciar sesión");
        i18n.getForm().setPassword("Contraseña");
        i18n.getErrorMessage().setTitle("Usuario/Contraseña inválidos");
        i18n.getErrorMessage()
                .setMessage("Confirme el usuario.");

        return i18n;
    }

}