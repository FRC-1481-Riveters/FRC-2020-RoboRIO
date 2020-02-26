/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

public class Kicker extends SubsystemBase implements Sendable {
  private static WPI_TalonSRX m_kicker = new WPI_TalonSRX(Constants.kickerMotorControllerCANId);
  protected DigitalInput m_powerCellDetectorDigitalInput = new DigitalInput(0);

  /**
   * Creates a new Kicker.
   */
  public Kicker() {
    m_kicker.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx,
        Constants.kTimeoutMs);

    m_kicker.config_kF(0, Constants.kGains_Kicker.kF);
    m_kicker.config_kP(0, Constants.kGains_Kicker.kP);
    m_kicker.config_kI(0, Constants.kGains_Kicker.kI);
    m_kicker.config_kD(0, Constants.kGains_Kicker.kD);

    m_kicker.setSensorPhase(Constants.kKickerSensorPhase);

    m_kicker.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_kicker.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_kicker.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_kicker.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    m_kicker.setInverted(Constants.kMotorInvert);

  }
  public void setSpeed(double Percent) {
    m_kicker.set(ControlMode.PercentOutput, Percent);
  }
  public void setClosedLoopSpeed(double RPM) {
    /*
     * Convert RPM to encoder units / 0.1s.
     * 
     * Encoder counters per 0.1s is the basic unit of velocity in the Talons.
     * 
     * Encoder counts per 100 ms = RPM * indexerEncoderCount / 600
     * 
     * Either direction: velocity setpoint is in units/100ms
     */
    double targetVelocity_UnitsPer100ms = RPM * Constants.kickerEncoderCount / 600;
    m_kicker.set(ControlMode.Velocity, targetVelocity_UnitsPer100ms);
  }


  public boolean getPowerCellDetected() {
    boolean powerCellDetected;

    /*
     * The IR Breakbeam sensor attached to the digital input is active low. This
     * means its value changes to false when the Power Cell breaks the beam when it
     * enters the Kicker in the right place. Invert this reading by negating the
     * value read from the DigitalInput such that when the Power Cell breaks the
     * beam, and is in position, the value is true. This makes more sense to people
     * using getPowerCellDetected(): true means it's detected, and false means it's
     * not detected.
     */
    powerCellDetected = !m_powerCellDetectorDigitalInput.get();

    return powerCellDetected;
  }

  @Override
  public void periodic() {
  }

  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);

    builder.addBooleanProperty(".PowerCellDetected", () -> getPowerCellDetected(), null);
    builder.addDoubleProperty(".KickerCommandedSpeed", () -> m_kicker.get(), null);
  }
}
