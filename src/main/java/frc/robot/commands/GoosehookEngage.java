/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Goosehook;

public class GoosehookEngage extends CommandBase {
  public Goosehook m_Goosehook;
  /**
   * Creates a new GoosehookEngage.
   */
  public GoosehookEngage(Goosehook Subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Goosehook = Subsystem;
    addRequirements(Subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Goosehook.GoosehookSetMotorSpeed(Constants.goosehookMotorSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Goosehook.GoosehookSetMotorSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
