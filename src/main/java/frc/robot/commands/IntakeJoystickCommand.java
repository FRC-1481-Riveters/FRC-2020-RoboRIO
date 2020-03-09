/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RumbleTimerJoystick;
import frc.robot.subsystems.Intake;

public class IntakeJoystickCommand extends CommandBase {
  private Intake m_intake;
  RumbleTimerJoystick m_RumbleTimerJoystick;
  final JoystickDeadband m_deadbander = new JoystickDeadband(0.15);
  /**
   * Creates a new IntakeIndexerJoystickCommand.
   */
  public IntakeJoystickCommand(Intake intake, RumbleTimerJoystick Joystick) {
    m_intake = intake;
    // Use addRequirements() here to declare subsystem dependencies.
    m_RumbleTimerJoystick = Joystick;
    addRequirements(intake);
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


    m_intake.setSpeed(m_deadbander.deadband(-m_leftJoystickValue));
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
