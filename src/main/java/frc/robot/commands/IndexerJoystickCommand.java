/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.RumbleTimerJoystick;
import frc.robot.subsystems.Indexer;

public class IndexerJoystickCommand extends CommandBase {
  private Indexer m_indexer;
  RumbleTimerJoystick m_RumbleTimerJoystick;
  final JoystickDeadband m_deadbander = new JoystickDeadband();

  /**
   * Creates a new IndexerJoystickCommand.
   */
  public IndexerJoystickCommand(Indexer indexer, RumbleTimerJoystick Joystick) {
    m_indexer = indexer;
    m_RumbleTimerJoystick = Joystick;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(indexer);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    final double m_rightJoystickValue;
    m_rightJoystickValue = m_RumbleTimerJoystick.getY(Hand.kRight);
    m_indexer.setClosedLoopSpeed(m_deadbander.deadband(-m_rightJoystickValue) * Constants.indexerMotorSpeed);
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
