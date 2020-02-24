/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Kicker;
import frc.robot.subsystems.Shooter;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonShoot3StackedPowerCellsAndDriveOffLine extends SequentialCommandGroup {
  /**
   * Creates a new public AutonShootAllStackedPowerCellsAndDriveOffLine
   */
  public AutonShoot3StackedPowerCellsAndDriveOffLine(Shooter shooter, Indexer indexer, Kicker kicker,
      DriveTrain drive) {
    addCommands( //
        new ParallelCommandGroup( //
            new ShooterYeetCommand(shooter, Constants.shooterYeetSpeedInitiation), //
            new IndexerShearFixedDistance(indexer, -2.0) // Unpack the Power Cells if they're too tightly packed in the
                                                         // Indexer by moving them away from the Kicker a little bit
        ).withTimeout(5.0), //
        new KickerAdvanceCommand(kicker, shooter).withTimeout(2.0), // Shoot the first Power Cell
        new KickerAdvanceCommand(kicker, shooter).withTimeout(1.0), // Shoot the second Power Cell
        new KickerAdvanceCommand(kicker, shooter).withTimeout(1.0), // Shoot the third Power Cell
        new ParallelCommandGroup( // run some commands in parallel so we don't delay before moving
            new ShooterYeetCommand(shooter, 0.0), // Shut down the shooter, we don't need it any longer
            new AutonRobotDriveDistance(drive, 4.0) // Move the robot off the initiation line
        ) //
    );
  }
}
