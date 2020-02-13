/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  private static WPI_TalonSRX m_intake = new WPI_TalonSRX(Constants.intakeMotorControllerCANId);
  /**
   * Creates a new Intake.
   */
  
  public void setSpeed(double Percent) {
    m_intake.set(ControlMode.PercentOutput, Percent);
  }

  public Intake() {
    m_intake.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_intake.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_intake.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_intake.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    setSpeed(0.0);

  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
