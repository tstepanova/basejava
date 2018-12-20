package ru.javawebinar.basejava;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainStreams {

    public static void main(String[] args) {
        int[] array1 = {1, 2, 3, 3, 2, 3};
        System.out.println(minValue(array1));
        System.out.println(oddOrEven(IntStream.of(array1).boxed().collect(Collectors.toList())));
        int array2[] = {9, 8};
        System.out.println(minValue(array2));
        System.out.println(oddOrEven(IntStream.of(array2).boxed().collect(Collectors.toList())));
    }

    private static int minValue(int[] values) {
        return IntStream.of(values).sorted().distinct().reduce(0, (x, y) -> x * 10 + y);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        Integer sum = integers.stream().reduce((s1, s2) -> s1 + s2).orElse(0);
        Map<Boolean, List<Integer>> map = integers.stream().collect(Collectors.partitioningBy((p) -> (p % 2) == 0));
        return map.get((sum % 2) != 0);
    }

}