package org.pavlov;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Tree {

    private Node rootNode;

    public Node findNodeByValue(int value) {
        Node curNode = rootNode;

        while(curNode.getValue() != value) {
            if (value < curNode.getValue()) {
                curNode = curNode.getLeftNode();
            } else {
                curNode = curNode.getRightNode();
            }
            if (curNode == null) {
                System.out.println("No such element: " + value);
                return null;
            }
        }
        return curNode;
    }

    public void insert(int value) {
        String addValue = "Node with value " + value + " added";

        if (rootNode == null) {
            rootNode = new Node(value);
            rootNode.setLevel(1);

            System.out.println("Root " + addValue);
            return;
        }

        int level = 1;
        Node curNode = rootNode;

        while(true) {

            level++;
            if (value < curNode.getValue()) {

                if (curNode.getLeftNode() != null) {
                    curNode = curNode.getLeftNode();
                } else {
                    curNode.setLeftNode(new Node(value));

                    Node leftNode = curNode.getLeftNode();

                    leftNode.setParentNode(curNode);
                    leftNode.setLevel(level);

                    System.out.println(addValue);
                    return;
                }
            } else if (value > curNode.getValue()) {

                if (curNode.getRightNode() != null) {
                    curNode = curNode.getRightNode();
                } else {
                    curNode.setRightNode(new Node(value));

                    Node rightNode = curNode.getRightNode();
                    rightNode.setParentNode(curNode);
                    rightNode.setLevel(level);

                    System.out.println(addValue);
                    return;
                }
            } else {
                System.out.println("Value " + value + " already added");
                return;
            }
        }
    }

    public void deleteNode(int value) {
        Node delNode = findNodeByValue(value);
        Node parentDelNode = delNode.getParentNode();

        if (delNode.getLeftNode() == null && delNode.getRightNode() == null) {
            if (delNode.getValue() > parentDelNode.getValue()) {
                parentDelNode.setRightNode(null);
            } else {
                parentDelNode.setLeftNode(null);
            }
        } else if (delNode.getLeftNode() != null && delNode.getRightNode() == null) {
            Node leftNode = delNode.getLeftNode();

            if (delNode.getValue() > parentDelNode.getValue()) {
                parentDelNode.setRightNode(null);
            } else {
                parentDelNode.setLeftNode(null);
            }


        } else if (delNode.getLeftNode() == null && delNode.getRightNode() != null) {

        }
    }

    public void printTree(Node node, int level) {
        if (node == null) {
            return;
        }

        printRightTree(node.getRightNode(), level + 1);

        if (level > 0) {
            System.out.print("\t".repeat(level - 1));
            System.out.print("  |__");
        }
        System.out.println(node.getValue());

        printTree(node.getLeftNode(), level + 1);
    }

    public void printRightTree(Node node, int level) {
        if (node == null) {
            return;
        }

        printRightTree(node.getRightNode(), level + 1);

        if (level > 0) {
            System.out.print("\t".repeat(level - 1));
            System.out.print("  /▔▔");
        }
        System.out.println(node.getValue());

        printTree(node.getLeftNode(), level + 1);
    }

}