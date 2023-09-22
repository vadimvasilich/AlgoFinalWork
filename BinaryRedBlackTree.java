import java.util.ArrayList;
import java.util.List;

public class BinaryTree<T extends Comparable<T>> {
    Node root;
    public boolean add(T value) {
        if (root == null) {
            root = new Node();
            root.value = value;
            root.color = Color.Black;
            return true;
        }
        return addNode(root, value);
    }

    private boolean addNode(Node node, T value) {
        if (node.value.compareTo(value) == 0)
            return false;
        if (node.value.compareTo(value) > 0) {
            if (node.leftChild != null) {
                boolean result = addNode(node.leftChild, value);
                node.leftChild = rebalanced(node.leftChild);
                return result;
            } else {
                node.leftChild = new Node();
                node.leftChild.color = Color.Red;
                node.leftChild.value = value;
                return true;
            }
        } else {
            if (node.rightChild != null) {
                boolean result = addNode(node.rightChild, value);
                node.rightChild = rebalanced(node.rightChild);
                return result;
            } else {
                node.rightChild = new Node();
                node.rightChild.color = Color.Red;
                node.rightChild.value = value;
                return true;
            }
        }
    }

    public boolean contain(T value) {
        Node currentNode = root;
        while (currentNode != null) {
            if (currentNode.value.equals(value))
                return true;
            if (currentNode.value.compareTo(value) > 0)
                currentNode = currentNode.leftChild;
            else
                currentNode = currentNode.rightChild;
        }
        return false;
    }

    public boolean remove(T value) {
        if (!contain(value))
            return false;
        Node deleteNode = root;
        Node prevNode = root;
        while (deleteNode != null) {
            if (deleteNode.value.compareTo(value) == 0) {
                Node currentNode = deleteNode.rightChild;
                if (currentNode == null) {
                    if (deleteNode == root) {
                        root = root.leftChild;
                        root.color = Color.Black;
                        return true;
                    }
                    if (deleteNode.leftChild == null) {
                        deleteNode = null;
                        return true;
                    }
                    if (prevNode.leftChild == deleteNode)
                        prevNode.leftChild = deleteNode.leftChild;
                    else
                        prevNode.rightChild = deleteNode.leftChild;
                    return true;
                }
                while (currentNode.leftChild != null)
                    currentNode = currentNode.leftChild;
                deleteNode.value = currentNode.value;
                currentNode = null;
                return true;
            }
            if (prevNode != deleteNode) {
                if (prevNode.value.compareTo(value) > 0)
                    prevNode = prevNode.leftChild;
                else
                    prevNode = prevNode.rightChild;
            }
            if (deleteNode.value.compareTo(value) > 0)
                deleteNode = deleteNode.leftChild;
            else
                deleteNode = deleteNode.rightChild;
        }
        return false;
    }

    private Node rebalanced(Node node) {
        Node result = node;
        boolean needRebalance;
        do {
            needRebalance = false;
            if (result.rightChild != null && result.rightChild.color == Color.Red
                    && (result.leftChild == null || result.leftChild.color == Color.Black)) {
                needRebalance = true;
                result = rightSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.Red
                    && result.leftChild.leftChild != null && result.leftChild.leftChild.color == Color.Red) {
                needRebalance = true;
                result = leftSwap(result);
            }
            if (result.leftChild != null && result.leftChild.color == Color.Red
                    && result.rightChild != null && result.rightChild.color == Color.Red) {
                needRebalance = true;
                colorSwap(result);
            }
        } while (needRebalance);
        return result;
    }

    private void colorSwap(Node node) {
        node.rightChild.color = Color.Black;
        node.leftChild.color = Color.Black;
        node.color = Color.Red;
    }

    private Node leftSwap(Node node) {
        Node leftChild = node.leftChild;
        Node between = leftChild.rightChild;
        leftChild.rightChild = node;
        node.leftChild = between;
        leftChild.color = node.color;
        node.color = Color.Red;
        return leftChild;
    }

    private Node rightSwap(Node node) {
        Node rightChild = node.rightChild;
        Node between = rightChild.leftChild;
        rightChild.leftChild = node;
        node.rightChild = between;
        rightChild.color = node.color;
        node.color = Color.Red;
        return rightChild;
    }

    private class Node {
        T value;
        Node leftChild;
        Node rightChild;
        Color color;
    }

    private class PrintNode {
        Node node;
        String str;
        int depth;

        public PrintNode() {
            node = null;
            str = " ";
            depth = 0;
        }

        public PrintNode(Node node) {
            depth = 0;
            this.node = node;
            this.str = node.value.toString();
        }
    }
    
    public void print() {

        int maxDepth = maxDepth() + 3;
        int nodeCount = nodeCount(root, 0);
        int width = 50;
        int height = nodeCount * 5;
        List<List<PrintNode>> list = new ArrayList<List<PrintNode>>();
        for (int i = 0; i < height; i++)  {
            ArrayList<PrintNode> row = new ArrayList<>();
            for (int j = 0; j < width; j++) {
                row.add(new PrintNode());
            }
            list.add(row);
        }

        list.get(height / 2).set(0, new PrintNode(root));
        list.get(height / 2).get(0).depth = 0;

        for (int j = 0; j < width; j++)  {
            for (int i = 0; i < height; i++) {
                PrintNode currentNode = list.get(i).get(j);
                if (currentNode.node != null) {
                    currentNode.str = currentNode.node.value.toString();
                    if (currentNode.node.leftChild != null) {
                        int in = i + (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.leftChild;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;

                    }
                    if (currentNode.node.rightChild != null) {
                        int in = i - (maxDepth / (int) Math.pow(2, currentNode.depth));
                        int jn = j + 3;
                        printLines(list, i, j, in, jn);
                        list.get(in).get(jn).node = currentNode.node.rightChild;
                        list.get(in).get(jn).depth = list.get(i).get(j).depth + 1;
                    }

                }
            }
        }
        for (int i = 0; i < height; i++){
            boolean flag = true;
            for (int j = 0; j < width; j++) {
                if (list.get(i).get(j).str != " ") {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                list.remove(i);
                i--;
                height--;
            }
        }

        for (var row : list) {
            for (var item : row) {
                System.out.print(item.str + " ");
            }
            System.out.println();
        }
    }

    private void printLines(List<List<PrintNode>> list, int i, int j, int i2, int j2) {
        if (i2 > i)
        {
            while (i < i2) {
                i++;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "\\";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        } else {
            while (i > i2) {
                i--;
                list.get(i).get(j).str = "|";
            }
            list.get(i).get(j).str = "/";
            while (j < j2) {
                j++;
                list.get(i).get(j).str = "-";
            }
        }
    }

    public int maxDepth() {
        return maxDepth2(0, root);
    }

    private int maxDepth2(int depth, Node node) {
        depth++;
        int leftChild = depth;
        int rightChild = depth;
        if (node.leftChild != null)
            leftChild = maxDepth2(depth, node.leftChild);
        if (node.rightChild != null)
            rightChild = maxDepth2(depth, node.rightChild);
        return leftChild > rightChild ? leftChild : rightChild;
    }

    private int nodeCount(Node node, int count) {
        if (node != null) {
            count++;
            return count + nodeCount(node.leftChild, 0) + nodeCount(node.rightChild, 0);
        }
        return count;
    }

}

enum Color {Red, Black};
