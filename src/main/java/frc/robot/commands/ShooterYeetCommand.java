/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShooterYeetCommand extends CommandBase {
  private Shooter m_shooter;
  private double m_shooterIntendedSpeed;

  /**
   * Creates a new ShooterYeetCommand.
   */
  public ShooterYeetCommand(Shooter subsystem, double shooterIntendedSpeed) {
    m_shooter = subsystem;
    m_shooterIntendedSpeed = shooterIntendedSpeed;
    addRequirements(subsystem);
    SmartDashboard.putNumber("Shooter Tuning Speed", m_shooterIntendedSpeed);
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_shooterIntendedSpeed = SmartDashboard.getNumber("Shooter Tuning Speed", 0);
    m_shooter.setClosedLoopSpeed(m_shooterIntendedSpeed);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_shooter.isAtSpeed();
  }
}
