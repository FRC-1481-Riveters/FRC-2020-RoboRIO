/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.Indexer;

public class IndexerCarryUpCommand extends CommandBase {
  private Indexer m_indexer;

  /**
   * Creates a new indexerCarryUpCommand.
   */
  public IndexerCarryUpCommand(Indexer subsystem) {
    m_indexer = subsystem;

    addRequirements(subsystem);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    /*
     * Move the indexer so that it draws a Power Cell from the base of the indexer
     * to the first stacking position in the indexer. This is the lowest position
     * that will allow another Power Cell to be indexed into the indexer and still
     * have the correct packing distance between Power Cells.
     * 
     * This command assumes that when the indexer moves, the Power Cell moves with
     * it.
     * 
     * If there's already a Power Cell near the base of the indexer, subtract the
     * extra distance that it's already travelled into the indexer from the distance
     * that the indexer must move the Power Cell to its first stacking location in
     * the indexer.
     * 
     * Assume that the Power Cell is in its home position at
     * Constants.distanceToPowerCellAtBaseOfIndexer and anything that's lower than
     * that represents a Power Cell that's already started to travel into the
     * indexer. Subtract the distances and offset the commanded distance the Power
     * Cell has to travel to account for the distance that the Power Cell has
     * ALREADY travelled (before we started this command)
     */

    double distanceThePowerCellHasAlreadyTravelled = Math.max(0.0,
        Constants.distanceToPowerCellAtBaseOfIndexer - m_indexer.getDistanceToPowerCell());

    double distanceToMovePowerCell = Constants.distanceToMovePowerCellWhenLoading
        - distanceThePowerCellHasAlreadyTravelled;

    m_indexer.moveClosedLoopDistance(distanceToMovePowerCell);

    SmartDashboard.putNumber("IndexerCarryUpCommand PowerCell dist already moved (cm)",
        distanceThePowerCellHasAlreadyTravelled);

    SmartDashboard.putNumber("IndexerCarryUpCommand Power Cell dist to move (cm)", distanceToMovePowerCell);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
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
