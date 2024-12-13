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
    private int level;

    public Node(int value) {
        setValue(value);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        try {
            result.append(getValue())
                    .append(", left: ").append(getLeftNode() != null ? getLeftNode().getValue() : null)
                    .append(", right: ").append(getRightNode() != null ? getRightNode().getValue() : null)
                    .append(", parent: ").append(getParentNode() != null ? getParentNode().getValue() : null)
                    .append(", operations to find: ").append(level);

        } catch (NullPointerException e) {
            System.out.println("No node could be found");
        }
        return result.toString();

    }
}
