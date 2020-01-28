/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.ExampleCommand;
import frc.robot.commands.PositionControlPanelCommand;
import frc.robot.commands.RotateOrJogControlPanelCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.colorsensor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.wheelOfFortuneColorSpinny;
import frc.robot.Constants;
import edu.wpi.cscore.UsbCamera;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final colorsensor m_colorsensor = new colorsensor();
  private final wheelOfFortuneColorSpinny m_wheelOfFortuneColorSpinny = new wheelOfFortuneColorSpinny();
  private final CameraSubsystem m_cameraSubsystem = new CameraSubsystem();
  private final Shooter m_shooter = new Shooter();
  private final DriveTrain m_drive = new DriveTrain(); 
  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);
  RumbleTimerJoystick m_driverController = new RumbleTimerJoystick(Constants.driverController);
  RumbleTimerJoystick m_operatorController = new RumbleTimerJoystick(Constants.operatorController);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */


  public class RumbleTimerJoystick extends XboxController {

    private long m_timeStampTimeout;

    public RumbleTimerJoystick(int port) {
      super(port);
    }
    public double getX(Hand hand) {return super.getX (hand); }
    public double getY(Hand hand) {return super.getY (hand); }

    public void rumbleTime(long durationMilliseconds) {

      long newTimestamp = durationMilliseconds + System.currentTimeMillis();

      m_timeStampTimeout = Math.max(m_timeStampTimeout, newTimestamp);

      setRumble(RumbleType.kLeftRumble, 1.0);
      setRumble(RumbleType.kRightRumble, 1.0);

    }

    public void periodic() {
      if (m_timeStampTimeout != 0 && System.currentTimeMillis() > m_timeStampTimeout) {
        setRumble(RumbleType.kLeftRumble, 0);
        setRumble(RumbleType.kRightRumble, 0);
        m_timeStampTimeout = 0;
      }
    }
  }

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
      camera.setResolution(640, 480);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // Grab the hatch when the 'A' button is pressed.
    new JoystickButton(m_driverController, Button.kA.value).whileHeld(new ExampleCommand(m_exampleSubsystem));
    new JoystickButton(m_operatorController, Button.kY.value)
        .whileHeld(new RotateOrJogControlPanelCommand(m_wheelOfFortuneColorSpinny, m_operatorController));
    new JoystickButton(m_operatorController, Button.kB.value)
        .whileHeld(new PositionControlPanelCommand(m_wheelOfFortuneColorSpinny, m_colorsensor, m_operatorController));
         // Assign default commands
    m_drive.setDefaultCommand(new ArcadeDrive(m_drive, m_driverController));
  }

  
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }

  public void periodic() {
    m_driverController.periodic();
    m_operatorController.periodic();
  }
}
