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
  private static WPI_TalonSRX m_upperIndexer = new WPI_TalonSRX(Constants.indexerMotorControllerCANId);
  private static WPI_TalonSRX m_lowerIndexer = new WPI_TalonSRX(Constants.secondIndexerMotorControllerCANId);

 

  /**
   * Creates a new Indexer.
   */
 
  public void setSpeed(double Percent) {
    m_upperIndexer.set(ControlMode.PercentOutput, Percent);
  }

  public Indexer() {

    m_upperIndexer.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_upperIndexer.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_upperIndexer.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_upperIndexer.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    /*
    m_lowerIndexer.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_lowerIndexer.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_lowerIndexer.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_lowerIndexer.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    /* Config the Velocity closed loop gains in slot0 */
		m_upperIndexer.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kF, Constants.kTimeoutMs);
		m_upperIndexer.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kP, Constants.kTimeoutMs);
	  m_upperIndexer.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kI, Constants.kTimeoutMs);
    m_upperIndexer.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kD, Constants.kTimeoutMs);
    
    m_lowerIndexer.config_kF(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kF, Constants.kTimeoutMs);
		m_lowerIndexer.config_kP(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kP, Constants.kTimeoutMs);
	  m_lowerIndexer.config_kI(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kI, Constants.kTimeoutMs);
		m_lowerIndexer.config_kD(Constants.kPIDLoopIdx, Constants.kGains_Indexer.kD, Constants.kTimeoutMs);
    // m_indexer.setInverted(Constants.add constant here);
    m_lowerIndexer.follow(m_upperIndexer); //runs inverse
    m_lowerIndexer.setInverted(true);
  }

  @Override
  public void periodic() {

    // This method will be called once per scheduler run

  }
}
