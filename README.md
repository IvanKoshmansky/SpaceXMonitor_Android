# SpaceXMonitor

## What is this project about? ##

This app provides up-to-date information about SpaceX launches starting from 2021.
The main screen displays a list of missions with brief information about each mission: mission name, success, launch date, and more.
Detailed information about each mission is also available: the logo, the number of first stage reuses, and information about the crew.

## Why do I need this project? ##

This project gave me the opportunity to improve my android development skills. Namely:

- Kotlin skills when working with Lists and Arrays
- Understanding the concept of dependency injection, and applying it to an android application using the Hilt library
- Improved layout implementation skills according to the Material Design guidelines
- Improved understanding of the principles of the HTTP protocol, and how to work with it using Retrofit2
- Made a migration from the Paging2 to the Paging3 library
- Applied the Coil library for loading images instead of the Glide

Also, this project keeps everyone up to date with current SpaceX events! :grinning:

## Application components ##
The application consists of the following components:
1. The Web service: package `webservice`  
The web service performs the interaction of the rest application components with the server.
The web service works in read-only mode, it cannot change the data stored on the server.
The web service provides access to its functions through the `IRemoteDataSource` interface.  
This interface contains methods that are used for:
    - requesting general information about all launches: `getLaunches()`,
    - requesting detailed information about the launch with the required Id: `getOneLaunchDetail()`,
    - requesting information about the crew that took part in the selected launch: `getCrewMembers()`.
    
    The concrete implementation of the web service interface is the `RemoteDataSource` class.
The `RemoteDataSource` class has a dependency on the **Retrofit** service.
This service is declared as `SpaceXApiService`.
`SpaceXApiService` deals directly with the HTTP protocol using `@POST` and `@GET` requests.
This web service organization scheme allows we to separate the interface from the implementation,
and gives the possibility to replace the API for working with the server without changes in the rest of the application.  

2. The Local database: package `localdb`
  The local DB is needed to save a local cache of missions downloaded from the server.
The database is based on the Room library.
Access to the database API is provided through the DatabaseDao interface.
In this interface, methods are declared that serve to:
    - reading information about all saved launches from the launches_table: getLaunches(),
    - inserting new rows into launches_table: insertLaunches(),
    - clearing the entire table: clearTable().
3. Repository: class Repository
The repository is the universal data store for the entire application.
It separates the data layer in the application from the layer of business logic and UI components.
The repository is also a data mediator and it determines the need
in requests to the server through the IRemoteDataSource interface
or reading data from a local database via the LocalDatabase interface.
Both of these dependencies are committed to the repository using the Hilt library.
4. ViewModel layer: class MainViewModel
The MainViewModel provides the link between the UI controllers and the data layer (repository).
This class implements features that provide LiveData updates for:
- the list adapter: overviewLiveData,
- the screen with the launch details: detailLiveData.
Also it provides methods that are needed for user feedback:
- reaction to taping on an element of the list: onItemClicked(),
- reaction to taping the floating button "to the top of the list": onClickFAB().
In this application, a single MainViewModel maintains the state of the UI controllers for the two screens.
5. UI controllers, which are contained in two fragments: OverviewFragment and DetailFragment
Provide direct user interaction: SpaceX mission list visualization,
transitions between screens, showing detailed information about each mission.
To display a list of missions with pagination, the Paging3 library is used.
The Navigation library is used to navigate between screens.
Coil library is used to load images.
6. Android Application object: SpaceXMonitorApplication
Is the root component of the application and is used to pass dependencies to other
components with Hilt.

## Screenshots ##

![main-screen](https://user-images.githubusercontent.com/102755986/196852367-d3fbdb0d-4e9e-471f-a921-e354fc5d155b.png)
![detail-screen](https://user-images.githubusercontent.com/102755986/196852359-ba1f3a4c-e3f1-4508-b0c3-736566ee2388.png)

### Download the APK ###
