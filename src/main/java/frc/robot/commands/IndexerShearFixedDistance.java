/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Indexer;

public class IndexerShearFixedDistance extends CommandBase {
  private final Indexer m_indexer;
  private final double m_distanceToMovePowerCell;

  /*
   * This class moves all the Power Cells in the Indexer a fixed distance. It does
   * this by moving only a single belt up or down 2x the specified distance
   * distanceInCentimeters. Thus, this command works if the Power Cell is between
   * the two belts, or pressed by the upper belt against the non-moving
   * super-structure of the robot. The command expects that the commanded distance
   * is how far the Power Cell will move, not how far the belt will move. (the
   * belt will move twice as far as the Power Cell because only a single belt is
   * moving and shearing the Power Cell)
   * 
   * Positive values move the Power Cells up, closer to the Kicker
   * 
   * Negative values move the Power Cells down, closer to the Intake
   * 
   * This command quits when it gets close to the target location, which is a
   * toleranced value and might not be exactly where the caller required.
   */
  public IndexerShearFixedDistance(final Indexer subsystem, final double distanceInCentimeters) {
    m_indexer = subsystem;

    addRequirements(subsystem);

    m_distanceToMovePowerCell = distanceInCentimeters;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {

    m_indexer.moveClosedLoopDistance(m_distanceToMovePowerCell);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    if (interrupted == true) {
      /*
       * Stop the indexer only if the command was interrupted. This represents an
       * aborted move, and we should stop moving immediately.
       */
      m_indexer.setClosedLoopSpeed(0.0);
    }
    /*
     * If the command ends regularily (interrupted == false), don't bother telling
     * the indexer to stop moving. It'll complete movement on its own. The command
     * doesn't have to worry about it and shouldn't interfere with it.
     */
  }

  @Override
  public boolean isFinished() {
    /*
     * Check to see if the indexer has moved far enough to consider the movement
     * complete.
     */
    return m_indexer.isOnTarget();
  }
}
