//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-474 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.10 at 05:36:14 PM CDT 
//


package org.hackystat.socnet.server.resource.nodes.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.hackystat.socnet.server.resource.nodes.jaxb package. 
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

    private final static QName _Name_QNAME = new QName("", "Name");
    private final static QName _Type_QNAME = new QName("", "Type");
    private final static QName _ID_QNAME = new QName("", "ID");
    private final static QName _CreatedAt_QNAME = new QName("", "CreatedAt");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.hackystat.socnet.server.resource.nodes.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NodeIndex }
     * 
     */
    public NodeIndex createNodeIndex() {
        return new NodeIndex();
    }

    /**
     * Create an instance of {@link Nodes }
     * 
     */
    public Nodes createNodes() {
        return new Nodes();
    }

    /**
     * Create an instance of {@link Node }
     * 
     */
    public Node createNode() {
        return new Node();
    }

    /**
     * Create an instance of {@link NodeRef }
     * 
     */
    public NodeRef createNodeRef() {
        return new NodeRef();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "Type")
    public JAXBElement<String> createType(String value) {
        return new JAXBElement<String>(_Type_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ID")
    public JAXBElement<Integer> createID(Integer value) {
        return new JAXBElement<Integer>(_ID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "CreatedAt")
    public JAXBElement<XMLGregorianCalendar> createCreatedAt(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_CreatedAt_QNAME, XMLGregorianCalendar.class, null, value);
    }

}
