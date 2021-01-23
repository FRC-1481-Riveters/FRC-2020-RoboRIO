/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ArcadeDrive;
import frc.robot.commands.AutonRobotDriveDistance;
import frc.robot.commands.AutonShoot3StackedPowerCellsAndDriveOffLine;
import frc.robot.commands.AutonSuperTrenchSequence;
import frc.robot.commands.CycleCameraFeedCommand;
import frc.robot.commands.ElevatorSolenoidPullIn;
import frc.robot.commands.ElevatorSolenoidPullOut;
import frc.robot.commands.GoosehookDisengage;
import frc.robot.commands.GoosehookEngage;
import frc.robot.commands.IndexerCarryUpCommand;
import frc.robot.commands.IndexerJoystickCommand;
import frc.robot.commands.IndexerSpitOutCommand;
import frc.robot.commands.IndexerStackOnePowerCell;
import frc.robot.commands.IntakeDropOffCommand;
import frc.robot.commands.IntakeJoystickCommand;
import frc.robot.commands.IntakePositionPowerCellCommand;
import frc.robot.commands.IntakeRunForABit;
import frc.robot.commands.KickerAdvanceCommand;
import frc.robot.commands.KickerCaptureCommand;
import frc.robot.commands.PositionControlPanelCommand;
import frc.robot.commands.RotateOrJogControlPanelCommand;
import frc.robot.commands.ShooterYeetCommand;
import frc.robot.commands.lowerElevator;
import frc.robot.commands.raiseElevator;
import frc.robot.subsystems.Kicker;
import frc.robot.subsystems.PowerCellArm;
import frc.robot.subsystems.AutoAssistSubsystem;
import frc.robot.subsystems.CameraSubsystem;
import frc.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.colorsensor;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.UltrasonicSensor;
import frc.robot.subsystems.wheelOfFortuneColorSpinny;
import irsensor.IRSensor;
import frc.robot.Constants;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Goosehook;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.CyanSus;

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
        private final PowerCellArm m_powerCellArm = new PowerCellArm();
        private final AutoAssistSubsystem m_autoAssist = new AutoAssistSubsystem();
        private final UltrasonicSensor m_ultrasonicSensor = new UltrasonicSensor();
        //public final CyanSus m_cyanSus = new CyanSus();

        RumbleTimerJoystick m_driverController = new RumbleTimerJoystick(Constants.driverController);
        RumbleTimerJoystick m_operatorController = new RumbleTimerJoystick(Constants.operatorController);

        SendableChooser<Command> m_chooser = new SendableChooser<>();

        /**
         * The container for the robot. Contains subsystems, OI devices, and commands.
         */

        public RobotContainer() {
                // Configure the button bindings
                configureButtonBindings();

                SmartDashboard.putData(new CycleCameraFeedCommand(m_cameraSubsystem));
                SmartDashboard.putData(new IndexerCarryUpCommand(m_indexer).withTimeout(5.0));
                SmartDashboard.putData(m_intakePowerCellPositionSensor);
                SmartDashboard.putData(new AutonRobotDriveDistance(m_drive, -4.0));

                /*
                 * Add more Autonomous commands to the Dashboard's "Auto Mode" drop down
                 * chooser, m_chooser with the addOption() method, e.g.:
                 *
                 * m_chooser.addOption("Title on dashboard",new CmdClassName(subs,args...));
                 * 
                 * 
                 * The command that that is selected on the dashboard will be reported by
                 * m_chooser.getSelected(), and returned by
                 * RobotContainer.getAutonomousCommand() and executed (scheduled) during
                 * Autonomous in Robot.autonomousInit().
                 */

                //m_chooser.setDefaultOption("Shoot 3, back robot off line", new AutonShoot3StackedPowerCellsAndDriveOffLine(m_shooter, m_indexer, m_kicker, m_drive));
                m_chooser.setDefaultOption("Shoot 3, back robot off line", new AutonSuperTrenchSequence(m_shooter, m_indexer, m_kicker, m_drive, m_intake));
                m_chooser.addOption("-= Do nothing =-", new SequentialCommandGroup(new PrintCommand("Do nothing selected for auton."), new WaitCommand(5.0)));

                SmartDashboard.putData("Auto mode", m_chooser);

        }

        /**
         * Use this method to define your button->command mappings. Buttons can be
         * created by instantiating a {@link GenericHID} or one of its subclasses
         * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
         * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
         */
        private void configureButtonBindings() {

                /* Wheel of Fortune */
                new JoystickButton(m_operatorController, Button.kX.value).whileHeld(new RotateOrJogControlPanelCommand(m_wheelOfFortuneColorSpinny, m_operatorController));
                new JoystickButton(m_operatorController, Button.kB.value).whileHeld(new PositionControlPanelCommand(m_wheelOfFortuneColorSpinny, m_colorsensor, m_operatorController));

                /* Shoot Power Cells */
                new JoystickButton(m_driverController, Button.kB.value).whileHeld(// initiation linev
                        new SequentialCommandGroup( //
                                new ShooterYeetCommand(m_shooter, Constants.shooterYeetSpeedInitiation), //
                                new ParallelCommandGroup( //
                                        new IndexerCarryUpCommand(m_indexer), //
                                        new KickerAdvanceCommand(m_kicker, m_shooter) //
                                ) //
                        ) //
                );

                new JoystickButton(m_driverController, Button.kB.value).whenReleased(new ShooterYeetCommand(m_shooter, 0.0));

                new JoystickButton(m_driverController, Button.kY.value).whileHeld( //
                        new SequentialCommandGroup( //
                                new ShooterYeetCommand(m_shooter, Constants.shooterYeetSpeedWall), //
                                new ParallelCommandGroup( //
                                        new IndexerCarryUpCommand(m_indexer), //
                                        new KickerAdvanceCommand(m_kicker, m_shooter) //
                                ) //
                        ) //
                );

                new JoystickButton(m_driverController, Button.kY.value).whenReleased(new ShooterYeetCommand(m_shooter, 0.0));

                /* Load Power Cells */
                new JoystickButton(m_operatorController, Button.kY.value).whileHeld(new IndexerCarryUpCommand(m_indexer));
                new JoystickButton(m_operatorController, Button.kA.value).whenReleased(new IndexerSpitOutCommand(m_indexer));

                new JoystickButton(m_operatorController, Button.kBumperLeft.value).whileHeld( //
                        new ParallelDeadlineGroup( // Run until the Intake and Indexer are done and end even if the
                                                   // kicker hasn't yet detected a power cell
                                new SequentialCommandGroup( //
                                        new IntakePositionPowerCellCommand(m_intake), // Pull in a PowerCell with the intake
                                        new ParallelCommandGroup( //
                                                new IntakeRunForABit(m_intake, .75), // Pin the Power Cell against the indexer
                                                new IndexerStackOnePowerCell(m_indexer) //
                                                        .withTimeout(Constants.indexerStack1PwrCellTimeout) // Lift the Power Cell
                                                                                                            // to its first stack
                                                                                                            // position
                                        ) //
                                ), //
                                new KickerCaptureCommand(m_kicker) // Load a single Power Cell into the kicker, when it gets there
                        ) //
                );

                new JoystickButton(m_operatorController, Button.kBumperRight.value).whileHeld( //
                        new ParallelCommandGroup( //
                                new IntakeDropOffCommand(m_intake), //
                                new IndexerSpitOutCommand(m_indexer) //
                        ) //
                );

                /* Elevator and goosehook */
                new JoystickButton(m_driverController, Button.kX.value).whileHeld(new raiseElevator(m_elevator));
                new JoystickButton(m_driverController, Button.kA.value).whileHeld(new lowerElevator(m_elevator));
                new JoystickButton(m_driverController, Button.kBumperLeft.value).whileHeld(new GoosehookEngage(m_goosehook));
                new JoystickButton(m_driverController, Button.kBumperRight.value).whileHeld(new GoosehookDisengage(m_goosehook));
                new JoystickButton(m_operatorController, Button.kStart.value).whenPressed(new ElevatorSolenoidPullIn(m_elevator));
                new JoystickButton(m_operatorController, Button.kBack.value).whenPressed(new ElevatorSolenoidPullOut(m_elevator));

                m_drive.setDefaultCommand(new ArcadeDrive(m_drive, m_driverController));
                m_intake.setDefaultCommand(new IntakeJoystickCommand(m_intake, m_operatorController));
                m_indexer.setDefaultCommand(new IndexerJoystickCommand(m_indexer, m_operatorController));
        }

        /**
         * Use this to pass the autonomous command to the main {@link Robot} class.
         *
         * @return the command to run in autonomous
         */
        public Command getAutonomousCommand() {

                return m_chooser.getSelected();
        }
}
