//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-474 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.08.18 at 12:12:04 AM CDT 
//

package org.hackystat.socnet.server.resource.socialmediagraph.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *       &lt;sequence&gt;
 *         &lt;element ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}Value&quot; maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}Name use=&quot;required&quot;&quot;/&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}Default use=&quot;required&quot;&quot;/&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}MinValue&quot;/&gt;
 *       &lt;attribute ref=&quot;{http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd}MaxValue&quot;/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "value" })
@XmlRootElement(name = "Type")
public class Type implements Serializable {

  private final static long serialVersionUID = 12343L;
  @XmlElement(name = "Value")
  protected List<String> value;
  @XmlAttribute(name = "Name", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", required = true)
  protected String name;
  @XmlAttribute(name = "Default", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd", required = true)
  protected String _default;
  @XmlAttribute(name = "MinValue", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd")
  protected String minValue;
  @XmlAttribute(name = "MaxValue", namespace = "http://hackystat-analysis-telemetry.googlecode.com/svn/trunk/xml/schema/telemetry.resource.xsd")
  protected String maxValue;

  /**
   * Gets the value of the value property.
   * 
   * <p>
   * This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the value property.
   * 
   * <p>
   * For example, to add a new item, do as follows:
   * 
   * <pre>
   * getValue().add(newItem);
   * </pre>
   * 
   * 
   * <p>
   * Objects of the following type(s) are allowed in the list {@link String }
   * 
   * 
   */
  public List<String> getValue() {
    if (value == null) {
      value = new ArrayList<String>();
    }
    return this.value;
  }

  public boolean isSetValue() {
    return ((this.value != null) && (!this.value.isEmpty()));
  }

  public void unsetValue() {
    this.value = null;
  }

  /**
   * Gets the value of the name property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the value of the name property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setName(String value) {
    this.name = value;
  }

  public boolean isSetName() {
    return (this.name != null);
  }

  /**
   * Gets the value of the default property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getDefault() {
    return _default;
  }

  /**
   * Sets the value of the default property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setDefault(String value) {
    this._default = value;
  }

  public boolean isSetDefault() {
    return (this._default != null);
  }

  /**
   * Gets the value of the minValue property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getMinValue() {
    return minValue;
  }

  /**
   * Sets the value of the minValue property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setMinValue(String value) {
    this.minValue = value;
  }

  public boolean isSetMinValue() {
    return (this.minValue != null);
  }

  /**
   * Gets the value of the maxValue property.
   * 
   * @return possible object is {@link String }
   * 
   */
  public String getMaxValue() {
    return maxValue;
  }

  /**
   * Sets the value of the maxValue property.
   * 
   * @param value allowed object is {@link String }
   * 
   */
  public void setMaxValue(String value) {
    this.maxValue = value;
  }

  public boolean isSetMaxValue() {
    return (this.maxValue != null);
  }

}
