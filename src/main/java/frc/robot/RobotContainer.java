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
import frc.robot.commands.CycleCameraFeedCommand;
import frc.robot.commands.GoosehookDisengage;
import frc.robot.commands.GoosehookEngage;
import frc.robot.commands.IndexerCarryUpCommand;
import frc.robot.commands.IndexerSpitOutCommand;
import frc.robot.commands.IntakeDropOffCommand;
import frc.robot.commands.IntakePickupCommand;
import frc.robot.commands.IntakeRunForABit;
import frc.robot.commands.KickerAdvanceCommand;
import frc.robot.commands.KickerCaptureCommand;
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
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.colorsensor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.wheelOfFortuneColorSpinny;
import irsensor.IRSensor;
import frc.robot.Constants;
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

  private final Shooter m_shooter = new Shooter();
  private final Kicker m_kicker = new Kicker();
  private final DriveTrain m_drive = new DriveTrain();

  private final CameraSubsystem m_cameraSubsystem = new CameraSubsystem(m_drive);
  private final Elevator m_elevator = new Elevator();
  private final Goosehook m_goosehook = new Goosehook();

  private final IRSensor m_intakePowerCellPositionSensor = new IRSensor(IRSensor.SensorType.GP2Y0A41SK0F, 0);
  private final Indexer m_indexer = new Indexer(m_intakePowerCellPositionSensor);
  private final Intake m_intake = new Intake(m_intakePowerCellPositionSensor);

  @SuppressWarnings("unused")
  private final PowerCellSlurpMulticommand m_powerCellSlurp = new PowerCellSlurpMulticommand();
  @SuppressWarnings("unused")
  private final PowerCellLoosenerMulticommand m_powerCellLoosener = new PowerCellLoosenerMulticommand();
  RumbleTimerJoystick m_driverController = new RumbleTimerJoystick(Constants.driverController);
  RumbleTimerJoystick m_operatorController = new RumbleTimerJoystick(Constants.operatorController);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */

  public RobotContainer() {
    // Configure the button bindings
    configureButtonBindings();

    SmartDashboard.putData(new CycleCameraFeedCommand(m_cameraSubsystem));
    SmartDashboard.putData(new IndexerCarryUpCommand(m_indexer).withTimeout(5.0));
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
    new JoystickButton(m_driverController, Button.kB.value) // initiation linev
        .whileHeld(new PowerCellYeeterInitiationMulticommand());
    new JoystickButton(m_driverController, Button.kB.value).whenReleased(new ShooterYeetCommand(m_shooter, 0.0));
    new JoystickButton(m_driverController, Button.kY.value).whileHeld(new PowerCellYeeterWallMulticommand());
    new JoystickButton(m_driverController, Button.kY.value).whenReleased(new ShooterYeetCommand(m_shooter, 0.0));
    new JoystickButton(m_operatorController, Button.kY.value).whileHeld(new IndexerCarryUpCommand(m_indexer));
    new JoystickButton(m_operatorController, Button.kA.value).whenReleased(new IndexerSpitOutCommand(m_indexer));
    // new Joystick
    // new JoystickButton(m_operatorController, Button.kA.value)
    // .whileHeld(new ShooterYeetCommand(m_shooter,
    // Constants.shooterYeetSpeedWall));
    // new JoystickButton(m_operatorController, Button.kA.value)
    // .whenReleased(new ShooterYeetCommand(m_shooter, 0.0));
    // Assign default commands
    new JoystickButton(m_driverController, Button.kX.value).whileHeld(new raiseElevator(m_elevator));
    new JoystickButton(m_driverController, Button.kA.value).whileHeld(new lowerElevator(m_elevator));
    new JoystickButton(m_driverController, Button.kBumperLeft.value).whileHeld(new GoosehookEngage(m_goosehook));
    new JoystickButton(m_driverController, Button.kBumperRight.value).whileHeld(new GoosehookDisengage(m_goosehook));
    new JoystickButton(m_operatorController, Button.kBumperLeft.value).whileHeld(new PowerCellAdvanceMulticommand());
    new JoystickButton(m_operatorController, Button.kBumperRight.value).whileHeld(new PowerCellLoosenerMulticommand());

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

  public class PowerCellYeeterInitiationMulticommand extends SequentialCommandGroup {
    /**
     * Creates a new PowerCellYeeterMulticommand.
     */
    public PowerCellYeeterInitiationMulticommand() {
      // Add your commands in the super() call, e.g.
      // super(new FooCommand(), new BarCommand());
      super(new ShooterYeetCommand(m_shooter, Constants.shooterYeetSpeedInitiation),
          new PowerCellAdvanceMulticommand());
      // intake, indexer, kicker, shooter
      // initiates shooter, moves everything else when shooter is at intended speed
    }
  }

  public class PowerCellYeeterWallMulticommand extends SequentialCommandGroup {
    /**
     * Creates a new PowerCellYeeterMulticommand.
     */
    public PowerCellYeeterWallMulticommand() {
      // Add your commands in the super() call, e.g.
      // super(new FooCommand(), new BarCommand());
      super(new ShooterYeetCommand(m_shooter, Constants.shooterYeetSpeedWall), new PowerCellAdvanceMulticommand());
      // intake, indexer, kicker, shooter
      // initiates shooter, moves everything else when shooter is at intended speed
    }
  }

  public class PowerCellSlurpMulticommand extends SequentialCommandGroup {
    public PowerCellSlurpMulticommand() {
      super(new IntakePickupCommand(m_intake), new PowerCellIndexMulticommand());
    }
    // intake and indexer: begins after power cell detected, carried thru indexer
  }

  public class PowerCellAdvanceMulticommand extends ParallelCommandGroup {
    public PowerCellAdvanceMulticommand() {
      super(new PowerCellSlurpMulticommand(), new KickerCaptureCommand(m_kicker));
    }
    // intake, indexer, and kicker: used in PowerCellYeeterMulticommand to advance
    // balls to shooter
  }

  public class PowerCellLoosenerMulticommand extends ParallelCommandGroup {
    public PowerCellLoosenerMulticommand() {
      super(new IntakeDropOffCommand(m_intake), new IndexerSpitOutCommand(m_indexer));
    }
    // intake and indexer: used to prevent jams in indexer and intake(but especially
    // indexer)
  }

  public class PowerCellIndexMulticommand extends ParallelCommandGroup {
    public PowerCellIndexMulticommand() {
      super(new IntakeRunForABit(m_intake, .75), new IndexerCarryUpCommand(m_indexer));
    }
  }
}
