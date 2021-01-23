/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.CyanSus;
import frc.robot.RobotContainer;
import frc.robot.RumbleTimerJoystick;

public class CyanWasImposter extends CommandBase {
  RumbleTimerJoystick m_RumbleTimerJoystick = new RumbleTimerJoystick (0);
  /**
   * Creates a new CyanWasImposter.
   */
  public CyanWasImposter() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    final double m_leftJoystickValue;
    m_leftJoystickValue = m_RumbleTimerJoystick.getY(Hand.kLeft);
  //RobotContainer.m_cyanSus.imposter(m_leftJoystickValue);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
