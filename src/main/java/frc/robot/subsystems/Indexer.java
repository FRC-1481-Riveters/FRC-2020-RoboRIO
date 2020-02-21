/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

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

  protected long m_positionSetpoint;

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

  private long cmToEncoderCounts(double cm) {
    long encoderCounts = 0;

    try {
      encoderCounts = Math.round(((Constants.indexerEncoderCount / (Math.PI * Constants.indexerPulleyDiameter)) * cm));
    } catch (Exception ex) {
    }

    return encoderCounts;
  }

  private double encoderCountsToCm(long encoderCounts) {
    double cm = 0.0;

    try {
      cm = (Math.PI * Constants.indexerPulleyDiameter * encoderCounts) / Constants.indexerEncoderCount;
    } catch (Exception ex) {
    }

    return cm;
  }

  public void moveClosedLoopDistance(double cm) {

    /*
     * Since only one belt is supposed to move, stop the other belt just in case
     * it's moving for some reason when we execute a fixed distance move
     */
    m_lowerIndexer.set(0.0);

    /*
     * Reset the encoder counts for this talon so the encoder is moving to a value
     * that's based on zero. This way, we don't have to add or subtract the current
     * encoder counts to the desired travel distance to get the new target counts.
     */
    m_upperIndexer.setSelectedSensorPosition(0);

    /*
     * Convert cm to encoder counts for an indexer that only moves one of its two
     * motors when loading the indexer with a Power Cell.
     * 
     * The 2 factor is because the Power Cells only move at half the speed and half
     * the distance of the single belt that's rolling them across an unmoving belt
     * on the other side of the Power Cell.
     */
    m_positionSetpoint = 2 * cmToEncoderCounts(cm);

    /*
     * Command the talon to move the Power Cell to the next position based on a
     * number of encoder counts that represents our target distance of travel, in cm
     */
    m_upperIndexer.set(ControlMode.MotionMagic, m_positionSetpoint);

  }

  public boolean isOnTarget() {

    /*
     * Try to figure out if the indexer has moved to its target position by
     * computing the remaining distance it must travel, and comparing that to a
     * fixed constant tolerated error. If the remaining distance is less than the
     * tolerated error, assume the talon has moved its motor the correct distance
     * and is either stopped or very soon will stop.
     */

    double remainingDistanceToTargetInCm = encoderCountsToCm(
        m_positionSetpoint - m_upperIndexer.getSelectedSensorPosition());

    if (Math.abs(remainingDistanceToTargetInCm) < Constants.indexerToleratedPositionError) {
      return true;
    }
    return false;
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

    intakeMotors_MaxAccel.setDouble(Constants.indexerMotionMagicMaxAcceleration);
    intakeMotors_MaxVel.setDouble(Constants.indexerMotionMagicMaxVelocity);
    /*
     * This is the maximum velocity of the indexer in units of encoder counts per
     * 100 ms (a decisecond)
     */
    m_upperIndexer
        .configMotionCruiseVelocity((int) intakeMotors_MaxVel.getDouble(Constants.indexerMotionMagicMaxVelocity));

    /*
     * This is the maximum acceleration of the indexer in units of encoder counts
     * per 100 ms (a decisecond) per second
     * 
     */
    m_upperIndexer
        .configMotionAcceleration((int) intakeMotors_MaxAccel.getDouble(Constants.indexerMotionMagicMaxAcceleration));

    intakeMotors_MaxAccel.addListener(event -> {
      m_upperIndexer
          .configMotionAcceleration((int) intakeMotors_MaxAccel.getDouble(Constants.indexerMotionMagicMaxAcceleration));
      m_lowerIndexer
          .configMotionAcceleration((int) intakeMotors_MaxAccel.getDouble(Constants.indexerMotionMagicMaxAcceleration));
    }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);

    intakeMotors_MaxVel.addListener(event -> {
      m_upperIndexer
          .configMotionCruiseVelocity((int) intakeMotors_MaxVel.getDouble(Constants.indexerMotionMagicMaxVelocity));
      m_lowerIndexer
          .configMotionCruiseVelocity((int) intakeMotors_MaxVel.getDouble(Constants.indexerMotionMagicMaxVelocity));
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

  public double getDistanceToPowerCell() {
    return m_powerCellPositionSensor.getRangeCm();
  }

  @Override
  public void periodic() {
  }
}
