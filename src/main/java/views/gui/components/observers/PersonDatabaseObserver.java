package views.gui.components.observers;

import controllers.PersonController;
import database.Database;
import database.Property;
import models.Person;
import views.gui.ViewJ2D;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PersonDatabaseObserver implements PropertyChangeListener {
    Database<Person> personDatabase;
    PersonController personController;
    ViewJ2D viewJ2D;

    public PersonDatabaseObserver(Database<Person> personDatabase, PersonController personController, ViewJ2D viewJ2D) {
        this.personDatabase = personDatabase;
        this.personController = personController;
        this.viewJ2D = viewJ2D;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(Property.CREATE.name)){
            System.out.println("Database updated");
            viewJ2D.getPersonPanel().updatePersonList();
        } else if (evt.getPropertyName().equals(Property.UPDATE.name)){
            viewJ2D.getPersonPanel().updatePersonList();
        }
         else if (evt.getPropertyName().equals(Property.DELETE.name)){

        }

    }
}
