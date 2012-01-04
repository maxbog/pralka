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
import javax.swing.SpinnerNumberModel;
import pralka.sim.ControlUnit;
import pralka.sim.Simulation;
import pralka.sim.WashingMachine;
import sun.org.mozilla.javascript.internal.ast.NumberLiteral;

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
    
    Simulation simulation;

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }
    
    
    DefaultListModel listModel;
    
    public void updateState() {
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
        lblSimulationTime.setText(Double.toString(simulation.getCurrentTime()));
        lblState.setText(washingMachine.getControlUnit().getControlUnitState() == null ? "" : washingMachine.getControlUnit().getControlUnitState().toString());
        lblStage.setText(washingMachine.getControlUnit().getCurrentStage() == null ? "" : washingMachine.getControlUnit().getCurrentStage().toString());
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
        jLabel9 = new javax.swing.JLabel();
        lblState = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblStage = new javax.swing.JLabel();
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
        jLabel7 = new javax.swing.JLabel();
        spnTimeScale = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        lblSimulationTime = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Aktywne stany sterownika"));
        jPanel1.setMinimumSize(new java.awt.Dimension(200, 0));
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 207));

        jScrollPane1.setViewportView(lstActiveStates);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 11;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);

        jLabel9.setText("Ogólny stan sterownika:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel9, gridBagConstraints);

        lblState.setText("jLabel10");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblState, gridBagConstraints);

        jLabel10.setText("Etap prania:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel10, gridBagConstraints);

        lblStage.setText("jLabel11");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblStage, gridBagConstraints);

        jLabel1.setText("Stan silnika:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel1, gridBagConstraints);

        jLabel2.setText("Stan grzałki:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel2, gridBagConstraints);

        jLabel3.setText("Stan pompy:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel3, gridBagConstraints);

        lblMotorState.setText("jLabel4");
        lblMotorState.setMinimumSize(new java.awt.Dimension(150, 14));
        lblMotorState.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblMotorState, gridBagConstraints);

        lblHeaterState.setText("jLabel5");
        lblHeaterState.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblHeaterState, gridBagConstraints);

        lblPumpState.setText("jLabel6");
        lblPumpState.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblPumpState, gridBagConstraints);

        jLabel4.setText("Kierunek obrotów:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel4, gridBagConstraints);

        lblWashDirection.setText("jLabel5");
        lblWashDirection.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblWashDirection, gridBagConstraints);

        jLabel5.setText("Ilość wody w pralce:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel5, gridBagConstraints);

        jLabel6.setText("Temperatura wody:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel6, gridBagConstraints);

        lblWaterLevel.setText("jLabel7");
        lblWaterLevel.setMaximumSize(new java.awt.Dimension(70, 14));
        lblWaterLevel.setMinimumSize(new java.awt.Dimension(70, 14));
        lblWaterLevel.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblWaterLevel, gridBagConstraints);

        lblTemperature.setText("jLabel8");
        lblTemperature.setMinimumSize(new java.awt.Dimension(70, 14));
        lblTemperature.setName(""); // NOI18N
        lblTemperature.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblTemperature, gridBagConstraints);

        jLabel7.setText("Skala czasowa:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel7, gridBagConstraints);

        spnTimeScale.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(1.0d), Double.valueOf(0.0d), null, Double.valueOf(0.0d)));
        spnTimeScale.setPreferredSize(new java.awt.Dimension(150, 20));
        spnTimeScale.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spnTimeScaleStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(spnTimeScale, gridBagConstraints);

        jLabel8.setText("Czas symulacji:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jLabel8, gridBagConstraints);

        lblSimulationTime.setText("lblSimulationTime");
        lblSimulationTime.setPreferredSize(new java.awt.Dimension(150, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(lblSimulationTime, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void spnTimeScaleStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spnTimeScaleStateChanged
        SpinnerNumberModel model = (SpinnerNumberModel)spnTimeScale.getModel();
        simulation.setTimeScale(model.getNumber().doubleValue());
    }//GEN-LAST:event_spnTimeScaleStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblHeaterState;
    private javax.swing.JLabel lblMotorState;
    private javax.swing.JLabel lblPumpState;
    private javax.swing.JLabel lblSimulationTime;
    private javax.swing.JLabel lblStage;
    private javax.swing.JLabel lblState;
    private javax.swing.JLabel lblTemperature;
    private javax.swing.JLabel lblWashDirection;
    private javax.swing.JLabel lblWaterLevel;
    private javax.swing.JList lstActiveStates;
    private javax.swing.JSpinner spnTimeScale;
    // End of variables declaration//GEN-END:variables
}
