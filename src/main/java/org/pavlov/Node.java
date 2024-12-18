package org.pavlov;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class Node {

    private int value;
    private Node leftNode;
    private Node rightNode;
    private Node parentNode;

    public Node(int value) {
        setValue(value);
    }

    @Override
    public String toString() {

        return  getValue() +
                ", left: " + (getLeftNode() != null ? getLeftNode().getValue() : null) +
                ", right: " + (getRightNode() != null ? getRightNode().getValue() : null) +
                ", parent: " + (getParentNode() != null ? getParentNode().getValue() : null);
    }
}
