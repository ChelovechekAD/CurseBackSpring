package it.academy.cursebackspring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {
    private Integer HttpStatusCode;
    private String ExceptionMessage;
    private List<String> ExceptionDetails;
    private LocalDateTime TimeStamp;

}