/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

public class Kicker extends SubsystemBase {
  private static WPI_TalonSRX m_kicker = new WPI_TalonSRX
    (Constants.kickerMotorControllerCANId);
  /**
   * Creates a new Kicker.
   */
  public Kicker() {
    m_kicker.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    Constants.kPIDLoopIdx, Constants.kTimeoutMs);    

    m_kicker.setSensorPhase(Constants.kSensorPhase);

     /**
     * 
     * Set based on what direction you want forward/positive to be. This does not
     * affect sensor phase.
     */
    /* Config the peak and nominal outputs, 12V means full */

    m_kicker.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_kicker.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_kicker.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_kicker.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    /**
     * Config the allowable closed-loop error, Closed-Loop output will be neutral
     * within this range. See Table in Section 17.2.1 for native units per rotation.
     */
    m_kicker.configAllowableClosedloopError(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    /* Config Position Closed Loop gains in slot0, tsypically kF stays zero. */
    m_kicker.config_kF(Constants.kPIDLoopIdx, Constants.kGains.kF, Constants.kTimeoutMs);
    m_kicker.config_kP(Constants.kPIDLoopIdx, Constants.kGains.kP, Constants.kTimeoutMs);
    m_kicker.config_kI(Constants.kPIDLoopIdx, Constants.kGains.kI, Constants.kTimeoutMs);
    m_kicker.config_kD(Constants.kPIDLoopIdx, Constants.kGains.kD, Constants.kTimeoutMs);
    m_kicker.setInverted(Constants.kMotorInvert);

    /* 10 Rotations * 4096 u/rev in either direction */
  }
  public void setClosedLoopSpeed(double RPM) {
        // This method will be called once per scheduler run
			/**
			 * Convert RPM to units / 100ms.
			 * 4096 Units/Rev * RPM / 600 100ms/min in either direction:
			 * velocity setpoint is in units/100ms
			 */
      double targetVelocity_UnitsPer100ms = RPM * 4096 / 600;
			/* 500 RPM in either direction */
			m_kicker.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
  }

  @Override
  public void periodic() {
  }
}
