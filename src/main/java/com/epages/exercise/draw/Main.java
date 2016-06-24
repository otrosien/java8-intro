package com.epages.exercise.draw;

import static com.epages.exercise.draw.Draw.*;

/**
 * HypnoToad
 */
public class Main {

    private static long MAX_X = 89;
    private static long MAX_Y = 44;

    private Main() {}

    public static void main(String[] args) throws InterruptedException {
        System.out.println(generateBoardStream());
        Thread.sleep(2000);
        print(new BoundedDiagonalMovesProvider(19, MAX_X), null, MAX_X * 6);
        Thread.sleep(2000);
        print(new SinusMovesSupplier(MAX_Y), Position.of(MAX_X, MAX_Y), MAX_X);
        print(new StaticSinusMovesSupplier(MAX_X, MAX_Y, -0.02), Position.of(MAX_X, MAX_Y), -1);
    }

}
