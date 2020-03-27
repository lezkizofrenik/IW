package es.uca.iw;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.data.validator.EmailValidator;

import es.uca.iw.backend.*;

import java.util.List;

public class UsuarioForm extends FormLayout {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private TextField id = new TextField("Id");

    private TextField username = new TextField("Nombre usuario");
    private TextField nombre = new TextField("Nombre");
    private TextField apellido1 = new TextField("Primer Apellido");
    private TextField apellido2 = new TextField("Segundo Apellido");
    private TextField dni = new TextField("Dni");
    private TextField telefono = new TextField("Teléfono");
    private EmailField email = new EmailField("Email");
    private ComboBox<String> rol = new ComboBox<>();
    private ComboBox<Ship> ship = new ComboBox<>("Barco");
    private PasswordField password = new PasswordField("Contraseña");
    private BeanValidationBinder<Usuario> binder = new BeanValidationBinder<>(Usuario.class);
    private UsuarioService service;
    private Button save = new Button("Continuar");
    private boolean isNew = false;
    private Usuario usuario;
    private BankAccountService bas;
    private UsuarioView usuarioView;
    private Button delete = new Button("Borrar");
    private boolean isReading = false;


    public UsuarioForm(UsuarioView usuarioView, UsuarioService service, BankAccountService bas, ShipService sp,
                       ReservationEstablishmentService reservationEstablishmentService, ReservationActivityService reservationActivityService,
                       ReservationRestaurantService reservationRestaurantService, ReservationHikeService reservationHikeService) {
    	ship.setItems(sp.listarShip());

        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null && !isNew) {
            usuario = service.listarPorUsername(UI.getCurrent().getSession().getAttribute(Usuario.class).getUsername());
            usuario.setPassword("");
            id.setValue(usuario.getId().toString());
            ship.setValue(usuario.getShip());
            binder.setBean(usuario);
            this.bas = bas;
        } else
            usuario = new Usuario();
        this.service = service;
        this.usuarioView = usuarioView;
        id.setVisible(false);
        nombre.setRequired(true);
        apellido1.setRequired(true);
        apellido2.setRequired(true);
        dni.setRequired(true);
        email.setRequiredIndicatorVisible(true);
        password.setRequired(true);
        username.setRequired(true);
        ship.setRequired(true);
        rol.setRequired(true);
        username.addValueChangeListener(e -> {
        	if(!isReading) {


        		if (service.listarPorUsername(username.getValue()) != null && usuario == null) {
        			username.clear();
        			Notification.show("Usuario repetido", 3000, Notification.Position.BOTTOM_START);
        		} else {
        			if (service.listarPorUsername(username.getValue()) != null && usuario != null
        					&& !service.listarPorUsername(username.getValue()).getId().equals(usuario.getId())) {
        				username.clear();
        				Notification.show("Usuario repetido", 3000, Notification.Position.BOTTOM_START);
        			}
        		}
        	}
        });
        email.addValueChangeListener(e -> {
        	if(!isReading) {
	            if (service.listarPorEmail(email.getValue()) != null && usuario == null) {
	                email.clear();
	                Notification.show("Email repetido", 3000, Notification.Position.BOTTOM_START);
	            } else {
	                if (service.listarPorEmail(email.getValue()) != null && usuario != null
	                        && !service.listarPorEmail(email.getValue()).getId().equals(usuario.getId())) {
	                    email.clear();
	                    Notification.show("Email repetido", 3000, Notification.Position.BOTTOM_START);
	                }
	            }
        	}
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        if (UI.getCurrent().getSession().getAttribute(Usuario.class) != null &&
        		UI.getCurrent().getSession().getAttribute(Usuario.class).getRole().equals("Admin")) {
        	rol.setItems("Admin", "User", "Gerente");
        	rol.setValue(UI.getCurrent().getSession().getAttribute(Usuario.class).getRole());
        	rol.setLabel("Rol");
        	ship.setLabel("Barco");
        	ship.setValue(UI.getCurrent().getSession().getAttribute(Usuario.class).getShip());
            delete.addClickListener(event -> {
                if (binder.getBean() != null)
                    delete(bas, service, reservationEstablishmentService, reservationActivityService,
                           reservationRestaurantService,reservationHikeService);
            });
            HorizontalLayout h = new HorizontalLayout();
            h.add(save, delete);
        	add( username, nombre, apellido1, apellido2, dni, telefono, email, password, rol, ship, h);
        }else {
        	add( username, nombre, apellido1, apellido2, dni, telefono, email, password, save);

        }
        save.addClickShortcut(Key.ENTER);
        binder.forField(id)
        .withConverter(
          new StringToLongConverter("Ha de introducir un número"))
        .bind(Usuario::getId, Usuario::setId);
        binder.bindInstanceFields(this);

        Dialog dialog = new Dialog();

        Button confirmButton = new Button("Confirmar", event -> {
            save();
            this.usuarioView.updateList();
            dialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        Button cancelButton = new Button("Cancelar", event -> {
            dialog.close();
        });
        dialog.add(confirmButton, cancelButton);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        save.addClickListener(event -> dialog.open());
        confirmButton.addClickShortcut(Key.ENTER);

    }

    public void save() {
        binder.forField(email)
                // Explicit validator instance
                .withValidator(
                        new EmailValidator("No es un formato válido. Ejemplo de formato válido: usuario@gmail.com"))
                .bind(Usuario::getEmail, Usuario::setEmail);
        if (binder.validate().isOk()) {
        	if(UI.getCurrent().getSession().getAttribute(Usuario.class) != null &&
        		UI.getCurrent().getSession().getAttribute(Usuario.class).getRole().equals("Admin"))
        		usuario.setRole(rol.getValue());
        	else
        		usuario.setRole("User");
        	
        	usuario.setId(Long.parseLong(id.getValue()));
            usuario.setUsername(username.getValue());
            usuario.setNombre(nombre.getValue());
            usuario.setApellido1(apellido1.getValue());
            usuario.setApellido2(apellido2.getValue());
            BankAccount ba = new BankAccount(0,1000);
            bas.guardarBankAccount(ba);
            usuario.setBankAccount(ba); //Dinero de prueba
            
            usuario.setDni(dni.getValue());
            usuario.setTelefono(telefono.getValue());
            usuario.setEmail(email.getValue());
            usuario.setPassword(password.getValue());
            usuario.setShip(ship.getValue());
            
            if(isReading)
            	service.save(usuario);
            else
            	service.create(usuario);
            

                Notification.show("Guardado con exito", 3000, Notification.Position.BOTTOM_START);

        } else 
            Notification.show("Rellene los campos", 3000, Notification.Position.BOTTOM_START);
        
    }
    public void delete(BankAccountService bas, UsuarioService service, ReservationEstablishmentService reservationEstablishmentService, ReservationActivityService reservationActivityService,
                       ReservationRestaurantService reservationRestaurantService, ReservationHikeService reservationHikeService) {
        Usuario u = binder.getBean();
        if (binder.validate().isOk()) {
            reservationEstablishmentService.deleteByUser(u);
            reservationHikeService.deleteByUser(u);
            reservationRestaurantService.deleteByUser(u);
            reservationActivityService.deleteByUser(u);
            service.delete(u);
        	bas.borrarBankAccount(u.getBankAccount());
            this.usuarioView.updateList();
            setUsuario(new Usuario());
        } else
            Notification.show("Rellene los campos", 3000, Notification.Position.BOTTOM_START);
    }
	public void setUsuario(Usuario usuario2) {
        if(usuario2 == null){
        	id.setValue("0");
        	username.clear();
        	nombre.clear();
        	apellido1.clear();
        	apellido2.clear();
        	dni.clear();
        	email.clear();
        	password.clear();
            telefono.clear();
            rol.clear();
            ship.clear();
        	setVisible(false);
        	ship.setVisible(false);
        	if(isNew && !isReading) {
            	setVisible(true);

	         	nombre.setVisible(true);
	            apellido1.setVisible(true);
	            apellido2.setVisible(true);
	            dni.setVisible(true);
	            email.setVisible(true);
	            password.setVisible(true);
	            username.setVisible(true);
	            telefono.setVisible(true);
	            rol.setVisible(true);
	            ship.setVisible(true);
        	}
        }else{
            binder.setBean(usuario2);
            
            setVisible(true);
            ship.setVisible(true);
            if(isReading) {
                id.setValue(usuario2.getId().toString());
                rol.setValue(usuario2.getRole());
            	nombre.setVisible(false);
                apellido1.setVisible(false);
                apellido2.setVisible(false);
                dni.setVisible(false);
                email.setVisible(false);
                password.setVisible(false);
                username.setVisible(false);
                telefono.setVisible(false);
                rol.setVisible(false);
            	
            }
        }

		
	}
	public void setNew(boolean isNew) {
		this.isNew = isNew;
        usuario = new Usuario();

	}

	public void setLectura(boolean b) {
		isReading = b;
		save.setVisible(true);
			
	}

}