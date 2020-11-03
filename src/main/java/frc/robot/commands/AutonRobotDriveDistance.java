/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileCommand;
import frc.robot.Constants;
import frc.robot.subsystems.DriveTrain;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutonRobotDriveDistance extends TrapezoidProfileCommand {

//  protected DriveTrain m_driveTrain;

  /**
   * Creates a new AutonRobotDriveDistance.
   * 
   * Positive values of distanceToTravelInFeet travel forward, which is toward the
   * direction of the shooter's shoot trajectory, which is the side the battery is
   * on.
   * 
   * Negative values of distanceToTravelInFeet travel backward, which is away from
   * the shooter's trajectory which is the side the Power Cell intake is on.
   */
  public AutonRobotDriveDistance(DriveTrain driveTrain, double distanceToTravelInFeet) {
    super(
        // The motion profile to be executed
        new TrapezoidProfile(
            // The motion profile constraints
            new TrapezoidProfile.Constraints(Constants.kMaxSpeedMetersPerSecond,
                Constants.kMaxAccelerationMetersPerSecondSquared),
            // Goal state
            new TrapezoidProfile.State(Units.feetToMeters(distanceToTravelInFeet), 0),
            // Initial state
            new TrapezoidProfile.State()),
        state -> {
          driveTrain.driveAtSpeed(state.velocity, state.velocity);
        }, driveTrain);
  }
}
