package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.List;

class DifferOptimizer<ItemType> {

    static class OptimizedDiff<ItemType> {
        private List<ItemType> sourceHead = new ArrayList<>();
        private List<ItemType> sourceMiddle = new ArrayList<>();
        private List<ItemType> sourceTail = new ArrayList<>();
        private List<ItemType> targetHead = new ArrayList<>();
        private List<ItemType> targetMiddle = new ArrayList<>();
        private List<ItemType> targetTail = new ArrayList<>();
    }

    int getStartOffset(List<ItemType> source, List<ItemType> target) {
        int sizeLimit = Math.min(source.size(), target.size());
        int startOffset = 0;
        for (int i=0; i < sizeLimit; i ++) {
            if (!source.get(i).equals(target.get(i))) {
                break;
            }
            startOffset = i;
        }
        return startOffset;
    }

    int getEndOffset(List<ItemType> source, List<ItemType> target) {
        int sizeLimit = Math.min(source.size(), target.size());
        int endOffset = 0;
        int sourceLast = source.size() - 1;
        int targetLast = target.size() - 1;
        for (int i=0; i < sizeLimit; i ++) {
            if (!source.get(sourceLast-i).equals(target.get(targetLast-i))) {
                break;
            }
            endOffset = i;
        }
        return endOffset;
    }

    OptimizedDiff<ItemType> apply(List<ItemType> source, List<ItemType> target) {

        int startOffset = getStartOffset(source, target);
        int endOffset = getEndOffset(source, target);

        int sourceLastIndex = (source.size() - 1) - endOffset;
        int targetLastIndex = (target.size() - 1) - endOffset;

        OptimizedDiff<ItemType> diff = new OptimizedDiff<>();

        diff.sourceHead = source.subList(0, startOffset);
        diff.targetHead = target.subList(0, startOffset);
        diff.sourceTail = source.subList(sourceLastIndex, source.size());
        diff.targetTail = target.subList(targetLastIndex, target.size());
        diff.sourceMiddle = source.subList(startOffset, sourceLastIndex);
        diff.targetMiddle = target.subList(startOffset, targetLastIndex);

        return diff;
    }

}
