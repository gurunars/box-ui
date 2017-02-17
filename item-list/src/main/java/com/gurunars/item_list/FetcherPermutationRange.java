package com.gurunars.item_list;

import java.util.List;

import java8.util.function.BiFunction;


/*
* For two lists returns such a sublist that the remaining parts represent the unchanged portions
* to the left and to the right of this sublist.
*
* E.g. for [1,2,3,4,5,6] and [1,3,4,5,2,6] returns [3,4,5,2]
*
* */
class FetcherPermutationRange<ItemType> implements BiFunction<List<ItemType>, List<ItemType>, FetcherPermutationRange.Range> {

    static class Range {
        private int start, end;

        Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        int getStart() {
            return start;
        }

        int getEnd() {
            return end;
        }

        @Override
        public String toString() {
            return "R(" + start + ", " + end + ")";
        }
    }

    @Override
    public Range apply(List<ItemType> source, List<ItemType> target) {
        int startOffset, endOffset;

        int sourceSize = source.size();
        int targetSize = target.size();

        int limit = Math.min(sourceSize, targetSize);

        for (startOffset=0; startOffset < limit; startOffset++) {
            if (!source.get(startOffset).equals(target.get(startOffset))) {
                break;
            }
        }

        for (endOffset=0; endOffset< limit; endOffset++) {
            if (!source.get(sourceSize - 1 - endOffset).equals(
                    target.get(targetSize - 1 - endOffset))) {
                break;
            }
        }

        return new Range(startOffset, targetSize-endOffset);
    }

}
