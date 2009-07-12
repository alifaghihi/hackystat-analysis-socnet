//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-474 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.11 at 09:46:06 PM CDT 
//


package org.hackystat.socnet.server.resource.socialmediagraph.jaxb;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.hackystat.socnet.server.resource.socialmediagraph.jaxb package. 
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
    private final static QName _IsBidirectional_QNAME = new QName("", "IsBidirectional");
    private final static QName _ID_QNAME = new QName("", "ID");
    private final static QName _EndTime_QNAME = new QName("", "EndTime");
    private final static QName _StartTime_QNAME = new QName("", "StartTime");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.hackystat.socnet.server.resource.socialmediagraph.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLNodeRef }
     * 
     */
    public XMLNodeRef createXMLNodeRef() {
        return new XMLNodeRef();
    }

    /**
     * Create an instance of {@link XMLNodes }
     * 
     */
    public XMLNodes createXMLNodes() {
        return new XMLNodes();
    }

    /**
     * Create an instance of {@link XMLRelationshipIndex }
     * 
     */
    public XMLRelationshipIndex createXMLRelationshipIndex() {
        return new XMLRelationshipIndex();
    }

    /**
     * Create an instance of {@link XMLNode }
     * 
     */
    public XMLNode createXMLNode() {
        return new XMLNode();
    }

    /**
     * Create an instance of {@link XMLRelationship }
     * 
     */
    public XMLRelationship createXMLRelationship() {
        return new XMLRelationship();
    }

    /**
     * Create an instance of {@link XMLTimeSpan }
     * 
     */
    public XMLTimeSpan createXMLTimeSpan() {
        return new XMLTimeSpan();
    }

    /**
     * Create an instance of {@link XMLRelationshipRef }
     * 
     */
    public XMLRelationshipRef createXMLRelationshipRef() {
        return new XMLRelationshipRef();
    }

    /**
     * Create an instance of {@link XMLNodeIndex }
     * 
     */
    public XMLNodeIndex createXMLNodeIndex() {
        return new XMLNodeIndex();
    }

    /**
     * Create an instance of {@link XMLRelationships }
     * 
     */
    public XMLRelationships createXMLRelationships() {
        return new XMLRelationships();
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IsBidirectional")
    public JAXBElement<Boolean> createIsBidirectional(Boolean value) {
        return new JAXBElement<Boolean>(_IsBidirectional_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ID")
    public JAXBElement<Long> createID(Long value) {
        return new JAXBElement<Long>(_ID_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "EndTime")
    public JAXBElement<XMLGregorianCalendar> createEndTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EndTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "StartTime")
    public JAXBElement<XMLGregorianCalendar> createStartTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_StartTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

}
