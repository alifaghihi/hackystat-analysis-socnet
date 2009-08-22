/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.hackystat.socnet.socialmediagraph.nodes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryChartData;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryPoint;
import org.hackystat.socnet.server.resource.socialmediagraph.jaxb.TelemetryStream;
import org.hackystat.socnet.socialmediagraph.graphmanagement.InvalidArgumentException;
import org.hackystat.utilities.tstamp.Tstamp;
import org.neo4j.api.core.Relationship;


/**
 *
 * @author Rachel Shadoan
 */
public class TelemetryDataStream {
    protected ArrayList<Long> timestamps;
    protected ArrayList<Double> values;
    
    public TelemetryDataStream(long[] times, double[] vals) throws InvalidArgumentException
    {
        if(times.length != vals.length)
            throw new InvalidArgumentException("The timestamp and value arrays " 
                    + "provided as arguments must be of equal length. The " 
                    + "timestamp array provided is of length " + 
                    times.length + " and the value array provided is of length " 
                    + vals.length + ".");
        
        timestamps = new ArrayList<Long>();
        values = new ArrayList<Double>();
        
        for(int i = 0; i<times.length; i++)
        {
            timestamps.add(times[i]);
            values.add(vals[i]);
        }
    }
    
    public TelemetryDataStream(TelemetryStream stream)
    {
        timestamps = new ArrayList<Long>();
        values = new ArrayList<Double>();
        List<TelemetryPoint> tpoints = stream.getTelemetryPoint();
        
        TelemetryPoint tp;
        for(int i = 0; i < tpoints.size(); i++)
        {
            tp = tpoints.get(i);
            timestamps.add(tp.getTime().toGregorianCalendar().getTimeInMillis());
            
            try
            {
                values.add(Double.parseDouble(tp.getValue()));
            }
            catch(NumberFormatException nfe)
            {
                throw new NumberFormatException("Error parsing telemetry values" +
                        "to store. The value passed was " + tp.getValue());
            }
        }
    }
            
    public TelemetryDataStream(TelemetryChartData tcdata) throws InvalidArgumentException, 
            NumberFormatException
    {
        List<TelemetryStream> tstreams = tcdata.getTelemetryStream();
        
        if(tstreams.size() != 1)
            throw new InvalidArgumentException("A TelemetryData object can hold" +
                    "only one Telemetry Stream, and the TelemetryChartData passed" +
                    "as a parameter contains more than one telemetry stream.");
        
        TelemetryStream stream = tstreams.get(0);
        
        List<TelemetryPoint> tpoints = stream.getTelemetryPoint();
        
        TelemetryPoint tp;
        for(int i = 0; i < tpoints.size(); i++)
        {
            tp = tpoints.get(i);
            timestamps.add(tp.getTime().toGregorianCalendar().getTimeInMillis());
            
            try
            {
                values.add(Double.parseDouble(tp.getValue()));
            }
            catch(NumberFormatException nfe)
            {
                throw new NumberFormatException("Error parsing telemetry values" +
                        "to store. The value passed was " + tp.getValue());
            }
        }
   
                   
    }

   TelemetryDataStream(long[] timestampStream, double[] valueStream,
            TelemetryStream stream)
    {
        timestamps = new ArrayList<Long>();
        values = new ArrayList<Double>();
        List<TelemetryPoint> tpoints = stream.getTelemetryPoint();
        
        for(int i = 0; i<timestampStream.length; i++)
        {
            timestamps.add(timestampStream[i]);
            values.add(valueStream[i]);
        }
        
        
        TelemetryPoint tp;
        for(int i = 0; i < tpoints.size(); i++)
        {
            tp = tpoints.get(i);
            
            if(tp.getTime().compare(Tstamp.makeTimestamp(timestampStream.length-1)) 
                    == DatatypeConstants.LESSER || 
                    tp.getTime().compare(Tstamp.makeTimestamp(timestampStream.length-1)) ==
                    DatatypeConstants.EQUAL)
            {
                continue;
            }
            else
            {
                timestamps.add(tp.getTime().toGregorianCalendar().getTimeInMillis());
            }
            
            try
            {
                values.add(Double.parseDouble(tp.getValue()));
            }
            catch(NumberFormatException nfe)
            {
                throw new NumberFormatException("Error parsing telemetry values" +
                        "to store. The value passed was " + tp.getValue());
            }
        }
    }


    public ArrayList<Long> getTimestamps()
    {
        return timestamps;
    }

    public long[] getTimestampsArray()
    {
        long[] times = new long[timestamps.size()];
        
        for(int i = 0; i<timestamps.size(); i++)
        {
            times[i] = (long) timestamps.get(i);
        }
        
        return times;
    }
    
    public void setTimestamps(ArrayList<Long> timestamps)
    {
        this.timestamps = timestamps;
    }

    
    public ArrayList<Double> getValues()
    {
        return values;
    }

    public double[] getValuesArray()
    {
        double[] vals = new double[values.size()];
        
        for(int i = 0; i<values.size(); i++)
        {
            vals[i] = (double) values.get(i);
        }
        
        return vals;
    }
    
    public void setValues(ArrayList<Double> values)
    {
        this.values = values;
    }

    public XMLGregorianCalendar getLastDate()
    {
        long time = timestamps.get(timestamps.size()-1);
        return Tstamp.makeTimestamp(time);
    }
   
}
