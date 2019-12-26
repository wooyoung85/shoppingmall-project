package com.wooyoung85.shoppingmallrestapi.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {
    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() > 0) {
            errors.rejectValue("basePrice", "wrongValue", "Base Price is Wrong!!");
            errors.rejectValue("maxPrice", "wrongValue", "Max Price is Wrong!!");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();


        if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime())
                || endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())
                || endEventDateTime.isBefore(eventDto.getBeginEventDateTime())) {
            errors.rejectValue("endEventDateTime","wrongValue","EndEventDateTime is Wrong!!");
        }
    }
}
