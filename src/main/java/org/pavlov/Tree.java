package org.pavlov;

import lombok.NoArgsConstructor;

import java.awt.desktop.SystemEventListener;

@NoArgsConstructor
public class Tree {

    private Node rootNode;

    public Node findNodeByValue(int value) {
        Node curNode = rootNode;
        int ops = 1;
        while(curNode.getValue() != value) {
            ops++;
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
        System.out.println("Operations to find " + curNode.getValue() + ": " + ops);
        return curNode;
    }

    public void insert(int value) {
        String addValue = "Node with value " + value + " added";
        int level = 1;

        if (rootNode == null) {
            rootNode = new Node(value);

            System.out.println("Root " + addValue);
            return;
        }

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

        if (delNode.getLeftNode() == null && delNode.getRightNode() == null) {                  //если нет листьев

            if (delNode.getValue() > parentDelNode.getValue()) {
                parentDelNode.setRightNode(null);
            } else {
                parentDelNode.setLeftNode(null);
            }
        } else if (delNode.getLeftNode() != null && delNode.getRightNode() == null) {       //если есть один левый лист

            if (delNode.getValue() > parentDelNode.getValue()) {
                parentDelNode.setRightNode(delNode.getLeftNode());
                delNode.getLeftNode().setParentNode(parentDelNode);
            } else {
                parentDelNode.setLeftNode(delNode.getLeftNode());
                delNode.getLeftNode().setParentNode(parentDelNode);
            }
        } else if (delNode.getLeftNode() == null && delNode.getRightNode() != null) {       //если есть один правый лист

            if (delNode.getValue() > parentDelNode.getValue()) {
                parentDelNode.setRightNode(delNode.getRightNode());
                delNode.getRightNode().setParentNode(parentDelNode);
            } else {
                parentDelNode.setLeftNode(delNode.getRightNode());
                delNode.getRightNode().setParentNode(parentDelNode);
            }
        } else if (delNode.getLeftNode() != null && delNode.getRightNode() != null) {                     //если оба листа

            if (delNode.getValue() > parentDelNode.getValue()) {
                Node curNode = delNode.getLeftNode();

                if (curNode.getRightNode() != null) {
                    while (curNode.getRightNode() != null) {
                        curNode = curNode.getRightNode();
                    }
                }
                System.out.println("Change node: " + curNode.getValue());


                Node delRightChild = delNode.getRightNode();
                Node delLeftChild = delNode.getLeftNode();

                parentDelNode.setRightNode(curNode);

                curNode.setRightNode(delRightChild);
                if (curNode != delLeftChild) {
                    curNode.setLeftNode(delLeftChild);
                } else if (delLeftChild.getLeftNode() != null){
                    curNode.setLeftNode(delLeftChild.getLeftNode());
                } else {
                    curNode.setLeftNode(null);
                }
                curNode.setParentNode(parentDelNode);


                delRightChild.setParentNode(curNode);
                delLeftChild.setParentNode(curNode);
            } else {
                Node curNode = delNode.getLeftNode();

                if (curNode.getRightNode() != null) {
                    while (curNode.getRightNode() != null) {
                        curNode = curNode.getRightNode();
                    }
                }
                System.out.println("Change node (parent is right): " + curNode.getValue());

                parentDelNode.setLeftNode(curNode);

                curNode.setRightNode(delNode.getRightNode());
                if (curNode != delNode.getLeftNode()) curNode.setLeftNode(delNode.getLeftNode());

                curNode.setParentNode(parentDelNode);


                delNode.getRightNode().setParentNode(curNode);
                delNode.getLeftNode().setParentNode(curNode);
            }
        }
        System.out.println("Node with value: " + delNode.getValue() + " deleted");
    }


    public void printTree() {
        if (rootNode == null) {
            return;
        }

        printRightTree(rootNode.getRightNode(), 1);
        System.out.println(rootNode.getValue());
        printLeftTree(rootNode.getLeftNode(),1);
    }

    public void printLeftTree(Node node, int level) {
        if (node == null) {
            return;
        }

        printRightTree(node.getRightNode(), level + 1);
        System.out.print("\t".repeat(level - 1));
        System.out.print("  |__");
        System.out.println(node.getValue());
        printLeftTree(node.getLeftNode(), level + 1);
    }


    public void printRightTree(Node node, int level) {
        if (node == null) {
            return;
        }

        printRightTree(node.getRightNode(), level + 1);
        System.out.print("\t".repeat(level - 1));
        System.out.print("  /▔▔");
        System.out.println(node.getValue());
        printLeftTree(node.getLeftNode(), level + 1);
    }

}