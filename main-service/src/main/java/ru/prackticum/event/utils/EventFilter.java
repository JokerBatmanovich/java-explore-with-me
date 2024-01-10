package ru.prackticum.event.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.prackticum.event.enums.State;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFilter {
   Integer[] users;
   State[] states;
   Integer[] categories;
   LocalDateTime rangeStart;
   LocalDateTime rangeEnd;
   String text;
   Boolean paid;
   Boolean onlyAvailable;

   public boolean isEmpty() {
      EventFilter emptyEventFilter = new EventFilter();
      return this.equals(emptyEventFilter);
   }
}


