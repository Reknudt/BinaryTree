package org.pavlov;

public class Main {
    public static void main(String[] args) {

        Tree tree1 = new Tree();


        int[] arr = {4, 1, 2, 3, 5, 5};

        for (int i : arr) {
            tree1.insert(i);
        }

        tree1.insert(1);
        tree1.insert(9);
        tree1.insert(7);
        tree1.insert(13);
        tree1.insert(12);
        tree1.insert(15);
        tree1.insert(10);

        tree1.printTree();

//        Node node1 = tree1.findNodeByValue(4);
//        Node node2 = tree1.findNodeByValue(5);
//
//        System.out.println(String.valueOf(node1));
//        System.out.println(String.valueOf(node2));
//
//        tree1.printTree();
//
//        tree1.deleteNode(9);
//
//        tree1.printTree();
//        tree1.insert(11);
//        tree1.deleteNode(13);
//        tree1.printTree();
//
//        System.out.println(String.valueOf(tree1.findNodeByValue(12)));
//
//        tree1.deleteNode(12);
//        tree1.printTree();
    }
}