# Kite OpenShift Example

This module provides an example of logging application events from a webapp to a Kite 
dataset via Flume (using log4j as the logging API) then querying the dataset using Hive 
JDBC.

To facilitate rapid development, the webapp runs a Hadoop minicluster that provides all
the necessary components.

The example runs in a local Tomcat instance, as well as on OpenShift.

## Running Locally

To build the project, type

```bash
mvn package
```

This creates a WAR file, which can be used in a Java EE 6 servlet container. For
this example we'll start an embedded Tomcat instance using Maven:

```bash
mvn tomcat7:run
```


Navigate to [http://localhost:8080/openshift-demo](http://localhost:8080/openshift-demo),
which presents you with a very simple web page for sending messages.

The message events are sent to the Flume agent
over a socket, and the agent writes the events to the Kite dataset sink.

Send a few messages using the web form. Then wait 30 seconds for the sink to roll the 
file so that the data is visible. This page (which uses a Hive JDBC connection to run a
 query), shows all the events in the dataset:
[http://localhost:8080/openshift-demo/all_events.jsp](http://localhost:8080/openshift-demo/all_events.jsp).

## Running on OpenShift

TODO
