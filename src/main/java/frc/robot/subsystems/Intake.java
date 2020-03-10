/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import java.util.function.DoubleSupplier;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import irsensor.IRSensor;

public class Intake extends SubsystemBase {
  private static WPI_TalonSRX m_intake = new WPI_TalonSRX(Constants.intakeMotorControllerCANId);
  private static WPI_TalonSRX m_intakeDoubleRoller = new WPI_TalonSRX(Constants.intakeDoubleRollerMotorControllerCANId);

  protected IRSensor m_powerDetectorSensor;

  /**
   * Creates a new Intake.
   */

  public void setSpeed(double Percent) {
    m_intake.set(ControlMode.PercentOutput, Percent);
    m_intakeDoubleRoller.set(ControlMode.PercentOutput, Percent);
  }

  public Intake(IRSensor IntakePowerCellPositionSensor) {
    m_intake.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_intake.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_intake.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_intake.configPeakOutputReverse(-1, Constants.kTimeoutMs);
    m_intakeDoubleRoller.configNominalOutputForward(0, Constants.kTimeoutMs);
    m_intakeDoubleRoller.configNominalOutputReverse(0, Constants.kTimeoutMs);
    m_intakeDoubleRoller.configPeakOutputForward(1, Constants.kTimeoutMs);
    m_intakeDoubleRoller.configPeakOutputReverse(-1, Constants.kTimeoutMs);

    setSpeed(0.0);
    m_intake.setInverted(true);
    //m_intakeDoubleRoller.setInverted(true);

    m_powerDetectorSensor = IntakePowerCellPositionSensor;

  }

  /*
   * Return the distance between the Power Cell at the end of the intake and the
   * entrance to the indexer in centimeters.
   */
  public double getDistanceToPowerCell() {
    return m_powerDetectorSensor.getRangeCm();
  }

  public DoubleSupplier getDistanceToPowerCellMeasurer() {
    return m_powerDetectorSensor;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("Intake Power Cell detector range (cm)", m_powerDetectorSensor.getRangeCm());

  }
}
