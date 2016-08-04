//package com.main.component;
//
//import javax.swing.*;
//import javax.swing.tree.DefaultMutableTreeNode;
//import javax.swing.tree.TreeSelectionModel;
//
///**
// * Created by Administrator on 2016/8/2.
// */
//public class Tree extends JTree{
//    public Tree(){
//        //Create the nodes.
//        DefaultMutableTreeNode top =
//                new DefaultMutableTreeNode("The Java Series");
//        createNodes(top);
//
//        //Create a tree that allows one selection at a time.
//        tree = new JTree(top);
//        tree.getSelectionModel().setSelectionMode
//                (TreeSelectionModel.SINGLE_TREE_SELECTION);
//
//        //Listen for when the selection changes.
//        tree.addTreeSelectionListener(this);
//
//        if (playWithLineStyle) {
//            System.out.println("line style = " + lineStyle);
//            tree.putClientProperty("JTree.lineStyle", lineStyle);
//        }
//    }
//}
