package com.javaspringboot.Project2.payload.response;

import com.javaspringboot.Project2.model.Goods;
import lombok.*;

import java.util.Set;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoodsTypeResponse {

    private long id;

    private String name;

    private String description;

    private Set<Goods> goods;
}
