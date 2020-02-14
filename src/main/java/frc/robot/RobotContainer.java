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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.BreakInGearboxCommand;
import frc.robot.commands.GoosehookDisengage;
import frc.robot.commands.GoosehookEngage;
import frc.robot.commands.IndexerCarryUpCommand;
import frc.robot.commands.IndexerSpitOutCommand;
import frc.robot.commands.IntakeDropOffCommand;
import frc.robot.commands.IntakePickupCommand;
import frc.robot.commands.KickerAdvanceCommand;
import frc.robot.commands.PositionControlPanelCommand;
import frc.robot.commands.RotateOrJogControlPanelCommand;
import frc.robot.commands.ShooterYeetCommand;
import frc.robot.commands.lowerElevator;
import frc.robot.commands.raiseElevator;
import frc.robot.subsystems.Kicker;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.colorsensor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.wheelOfFortuneColorSpinny;
import frc.robot.Constants;
import edu.wpi.cscore.UsbCamera;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Goosehook;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;


/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final colorsensor m_colorsensor = new colorsensor();
  private final wheelOfFortuneColorSpinny m_wheelOfFortuneColorSpinny = new wheelOfFortuneColorSpinny();

  @SuppressWarnings("unused")
  private final CameraSubsystem m_cameraSubsystem = new CameraSubsystem();
  private final Shooter m_shooter = new Shooter();
  private final Kicker m_kicker = new Kicker();
  private final DriveTrain m_drive = new DriveTrain();
  private final Elevator m_elevator = new Elevator();
  private final Goosehook m_goosehook = new Goosehook();
  private final Indexer m_indexer = new Indexer();
  private final Intake m_intake = new Intake();

  @SuppressWarnings("unused")
  private final PowerCellYeeterMulticommand m_powerCellYeeter = new PowerCellYeeterMulticommand();
  private final PowerCellSlurpMulticommand m_powerCellSlurp = new PowerCellSlurpMulticommand();
  private final PowerCellLoosenerMulticommand m_powerCellLoosener = new PowerCellLoosenerMulticommand();
  RumbleTimerJoystick m_driverController = new RumbleTimerJoystick(Constants.driverController);
  RumbleTimerJoystick m_operatorController = new RumbleTimerJoystick(Constants.operatorController);

  private final BreakInGearboxCommand m_breakInGearboxCommand = new BreakInGearboxCommand(m_drive);
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();
    UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
      camera.setResolution(640, 480);


      SmartDashboard.putData(m_breakInGearboxCommand);
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    new JoystickButton(m_operatorController, Button.kX.value)
        .whileHeld(new RotateOrJogControlPanelCommand(m_wheelOfFortuneColorSpinny, m_operatorController));
    new JoystickButton(m_operatorController, Button.kB.value)
        .whileHeld(new PositionControlPanelCommand(m_wheelOfFortuneColorSpinny, m_colorsensor, m_operatorController));
    new JoystickButton(m_driverController, Button.kB.value)
        .whileHeld(new PowerCellYeeterMulticommand());
    new JoystickButton(m_driverController, Button.kB.value)
        .whenReleased(new ShooterYeetCommand(m_shooter, 0.0));
    new JoystickButton(m_operatorController, Button.kY.value)
        .whileHeld(new ShooterYeetCommand(m_shooter, Constants.shooterYeetSpeedInitiation));
    new JoystickButton(m_operatorController, Button.kY.value)
        .whenReleased(new ShooterYeetCommand(m_shooter, 0.0));
    new JoystickButton(m_operatorController, Button.kA.value)
        .whileHeld(new ShooterYeetCommand(m_shooter, Constants.shooterYeetSpeedWall));
    new JoystickButton(m_operatorController, Button.kA.value)
        .whenReleased(new ShooterYeetCommand(m_shooter, 0.0));
         // Assign default commands
    new JoystickButton(m_driverController, Button.kX.value)
        .whileHeld(new raiseElevator(m_elevator));
    new JoystickButton(m_driverController, Button.kA.value)
        .whileHeld(new lowerElevator(m_elevator));
    new  JoystickButton(m_driverController, Button.kBumperLeft.value)
        .whileHeld(new GoosehookEngage(m_goosehook));
    new  JoystickButton(m_driverController, Button.kBumperRight.value)
        .whileHeld(new GoosehookDisengage(m_goosehook));
    new JoystickButton (m_operatorController, Button.kBumperLeft.value)
        .whileHeld(new PowerCellSlurpMulticommand());
    new JoystickButton (m_operatorController, Button.kBumperRight.value)
        .whileHeld(new PowerCellLoosenerMulticommand());

    m_drive.setDefaultCommand(new ArcadeDrive(m_drive, m_driverController));
  }

  
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {

    return null;
  }

  public class PowerCellYeeterMulticommand extends SequentialCommandGroup {
    /**
     * Creates a new PowerCellYeeterMulticommand.
     */
    public PowerCellYeeterMulticommand() {
      // Add your commands in the super() call, e.g.
      // super(new FooCommand(), new BarCommand());
      super(new ShooterYeetCommand(m_shooter, Constants.shooterIntendedSpeed), new KickerAdvanceCommand(m_kicker, m_shooter));
    }
  }
  public class PowerCellSlurpMulticommand extends ParallelCommandGroup{
    public PowerCellSlurpMulticommand() {
      super(new IntakePickupCommand(m_intake), new IndexerCarryUpCommand(m_indexer));
    }
  }
  public class PowerCellLoosenerMulticommand extends ParallelCommandGroup{
    public PowerCellLoosenerMulticommand() {
      super(new IntakeDropOffCommand(m_intake), new IndexerSpitOutCommand(m_indexer));
    }
  }
}
