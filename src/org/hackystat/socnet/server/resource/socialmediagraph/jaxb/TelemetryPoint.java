//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-474 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.18 at 12:12:04 AM CDT 
//

package org.hackystat.socnet.server.resource.socialmediagraph.jaxb;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}Time use=&quot;required&quot;&quot;/&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}Value use=&quot;required&quot;&quot;/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "TelemetryPoint")
public class TelemetryPoint implements Serializable {

  private final static long serialVersionUID = 12343L;
  @XmlAttribute(name = "Time", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", required = true)
  @XmlSchemaType(name = "date")
  protected XMLGregorianCalendar time;
  @XmlAttribute(name = "Value", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", required = true)
  protected String value;

  /**
   * Gets the value of the time property.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getTime() {
    return time;
  }

  /**
   * Sets the value of the time property.
   * 
   * @param value allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setTime(XMLGregorianCalendar value) {
    this.time = value;
  }

  public boolean isSetTime() {
    return (this.time != null);
  }

  /**
   * Gets the value of the value property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the value property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setValue(String value) {
    this.value = value;
  }

  public boolean isSetValue() {
    return (this.value != null);
  }

}
