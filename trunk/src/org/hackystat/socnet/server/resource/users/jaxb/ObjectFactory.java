//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-474 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.11 at 12:20:50 PM CDT 
//


package org.hackystat.socnet.server.resource.users.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.hackystat.socnet.server.resource.users.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Email_QNAME = new QName("", "Email");
    private final static QName _Password_QNAME = new QName("", "Password");
    private final static QName _Role_QNAME = new QName("", "Role");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.hackystat.socnet.server.resource.users.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLUserRef }
     * 
     */
    public XMLUserRef createUserRef() {
        return new XMLUserRef();
    }

    /**
     * Create an instance of {@link XMLUser }
     * 
     */
    public XMLUser createUser() {
        return new XMLUser();
    }

    /**
     * Create an instance of {@link XMLUserIndex }
     * 
     */
    public XMLUserIndex createUserIndex() {
        return new XMLUserIndex();
    }

    /**
     * Create an instance of {@link XMLUsers }
     * 
     */
    public XMLUsers createUsers() {
        return new XMLUsers();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Email")
    public JAXBElement<String> createEmail(String value) {
        return new JAXBElement<String>(_Email_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Password")
    public JAXBElement<String> createPassword(String value) {
        return new JAXBElement<String>(_Password_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Role")
    public JAXBElement<String> createRole(String value) {
        return new JAXBElement<String>(_Role_QNAME, String.class, null, value);
    }

}
