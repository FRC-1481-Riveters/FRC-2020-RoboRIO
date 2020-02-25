/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;

public class IndexerMoveAtFixedSpeed extends CommandBase {
  private Indexer m_indexer;
  private double m_speed;

  /**
   * Creates a new IndexerMoveAtFixedSpeed.
   * 
   * This class moves both the Indexer's belts at a fixed speed.
   * 
   * Postive speedRPM moves the Power Cells up toward the Kicker and Shooter
   * 
   * Negative speedRPM moves the Power Cells down toward the Intake
   * 
   * This command never stops. You must time it out. For example, when you
   * instatiate this command, use the .withTimeout() to specify a timeout, e.g.:
   * 
   * // Move the indexer for 2.0 seconds at indexerMotorSpeed RPM new
   * IndexerMoveAtFixedSpeed(indexer,Constants.indexerMotorSpeed).withTimeout(2.0);
   */
  public IndexerMoveAtFixedSpeed(Indexer indexer, double speedRPM) {
    addRequirements(indexer);
    m_indexer = indexer;
    m_speed = speedRPM;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_indexer.setClosedLoopSpeed(m_speed);
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
    /*
     * Only end if we're given a very, very slow speed to move at. This is basically
     * trying to detect the value 0.0. It's considered poor practice to check
     * floating point values exactly against 0.0, so use the smallest value that a
     * float can be, (which is larger than the smallest value a double can be) and
     * check if we're bigger than that. If we are, we're definitely not exactly 0.0
     * so keep spinning.
     */
    if (Math.abs(m_speed) > Float.MIN_VALUE) {
      return false;
    } else {
      return true;
    }
  }
}
