//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-474 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.18 at 12:12:04 AM CDT 
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

    private final static QName _Value_QNAME = new QName("http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", "Value");
    private final static QName _Description_QNAME = new QName("http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", "Description");
    private final static QName _StartTime_QNAME = new QName("http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", "StartTime");
    private final static QName _EndTime_QNAME = new QName("http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", "EndTime");
    private final static QName _Default_QNAME = new QName("http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", "Default");
    private final static QName _ID_QNAME = new QName("http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", "ID");
    private final static QName _SourceCode_QNAME = new QName("http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", "SourceCode");
    private final static QName _Name_QNAME = new QName("http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", "Name");
    private final static QName _Type_QNAME = new QName("http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", "Type");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.hackystat.socnet.server.resource.socialmediagraph.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link XMLRelationshipRef }
     * 
     */
    public XMLRelationshipRef createXMLRelationshipRef() {
        return new XMLRelationshipRef();
    }

    /**
     * Create an instance of {@link XMLNodeRef }
     * 
     */
    public XMLNodeRef createXMLNodeRef() {
        return new XMLNodeRef();
    }

    /**
     * Create an instance of {@link TelemetryChartData }
     * 
     */
    public TelemetryChartData createTelemetryChartData() {
        return new TelemetryChartData();
    }

    /**
     * Create an instance of {@link TelemetryStream }
     * 
     */
    public TelemetryStream createTelemetryStream() {
        return new TelemetryStream();
    }

    /**
     * Create an instance of {@link TelemetryPoint }
     * 
     */
    public TelemetryPoint createTelemetryPoint() {
        return new TelemetryPoint();
    }

    /**
     * Create an instance of {@link TelemetryChartIndex }
     * 
     */
    public TelemetryChartIndex createTelemetryChartIndex() {
        return new TelemetryChartIndex();
    }

    /**
     * Create an instance of {@link XMLTimeSpan }
     * 
     */
    public XMLTimeSpan createXMLTimeSpan() {
        return new XMLTimeSpan();
    }

    /**
     * Create an instance of {@link XMLNodes }
     * 
     */
    public XMLNodes createXMLNodes() {
        return new XMLNodes();
    }

    /**
     * Create an instance of {@link TelemetryChartRef }
     * 
     */
    public TelemetryChartRef createTelemetryChartRef() {
        return new TelemetryChartRef();
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
     * Create an instance of {@link TelemetryChartDefinition }
     * 
     */
    public TelemetryChartDefinition createTelemetryChartDefinition() {
        return new TelemetryChartDefinition();
    }

    /**
     * Create an instance of {@link XMLRelationshipIndex }
     * 
     */
    public XMLRelationshipIndex createXMLRelationshipIndex() {
        return new XMLRelationshipIndex();
    }

    /**
     * Create an instance of {@link XMLRelationships }
     * 
     */
    public XMLRelationships createXMLRelationships() {
        return new XMLRelationships();
    }

    /**
     * Create an instance of {@link Parameter }
     * 
     */
    public Parameter createParameter() {
        return new Parameter();
    }

    /**
     * Create an instance of {@link XMLNodeIndex }
     * 
     */
    public XMLNodeIndex createXMLNodeIndex() {
        return new XMLNodeIndex();
    }

    /**
     * Create an instance of {@link ParameterDefinition }
     * 
     */
    public ParameterDefinition createParameterDefinition() {
        return new ParameterDefinition();
    }

    /**
     * Create an instance of {@link YAxis }
     * 
     */
    public YAxis createYAxis() {
        return new YAxis();
    }

    /**
     * Create an instance of {@link Type }
     * 
     */
    public Type createType() {
        return new Type();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", name = "Value")
    public JAXBElement<String> createValue(String value) {
        return new JAXBElement<String>(_Value_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", name = "Description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", name = "StartTime")
    public JAXBElement<XMLGregorianCalendar> createStartTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_StartTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", name = "EndTime")
    public JAXBElement<XMLGregorianCalendar> createEndTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_EndTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", name = "Default")
    public JAXBElement<String> createDefault(String value) {
        return new JAXBElement<String>(_Default_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", name = "ID")
    public JAXBElement<Long> createID(Long value) {
        return new JAXBElement<Long>(_ID_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", name = "SourceCode")
    public JAXBElement<String> createSourceCode(String value) {
        return new JAXBElement<String>(_SourceCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", name = "Name")
    public JAXBElement<String> createName(String value) {
        return new JAXBElement<String>(_Name_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://hackystat-analysis-socnet.googlecode.com/svn/trunk/xml/schema/socialmediagraph.xsd", name = "Type")
    public JAXBElement<String> createType(String value) {
        return new JAXBElement<String>(_Type_QNAME, String.class, null, value);
    }

}
