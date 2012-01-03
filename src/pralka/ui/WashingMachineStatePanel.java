/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * WashingMachineStatePanel.java
 *
 * Created on 2012-01-03, 17:05:31
 */
package pralka.ui;

import java.text.NumberFormat;
import javax.swing.DefaultListModel;
import pralka.sim.ControlUnit;
import pralka.sim.Simulation;
import pralka.sim.WashingMachine;

/**
 *
 * @author Max
 */
public class WashingMachineStatePanel extends javax.swing.JPanel {

    /** Creates new form WashingMachineStatePanel */
    public WashingMachineStatePanel() {
        initComponents();
        listModel = new DefaultListModel();
        lstActiveStates.setModel(listModel);
    }
    
    DefaultListModel listModel;
    
    public void updateState(Simulation simulation) {
        listModel.clear();
        final WashingMachine washingMachine = simulation.getWashingMachine();
        for(Object state : washingMachine.getControlUnit().getWorkingStates()) {
            listModel.addElement(state);
        }
        lblMotorState.setText(washingMachine.getMotor().getMotorState() == null ? "" : washingMachine.getMotor().getMotorState().toString());
        lblWashDirection.setText(washingMachine.getMotor().getDirection() == null ? "" : washingMachine.getMotor().getDirection().toString());
        lblPumpState.setText(washingMachine.getPump().getPumpState() == null ? "" : washingMachine.getPump().getPumpState().toString());
        lblHeaterState.setText(washingMachine.getHeater().getHeaterState() == null ? "" : washingMachine.getHeater().getHeaterState().toString());
        lblWaterLevel.setText(Double.toString(simulation.getEnvironment().getWaterLevel()));
        lblTemperature.setText(Double.toString(simulation.getEnvironment().getWaterTemperature()));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstActiveStates = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblMotorState = new javax.swing.JLabel();
        lblHeaterState = new javax.swing.JLabel();
        lblPumpState = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblWashDirection = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblWaterLevel = new javax.swing.JLabel();
        lblTemperature = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Aktywne stany sterownika"));

        jScrollPane1.setViewportView(lstActiveStates);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

        jLabel1.setText("Stan silnika:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel1, gridBagConstraints);

        jLabel2.setText("Stan grzałki:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel2, gridBagConstraints);

        jLabel3.setText("Stan pompy:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel3, gridBagConstraints);

        lblMotorState.setText("jLabel4");
        lblMotorState.setMinimumSize(new java.awt.Dimension(150, 14));
        lblMotorState.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        add(lblMotorState, gridBagConstraints);

        lblHeaterState.setText("jLabel5");
        lblHeaterState.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        add(lblHeaterState, gridBagConstraints);

        lblPumpState.setText("jLabel6");
        lblPumpState.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        add(lblPumpState, gridBagConstraints);

        jLabel4.setText("Kierunek obrotów:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel4, gridBagConstraints);

        lblWashDirection.setText("jLabel5");
        lblWashDirection.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        add(lblWashDirection, gridBagConstraints);

        jLabel5.setText("Ilość wody w pralce:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 10;
        add(jLabel5, gridBagConstraints);

        jLabel6.setText("Temperatura wody:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 10;
        add(jLabel6, gridBagConstraints);

        lblWaterLevel.setText("jLabel7");
        lblWaterLevel.setMaximumSize(new java.awt.Dimension(70, 14));
        lblWaterLevel.setMinimumSize(new java.awt.Dimension(70, 14));
        lblWaterLevel.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        add(lblWaterLevel, gridBagConstraints);

        lblTemperature.setText("jLabel8");
        lblTemperature.setMinimumSize(new java.awt.Dimension(70, 14));
        lblTemperature.setName(""); // NOI18N
        lblTemperature.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        add(lblTemperature, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHeaterState;
    private javax.swing.JLabel lblMotorState;
    private javax.swing.JLabel lblPumpState;
    private javax.swing.JLabel lblTemperature;
    private javax.swing.JLabel lblWashDirection;
    private javax.swing.JLabel lblWaterLevel;
    private javax.swing.JList lstActiveStates;
    // End of variables declaration//GEN-END:variables
}