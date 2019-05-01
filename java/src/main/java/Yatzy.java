import io.vavr.collection.List;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.util.function.Function;

public class Yatzy {

    public static final int PAIR = 2;
    public static final int THREE_OF_KIND = 3;
    public static final int FOUR_OF_KIND = 4;
    public static final List<Integer> SMALL_STRAIGHT = List.rangeClosed(1, 5);
    public static final List<Integer> LARGE_STRAIGHT = List.rangeClosed(2, 6);


    public static int chance(int d1, int d2, int d3, int d4, int d5) {
        return List.of(d1, d2, d3, d4, d5)
                .sum()
                .intValue();
    }

    public static int yatzy(int... dice) {
        int distinct = List.ofAll(dice)
                .distinct()
                .size();
        return distinct == 1 ? 50 : 0;
    }

    public static int ones(int d1, int d2, int d3, int d4, int d5) {
        return sumReadValues(d1, d2, d3, d4, d5, 1);
    }

    public static int twos(int d1, int d2, int d3, int d4, int d5) {
        return sumReadValues(d1, d2, d3, d4, d5, 2);
    }

    public static int threes(int d1, int d2, int d3, int d4, int d5) {
        return sumReadValues(d1, d2, d3, d4, d5, 3);
    }

    private static int sumReadValues(int d1, int d2, int d3, int d4, int d5, int wantedValue) {
        return sumReadValues(List.of(d1, d2, d3, d4, d5), wantedValue);
    }

    private static int sumReadValues(List<Integer> dice, int wantedValue) {
        int count = dice.count(value -> value == wantedValue);
        return count * wantedValue;
    }


    private final List<Integer> dice;

    public Yatzy(int d1, int d2, int d3, int d4, int d5) {
        dice = List.of(d1, d2, d3, d4, d5);
    }

    public int fours() {
        return sumReadValues(dice, 4);
    }

    public int fives() {
        return sumReadValues(dice, 5);
    }

    public int sixes() {
        return sumReadValues(dice, 6);
    }

    public static int score_pair(int d1, int d2, int d3, int d4, int d5) {
        return searchByNumberOfKind(d1, d2, d3, d4, d5, PAIR)
                .max()
                .map(value -> value * PAIR)
                .getOrElse(0);
    }

    public static int two_pair(int d1, int d2, int d3, int d4, int d5) {
        Set<Integer> pairedValues = searchByNumberOfKind(d1, d2, d3, d4, d5, PAIR);
        return pairedValues.size() == 2 ?
                pairedValues.map(value -> value * PAIR).sum().intValue() : 0;
    }

    private static Set<Integer> searchByNumberOfKind(int d1, int d2, int d3, int d4, int d5, int wantedNumber) {
        return List.of(d1, d2, d3, d4, d5)
                .groupBy(Function.identity())
                .filterValues(values -> values.size() >= wantedNumber)
                .keySet();
    }

    public static int four_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
        return searchByNumberOfKind(d1, d2, d3, d4, d5, FOUR_OF_KIND)
                .headOption()
                .map(value -> value * FOUR_OF_KIND)
                .getOrElse(0);
    }

    public static int three_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
        return searchByNumberOfKind(d1, d2, d3, d4, d5, THREE_OF_KIND)
                .headOption()
                .map(value -> value * THREE_OF_KIND)
                .getOrElse(0);
    }

    public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {
        List<Integer> dice = List.of(d1, d2, d3, d4, d5);
        return SMALL_STRAIGHT.find(val -> !dice.contains(val)).isDefined() ? 0 : 15;
    }

    public static int largeStraight(int d1, int d2, int d3, int d4, int d5) {
        List<Integer> dice = List.of(d1, d2, d3, d4, d5);
        return LARGE_STRAIGHT.find(val -> !dice.contains(val)).isDefined() ? 0 : 20;
    }

    public static int fullHouse(int d1, int d2, int d3, int d4, int d5) {
        int threeOfAKind = three_of_a_kind(d1, d2, d3, d4, d5);
        if (threeOfAKind != 0) {
            return findPairWhenAlreadyThreeOfKind(d1, d2, d3, d4, d5)
                    .map(value -> value + threeOfAKind)
                    .getOrElse(0);
        }
        return 0;
    }

    private static Option<Integer> findPairWhenAlreadyThreeOfKind(int d1, int d2, int d3, int d4, int d5) {
        return List.of(d1, d2, d3, d4, d5)
                .groupBy(Function.identity())
                .filterValues(values -> values.size() == PAIR)
                .keySet()
                .headOption()
                .map(value -> value * PAIR);
    }

}



