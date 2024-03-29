//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-474 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.17 at 10:50:34 PM CDT 
//

package org.hackystat.socnet.server.resource.users.jaxb;

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
 *       &lt;attribute ref=&quot;{}Email use=&quot;required&quot;&quot;/&gt;
 *       &lt;attribute ref=&quot;{}Href use=&quot;required&quot;&quot;/&gt;
 *       &lt;attribute ref=&quot;{}LastMod use=&quot;required&quot;&quot;/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "XMLUserRef")
public class XMLUserRef implements Serializable {

  private final static long serialVersionUID = 12343L;
  @XmlAttribute(name = "Email", required = true)
  protected String email;
  @XmlAttribute(name = "Href", required = true)
  @XmlSchemaType(name = "anyURI")
  protected String href;
  @XmlAttribute(name = "LastMod", required = true)
  @XmlSchemaType(name = "dateTime")
  protected XMLGregorianCalendar lastMod;

  /**
   * Gets the value of the email property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the value of the email property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setEmail(String value) {
    this.email = value;
  }

  public boolean isSetEmail() {
    return (this.email != null);
  }

  /**
   * Gets the value of the href property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets the value of the href property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setHref(String value) {
    this.href = value;
  }

  public boolean isSetHref() {
    return (this.href != null);
  }

  /**
   * Gets the value of the lastMod property.
   * 
   * @return possible object is {@link XMLGregorianCalendar }
   * 
   */
  public XMLGregorianCalendar getLastMod() {
    return lastMod;
  }

  /**
   * Sets the value of the lastMod property.
   * 
   * @param value allowed object is {@link XMLGregorianCalendar }
   * 
   */
  public void setLastMod(XMLGregorianCalendar value) {
    this.lastMod = value;
  }

  public boolean isSetLastMod() {
    return (this.lastMod != null);
  }

}
