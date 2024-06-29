/* (C) Games24x7 */
package in.co.madhur.controller;

import in.co.madhur.component.TimeSimulator;
import in.co.madhur.dto.GetTimeResponse;
import io.github.faketime.FakeTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/time-manipulation")
@ConditionalOnProperty(prefix = "enable", name = "time-manipulation.apis")
public class TimeManipulationController {

  @Autowired private TimeSimulator timeSimulator;

  @PostConstruct
  public void init() {
    log.info("Time manipulation APIs enabled");
  }

  @PostMapping("/setTime")
  public ResponseEntity<String> setFakeTime(@RequestParam String dateTime) {
    log.info("Setting fake time to: {}", dateTime);

    LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime);
    FakeTime.stopAt(parsedDateTime);
    timeSimulator.startTimeSimulation();

    log.info("Fake time set to: {}", LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.OK)
        .body("Time set");
  }

  @GetMapping("/getTime")
  public ResponseEntity<GetTimeResponse> getTime() {
    return ResponseEntity.status(HttpStatus.OK)
        .body(new GetTimeResponse());
  }

  @PostMapping("/restoreTime")
  public ResponseEntity<String> resetTime() {
    log.info("Restoring real time");
    timeSimulator.stopTimeSimulation();
    FakeTime.restoreReal();
    log.info("Real time restored: {}", LocalDateTime.now());
    return ResponseEntity.status(HttpStatus.OK)
        .body("Time restored");
  }
}
