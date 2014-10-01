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
git clone https://github.com/tomwhite/kite-example.git
cd kite-example
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

Before going any further you need to open an [OpenShift account](https://www.openshift.com/)
and install the [client tools](https://developers.openshift.com/en/getting-started-client-tools.html).

**Note**: You will need to have an account that allows you to run large gear sizes (this
is not possible with the free account).

On your local machine run the following commands to create a new application running on
OpenShift online:

```bash
rhc app create sessionization jbossews-2.0 -g large
```

This will create a plain webapp (its URL will be printed to the console).
To replace it with the Kite webapp, type the following:

```bash
cd sessionization
git remote add upstream -m master git://github.com/tomwhite/kite-example.git
git pull -s recursive -X theirs upstream master
git push origin master
```

The push will do a full build and deploy, which will take a few minutes. When it’s done,
the home page will be available at http://sessionization-[your-domain].rhcloud.com/,
the URL from the `rhc app create` command (you can also access your OpenShift applications
from https://openshift.redhat.com/app/console/applications).

**Note**: You can see the webapp container logs if you log into the OpenShift machine
(details on the OpenShift application page, under 'Remote Access')
and type:

```bash
tail -f app-root/logs/jbossews.log
```

The page at http://sessionization-<your-domain>.rhcloud.com/
presents you with a very simple web page for sending messages.

The message events are sent to the Flume agent
over a socket, and the agent writes the events to the Kite dataset sink.

Send a few messages using the web form. Then wait 30 seconds for the sink to roll the 
file so that the data is visible. This page (which uses a Hive JDBC connection to run a
 query), shows all the events in the dataset:
 http://sessionization-[your-domain].rhcloud.com/all_events.jsp
 
**Note**: By default the application only has 1GB of disk space, 
which is not enough if you leave the application running for long. You can increase it 
via the web console, or by running:

```bash
rhc cartridge storage jbossews-2.0 -a sessionization --set 4
```

Stop the application with

```bash
rhc app stop sessionization
```

When you've finished with the application, delete it permanently with

```bash
rhc app delete sessionization
```