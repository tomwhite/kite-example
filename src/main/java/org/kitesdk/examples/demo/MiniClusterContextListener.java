package org.kitesdk.examples.demo;

import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.kitesdk.data.CompressionType;
import org.kitesdk.data.DatasetDescriptor;
import org.kitesdk.data.Datasets;
import org.kitesdk.minicluster.FlumeService;
import org.kitesdk.minicluster.HdfsService;
import org.kitesdk.minicluster.HiveService;
import org.kitesdk.minicluster.MiniCluster;

public class MiniClusterContextListener implements ServletContextListener {

  private final Logger LOG = Logger.getLogger(MiniClusterContextListener.class);

  private MiniCluster miniCluster;

  @Override
  public void contextInitialized(ServletContextEvent event) {
    File tempDir = (File) event.getServletContext().getAttribute(ServletContext.TEMPDIR);
    File workDir = new File(tempDir, "kite-minicluster-webapp");
    LOG.info("Work dir for kite-minicluster: " + workDir);
    miniCluster = new MiniCluster.Builder().workDir(workDir.getAbsolutePath())
        .bindIP("127.0.0.1")
        .addService(HdfsService.class)
        .addService(HiveService.class)
        .addService(FlumeService.class)
        .flumeConfiguration("flume.properties").flumeAgentName("tier1")
        .clean(true).build();
    try {
      miniCluster.start();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    try {
      DatasetDescriptor descriptor = new DatasetDescriptor.Builder()
          .schema(Resources.getResource("standard_event.avsc").openStream())
          .compressionType(CompressionType.Deflate) // problem loading snappy in webapps
          .build();
      Datasets.create("dataset:hive:default/events", descriptor);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // We need to explicity reactive the logger, since it will have failed since the
    // Flume agent wasn't listening at startup
    AppenderSkeleton flumeAppender = (AppenderSkeleton) Logger.getLogger(LoggingServlet.class).getAppender("flume");
    flumeAppender.activateOptions();
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    if (miniCluster != null) {
      try {
        miniCluster.stop();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

}