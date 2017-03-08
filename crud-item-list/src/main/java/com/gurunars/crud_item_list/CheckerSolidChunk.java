package com.gurunars.crud_item_list;

import java.util.List;

import java8.util.function.Function;

final class CheckerSolidChunk implements Function<List<Integer>, Boolean> {

    @Override
    public Boolean apply(List<Integer> positions) {
        if (positions.size() == 0) {
            return false;
        }

        for (int i = 1; i < positions.size(); i++) {
            if(positions.get(i) - positions.get(i-1) != 1) {
                return false;
            }
        }
        return true;
    }

}
