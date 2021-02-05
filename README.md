# provision-car-track
Sample Java Rest API

Design Doc

I will model the domain from a truck  freight company perspective, Not from a car repair shop 
Therefore, the maintenance tasks and readings will be modeled for analitical purposes, not a sales purpose


Domain: CAR MAINTENANCE 

Context: Tracking of mechanical readings and maintenance tasks

Main Actions: 
 - Register READINGS of Odometer, Tire wear, battery capacity, brake fluid level, oil level.
 - Register MAINTENANCE TASKS like oil change, tire rotation, Brake Jobs, Filter change.
 - Manage Base Entities

Entities
 - Cars
 - Readings
 - Maintenance Tasks
 - Users of the system

We can divide this system in 2 main areas
 - Record Entities: These represent the general entity register, low volume tables
    
    Engine Type: Name 
    
    Make: Name
    
    Model: Make id, Name, Year
    
    
    Vehicle: Model id, Driver id
    
    Reading Type: Name (Odometer, Battery strength, tire thread, oil level, disk wear, filter wear)
    
    Task Type: Name, Allowed Engine Type ids []
 
 
 - Action Entities: These will have a high volume as they represent every event that happened to a car 
    
    Task Execution: Task Type id, executed_at date, user_executed_by id
    
    Reading Execution: Car id, Reading Type id, Odometer value, reading value 
    
    Event: Event Type ex: Task_created, Task_updated, Task_removed, Reading_created, Reading_updated, Reading_removed, User id, timestamp, payload json
    
#### Notes on future evolution of the system

   The functional separation of concerns in this system is very simple, we are simply tracking events of the past, in this case it is ok to use a CRUD approach for all features of the system. 
    Moreover, if use cases that involve changing state over time, long running workflows with steps (ie: hospital patient record, legal contracts) or even growing different contexts/models over the same entities, I would try to move away from  CRUD and introduce an Event Sourcing architecture. 
    The main reasons are that on a classic CRUD there is inevitable loss of audit/action data, and as the system grows it is very difficult to separate concerns because columns start being added to tables for disparate reasons that only make sense in the context of the domain that uses them. 
    Also the system starts to suffer from very long methods with lots of IFs/ELSEs that makes it extremelly difficult to evolve the feature set without breaking or being forced to change what is already working.
    Event Sourcing is harder to implement and scale, but the opportunity to stay away from a very difficult to evolve system has a greater benefit that CRUD cannot deliver.



#### Moving Parts
- To run the tests: `mvn clean test`
- To run the app: `mvn spring-boot:run`
- Go to `http://localhost:8080/swagger-ui.html` to see docs in swagger page
