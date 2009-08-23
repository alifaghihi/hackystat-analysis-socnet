/*
 * SelectionGUI.java
 *
 * Created on August 3, 2009, 12:08 AM
 */
package org.hackystat.socnet.sensors.hackystatclient;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.hackystat.sensorbase.client.SensorBaseClient;
import org.hackystat.sensorbase.client.SensorBaseClientException;
import org.hackystat.sensorbase.resource.projects.jaxb.Project;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectIndex;
import org.hackystat.sensorbase.resource.projects.jaxb.ProjectRef;
import org.hackystat.socnet.server.client.SocNetClient;

/**
 * 
 * @author cody
 */
public class SelectionGUI extends javax.swing.JFrame {
    
    /** This is a necessary field for serializable */
  public static final long serialVersionUID = 1l;

  private ArrayList<String> projectNames;
  private HashMap<String, Boolean> sendToSocnet;
  private HashMap<String, Date> startDates;
  private HashMap<String, Date> endDates;
  
  /** Creates new form SelectionGUI */
  public SelectionGUI(ArrayList<String> projects, HashMap<String, Date> starts,
          HashMap<String, Date> ends)
      throws SensorBaseClientException, IOException, ParseException {

    projectNames = projects;
    sendToSocnet = new HashMap<String, Boolean>();
    startDates = starts;
    endDates = ends;
    
    initComponents();

    loadData(); //Load the data here.
    setControlsEnabled(false);
    
    String projectName = (String) projectList.getSelectedValue();
    Date startDate = new GregorianCalendar(2001, Calendar.JANUARY, 1).getTime();
    Date endDate = new GregorianCalendar().getTime();

    if (projectName != null) {
      /*
       * Project project = sensorBaseClient.getProject(email, projectName); XMLGregorianCalendar
       * startTime = project.getStartTime(); startDate = startTime.toGregorianCalendar().getTime();
       * endDate = project.getEndTime().toGregorianCalendar().getTime();
       */
    }

    startDateChooser.setDate(startDate);
    endDateChooser.setDate(endDate);
  }

  private SelectionGUI() throws IOException, ParseException, SensorBaseClientException {
    initComponents();
    loadData();
  }
  
  private void loadData() throws IOException, ParseException, SensorBaseClientException
  {
      DefaultListModel lm = new DefaultListModel();
      
      for(String s : projectNames)
      {
          lm.addElement(s);
      }
      
      projectList.setModel(lm);
      
      //Load things from config
      ClientConfig conf = new ClientConfig();
      
      for(String proj : conf.getProjectNames())
      {
          sendToSocnet.put(proj, Boolean.TRUE);
          startDates.put(proj, conf.getStartDate(proj));
          endDates.put(proj, conf.getEndDate(proj));
      }
      
      this.invalidate();
  }

  private void setControlsEnabled(boolean enabled)
  {
      startDateChooser.setEnabled(enabled);
      endDateChooser.setEnabled(enabled);
      sendCheckBox.setEnabled(enabled);
  }
  
  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        projectList = new javax.swing.JList();
        selectionLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        startDateChooser = new com.toedter.calendar.JDateChooser();
        startDateLabel = new javax.swing.JLabel();
        endDateChooser = new com.toedter.calendar.JDateChooser();
        endDateLabel = new javax.swing.JLabel();
        sendCheckBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        projectList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        projectList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                projectListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(projectList);

        selectionLabel.setText("Select the project for which you want to send data to the SocNet server.");

        saveButton.setText("Save and Close");
        saveButton.setToolTipText("Submit this project's telemetry data to the SocNet Server");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        startDateChooser.setToolTipText("");
        startDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                startDateChooserPropertyChange(evt);
            }
        });

        startDateLabel.setText("Start Date");
        startDateLabel.setToolTipText("<html>Set the start date for the project here. <br><br>Telemetry data for this project between the start date and the end date will be available to the SocNet server. <br><br>The default start date is the date of project creation.");

        endDateChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                endDateChooserPropertyChange(evt);
            }
        });

        endDateLabel.setText("End Date");
        endDateLabel.setToolTipText("<html>Set the end date for the project here. <br>Telemetry data for this project between the start date and the end date will be available to the SocNet server. <br>The default end date is the project end date as listed in the Sensorbase. <br><br>If you want to continue sending this project's telemetry data to the SocNet server, leave this field blank. <br> ");

        sendCheckBox.setText("Send to SocNet");
        sendCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(selectionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(saveButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(8, 8, 8)))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(startDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                                    .addComponent(endDateLabel))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(startDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(endDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(sendCheckBox)
                                .addContainerGap())))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(selectionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(startDateLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startDateChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(endDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(endDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(sendCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(saveButton)
                            .addComponent(cancelButton)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE))
                .addContainerGap())
        );

        saveButton.getAccessibleContext().setAccessibleName("Okay");

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        try
        {
            ClientConfig conf = new ClientConfig();
            
            ArrayList<String> names = new ArrayList<String>();
            
            for(String project : projectNames)
            {
                Boolean send = sendToSocnet.get(project);
                if(send != null && send == Boolean.TRUE)
                {
                    names.add(project);
                    Date start = startDates.get(project);
                    Date end = endDates.get(project);
                    conf.setEndDate(project, end);
                    conf.setStartDate(project, start);
                }
            }
            conf.setProjectNames(names);
            conf.save();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            
            JOptionPane.showMessageDialog(this, "There was an error: " + 
                    ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(SelectionGUI.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    
        System.exit(0);
    
}//GEN-LAST:event_saveButtonActionPerformed

private void sendCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendCheckBoxActionPerformed
    String projectName = (String) projectList.getSelectedValue();
    Boolean send = this.sendCheckBox.isSelected();
    
    if(projectName != null && !projectName.isEmpty())
        sendToSocnet.put(projectName, send);
}//GEN-LAST:event_sendCheckBoxActionPerformed

private void projectListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_projectListValueChanged
    String project = (String)projectList.getSelectedValue();
    
    if(project == null)
    {
        setControlsEnabled(false);
        return;
    }
    
    setControlsEnabled(true);
    
    Boolean send = sendToSocnet.get(project);
    if(send == null)
        send = Boolean.FALSE;
    
    sendCheckBox.setSelected(send);
    
    startDateChooser.setDate(startDates.get(project));
    endDateChooser.setDate(endDates.get(project));
}//GEN-LAST:event_projectListValueChanged

private void startDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_startDateChooserPropertyChange
    String project = (String)projectList.getSelectedValue();
    
    Date d = startDateChooser.getDate();
    
    startDates.put(project, d);
}//GEN-LAST:event_startDateChooserPropertyChange

private void endDateChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_endDateChooserPropertyChange
    String project = (String)projectList.getSelectedValue();
    
    Date d = endDateChooser.getDate();
    
    endDates.put(project, d);
}//GEN-LAST:event_endDateChooserPropertyChange

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    System.exit(0);
}//GEN-LAST:event_cancelButtonActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
                try
                {
                    new SelectionGUI().setVisible(true);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(SelectionGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
                catch (ParseException ex)
                {
                    Logger.getLogger(SelectionGUI.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
                catch(SensorBaseClientException sbce)
                {
                    Logger.getLogger(SelectionGUI.class.getName()).
                            log(Level.SEVERE, null, sbce);
                }
      }
    });
  }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private com.toedter.calendar.JDateChooser endDateChooser;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList projectList;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel selectionLabel;
    private javax.swing.JCheckBox sendCheckBox;
    private com.toedter.calendar.JDateChooser startDateChooser;
    private javax.swing.JLabel startDateLabel;
    // End of variables declaration//GEN-END:variables

}
