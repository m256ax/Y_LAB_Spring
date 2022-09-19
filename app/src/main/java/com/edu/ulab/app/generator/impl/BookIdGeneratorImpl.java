package com.edu.ulab.app.generator.impl;

import com.edu.ulab.app.generator.IdGenerator;
import org.springframework.stereotype.Component;

@Component("BookId")
public class BookIdGeneratorImpl implements IdGenerator {

    private final Long[] number = new Long[]{0L};

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Long next() {
        Long lastId = number[0];
        lastId++;
        number[0] = lastId;
        return lastId;
    }
}
