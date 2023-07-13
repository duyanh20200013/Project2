package com.javaspringboot.Project2.advice;

public interface CustomMapper<S,D> {
    D map(S source);
}
