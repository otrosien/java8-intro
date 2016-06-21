package com.epages.docs.exercise;

import com.google.common.base.Strings;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static com.google.common.collect.Maps.newHashMap;

/**
 * Another exercise. Solve Lombok and OptionalStream first.
 *
 * We are going to generate boards/coordinate systems as Strings and print them on the terminal.
 *
 * Look for TODOs and run the main to check your results.
 */
public class Draw {

    private static final long MAX = 44;


    public static String generateBoard() {
        StringBuilder board = new StringBuilder();

        for (long y = 0; y < MAX; y++) {
            for (long x = 0; x < MAX; x++) {
                if (x == y || x + y == MAX - 1) {
                    board.append("x ");
                } else {
                    board.append("- ");
                }
            }
            board.append("\n");
        }
        return board.toString();
    }

    /**
     * TODO: Reimplement the method above using Streams.
     */
    public static String generateBoardStream() {
        StringBuilder board = new StringBuilder();

        LongStream.range(0, MAX).forEach(y -> {
            LongStream.range(0, MAX).forEach(x -> {
                if ((x == y || x + y == MAX - 1)) {
                    board.append("x ");
                } else {
                    board.append("- ");
                }
            });
            board.append("\n");
        });

        return board.toString();
    }

    @EqualsAndHashCode
    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class Position implements Comparable<Position> {
        private final long x;
        private final long y;

        @Override
        public int compareTo(Position o) {
            return x > o.x ? +1 : x < o.x ? -1 : y > o.y ? +1 : y < o.y ? -1 : 0;
        }
    }

    private enum Point {
        X
    }


    @NoArgsConstructor
    @AllArgsConstructor
    static class Board {
        private static String BLANK = " ";

        @NonNull
        private Map<Position, Point> map = newHashMap();

        Point put(Position pos) {
            return map.put(pos, Point.X);
        }

        Point put(long x, long y) {
            return put(Position.of(x,y));
        }

        Optional<Point> get(long x, long y) {
            return Optional.ofNullable(map.get(Position.of(x,y)));
        }

        /**
         * TODO: Return the maximum x and y dimensions of the board.
         */
        Position getDimensions() {
            return map.keySet().stream()
                    .reduce(Position.of(0, 0), (p1, p2) -> Position.of(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y)));
        }

        Board copy() {
            return new Board(newHashMap(this.map));
        }

        String toString(@Nullable Position dimensions) {
            StringBuilder res = new StringBuilder();

            Position dim = Optional.ofNullable(dimensions).orElseGet(this::getDimensions);

            LongStream.rangeClosed(0, dim.y).forEach(y -> {
                LongStream.rangeClosed(0, dim.x).forEach(x -> {
                    res.append(get(x, y).map(Point::toString).orElse(BLANK)).append(" ");
                });
                res.append("\n");
            });
            return res.toString();
        }


        @Override
        public String toString() {
            return toString(getDimensions());
        }
    }

    /**
     * TODO: Print the stream of Boards using the given dimensions argument.
     * Before printing reset the screen using 'reset()'.
     * The 'max' parameter defines how many boards should be printed.
     */
     public static void print(Supplier<Board> boardSupplier, @Nullable Position dim, long max) {
        Stream<Board> stream = Stream.generate(boardSupplier);
        if (max >= 0) {
            stream = stream.limit(max);
        }
        stream.forEach(b -> {
            reset();
            System.out.println(b.toString(dim));
        });
    }

    @RequiredArgsConstructor
    public static class BoundedDiagonalMovesProvider implements Supplier<Board> {
        Board board = new Board();

        private long x = 0;
        private long y = 0;

        private long dy = +1;
        private long dx = +1;

        private final long maxY;
        private final long maxX;

        @Override
        public Board get() {
            board.put(x,y);
            x += dx;
            y += dy;
            if (x >= maxX || x <= 0) {
                dx *= -1;
            }
            if (y >= maxY || y <= 0) {
                dy *= -1;
            }
            return board;
        }
    }

    @RequiredArgsConstructor
    public static class SinusMovesSupplier implements Supplier<Board> {
        Board board = new Board();

        private long x = 0;
        private long dx = +1;

        private final long maxY;

        @Override
        public Board get() {
            Board newBoard = board.copy();
            newBoard.put(x, sinY(x, maxY));
            x += dx;
            board = newBoard;
            return board;
        }
    }

    @RequiredArgsConstructor
    public static class StaticSinusMovesSupplier implements Supplier<Board> {

        private final long maxX;
        private final long maxY;

        private final long dx = +1;
        private long xOffset = 0;

        private static final double MIN_ST = 0;
        private static final double MAX_ST = 15;
        @NonNull
        private double std;
        private double st = 5;

        @Override
        public Board get() {
            Board newBoard = new Board();

            LongStream.rangeClosed(0, maxX).forEach(x -> {
                newBoard.put(x, sinY(x+xOffset, maxY, st));
            });
            xOffset += dx;
            st += std;
            if (st < MIN_ST || st > MAX_ST) {
                std *= -1;
            }
            return newBoard;
        }
    }

    private static long sinY(long x, long maxY) {
        return sinY(x, maxY, 5.0);
    }

    private static long sinY(long x, long maxY, double trans) {
        double yp = Math.sin(x * (trans/maxY));
        yp += 1;
        yp = yp * (maxY / 2.0);
        return Math.round(yp);
    }


    /**
     * Utility functions below
     */


    /**
     * Clears the screen.
     */
    private static void clear() {
//        System.out.println("\u001B[2J"); // only with consoles with ANSI support
        System.out.println(Strings.repeat("\n", 100));
    }

    /**
     * zZz
     */
    private static void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            // no
        }
    }

    private static void reset() {
        sleep();
        clear();
    }
}