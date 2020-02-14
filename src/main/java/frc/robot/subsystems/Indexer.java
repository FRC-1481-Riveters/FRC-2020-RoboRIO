/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Indexer extends SubsystemBase {
  private static WPI_TalonSRX m_upperIndexer = new WPI_TalonSRX(Constants.indexerMotorControllerCANId);
  private static WPI_TalonSRX m_lowerIndexer = new WPI_TalonSRX(Constants.secondIndexerMotorControllerCANId);

 

  /**
   * Creates a new Indexer.
   */
 
  public void setClosedLoopSpeed(double RPM) {
    /**
			 * Convert RPM to units / 100ms.
			 * RPM * 4096 Units 600 100ms/min in either direction:
			 * velocity setpoint is in units/100ms
			 */
			double targetVelocity_UnitsPer100ms = RPM * 4096 / 600;
      m_upperIndexer.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
      m_lowerIndexer.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
  }

  public Indexer() {
    m_upperIndexer.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    m_upperIndexer.config_kF(0, 0.0);
    m_upperIndexer.config_kP(0, 0.2);
    m_upperIndexer.config_kI(0, 0.0);
    m_upperIndexer.config_kD(0, 0.0);

    m_upperIndexer.setSensorPhase(Constants.kIndexerSensorPhase);

    m_lowerIndexer.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    Constants.kPIDLoopIdx, Constants.kTimeoutMs);

    m_lowerIndexer.config_kF(0, 0.0);
    m_lowerIndexer.config_kP(0, 0.2);
    m_lowerIndexer.config_kI(0, 0.0);
    m_lowerIndexer.config_kD(0, 0.0);

    m_lowerIndexer.setSensorPhase(Constants.kIndexerSensorPhase);

  }

  @Override
  public void periodic() {

    // This method will be called once per scheduler run

  }
}
