package seedu.ddd.model.event.common;

import static seedu.ddd.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import seedu.ddd.commons.util.ToStringBuilder;
import seedu.ddd.model.Displayable;
import seedu.ddd.model.common.Id;
import seedu.ddd.model.common.Name;
import seedu.ddd.model.contact.client.Client;
import seedu.ddd.model.contact.common.Contact;
import seedu.ddd.model.contact.exceptions.ContactNotFoundException;
import seedu.ddd.model.contact.vendor.Vendor;

/**
 * Represents a Event in the address book.
 * Guarantees: to be determined.
 */
public class Event implements Displayable {

    public static final String MESSAGE_CONSTRAINTS =
            "There must be at least one client in a specific event.";

    // Identity fields
    private final Name name;
    private final Description description;
    private final Date date;
    private final Id eventId;

    // References
    private final List<Client> clients;
    private final List<Vendor> vendors;

    /**
     * Constructs a {@code Event}.
     *
     * @param clients A list of client.
     * @param vendors A list of vendors.
     */
    public Event(
        Name name,
        Description description,
        Date date,
        List<Client> clients,
        List<Vendor> vendors,
        Id eventId
    ) {
        requireAllNonNull(name, description, date, clients, vendors, eventId);
        assert !clients.isEmpty();

        this.name = name;
        this.description = description;
        this.date = date;
        this.clients = new ArrayList<>(clients);
        this.vendors = new ArrayList<>(vendors);
        this.eventId = eventId;
    }

    /**
     * Constructs a {@code Event}.
     *
     * Alternative constructor that defers the loading of clients and vendors.
     */
    public Event(Name name, Description description, Date date, Id eventId) {
        requireAllNonNull(name, description, date, eventId);

        this.name = name;
        this.date = date;
        this.description = description;
        this.clients = new ArrayList<>();
        this.vendors = new ArrayList<>();
        this.eventId = eventId;
    }

    /**
     * Returns the contact list.
     * @return A {@code List} of {@code Contacts}.
     */
    public List<Contact> getContacts() {
        List<Contact> contacts = new ArrayList<>();
        contacts.addAll(clients);
        contacts.addAll(vendors);
        return Collections.unmodifiableList(contacts);
    }

    /**
     * Adds a {@code Client} to an {@code Event}
     * If the current event is not stored in the client's set of events,
     * it will add the association
     */
    public void addClient(Client client) {
        clients.add(client);
        Set<Event> events = client.getEvents();
        if (!events.contains(this)) {
            client.addEvent(this);
        }
    }

    /**
     * Adds a {@code Vendor} to an {@code Event}
     * If the current event is not stored in the vendor's set of events,
     * it will add the association
     */
    public void addVendor(Vendor vendor) {
        vendors.add(vendor);
        Set<Event> events = vendor.getEvents();
        if (!events.contains(this)) {
            vendor.addEvent(this);
        }
    }

    /**
     * Removes a {@code Contact} from an {@code Event}
     * Removes the association of the current event in the contact as well.
     */
    public void removeContact(Contact contact) {
        if (contact instanceof Client client) {
            if (!clients.contains(client)) {
                throw new ContactNotFoundException();
            }
            Set<Event> clientEvents = client.getEvents();
            if (clientEvents.contains(this)) {
                client.removeEvent(this);
            }
            clients.remove(client);
        } else if (contact instanceof Vendor vendor) {
            if (!vendors.contains(vendor)) {
                throw new ContactNotFoundException();
            }
            Set<Event> vendorEvents = vendor.getEvents();
            if (vendorEvents.contains(this)) {
                vendor.removeEvent(this);
            }
            vendors.remove(vendor);
        }
    }

    /**
     * Returns the event name.
     * @return A {@code Name} which represents the name of the event.
     */
    public Name getName() {
        return this.name;
    }

    /**
     * Returns the event description.
     * @return A {@code Description} which represents the description of the event.
     */
    public Description getDescription() {
        return this.description;
    }

    /**
     * Returns the event date.
     * @return A {@code Date} which represents the date of the event.
     */
    public Date getDate() {
        return this.date;
    }

    /**
     * Returns the client list.
     * @return A {@code List} of {@code Clients}.
     */
    public List<Client> getClients() {
        return Collections.unmodifiableList(clients);
    }

    /**
     * Returns the vendor list.
     * @return A {@code List} of {@code Vendors}.
     */
    public List<Vendor> getVendors() {
        return Collections.unmodifiableList(vendors);
    }

    /**
     * Returns the list of client ids.
     * @return A {@code List} of {@code ContactId}.
     */
    public List<Id> getClientIds() {
        return clients.stream().map(Contact::getId).toList();
    }

    /**
     * Returns the list of vendor ids.
     * @return A {@code List} of {@code ContactId}.
     */
    public List<Id> getVendorIds() {
        return vendors.stream().map(Contact::getId).toList();
    }

    /**
     * Returns the unique event ID.
     */
    public Id getEventId() {
        return this.eventId;
    }

    /**
     * Return if the two events are the same.
     *
     * Two events are considered the same if they have the same name.
     * @param otherEvent Another event.
     * @return A boolean value which represents the result.
     */
    public boolean isSameEvent(Event otherEvent) {
        return this.getName().equals(otherEvent.getName());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Event)) {
            return false;
        }

        Event otherEvent = (Event) other;

        return this.getName().equals(otherEvent.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClients(), this.getVendors(),
                this.getDescription(), this.getEventId());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", this.getName())
                .add("description", this.getDescription())
                .add("date", this.getDate())
                .add("clients", this.getClients())
                .add("vendors", this.getVendors())
                .add("id", eventId)
                .toString();
    }
}
