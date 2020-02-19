/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;
import java.util.function.DoubleSupplier;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;
import frc.robot.Constants;
import frc.robot.fastSPILink;
import frc.robot.commands.CameraFeeds;

public class CameraSubsystem extends SubsystemBase implements Sendable {

  private Pixy2 pixycam;
  ArrayList<Block> blocks;

  protected DoubleSupplier m_robotDistanceSupplier;

  protected NetworkTableEntry m_currentCameraFeedNameEntry = NetworkTableInstance.getDefault().getEntry("DriverCamera");
  protected double m_lastOdometer;
  protected double m_lastDirectionOfTravel;
  protected double m_odometerAtLastChangeInDirection;

  /**
   * Creates a new CameraSubsystem.
   */
  public CameraSubsystem(DoubleSupplier robotDistanceSupplier) {
    pixycam = Pixy2.createInstance(new fastSPILink(1_500_000));
    pixycam.init(0);

    m_robotDistanceSupplier = robotDistanceSupplier;

    pickDriverCameraFeed("Front");
  }

  public void selectNextDriverCameraFeed() {

    String currentCameraFeedName = m_currentCameraFeedNameEntry.getString(/* default value */ "Front");

    /*
     * Determine the index number of the currently selected DriverCamera value by
     * taking the string value that's currently selected, like "Front", determine
     * where THAT string is in the list of camera feeds, then select the NEXT list
     * item. So, for example, if currentCameraFeedName="Front" and the
     * CameraFeeds.names list has the following cameras in it: "Front", "Rear" Then
     * change the selected driver camera string in DriverCamera to "Rear" by
     * incrementing the index value to the next element in the array
     * CameraFeeds.names. This will cause the RPi camera server to switch to feed
     * "Rear" when it sees this NetworkTables signal change.
     * 
     * If the camera at the end of the list is already selected, (like "Rear" in
     * this example) just start the list over and select the item at index 0 (which
     * is "Front") in this example.
     * 
     * If the software can't even find the currently selected camera in
     * CameraFeeds.names then just default to the first item CameraFeeds.names.
     * 
     */
    int currentFeedIndex = CameraFeeds.names.indexOf(currentCameraFeedName);

    if (currentFeedIndex >= 0) {
      currentFeedIndex++;
      currentFeedIndex = currentFeedIndex % CameraFeeds.names.size();
      m_currentCameraFeedNameEntry.setString(CameraFeeds.names.get(currentFeedIndex));
    } else {
      m_currentCameraFeedNameEntry.setString(CameraFeeds.names.get(0));
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    updateDriverCameraFeed();

    int blockState = pixycam.getCCC().getBlocks(false, 255, 8); // run getBlocks with arguments to have the camera

    if (blockState >= 0) {
      // acquire target data
      blocks = pixycam.getCCC().getBlocks(); // assign the data to an ArrayList for convenience

      if (blocks.size() > 0) {
        double xcoord = blocks.get(0).getX(); // x position of the largest target
        double ycoord = blocks.get(0).getY(); // y position of the largest target

        String data = blocks.get(0).toString(); // string containing target info

        SmartDashboard.putNumber("Pixycam Xcoord", xcoord);
        SmartDashboard.putNumber("Pixycam Ycoord", ycoord);
        SmartDashboard.putString("Pixycam Data", data);

      }

      // push to dashboard how many targets are detected
      SmartDashboard.putNumber("Pixycam blocks detected", blocks.size());
    }
  }

  protected void updateDriverCameraFeed() {

    /* Get the cumulative distance the robot has travelled */
    double currentOdometer = m_robotDistanceSupplier.getAsDouble();

    /*
     * Determine how far the robot has travelled since the last time we asked for
     * the robot's odometer.
     */
    double recentDistanceTravelled = currentOdometer - m_lastOdometer;

    /*
     * We're done calculating with m_lastOdometer. Update it with the latest value
     * of the currentOdometer so we're ready to test the *next* odometer reading
     * later.
     */
    m_lastOdometer = currentOdometer;

    /*
     * Determine if we've changed our direction of travel, either
     * forward-to-backwards or backwards-to-forwards.
     * 
     * The Math.signum() function returns:
     * 
     * -1.0 if the argument is negative (meaning we just changed from driving
     * forward to backward)
     * 
     * 0.0 if the argument is a 0.0 (meaning we're not moving)
     * 
     * 1.0 if the argument is positive (meaning we just changed from driving
     * backward to forward)
     */
    double directionSignum = Math.signum(recentDistanceTravelled);
    if (directionSignum != m_lastDirectionOfTravel && directionSignum != 0.0) {
      /*
       * The robot has changed direction since the last time we looked because the
       * recentDistanceTravelled has a different sign from the last time we did this
       * calculation. Get a snapshot of the current odometer. It's important because
       * we'll use it later to determine how far we've travelled since we changed
       * direction.
       */
      m_odometerAtLastChangeInDirection = currentOdometer;

      m_lastDirectionOfTravel = directionSignum;
    }

    /*
     * Determine if we've driving in this direction far enough to trigger the change
     * in the driver's camera view. We don't want quick, short changes in direction
     * to trigger the camera view change but we don't want to delay the change any
     * longer than necessary after we actually start moving, in earnest, in a new
     * direction.
     */
    double distanceTravelledSinceLastChangeInDirection = currentOdometer - m_odometerAtLastChangeInDirection;

    if (distanceTravelledSinceLastChangeInDirection > Constants.distanceToSwitchCameraFeed) {
      /* We're definitely driving forwards. Change the camera to the front camera. */
      pickDriverCameraFeed("Front");
    }

    if (distanceTravelledSinceLastChangeInDirection < -Constants.distanceToSwitchCameraFeed) {
      /* We're definitely drive backwards. Change the camera to the rear camera. */
      pickDriverCameraFeed("Rear");
    }

  }

  public void pickDriverCameraFeed(String cameraFeedName) {
    m_currentCameraFeedNameEntry.setString(cameraFeedName);
  }

  public void initSendable(SendableBuilder builder) {
    super.initSendable(builder);

    builder.addDoubleProperty(".Odometer", () -> {
      return m_lastOdometer;
    }, null);
    builder.addDoubleProperty(".OdometerAtLastChangeInDirection", () -> {
      return m_odometerAtLastChangeInDirection;
    }, null);
    builder.addDoubleProperty(".DirectionOfTravel", () -> {
      return m_lastDirectionOfTravel;
    }, null);
  }
}
