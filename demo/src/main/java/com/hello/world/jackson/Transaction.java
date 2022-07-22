package com.hello.world.jackson;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

@Data
@AllArgsConstructor
public class Transaction {

    private String type = null;

    private Date date = null;

}
