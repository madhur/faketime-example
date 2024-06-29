/* (C) Games24x7 */
package in.co.madhur.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Getter
public class GetTimeResponse {

  private final LocalDateTime localDateTime = LocalDateTime.now();

  private final long timestamp = System.currentTimeMillis();

  private final Instant instant = Instant.now();

  private final Date date = new Date();

  public Date getDate() {
    return new Date(date.getTime());
  }
}
