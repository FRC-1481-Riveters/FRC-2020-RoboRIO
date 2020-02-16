/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;

import frc.robot.fastSPILink;
import frc.robot.commands.CameraFeeds;

public class CameraSubsystem extends SubsystemBase {

  private Pixy2 pixycam;
  ArrayList<Block> blocks;

  /**
   * Creates a new CameraSubsystem.
   */
  public CameraSubsystem() {
    pixycam = Pixy2.createInstance(new fastSPILink(1_500_000));
    pixycam.init(0);

    // UsbCamera front = CameraServer.getInstance().startAutomaticCapture(0)
  }

  public void selectNextDriverCameraFeed() {

    NetworkTableEntry currentCameraFeedNameEntry = NetworkTableInstance.getDefault().getEntry("DriverCamera");

    String currentCameraFeedName = currentCameraFeedNameEntry.getString(/* default value */ "Front");

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
      currentCameraFeedNameEntry.setString(CameraFeeds.names.get(currentFeedIndex));
    } else {
      currentCameraFeedNameEntry.setString(CameraFeeds.names.get(0));
    }
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

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
}
