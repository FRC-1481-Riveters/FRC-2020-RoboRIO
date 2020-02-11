/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Indexer extends SubsystemBase {
  private static WPI_TalonSRX m_indexer = new WPI_TalonSRX(Constants.indexerMotorControllerCANId);

  /**
   * Creates a new Indexer.
   */
 
  public void setClosedLoopSpeed(double RPM) {
    double targetVelocity_UnitsPer100ms = RPM  * 4096 / 600;
    m_indexer.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
  }

  public Indexer() {

    m_indexer.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_indexer.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_indexer.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_indexer.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    /* Config the Velocity closed loop gains in slot0 */
		m_indexer.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kF, Constants.kTimeoutMs);
		m_indexer.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kP, Constants.kTimeoutMs);
	  m_indexer.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kI, Constants.kTimeoutMs);
		m_indexer.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kD, Constants.kTimeoutMs);

    // m_indexer.setInverted(Constants.add constant here);
    setClosedLoopSpeed(0.0);

  }

  @Override
  public void periodic() {

    // This method will be called once per scheduler run

  }
}
