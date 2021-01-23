/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Kicker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonSuperTrenchSequence extends SequentialCommandGroup {
  /**
   * Creates a new public AutonShootAllStackedPowerCellsAndDriveOffLine
   */
  public AutonSuperTrenchSequence(Shooter shooter, Indexer indexer, Kicker kicker,
      DriveTrain drive, Intake intake) {
    addCommands( //
        // Spin up the shooter and reposition the stack of Power Cells to keep them from
        // jamming
        new ParallelCommandGroup( //
            new ShooterYeetCommand(shooter, Constants.shooterYeetSpeedInitiation), //
            new IndexerShearFixedDistance(indexer, -2.0) // Unpack the Power Cells if they're too tightly packed in the
                                                         // Indexer by moving them away from the Kicker a little bit
        ).withTimeout(2.0), // Don't wait too long for the shooter to spin up or the indexer to reposition
                            // the stack. The Auton *must* continue and end with movement before auton
                            // expires

       // new KickerMoveAtFixedSpeed(kicker, -5000.0).withTimeout(0.5), // Start the kicker moving

        // Sequence the indexer to feed all the balls into the kicker (and shooter).
        new IndexerMoveAtFixedSpeed(indexer, Constants.indexerMotorSpeed).withTimeout(3.0),
        new AutonRobotDriveTurnDegreez(drive, -60).withTimeout(3.0),
        // new IntakeArmDown(intake,100).withTimeout(3.0),
        new ParallelCommandGroup(
            new IntakePositionPowerCellCommand(intake),
            new IndexerCarryUpCommand(indexer), //
            new KickerAdvanceCommand(kicker, shooter), //
            new AutonRobotDriveDistance(drive, -4.0)
             ).withTimeout(6.0), //
        new AutonRobotDriveDistance (drive, 4.0).withTimeout(6.0),
        new AutonRobotDriveTurnDegreez(drive, 60).withTimeout(3.0),
        new ParallelCommandGroup( //
        new ShooterYeetCommand(shooter, Constants.shooterYeetSpeedInitiation), //
        new IndexerShearFixedDistance(indexer, -2.0) // Unpack the Power Cells if they're too tightly packed in the
                                                     // Indexer by moving them away from the Kicker a little bit
    ).withTimeout(10.0), // Don't wait too long for the shooter to spin up or the indexer to reposition
                        // the stack. The Auton *must* continue and end with movement before auton
                        // expires

new KickerMoveAtFixedSpeed(kicker, -5000.0).withTimeout(0.5), // Start the kicker moving

new ParallelDeadlineGroup( //
    new ShooterYeetCommand(shooter, 0.0), // Shut down the shooter, we don't need it any longer.
    new IndexerMoveAtFixedSpeed(indexer, 0.0), // Shutdown the indexer. We don't need it any longer.
    new KickerMoveAtFixedSpeed(kicker, 0.0) //
) //
    );
  }
}
/*
TODO: 
Start @ initation line
Shoot 3 balls
Backup intake end to wall
Turn intake to face power cells
Lower arm
Run Intake, catapilla
Move intake end forward
Stop intake, catapilla
Raise arm
Move shooter end to goal 
Turn shooter end to goal 
Shoot 3 balls

intake: front
shooter: back
*/