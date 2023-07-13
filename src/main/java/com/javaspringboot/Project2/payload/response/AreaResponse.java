package com.javaspringboot.Project2.payload.response;

import com.javaspringboot.Project2.model.Ban;
import lombok.*;

import java.util.Set;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AreaResponse {

    private long id;

    private String name;

    private String description;

    private Set<Ban> ban;
}
