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
  

    m_kicker.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_kicker.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_kicker.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_kicker.configPeakOutputReverse(-1, Constants.kTimeoutMs);

  
    m_kicker.setInverted(Constants.kMotorInvert);


  }
  public void setSpeed(double Percent) {
  

			m_kicker.set(ControlMode.PercentOutput, Percent);
  }

  @Override
  public void periodic() {
  }
}
