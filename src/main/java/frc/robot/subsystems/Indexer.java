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

  private NetworkTableEntry intakeMotors_kP;
  private NetworkTableEntry intakeMotors_kI;
  private NetworkTableEntry intakeMotors_kD;
  private NetworkTableEntry intakeMotors_kF;

  protected IRSensor m_powerCellPositionSensor;

  protected long m_positionSetpoint;

  public Indexer(IRSensor IntakePowerCellPositionSensor) {

    /*
     * Setup the Talons to support Velocity control (which uses a PID in each talon
     * to achieve the velocity).
     */
    m_upperIndexer.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.kPIDLoopIdx,
        Constants.kTimeoutMs);

    m_upperIndexer.config_kF(0, Constants.kGains_Indexer.kF);
    m_upperIndexer.config_kP(0, Constants.kGains_Indexer.kP);
    m_upperIndexer.config_kI(0, Constants.kGains_Indexer.kI);
    m_upperIndexer.config_kD(0, Constants.kGains_Indexer.kD);

    m_lowerIndexer.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, Constants.kPIDLoopIdx,
        Constants.kTimeoutMs);

    m_lowerIndexer.config_kF(0, Constants.kGains_Indexer.kF);
    m_lowerIndexer.config_kP(0, Constants.kGains_Indexer.kP);
    m_lowerIndexer.config_kI(0, Constants.kGains_Indexer.kI);
    m_lowerIndexer.config_kD(0, Constants.kGains_Indexer.kD);

    /* Setup electrical data for each of the Talon's encoders. */
    m_upperIndexer.setSensorPhase(Constants.kIndexerSensorPhase);
    m_lowerIndexer.setSensorPhase(Constants.kIndexerSensorPhase);

    /*
     * Setup the basic geometry of each of the motors. The upper indexer motor has
     * to spin backwards, and the lower indexer motor spins forwards to coordinate
     * movement of a Power Cell
     */
    m_upperIndexer.setInverted(true);
    m_lowerIndexer.setInverted(false);

    /*
     * Save a reference to the IR Sensor so we can use it later to measure where the
     * Power Cell is located inside the Intake area.
     */
    m_powerCellPositionSensor = IntakePowerCellPositionSensor;

    /*
     * Setup the MotionMagic profile and SmartDashboard tuning hooks for each motor
     * to support fixed position mode (moveClosedLoopDistance())
     * 
     * This is NOT for velocity mode!
     * 
     * This also relies on the PID setup in each talon.
     */

    intakeMotors_MaxAccel = NetworkTableInstance.getDefault().getTable("SmartDashboard")
        .getEntry("IntakeMotorMaxAccel");
    intakeMotors_MaxVel = NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry("IntakeMotorMaxVel");

    intakeMotors_kP = NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry("IntakeMotorKP");
    intakeMotors_kI = NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry("IntakeMotorKI");
    intakeMotors_kD = NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry("IntakeMotorKD");
    intakeMotors_kF = NetworkTableInstance.getDefault().getTable("SmartDashboard").getEntry("IntakeMotorKF");

    intakeMotors_MaxAccel.setDouble(Constants.indexerMotionMagicMaxAcceleration);
    intakeMotors_MaxVel.setDouble(Constants.indexerMotionMagicMaxVelocity);
    intakeMotors_kP.setDouble(Constants.kGains_Indexer.kP);
    intakeMotors_kI.setDouble(Constants.kGains_Indexer.kI);
    intakeMotors_kD.setDouble(Constants.kGains_Indexer.kD);
    intakeMotors_kF.setDouble(Constants.kGains_Indexer.kF);

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

    /*
     * Setup SmartDashboard debugging hooks that update and reconfigure the Talons
     * with the tuning values we send using SmartDashboard and Shuffleboard the
     * instant they arrive into the RoboRIO.
     * 
     * Since the Upper Indexer belt is the only one that moves using Motion Magic,
     * don't bother updating the Lower Indexer belt's talon with Motion Magic stuff
     * it won't use.
     */
    intakeMotors_MaxAccel.addListener(event -> {
      m_upperIndexer
          .configMotionAcceleration((int) intakeMotors_MaxAccel.getDouble(Constants.indexerMotionMagicMaxAcceleration));
    }, EntryListenerFlags.kUpdate);

    intakeMotors_MaxVel.addListener(event -> {
      m_upperIndexer
          .configMotionCruiseVelocity((int) intakeMotors_MaxVel.getDouble(Constants.indexerMotionMagicMaxVelocity));
    }, EntryListenerFlags.kUpdate);

    intakeMotors_kP.addListener(event -> {
      m_upperIndexer.config_kP(0, intakeMotors_kP.getDouble(0));
    }, EntryListenerFlags.kUpdate);

    intakeMotors_kI.addListener(event -> {
      m_upperIndexer.config_kI(0, intakeMotors_kI.getDouble(0));
    }, EntryListenerFlags.kUpdate);
    
    intakeMotors_kD.addListener(event -> {
      m_upperIndexer.config_kD(0, intakeMotors_kD.getDouble(0));
    }, EntryListenerFlags.kUpdate);

    intakeMotors_kF.addListener(event -> {
      m_upperIndexer.config_kF(0, intakeMotors_kF.getDouble(0));
    }, EntryListenerFlags.kUpdate);

  }

  public void setClosedLoopSpeed(double RPM) {
    /*
     * Start the Indexer moving at a fixed speed. This moves both Indexers, in the
     * relative and correct direction, at the same speed such that a Power Cell is
     * carried between the belts up to the Kicker and Shooter.
     * 
     * Positive RPM moves the Power Cell up toward the Kicker and Shooter.
     * 
     * Negative RPM moves the Power Cell down toward the Intake.
     */

    /*
     * Convert RPM to encoder units / 0.1s.
     * 
     * Encoder counters per 0.1s is the basic unit of velocity in the Talons.
     * 
     * Encoder counts per 100 ms = RPM * indexerEncoderCount / 600
     * 
     * Either direction: velocity setpoint is in units/100ms
     */
    double targetVelocity_UnitsPer100ms = RPM * Constants.indexerEncoderCount / 600;
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

  public double getDistanceToPowerCell() {
    return m_powerCellPositionSensor.getRangeCm();
  }

  @Override
  public void periodic() {
  }
}
