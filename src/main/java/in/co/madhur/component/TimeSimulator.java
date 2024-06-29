/* (C) Games24x7 */
package in.co.madhur.component;

import io.github.faketime.FakeTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Slf4j
@Component
@ConditionalOnProperty(prefix = "enable", name = "time-manipulation.apis")
public class TimeSimulator {

  private volatile boolean isAdvancementEnabled = false;

  private Thread timeSimulationThread;

  static final long NANOS_IN_A_MILLI_SECOND = 1_000_000L;

  @Value("${time-manipulation.time-simulation.update-time-duration-in-millis:10}")
  private long updateTimeDurationInMillis;

  public void startTimeSimulation() {
    stopTimeSimulation();
    log.info("Starting time simulation");
    isAdvancementEnabled = true;

    timeSimulationThread =
        new Thread(
            () -> {
              long offset;

              long start = System.nanoTime();
              while (isAdvancementEnabled) {
                try {
                  Thread.sleep(updateTimeDurationInMillis);
                } catch (InterruptedException e) {
                  log.error("Error in time simulation", e);
                  throw new RuntimeException(e);
                }
                offset = (System.nanoTime() - start) / NANOS_IN_A_MILLI_SECOND;
                FakeTime.offsetStoppedBy(offset);
              }
              log.info("Time simulation stopped");
            });

    timeSimulationThread.setPriority(Thread.MIN_PRIORITY);
    timeSimulationThread.start();
  }

  public void stopTimeSimulation() {
    isAdvancementEnabled = false;

    if (timeSimulationThread != null) {
      try {
        timeSimulationThread.join(100 + updateTimeDurationInMillis);
      } catch (InterruptedException e) {
        log.error("Error waiting for time simulation thread to terminate", e);
      }
      if (timeSimulationThread.isAlive()) {
        log.error("Time simulation thread is still alive. Interrupting it");
        timeSimulationThread.interrupt();
      }
    }
  }

  @PreDestroy
  public void destroy() {
    stopTimeSimulation();
  }
}
