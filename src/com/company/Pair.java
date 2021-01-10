package com.company;

import java.util.Objects;

public class Pair {
    int first;
    int second;

    Pair(int first, int second){
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return (first == pair.first && second == pair.second) ||
                (first == pair.second && second == pair.first);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
