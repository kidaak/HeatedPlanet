/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.query;

import PlanetSim.ControllerGUI;
import PlanetSim.Demo;
import common.EarthGridProperties;
import common.EarthGridProperties.EarthGridProperty;
import common.IGrid;
import persistenceManager.PersistenceManager;
import persistenceManager.PersistenceManagerQueryResult;
import persistenceManager.QueryCalculator;
import simulation.Earth;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author dwelker
 */
public class QueryDialog extends JFrame//extends javax.swing.JDialog
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4961796121859584333L;
	private Demo pcontrol;
	private QueryCalculator qc;
	
	/**
     * Creates new form QueryDialog
     */
    public QueryDialog(java.awt.Frame parent, boolean modal, Demo pcontrol)
    {
//        super(parent, modal);
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pcontrol = pcontrol; // used to change viewable window
    	qc = new QueryCalculator();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        simulationNameComboBox = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        startDateSpinner = new javax.swing.JSpinner();
        jLabel5 = new javax.swing.JLabel();
        endDateSpinner = new javax.swing.JSpinner();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        startLongitudeSpinner = new javax.swing.JSpinner();
        endLongitudeSpinner = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        endLatitudeSpinner = new javax.swing.JSpinner();
        startLatitudeSpinner = new javax.swing.JSpinner();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        minTempCheckBox = new javax.swing.JCheckBox();
        maxTempCheckBox = new javax.swing.JCheckBox();
        meanTempOverRegionCheckBox = new javax.swing.JCheckBox();
        meanTempOverTimesCheckBox = new javax.swing.JCheckBox();
        actualValuesCheckBox = new javax.swing.JCheckBox();
        queryButton = new javax.swing.JButton();
        axialTiltSpinner = new javax.swing.JSpinner();
        eccentricitySpinner = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Simulation Name:");

        updateSimList();

        jLabel2.setText("Axial Tilt");

        jLabel3.setText("Orbital Eccentricity");

        jLabel4.setText("Start Date");

        startDateSpinner.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(1388865600000L), new java.util.Date(1388865600000L), new java.util.Date(4544539200000L), java.util.Calendar.YEAR));
        startDateSpinner.setEditor(new javax.swing.JSpinner.DateEditor(startDateSpinner, "MM/dd/yyyy hh:mm a"));

        jLabel5.setText("End Date");

        endDateSpinner.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(4544539200000L), new java.util.Date(1388865600000L), new java.util.Date(4544539200000L), java.util.Calendar.YEAR));
        endDateSpinner.setEditor(new javax.swing.JSpinner.DateEditor(endDateSpinner, "MM/dd/yyyy hh:mm a"));

        jLabel6.setText("Longitude");

        startLongitudeSpinner.setModel(new javax.swing.SpinnerNumberModel(-180, -180, 180, 1));

        endLongitudeSpinner.setModel(new javax.swing.SpinnerNumberModel(180, -180, 180, 1));

        jLabel10.setText("to");

        jLabel7.setText("Latitude");

        jLabel11.setText("to");

        endLatitudeSpinner.setModel(new javax.swing.SpinnerNumberModel(90, -90, 90, 1));

        startLatitudeSpinner.setModel(new javax.swing.SpinnerNumberModel(-90, -90, 90, 1));

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel8.setText("Results to Show:");

        minTempCheckBox.setText("Minimum temperature");

        maxTempCheckBox.setText("Maximum temperature");

        meanTempOverRegionCheckBox.setText("Mean temperature over the region for the requested times");

        meanTempOverTimesCheckBox.setText("Mean temperature over the times for the requested region");

        actualValuesCheckBox.setText("Actual values");
        actualValuesCheckBox.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                actualValuesCheckBoxActionPerformed(evt);
            }
        });

        queryButton.setText("Query");
        queryButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                queryButtonActionPerformed(evt);
            }
        });

        axialTiltSpinner.setModel(new javax.swing.SpinnerNumberModel(22.4d, -180.0d, 180.0d, 0.1d));

        eccentricitySpinner.setModel(new javax.swing.SpinnerNumberModel(0.0167, //initial value
            0.0, //min
            1.0, //max
            0.0001));
    eccentricitySpinner.setEditor(new javax.swing.JSpinner.NumberEditor(eccentricitySpinner, "0.0000"));

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator1)
                .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                .addComponent(jSeparator3)
                .addComponent(jSeparator4)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(simulationNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(startDateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(endDateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                            .addComponent(jLabel6)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startLongitudeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endLongitudeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(startLatitudeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(endLatitudeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(63, 63, 63)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(axialTiltSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(18, 18, 18)
                            .addComponent(eccentricitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(minTempCheckBox)
                        .addComponent(maxTempCheckBox)
                        .addComponent(meanTempOverRegionCheckBox)
                        .addComponent(meanTempOverTimesCheckBox)
                        .addComponent(actualValuesCheckBox))
                    .addGap(0, 81, Short.MAX_VALUE))
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(queryButton)))
            .addContainerGap())
    );
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel1)
                .addComponent(simulationNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel2)
                .addComponent(axialTiltSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel3)
                .addComponent(eccentricitySpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel4)
                .addComponent(startDateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel5)
                .addComponent(endDateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel6)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(startLongitudeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(endLongitudeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel10))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel7)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(startLatitudeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(endLatitudeSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel11))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jLabel8)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(minTempCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(maxTempCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(meanTempOverRegionCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(meanTempOverTimesCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(actualValuesCheckBox)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(queryButton)
            .addContainerGap())
    );

    pack();
    }// </editor-fold>//GEN-END:initComponents

    public void updateSimList() {
        ArrayList<String> simNames = PersistenceManager.getAllSimNames();
        simNames.add(0, "Any");
        simulationNameComboBox.setModel(new javax.swing.DefaultComboBoxModel(simNames.toArray()));
    }
    
    private void actualValuesCheckBoxActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_actualValuesCheckBoxActionPerformed
    {//GEN-HEADEREND:event_actualValuesCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_actualValuesCheckBoxActionPerformed

    private void queryButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_queryButtonActionPerformed
    {//GEN-HEADEREND:event_queryButtonActionPerformed
        EarthGridProperties egp = new EarthGridProperties();
        String name = simulationNameComboBox.getSelectedItem().toString();
        if( !name.equals("Any") )
            egp.setProperty(EarthGridProperties.EarthGridProperty.NAME, name);
        Number axialTilt = (Number) axialTiltSpinner.getValue();
        Number eccentricity = (Number) eccentricitySpinner.getValue();
        Date startDate = (Date) startDateSpinner.getValue();
        Date endDate = (Date) endDateSpinner.getValue();
        Float startLatitude = (float)(Integer) startLatitudeSpinner.getValue();
        Float endLatitude = (float)(Integer) endLatitudeSpinner.getValue();
        Float startLongitude = (float)(Integer) startLongitudeSpinner.getValue();
        Float endLongitude = (float)(Integer) endLongitudeSpinner.getValue();

        egp.setProperty(EarthGridProperty.AXIAL_TILT, axialTilt.doubleValue());
        egp.setProperty(EarthGridProperty.ECCENTRICITY, eccentricity.doubleValue());
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        egp.setProperty(EarthGridProperty.START_DATE, start);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        egp.setProperty(EarthGridProperty.END_DATE, end);

        PersistenceManagerQueryResult queryResult = PersistenceManager.getQueryData(egp);

        // Check query result
        if(queryResult == null) {
            // If no data available for query, ask user if they'd like to perform
            // simulation.
        	JFrame frame = new JFrame();
            String message = "No matching sim found.  Would you like to perform a simulation?";
            int answer = JOptionPane.showOptionDialog(frame, message, "Run Simulation?", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            if (answer == JOptionPane.YES_OPTION) {
            	// User clicked YES.
            	System.out.printf("perform sim!\n");
            	if(pcontrol != null) {
            		pcontrol.viewSimGUI();
            	}
            	else {
            		System.out.printf("no control available to start sim.  Start with Demo.java...\n");
            	}
            	
            } else {
            	// User clicked NO.
            	System.out.printf("no sim...\n");
            }
        }
        else {
        	// Query successful, return results in text window
//        	qc.setSimProp(queryResult.gridProps); //FIXME: eqp should actually be from result...
        	egp.setProperty(EarthGridProperty.SIMULATION_TIME_STEP, 1440); //FIXME: REMOVE THIS ONCE ABOVE WORKING
        	qc.setSimProp(egp); //FIXME: eqp should actually be from result...
        	qc.setGrid(queryResult.grids);
        	startLatitudeSpinner.getValue();
        	qc.setLocation(startLatitude, endLatitude, startLongitude, endLongitude);
        	qc.setDoMin(minTempCheckBox.isSelected());
        	qc.setDoMax(maxTempCheckBox.isSelected());
        	qc.setDoAvgAcrossGrid(meanTempOverRegionCheckBox.isSelected());
        	qc.setDoAvgAcrossTime(meanTempOverTimesCheckBox.isSelected());
        	qc.setDoAllData(actualValuesCheckBox.isSelected());
        	
        	// now get QueryCalculator result and display
        	String resultStr = qc.getOutputText();
        	
            // create a JTextArea
            JTextArea textArea = new JTextArea();
            textArea.setFont(new Font("monospaced", Font.PLAIN, 12));
            textArea.setText(resultStr);
            textArea.setEditable(false);
             
            // wrap a scrollpane around it
            JScrollPane scrollPane = new JScrollPane(textArea);
             
            // display them in a message dialog
        	JFrame frame = new JFrame();
        	frame.add(scrollPane);
            //Display the window.
            frame.pack();
            frame.setTitle("Query Results");
    		frame.setSize(800, 600);
            frame.setVisible(true);
        }
        
    }//GEN-LAST:event_queryButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(QueryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(QueryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(QueryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(QueryDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                QueryDialog dialog = new QueryDialog(new javax.swing.JFrame(), true, null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox actualValuesCheckBox;
    private javax.swing.JSpinner axialTiltSpinner;
    private javax.swing.JSpinner eccentricitySpinner;
    private javax.swing.JSpinner endDateSpinner;
    private javax.swing.JSpinner endLatitudeSpinner;
    private javax.swing.JSpinner endLongitudeSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JCheckBox maxTempCheckBox;
    private javax.swing.JCheckBox meanTempOverRegionCheckBox;
    private javax.swing.JCheckBox meanTempOverTimesCheckBox;
    private javax.swing.JCheckBox minTempCheckBox;
    private javax.swing.JButton queryButton;
    private javax.swing.JComboBox simulationNameComboBox;
    private javax.swing.JSpinner startDateSpinner;
    private javax.swing.JSpinner startLatitudeSpinner;
    private javax.swing.JSpinner startLongitudeSpinner;
    // End of variables declaration//GEN-END:variables
}
