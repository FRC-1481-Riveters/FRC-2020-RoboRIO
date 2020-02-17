/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.*;

public class Kicker extends SubsystemBase {
  private static WPI_TalonSRX m_kicker = new WPI_TalonSRX(Constants.kickerMotorControllerCANId);
  protected DigitalInput m_powerCellDetectorDigitalInput = new DigitalInput(0);

  /**
   * Creates a new Kicker.
   */
  public Kicker() {

    m_kicker.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_kicker.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_kicker.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_kicker.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    m_kicker.setInverted(Constants.kMotorInvert);

  }

  public void setSpeed(double Percent) {

    m_kicker.set(ControlMode.PercentOutput, Percent);
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
}
