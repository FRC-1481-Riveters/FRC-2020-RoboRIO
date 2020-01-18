/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;
import frc.robot.Constants;

public class wheelOfFortuneColorSpinny extends SubsystemBase {
  private static WPI_TalonSRX m_controlPanelMotorControlBrad = new WPI_TalonSRX(
      Constants.controlPanelMotorControllerCANId);

  /**
   * Creates a new wheelOfFortuneColorSpinny.
   */
  public wheelOfFortuneColorSpinny() {
    m_controlPanelMotorControlBrad.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
        Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    /* Ensure sensor is positive when output is positive */
    m_controlPanelMotorControlBrad.setSensorPhase(Constants.kSensorPhase);

    /**
     * 
     * Set based on what direction you want forward/positive to be. This does not
     * affect sensor phase.
     */
    /* Config the peak and nominal outputs, 12V means full */

    m_controlPanelMotorControlBrad.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_controlPanelMotorControlBrad.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_controlPanelMotorControlBrad.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_controlPanelMotorControlBrad.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will be neutral
     * within this range. See Table in Section 17.2.1 for native units per rotation.
     */
    m_controlPanelMotorControlBrad.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    /* Config Position Closed Loop gains in slot0, tsypically kF stays zero. */
    m_controlPanelMotorControlBrad.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
    m_controlPanelMotorControlBrad.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
    m_controlPanelMotorControlBrad.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
    m_controlPanelMotorControlBrad.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
    m_controlPanelMotorControlBrad.setInverted(Constants.kMotorInvert);

    /* 10 Rotations * 4096 u/rev in either direction */
  }

  public void spinToPosition(int encoderCount) {
    m_controlPanelMotorControlBrad.set(ControlMode.Position, encoderCount);
  }

  public void stopBrad() {
    m_controlPanelMotorControlBrad.set(ControlMode.PercentOutput, 0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public int getActualPosition() {
    /*
     * This value is converted with a negative sign to switch the way the sensor
     * reports rotation. It's backwards compared to how the code expects the sensor
     * to rotate.
     */
    int getSelectedSensorPosition =  m_controlPanelMotorControlBrad.getSelectedSensorPosition();
    return getSelectedSensorPosition;
  }
  public void zeroPosition() {
    m_controlPanelMotorControlBrad.getSensorCollection().setQuadraturePosition(0, Constants.kTimeoutMs);
  }
}
