/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import frc.robot.Gains;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
	public double controlPanelWheelEncoderCountsToRPM(int encoderCounts) {
		double RPM;
		RPM = encoderCounts / 1.0; // TODO: determine current constant to use here
		return RPM;
	}

	public int controlPanelWheelAngleToEncoderCounts(int degrees) {
		int encoderCounts;
		encoderCounts = degrees * 200; // TODO: find out the number because we dont know
		return encoderCounts;
	}

	public static int driverController = 0;
	public static int operatorController = 1;

	public static int controlPanelMotorControllerCANId = 4;
	public static int rotationControlButton = 5;
	public static int positionControlButton = 6;
	public static I2C.Port i2cPortColorSensor = I2C.Port.kOnboard; // use for color sensor
	public static double controlPanelSpeedMax = 60.0; // RPM
	public static double controlPanelMaxAcceleration = 60.0; // RPM/s
	public static double controlPanelStopMotor = 0.0;
	public static int controlPanelEncoderCounts = 4000; // hypothetical value between 3-5 rotations --more than 3//
	public static int controlPanelVibrationTime = 1000; // miliseconds
	public static int acceptableErrorControlPanel = 100; // acceptable error for control panel spinning

	public static int intakeMotorControllerCANId = 5;
	public static int indexerMotorControllerCANId = 6;
	public static int kickerMotorControllerCANId = 7;
	public static int shooterMotorControllerCANId = 8;
	public static double intakeMotorSpeed = 60.0; // RPM
	public static double indexerMotorSpeed = 60.0; // RPM
	public static double kickerMotorSpeed = 60.0; // RPM
	public static double shooterIntendedSpeed = 3000.0; // RPM
	public static double shooterIntendedSpeedTolerance = 0.05;
	// TODO: find actual speeds of motors
	public static int visionCameraPort = 0;
	public static int frontCamera = 1;
	public static int rearCamera = 2;

	/**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from 0,1,2
	 * or 3. Only the first two (0,1) are visible in web-based configuration.
	 */

	public static final int kSlotIdx = 0;

	/**
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For now
	 * we just want the primary one.
	 */

	public static final int kPIDLoopIdx = 0;
	/**
	 * Set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int kTimeoutMs = 30;

	/* Choose so that Talon does not report sensor out of phase */
	public static boolean kSensorPhase = true;

	/**
	 * Choose based on what direction you want to be positive, this does not affect
	 * motor invert.
	 */
	public static boolean kMotorInvert = false;

	/**
	 * Gains used in Position Closed Loop, to be adjusted accordingly Gains(kp, ki,
	 * kd, kf, izone, peak output);
	 */
	public static final Gains kGains = new Gains(0.15, 0.0, 1.0, 0.0, 0, 1.0);

	public static int frontLeftMotor = 3;
	public static int rearLeftMotor = 2;
	public static int frontRightMotor = 12;
	public static int rearRightMotor = 13;

	// public static int neoEncoderLeft = 5;
	// public static int neoEncoderRight = 6;

}
