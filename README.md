# Voting System

A desktop voting application built with **Java** and **JavaFX**. The project provides a graphical interface for user authentication, department-based candidate voting, live vote statistics, and winner presentation.

> **Project type:** JavaFX desktop application  
> **IDE/project format:** Eclipse  
> **Java target configured in the original Eclipse project:** Java 23

## Features

- User authentication using a predefined user ID.
- Department-based voting (SE, EE, and ME in the sample data).
- Candidate profiles with names, departments, descriptions, images, and external detail links.
- One-vote-per-user enforcement.
- Vote confirmation before a vote is recorded.
- Persistent voting state using Java object serialization.
- Automatic reset of voting state when the department voting cycle is completed.
- Statistics dashboard with charts for vote results.
- Winner page showing vote count and percentage.
- Save/print functionality for the winner page as an image.
- Observer-based UI updates when candidate vote counts change.

## Technologies

- **Java**
- **JavaFX**
- **Eclipse IDE project configuration**
- Java Serialization for local persistence
- JavaFX Charts for result visualization
- JavaFX CSS for interface styling

## Project Structure

```text
VotingSystem/
├── src/
│   └── application/
│       ├── Main.java
│       ├── WelcomePage.java
│       ├── HomePage.java
│       ├── DisplayPage.java
│       ├── WinnerPage.java
│       ├── VotingManager.java
│       ├── Database.java
│       ├── Candidate.java
│       ├── CandidateInformation.java
│       ├── User.java
│       ├── Observer.java
│       └── application.css
├── .classpath
├── .project
├── .settings/
├── build.fxbuild
├── .gitignore
└── README.md
```

## Application Flow

```text
Start Application
       │
       ▼
   Login Page
       │
       ▼
 Authenticate User ID
       │
       ▼
 Candidate Voting Page
       │
       ├── View Candidate Details
       │
       └── Cast Vote
              │
              ▼
       Save Voting State
              │
              ▼
       Statistics / Results
              │
              ▼
          Winner Page
```

## Main Components

### `Main`
Application entry point. It launches the `WelcomePage`.

### `WelcomePage`
Provides the initial login interface and authenticates users through the `VotingManager`.

### `HomePage`
Displays candidates for the authenticated user's department and provides voting and candidate-detail actions.

### `VotingManager`
Acts as the central voting controller. It manages users, candidates, authentication, vote recording, department progression, state persistence, and resets.

### `Database`
Contains the sample users and candidate information used by the application.

### `Candidate` / `CandidateInformation`
Defines and implements the candidate model, including vote management and observer notifications.

### `Observer`
Provides the observer interface used to notify interested UI components when candidate vote counts change.

### `DisplayPage`
Presents voting statistics and charts for the selected department.

### `WinnerPage`
Displays the winner or tied candidates together with vote counts and percentages and provides a save/print option.

## Design Concepts

The project demonstrates several object-oriented software engineering concepts:

- **Singleton pattern:** `VotingManager` provides a single central voting manager instance.
- **Observer pattern:** `CandidateInformation` notifies registered observers after vote changes.
- **Interface-based design:** `Candidate` defines candidate operations independently from its implementation.
- **Encapsulation:** User, candidate, and voting-management responsibilities are separated into dedicated classes.
- **Serialization:** User and candidate state can be stored locally and restored when the application starts.

## Requirements

To run the project, you need:

1. A Java Development Kit compatible with the project's configured Java version.
2. JavaFX SDK and JavaFX modules required by the application.
3. Eclipse IDE with Java and JavaFX support, or another Java IDE configured with JavaFX.

The original Eclipse project is configured for **JavaSE-23** and a JavaFX user library.

## Running in Eclipse

1. Clone or download this repository.
2. Open Eclipse.
3. Choose **File → Import → Existing Projects into Workspace**.
4. Select the cloned `VotingSystem` directory.
5. Make sure the configured JDK matches the project configuration.
6. Configure the JavaFX SDK/user library if Eclipse reports missing JavaFX modules.
7. Run:

```text
src/application/Main.java
```

The application starts from the login page.

## Sample User IDs

The included sample database contains users such as:

| ID | Name | Department |
|---|---|---|
| 1001 | Shiban | SE |
| 1002 | Khaled | SE |
| 1003 | Ziad | SE |
| 1004 | Ahmad | SE |
| 1005 | Ali | SE |
| 1006 | Eve | EE |
| 1007 | Frank | EE |
| 1008 | Grace | EE |
| 1009 | Hannah | ME |
| 1010 | Ian | ME |
| 1011 | Jack | ME |

These are demonstration records defined directly in `Database.java`.

## Data Persistence

The application uses `voting_state.ser` as a local runtime file for saving voting state. This file is intentionally excluded from Git because it is generated during execution and may contain changing application state.

If you want to start from a clean voting state, delete the local `voting_state.ser` file before running the application again.

## Notes Before Public Deployment

This repository represents an academic/demo desktop voting application. It is **not intended to be used as a production election system**.

For a production-grade voting platform, additional security and reliability controls would be required, including secure authentication, encrypted data storage, authorization, audit logging, tamper resistance, database-backed persistence, secure network communication, accessibility testing, and formal security verification.

## Future Improvements

- Replace hard-coded users and candidates with a database.
- Add secure authentication and role-based authorization.
- Add automated unit and integration tests.
- Improve error handling and validation.
- Package the JavaFX application for easier installation.
- Add local image assets instead of relying on external image URLs.
- Add an administrator interface for managing users and candidates.
- Improve accessibility and responsive layout behavior.

## Author

Developed by shiban etoum shiban.etoum.2003@gmail.com as a Java/JavaFX software engineering project.
