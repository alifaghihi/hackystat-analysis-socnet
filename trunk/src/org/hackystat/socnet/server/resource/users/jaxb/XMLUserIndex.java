//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-474 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.07.11 at 12:20:50 PM CDT 
//


package org.hackystat.socnet.server.resource.users.jaxb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}XMLUserRef" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute ref="{}LastMod use="required""/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "userRef"
})
@XmlRootElement(name = "UserIndex")
public class XMLUserIndex
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(name = "UserRef")
    protected List<XMLUserRef> userRef;
    @XmlAttribute(name = "LastMod", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastMod;

    /**
     * Gets the value of the userRef property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userRef property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserRef().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLUserRef }
     * 
     * 
     */
    public List<XMLUserRef> getUserRef() {
        if (userRef == null) {
            userRef = new ArrayList<XMLUserRef>();
        }
        return this.userRef;
    }

    public boolean isSetUserRef() {
        return ((this.userRef!= null)&&(!this.userRef.isEmpty()));
    }

    public void unsetUserRef() {
        this.userRef = null;
    }

    /**
     * Gets the value of the lastMod property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastMod() {
        return lastMod;
    }

    /**
     * Sets the value of the lastMod property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastMod(XMLGregorianCalendar value) {
        this.lastMod = value;
    }

    public boolean isSetLastMod() {
        return (this.lastMod!= null);
    }

}