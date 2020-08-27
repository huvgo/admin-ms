package com.company.project.util;

import com.company.project.core.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class TreeUtil {

    /**
     * 树构建
     *
     * @param <E>      ID类型
     * @param list     源数据集合
     * @param parentId 最顶层父id值 一般为 0 之类
     * @return List
     */
    public static <E extends TreeNode<E>> List<E> build(List<E> list, Integer parentId) {
        List<E> tree = new ArrayList<>();
        for (E treeNode : list) {
            if (parentId.equals(treeNode.getParentId())) {
                tree.add(treeNode);
                treeNode.setChildren(build(list, treeNode.getId()));
            }
        }
        return tree;
    }



}
