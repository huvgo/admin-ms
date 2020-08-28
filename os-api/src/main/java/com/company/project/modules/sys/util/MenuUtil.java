package com.company.project.modules.sys.util;

import com.company.project.modules.common.TreeEntity;

import java.util.ArrayList;
import java.util.List;

public class MenuUtil {

    /**
     * 树构建
     *
     * @param <E>      ID类型
     * @param list     源数据集合
     * @param parentId 最顶层父id值 一般为 0 之类
     * @return List
     */
    public static <T, E extends TreeEntity<T, E>> List<E> buildTree(List<E> list, T parentId) {
        List<E> tree = new ArrayList<>();
        for (E treeNode : list) {
            if (parentId.equals(treeNode.getParentId())) {
                tree.add(treeNode);
                treeNode.setChildren(buildTree(list, treeNode.getId()));
            }
        }
        return tree;
    }


}
