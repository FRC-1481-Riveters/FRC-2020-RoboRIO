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

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import irsensor.IRSensor;

public class Indexer extends SubsystemBase {
  private static WPI_TalonSRX m_upperIndexer = new WPI_TalonSRX(Constants.indexerMotorControllerCANId);
  private static WPI_TalonSRX m_lowerIndexer = new WPI_TalonSRX(Constants.secondIndexerMotorControllerCANId);
  private NetworkTableEntry intakeMotors_MaxAccel;
  private NetworkTableEntry intakeMotors_MaxVel;
  protected IRSensor m_powerCellPositionSensor;

  /**
   * Creates a new Indexer.
   */

  public void setClosedLoopSpeed(double RPM) {
    /**
     * Convert RPM to units / 100ms. RPM * 8192 Units 600 100ms/min in either
     * direction: velocity setpoint is in units/100ms
     */
    double targetVelocity_UnitsPer100ms = RPM * 8192 / 600;
    m_upperIndexer.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
    m_lowerIndexer.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
  }

  public Indexer(IRSensor IntakePowerCellPositionSensor) {

    m_powerCellPositionSensor = IntakePowerCellPositionSensor;
    
    m_upperIndexer.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.kPIDLoopIdx,
        Constants.kTimeoutMs);

    m_upperIndexer.config_kF(0, Constants.kGains_Indexer.kF);
    m_upperIndexer.config_kP(0, Constants.kGains_Indexer.kP);
    m_upperIndexer.config_kI(0, Constants.kGains_Indexer.kI);
    m_upperIndexer.config_kD(0, Constants.kGains_Indexer.kD);

    m_upperIndexer.setSensorPhase(Constants.kIndexerSensorPhase);

    m_upperIndexer.setInverted(true);

    intakeMotors_MaxAccel = NetworkTableInstance.getDefault().getTable("SmartDashboard")
        .getEntry("IntakeMotorMaxAccel");
    intakeMotors_MaxVel = NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry("IntakeMotorMaxVel");

    intakeMotors_MaxAccel.setDouble(250.0);
    intakeMotors_MaxVel.setDouble(400.0);
    /*
     * This is the maximum velocity of the indexer in units of encoder counts per
     * 100 ms (a decisecond)
     */
    m_upperIndexer.configMotionCruiseVelocity((int) intakeMotors_MaxVel.getDouble(400.0));

    /*
     * This is the maximum acceleration of the indexer in units of encoder counts
     * per 100 ms (a decisecond) per second
     * 
     */
    m_upperIndexer.configMotionAcceleration((int) intakeMotors_MaxAccel.getDouble(250.0));

    intakeMotors_MaxAccel.addListener(event -> {
      m_upperIndexer.configMotionAcceleration((int) intakeMotors_MaxAccel.getDouble(250.0));
      m_lowerIndexer.configMotionAcceleration((int) intakeMotors_MaxAccel.getDouble(250.0));
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    intakeMotors_MaxVel.addListener(event -> {
      m_upperIndexer.configMotionCruiseVelocity((int) intakeMotors_MaxVel.getDouble(400.0));
      m_lowerIndexer.configMotionCruiseVelocity((int) intakeMotors_MaxVel.getDouble(400.0));
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    m_lowerIndexer.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.kPIDLoopIdx,
        Constants.kTimeoutMs);

    m_lowerIndexer.config_kF(0, Constants.kGains_Indexer.kF);
    m_lowerIndexer.config_kP(0, Constants.kGains_Indexer.kP);
    m_lowerIndexer.config_kI(0, Constants.kGains_Indexer.kI);
    m_lowerIndexer.config_kD(0, Constants.kGains_Indexer.kD);

    m_lowerIndexer.setSensorPhase(Constants.kIndexerSensorPhase);

    m_lowerIndexer.setInverted(false);

  }

  @Override
  public void periodic() {

    // This method will be called once per scheduler run

  }
}
