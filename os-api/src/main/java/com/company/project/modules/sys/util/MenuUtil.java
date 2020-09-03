package com.company.project.modules.sys.util;

import com.company.project.modules.base.base.TreeEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MenuUtil {

    /**
     * 树构建
     *
     * @param <E>      ID类型
     * @param list     源数据集合
     * @param parentId 最顶层父id值 一般为 0 之类
     * @return List
     */
    public static <E extends TreeEntity<E>> List<E> buildTree(List<E> list, Integer parentId) {

        List<E> tree = new ArrayList<>();
        for (E treeNode : list) {
            if (treeNode.getParentId() == null) {
                treeNode.setParentId(0);
            }
            if (parentId.equals(treeNode.getParentId())) {
                tree.add(treeNode);
                treeNode.setChildren(buildTree(list, treeNode.getId()));
            }
        }

        if (tree.isEmpty()) {
            return null;
        }
        tree = tree.stream().sorted(Comparator.comparing(TreeEntity::getSort, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());
        return tree;
    }


}
