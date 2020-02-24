/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RumbleTimerJoystick;
import frc.robot.subsystems.colorsensor;
import frc.robot.subsystems.wheelOfFortuneColorSpinny;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;

public class PositionControlPanelCommand extends CommandBase {
  public colorsensor m_colorsensor;
  public wheelOfFortuneColorSpinny m_wheelOfFortuneColorSpinny;
  private final RumbleTimerJoystick m_RumbleTimerJoystick;
  private int m_targetPosition;

  /**
   * Creates a new colorsensor.
   */
  public PositionControlPanelCommand(wheelOfFortuneColorSpinny Subsystem, colorsensor subsystem2Colorsensor,
      RumbleTimerJoystick Joystick) {
    m_wheelOfFortuneColorSpinny = Subsystem;
    m_colorsensor = subsystem2Colorsensor;
    m_RumbleTimerJoystick = Joystick;
    addRequirements(Subsystem, subsystem2Colorsensor);
    // Use addRequirements() here to declare subsystem dependencies.

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    m_wheelOfFortuneColorSpinny.zeroPosition(); // sets encoder count to 0

    String gameData;
    gameData = DriverStation.getInstance().getGameSpecificMessage();

    String ourColorSensor = m_colorsensor.getOurColorSensor();
    String colorCombo = new String(ourColorSensor + gameData);
    int angleToRotate = 0;

    if (gameData.length() > 0) {
      switch (colorCombo) {
      case "RB":
        angleToRotate = 90;
        break;
      case "RY":
        angleToRotate = -45;
        break;
      case "RG":
        angleToRotate = 45;
        break;
      case "BR":
        angleToRotate = 90;
        break;
      case "BY":
        angleToRotate = 45;
        break;
      case "BG":
        angleToRotate = -45;
        break;
      case "YR":
        angleToRotate = 45;
        break;
      case "YB":
        angleToRotate = -45;
        break;
      case "YG":
        angleToRotate = 90;
        break;
      case "GR":
        angleToRotate = -45;
        break;
      case "GB":
        angleToRotate = 45;
        break;
      case "GY":
        angleToRotate = 90;
        break;
      default:
        angleToRotate = 0;
        // This is corrupt data
        break;
      }
    } else {
      angleToRotate = 0;
      // Code for no data received yet
      // TODO: offset to be added
    }
    m_targetPosition = Constants.controlPanelWheelAngleToEncoderCounts(angleToRotate);
    m_wheelOfFortuneColorSpinny.spinToPosition(m_targetPosition);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_wheelOfFortuneColorSpinny.stopBrad();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (Math.abs(
        m_wheelOfFortuneColorSpinny.getActualPosition() - m_targetPosition) <= Constants.acceptableErrorControlPanel) {

      m_RumbleTimerJoystick.rumbleTime(Constants.controlPanelVibrationTime);
      return true;
    } else {
      return false;
    }
  }
}
