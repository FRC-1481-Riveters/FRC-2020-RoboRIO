/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Kicker;
import frc.robot.subsystems.Shooter;

public class KickerAdvanceCommand extends CommandBase {
  private Kicker m_kicker;
  private Shooter m_shooter;
  private long previousCellsYeeted;
  /**
   * Creates a new KickerAdvanceCommand.
   */
  public KickerAdvanceCommand(Kicker subsystem, Shooter subsystemShooterTime) {
    m_kicker = subsystem;
    m_shooter = subsystemShooterTime;
    addRequirements(subsystem);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  //start kicker motor
  m_kicker.setSpeed(Constants.kickerMotorSpeed);
  previousCellsYeeted = m_shooter.getTotalYeets();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  //take balls from indexer, sends ball to shooter

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
   //button released
    m_kicker.setSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    if (m_shooter.getTotalYeets() > previousCellsYeeted){
      return true;
    }
    else {
      return false;}
    //optical sensor senses no balls
  }
}
