/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.wheelOfFortuneColorSpinny;
import frc.robot.Constants;
import frc.robot.RumbleTimerJoystick;

public class RotateOrJogControlPanelCommand extends CommandBase {
  private final wheelOfFortuneColorSpinny m_wheelOfFortuneColorSpinny;
  private final RumbleTimerJoystick m_RumbleTimerJoystick;

  /**
   * Creates a new RotateOrJogControlPanelCommand.
   */
  public RotateOrJogControlPanelCommand(wheelOfFortuneColorSpinny Subsystem, RumbleTimerJoystick Joystick) {
    m_wheelOfFortuneColorSpinny = Subsystem;
    addRequirements(Subsystem);

    m_RumbleTimerJoystick = Joystick;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_wheelOfFortuneColorSpinny.zeroPosition(); // sets encoder count to 0
    m_wheelOfFortuneColorSpinny.spinToPosition(Constants.controlPanelEncoderCounts);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    // motor stops change mode
    m_wheelOfFortuneColorSpinny.stopBrad();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_wheelOfFortuneColorSpinny.getActualPosition() <= Constants.controlPanelEncoderCounts
        - Constants.acceptableErrorControlPanel) {
      return false;
    } else {
      m_RumbleTimerJoystick.rumbleTime(Constants.controlPanelVibrationTime);
      return true;

    }
    // if <4000 then return false
    // else return true

  }
}
