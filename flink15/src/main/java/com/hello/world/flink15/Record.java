package com.hello.world.flink15;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    private String name;

    private Integer score;

    private Long timeStamp;

    private String time;

    public Record(String name, Integer score) {
        this.name = name;
        this.score = score;
    }
}
