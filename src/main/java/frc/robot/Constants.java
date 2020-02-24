/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Units;
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
	public static final int driverController = 0;
	public static final int operatorController = 1;

	/* Control Panel ------------------------------ */
	public static final int controlPanelMotorControllerCANId = 5; // find actual value
	public static final I2C.Port i2cPortColorSensor = I2C.Port.kOnboard; // use for color sensor
	public static final double controlPanelSpeedMax = 60.0; // RPM
	public static final double controlPanelMaxAcceleration = 200.0; // RPM/s
	public static final double controlPanelStopMotor = 0.0;
	public static final int controlPanelEncoderCounts = 4000; // hypothetical value between 3-5 rotations --more than
																// 3//
	public static final int controlPanelVibrationTime = 1000; // miliseconds
	public static final int acceptableErrorControlPanel = 100; // acceptable error for control panel spinning

	public static double controlPanelWheelEncoderCountsToRPM(int encoderCounts) {
		double RPM;
		RPM = encoderCounts / 1.0; // TODO: determine current constant to use here
		return RPM;
	}

	public static int controlPanelWheelAngleToEncoderCounts(int degrees) {
		int encoderCounts;
		encoderCounts = degrees * 200; // TODO: find out the number because we dont know
		return encoderCounts;
	}

	/**
	 * Gains used in Position Closed Loop, to be adjusted accordingly Gains(kp, ki,
	 * kd, kf, izone, peak output);
	 */
	public static final Gains kGains = new Gains(0.25, 0.0, 0.0, 0.0, 0, 1.0);

	/* Intake ------------------------------------ */
	public static final int intakeMotorControllerCANId = 10;
	public static final double intakeMotorSpeed = .75; // percent
	public static final double intakeRunMotorSpeed = .325;
	public static final double howCloseIsThePowerCell = 18.6; // cm
	public static final double howCloseIsThePowerCellTolerance = 1; // cm

	/* Kicker ----------------------------- */
	public static final int kickerMotorControllerCANId = 7;
	public static final double kickerMotorSpeed = -1.0; // percent
	public static final double kickerCaptureSpeed = -0.30; // percent

	/* Shooter ------------------------------ */
	public static final int shooterMotorControllerCANId = 1;
	public static final double shooterIntendedSpeedTolerance = 0.02;
	public static final double shooterYeetSpeedInitiation = 4200.0; // RPM 3100:40in
	public static final double shooterYeetSpeedWall = 3800.0; // RPM

	/* Cameras ---------------------------------------- */
	public static final int visionCameraPort = 0;
	public static final int frontCamera = 1;
	public static final int rearCamera = 2;
	// inches to travel in one direction before auto changing camera feed
	public static final double distanceToSwitchCameraFeed = 24.0;

	/* Elevator ----------------------------------------- */
	public static final int maxWinchEncoderCounts = 18000; // get winch value
	public static final int minWinchEncoderCounts = 0;

	public static final int raiseElevatorButton = 1; // get buttons value from SAD
	public static final int lowerElevatorButton = 2;
	public static final int engageUntitledGooseButton = 1;
	public static final int disengageUntitledGooseButton = 1;
	public static final double latchReleaseEncoderCount = 500.0; // (automatic???)
	public static final double goosehookMotorSpeed = 0.50;

	public static final int raiseElevatorButtonjoystick = 1; // 1 for operator, 0 for driver, tbd
	public static final int lowerElevatorButtonjoystick = 1;
	public static final int engageUntitledGooseButtonjoystick = 1;
	public static final int disengageUntitledGooseButtonjoystick = 1;
	public static final boolean kElevatorSensorPhase = false;

	public static final int winchMotorElevatorCANId = 8;
	public static final int winchMotorElevator2CANId = 6;
	public static final int untitledGooseMotorCANId = 4; // change back to 9
	public static final int relayElevatorSolenoid = 0;

	/* Drive --------------------------------------- */
	public static final double driveMotorRampRate = 0.5; // number = seconds till full speed
	public static final double rotationInQuarter = 0.7;
	public static final int frontLeftMotor = 13;
	public static final int rearLeftMotor = 12;
	public static final int frontRightMotor = 2;
	public static final int rearRightMotor = 3;
	public static final double closedLoopRampRate = 0.1; // Volts / msS
	public static final double kMaxSpeedMetersPerSecond = Units.feetToMeters(14.0 /* ft/s */);
	public static final double kMaxAccelerationMetersPerSecondSquared = Units.feetToMeters(3.0 /* ft/s^2 */);
	public static final double driveTireDiameterInMeters = Units.inchesToMeters(8.0);

	public static final Gains kDriveGains = new Gains(2.2e-4, .00000005, 0.00001, 1.5e-4, 0, 1.0);
	// Number of inches of robot travel per revolution of the NEO (which is returned
	// in getPosition())
	// This must comprehend the entire drivetrain; NEO counts read from
	// getPosition(), wheel diameter and gearbox gearing ratio
	public static final double driveTrainInchesPerEncoderCounts = 2.3;
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
	public static final boolean kSensorPhase = true;

	/**
	 * Choose based on what direction you want to be positive, this does not affect
	 * motor invert.
	 */
	public static final boolean kMotorInvert = false;

	/* Indexer ----------------------------------- */
	public static final int indexerMotorControllerCANId = 9;
	public static final int secondIndexerMotorControllerCANId = 11;
	public static final double indexerMotorSpeed = 400; // RPM
	public static final boolean kIndexerSensorPhase = true;
	public static final int indexerEncoderCount = 8192;
	/*
	 * Set MotionMagic's maximum velocity when moving from position to position to
	 * the indexerMotorSpeed. Convert the indexerMotorSpeed from RPM to encoder
	 * counts per 0.1 seconds.
	 */
	public static final int indexerMotionMagicMaxVelocity = Math
			.toIntExact(Math.round(indexerEncoderCount * indexerMotorSpeed / 600.0));

	/*
	 * Set MotionMagic's maximum acceleration when moving from position to position
	 * to 3000 RPM/s. Convert the acceleration from RPM/s to encoder counts per 0.1
	 * seconds per second.
	 */
	public static final int indexerMotionMagicMaxAcceleration = Math
			.toIntExact(Math.round(indexerEncoderCount * 3000.0 / 600.0));

	/*
	 * This is the maximum time the indexer will wait to stack a single power cell
	 * during automatic stacking while loading the indexer.
	 */
	public static final double indexerStack1PwrCellTimeout = 5.0; // seconds

	public static final double distanceToMovePowerCellWhenLoading = 19; // cm
	public static final double indexerPulleyDiameter = 6.68528; // diameter of indexer pulleys in centimeters
	/*
	 * Distance of the Power Cell that's in a normal position at the base of the
	 * indexer. This is used to modify the distance the Power Cell is pulled into
	 * the indexer to its first stacked position to compensate for the distance the
	 * ball has already travelled when the indexer starts moving. This way, the
	 * Power Cell doesn't travel too far up the indexer if it starts already part of
	 * the way up the indexer when the command starts.
	 */

	public static final double distanceToPowerCellAtBaseOfIndexer = 18.0;
	/*
	 * tolerated error in distance to the target when moving the belts to a fixed
	 * position
	 */
	public static final double indexerToleratedPositionError = 1.5;
	/*
	 * TalonSRX's PIDF gains are calculated base on 1023 as the maximum output,
	 * which is 100% duty cycle, which is all of the Talon's speed and capability.
	 * Thus, if you want to drive the TalonSRX at 100% if the error is 200 units
	 * (e.g. RPM for Velocity mode, or encoder counts for position mode), divide
	 * 1023 by 200 to get your Kp gain:
	 * 
	 * Kp = 1023.0 / ErrorAt100%Output
	 * 
	 * Kp = 1023.0 / 200.0
	 * 
	 * Kp = 5.12
	 * 
	 * The TalonSRX's measure velocity in units of encoder counts per interval of
	 * 0.1 seconds. So, if you're spinning at 100 RPM, and your encoder has 8192
	 * counts per revolution, that's equal to:
	 * 
	 * CountsPerInterval(100 RPM) = 100 revolution/minute * 8192 counts/revolution *
	 * minute/60 second * second/10 interval
	 * 
	 * CountsPerInterval(100 RPM) = (100 * 8192) / (60 * 10)
	 * 
	 * CountsPerInterval(100 RPM) = 1365.33 counts per interval
	 * 
	 * Put this all together to compute the Kp for a TalonSRX in Closed Loop
	 * velocity mode with an encoder that has 2048 counts per revolution:
	 * 
	 * Kp = 1023.0 / 1365.33
	 * 
	 * Kp = 0.75
	 */

	public static final Gains kGains_Indexer = new Gains(0.15, 0.0, 0.0, 0.0, 0, 1.0);

}
